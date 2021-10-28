package app.juntrack.youtube;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.common.data.domain.LiveRecord;
import app.juntrack.common.data.mapper.LiveRecordMapper;
import app.juntrack.twitter.TwitterClient;
import app.juntrack.youtube.http.client.YoutubeApiClient;
import app.juntrack.youtube.http.endpoint.YoutubeEndpoint;
import app.juntrack.youtube.http.response.search.YoutubeSearchListResponse;
import app.juntrack.youtube.http.response.videos.Item;
import app.juntrack.youtube.http.response.videos.Snippet;
import app.juntrack.youtube.http.response.videos.YoutubeVideosListResponse;
import app.juntrack.youtube.wrapper.YoutubeChannelIdWrapper;
import app.juntrack.youtube.wrapper.YoutubeCredentialWrapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class YoutubeLiveRecordClient {

	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, queue);

	private static final AtomicInteger count = new AtomicInteger(0);

	@Autowired
	LiveRecordMapper liveRecordMapper;

	@Autowired
	ApplicationContext context;

	@Autowired
	YoutubeCredentialWrapper youtubeCredentialWrapper;

	@Autowired
	TwitterClient twitterClient;

	@Autowired
	YoutubeChannelIdWrapper channelIdWrapper;

	@Scheduled(initialDelay = 0, fixedDelayString = "${jun.live.search.interval}")
	public void searchJunLive() {
		if (threadPoolExecutor.getActiveCount() != 0) {
			return;
		}

		try {
			log.info(Integer.toString(count.incrementAndGet()));
			RecordDto recordDto = null;
			if (count.compareAndSet(15, 0)) {
				recordDto = createRecordDtoBySearchList();
			} else {
				recordDto = createRecorDtoByTweet();
			}

			if (recordDto == null) {
				log.info("YoutubeLiveで配信されてません。");
				return;
			}

			YoutubeLiveRecordWorker recoder = context.getBean(YoutubeLiveRecordWorker.class);
			recoder.setRecordDto(recordDto);
			threadPoolExecutor.execute(recoder);
		} catch (Exception exc) {
			log.error("想定外の異常が発生しました。{}", exc);
		}
	}

	private RecordDto createRecordDtoBySearchList() {
		YoutubeApiClient<YoutubeSearchListResponse> tubeClient = new YoutubeApiClient<YoutubeSearchListResponse>(
				youtubeCredentialWrapper.getCredential());
		YoutubeSearchListResponse searchList = tubeClient.sendGetRequest(
				YoutubeEndpoint.SEARCH_LIST.getUrl() + channelIdWrapper.getChannelId(),
				YoutubeEndpoint.SEARCH_LIST.getApiName(),
				YoutubeSearchListResponse.class);
		if (searchList.getPageInfo() == null || searchList.getPageInfo().getTotalResults() == 0) {
			return null;
		}

		return new RecordDto.Builder()
				.setContentId(searchList.getItems().get(0).getId().getVideoId())
				.setStreamingSiteType(StreamingSiteType.YOUTUBE)
				.setTitle(searchList.getItems().get(0).getSnippet().getTitle())
				.build();
	}

	private RecordDto createRecorDtoByTweet() {
		String videoId = twitterClient.getVideoIdOfTweet();
		if (videoId == null) {
			return null;
		}

		List<LiveRecord> records = liveRecordMapper.selectByContentIdOrderByRegisteredAtAsc(videoId);
		if (records.size() != 0) {
			log.info("記録済みの配信です。 video_id = {}", videoId);
			return null;
		}

		YoutubeApiClient<YoutubeVideosListResponse> client = new YoutubeApiClient<YoutubeVideosListResponse>(
				youtubeCredentialWrapper.getCredential());
		YoutubeVideosListResponse videosResponse = client.sendGetRequest(
				YoutubeEndpoint.VIDEOS_LIST.getUrl() + videoId,
				YoutubeEndpoint.VIDEOS_LIST.getApiName(), YoutubeVideosListResponse.class);
		List<Item> items = videosResponse.getItems();
		if (items == null || items.size() == 0) {
			return null;
		}

		Item item = items.get(0);
		if (item.getLiveStreamingDetails() == null || item.getLiveStreamingDetails().getConcurrentViewers() == null) {
			return null;
		}

		Snippet snippet = item.getSnippet();
		if (snippet == null) {
			return null;
		}
		if (!StringUtils.equals(snippet.getChannelId(), channelIdWrapper.getChannelId())) {
			log.info("jun channelの配信ではないです。 snippet = {}", snippet.toString());
			return null;
		}

		return new RecordDto.Builder().setContentId(videoId).setStreamingSiteType(StreamingSiteType.YOUTUBE)
				.setTitle(snippet.getTitle()).build();
	}

}
