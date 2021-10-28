package app.juntrack.twitch;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.common.data.domain.LiveRecord;
import app.juntrack.common.data.domain.TweetRecord;
import app.juntrack.common.data.mapper.LiveRecordMapper;
import app.juntrack.common.data.mapper.TweetRecordMapper;
import app.juntrack.twitch.client.http.client.TwitchGetStreamsClient;
import app.juntrack.twitch.client.http.response.streams.TwitchGetStreamsResponse;
import app.juntrack.twitter.TwitterClient;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class TwitchRecordWorker implements Runnable {

	private RecordDto recordDto;

	@Autowired
	LiveRecordMapper liveRecordMapper;

	@Autowired
	TweetRecordMapper tweetRecordMapper;

	@Autowired
	TwitterClient twitterClient;

	@Autowired
	private TwitchGetStreamsClient getStreamsClient;

	public void setRecordDto(RecordDto recordDto) {
		this.recordDto = recordDto;
	}

	@Override
	public void run() {
		log.info("Twitchで配信中なので記録を開始します。 {}", recordDto);

		try {
			record(recordDto);
		} catch (Exception exc) {
			log.error("想定外の異常が発生しましたので記録を終了します。{}", exc);
			return;
		}

		log.info("Twitchの配信を終了したので記録を終了します。 {}", recordDto);
	}

	public void record(RecordDto recordDto) {
		while (true) {
			TwitchGetStreamsResponse response = getStreamsClient.getStreams();
			if (response == null || CollectionUtils.isEmpty(response.getData())) {
				break;
			}

			if (!StringUtils.equals(recordDto.getContentId(), response.getData().get(0).getId())) {
				break;
			}

			this.createLiveRecord(recordDto.getContentId(), response.getData().get(0).getViewerCount());

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.error("想定外のエラーが発生しました。{}", e);
			}
		}

		tweet(recordDto);
	}

	private void createLiveRecord(String contentId, Integer viewers) {
		LiveRecord liveRecord = new LiveRecord();
		liveRecord.setContentId(contentId);
		liveRecord.setViewers(viewers);
		liveRecord.setRegisteredAt(new Date());
		liveRecord.setStreamingSite(StreamingSiteType.TWITCH.getValue());

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
