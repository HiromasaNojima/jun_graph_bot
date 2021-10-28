package app.juntrack.twitter;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.juntrack.chart.client.ChartApiClient;
import app.juntrack.common.RecordDto;
import app.juntrack.common.StreamingSiteType;
import app.juntrack.twitch.client.http.endpoint.TwitchEndpoint;
import app.juntrack.youtube.http.endpoint.YoutubeEndpoint;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

@Component
@Slf4j
public class TwitterClient {

	@Autowired
	ChartApiClient chartApiClient;

	@Autowired
	MessageSource messageSource;

	public void tweetJunLive(RecordDto recordDto) {
		Twitter twitter = new TwitterFactory().getInstance();

		String tweetContent = null;
		if (recordDto.getStreamingSiteType() == StreamingSiteType.YOUTUBE) {
			tweetContent = messageSource.getMessage("jun.tweet.message",
					new String[] { recordDto.getTitle(), YoutubeEndpoint.WATCH.getUrl() + recordDto.getContentId() },
					Locale.JAPAN);
		} else {
			tweetContent = messageSource.getMessage("jun.tweet.message",
					new String[] { recordDto.getTitle(), TwitchEndpoint.WATCH.getUrl() + recordDto.getVideoId() },
					Locale.JAPAN);
		}

		log.info(tweetContent);
		StatusUpdate statusUpdate = new StatusUpdate(tweetContent);

		try (Response response = chartApiClient.postGraph(recordDto.getContentId())) {
			statusUpdate.setMedia("同時視聴者接続数グラフ", response.body().byteStream());
			twitter.updateStatus(statusUpdate);
		} catch (TwitterException e) {
			log.error("ツイッターの投稿に失敗しました。{}", e);
		}
	}

	private ResponseList<Status> getJunTweet() {
		Twitter twitter = new TwitterFactory().getInstance();
		ResponseList<Status> responses = null;
		try {
			responses = twitter.getUserTimeline(3239782022l);
		} catch (TwitterException e) {
			log.error("タイムラインの取得に失敗しました。{}", e);
		}

		return responses;
	}

	public String getVideoIdOfTweet() {
		ResponseList<Status> responses = this.getJunTweet();
		if (responses.size() == 0) {
			return null;
		}

		Status response = responses.get(0);
		log.info(response.toString());
		URLEntity[] urlEntities = response.getURLEntities();
		for (URLEntity urlEntity : urlEntities) {
			String url = urlEntity.getExpandedURL();
			if (StringUtils.contains(url, "youtube")) {
				int index = StringUtils.indexOf(url, "=");
				return StringUtils.substring(url, index + 1);
			}
		}

		return null;
	}
}
