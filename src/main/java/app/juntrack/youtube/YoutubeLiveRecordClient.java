package app.juntrack.youtube;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.common.data.mapper.LiveRecordMapper;
import app.juntrack.youtube.http.client.YouubeApiClient;
import app.juntrack.youtube.http.response.search.YoutubeSearchListResponse;
import app.juntrack.youtube.wrapper.YoutubeChannelIdWrapper;
import app.juntrack.youtube.wrapper.YoutubeCredentialWrapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class YoutubeLiveRecordClient {

	private final String apiName = "Search: list";

	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private final ThreadPoolExecutor threadPoolExecutor;

	private final String url;

	@Autowired
	LiveRecordMapper liveRecordMapper;

	@Autowired
	ApplicationContext context;

	@Autowired
	YoutubeCredentialWrapper youtubeCredentialWrapper;

	public YoutubeLiveRecordClient(@Autowired YoutubeChannelIdWrapper channelIdwrapper) throws Exception {
		threadPoolExecutor = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, queue);
		url = "https://www.googleapis.com/youtube/v3/search?part=id, snippet&type=video&eventType=live&channelId="
				+ channelIdwrapper.getChannelId();
	}

	@Scheduled(initialDelay = 0, fixedDelay = 900000)
	public void searchJunLive() {
		if (threadPoolExecutor.getActiveCount() != 0) {
			return;
		}

		YouubeApiClient<YoutubeSearchListResponse> tubeClient = new YouubeApiClient<YoutubeSearchListResponse>(
				youtubeCredentialWrapper.getCredential());
		YoutubeSearchListResponse searchList = tubeClient.sendGetRequest(url, apiName,
				YoutubeSearchListResponse.class);
		if (searchList.getPageInfo() == null || searchList.getPageInfo().getTotalResults() == 0) {
			log.info("YoutubeLiveで配信されてません。");
			return;
		}

		RecordDto recordDto = new RecordDto.Builder()
				.setContentId(searchList.getItems().get(0).getId().getVideoId())
				.setStreamingSiteType(StreamingSiteType.YOUTUBE)
				.setTitle(searchList.getItems().get(0).getSnippet().getTitle())
				.build();

		YoutubeLiveRecordWorker recoder = context.getBean(YoutubeLiveRecordWorker.class);
		recoder.setRecordDto(recordDto);
		threadPoolExecutor.execute(recoder);
	}

}
