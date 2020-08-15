package app.juntrack.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.juntrack.chart.client.ChartApiClient;
import app.juntrack.common.RecordDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Component
@Slf4j
public class TwitterClient {

	private static final String YOUTUBE_URL_BASE = "https://www.youtube.com/watch?v=";

	@Autowired
	ChartApiClient chartApiClient;

	public void tweetJunLive(RecordDto recordDto) {
		Twitter twitter = new TwitterFactory().getInstance();
		String tweetContent = new StringBuilder("加藤純一さんの配信【")
				.append(recordDto.getTitle())
				.append("(").append(YOUTUBE_URL_BASE).append(recordDto.getContentId()).append(")")
				.append("】").append("の同時接続視聴者数の推移はグラフのようになりました。").toString();
		StatusUpdate statusUpdate = new StatusUpdate(tweetContent);

		try (Response response = chartApiClient.postGraph(recordDto.getContentId())) {
			statusUpdate.setMedia("同時視聴者接続数グラフ", response.body().byteStream());
			twitter.updateStatus(statusUpdate);
		} catch (TwitterException e) {
			log.error("ツイッターの投稿に失敗しました。{}", e);
		}

	}
}
