package app.juntrack.youtube;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.common.data.domain.LiveRecord;
import app.juntrack.common.data.domain.TweetRecord;
import app.juntrack.common.data.mapper.LiveRecordMapper;
import app.juntrack.common.data.mapper.TweetRecordMapper;
import app.juntrack.twitter.TwitterClient;
import app.juntrack.youtube.http.client.YouubeApiClient;
import app.juntrack.youtube.http.response.videos.Item;
import app.juntrack.youtube.http.response.videos.LiveStreamingDetails;
import app.juntrack.youtube.http.response.videos.YoutubeVideosListResponse;
import app.juntrack.youtube.wrapper.YoutubeCredentialWrapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class YoutubeLiveRecordWorker implements Runnable {

	private static final String API_NAME = "Videos: list";

	private static final String URL_BASE = "https://www.googleapis.com/youtube/v3/videos?part=snippet,liveStreamingDetails&id=";

	private RecordDto recordDto;

	@Autowired
	LiveRecordMapper liveRecordMapper;

	@Autowired
	TweetRecordMapper tweetRecordMapper;

	@Autowired
	TwitterClient twitterClient;

	@Autowired
	YoutubeCredentialWrapper youtubeCredentialWrapper;

	public void setRecordDto(RecordDto recordDto) {
		this.recordDto = recordDto;
	}

	@Override
	public void run() {
		log.info("YoutubeLiveで配信中なので記録を開始します。 {}", recordDto);

		record(recordDto);

		log.info("YoutubeLiveの配信を終了したので記録を終了します。 {}", recordDto);
	}

	public void record(RecordDto recordDto) {
		while (true) {
			YouubeApiClient<YoutubeVideosListResponse> client = new YouubeApiClient<YoutubeVideosListResponse>(
					youtubeCredentialWrapper.getCredential());
			YoutubeVideosListResponse videosResponse = client.sendGetRequest(URL_BASE + recordDto.getContentId(),
					API_NAME, YoutubeVideosListResponse.class);
			List<Item> items = videosResponse.getItems();
			if (items == null || items.size() == 0) {
				break;
			}

			LiveStreamingDetails details = videosResponse.getItems().get(0).getLiveStreamingDetails();
			if (details == null || details.getConcurrentViewers() == null) {
				break;
			}

			this.createLiveRecord(recordDto.getContentId(), details.getConcurrentViewers());

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.error("想定外のエラーが発生しました。{}", e);
			}
		}

		tweet(recordDto);
	}

	private void createLiveRecord(String contentId, String viewers) {
		LiveRecord liveRecord = new LiveRecord();
		liveRecord.setContentId(contentId);
		liveRecord.setViewers(Integer.parseInt(viewers));
		liveRecord.setRegisteredAt(new Date());
		liveRecord.setStreamingSite(StreamingSiteType.YOUTUBE.getValue());

		this.liveRecordMapper.insert(liveRecord);
		log.info("live_recordにデータを登録しました。 {}", liveRecord);
	}

	private void tweet(RecordDto recordDto) {
		TweetRecord tweetRecord = tweetRecordMapper.selectByContentId(recordDto.getContentId());
		if (tweetRecord != null) {
			log.info("すでにツイート済みなので、ツイートしません。 {}", tweetRecord);
			return;
		}

		twitterClient.tweetJunLive(recordDto);

		tweetRecord = new TweetRecord();
		tweetRecord.setContentId(recordDto.getContentId());
		tweetRecord.setTweetedAt(new Date());
		tweetRecordMapper.insert(tweetRecord);
		log.info("tweet_recordにデータを登録しました。 {}", tweetRecord);
	}

}
