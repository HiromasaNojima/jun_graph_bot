package app.juntrack.twitch.client.http.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import app.juntrack.twitch.client.http.response.streams.TwitchGetStreamsResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
@Slf4j
public class TwitchGetStreamsClient {

	private final String accessToken;

	private final String clientId;

	private final OkHttpClient httpClient = new OkHttpClient();

	private final String url = "https://api.twitch.tv/helix/streams?user_id=545050196";

	public TwitchGetStreamsClient(@Autowired Environment env) {
		accessToken = env.getProperty("app.twitch.access.token");
		clientId = env.getProperty("app.twitch.client.id");
	}

	public TwitchGetStreamsResponse getStreams() {
		TwitchGetStreamsResponse converted = null;
		try (Response response = httpClient.newCall(new Request.Builder()
				.addHeader("Authorization", "Bearer " + accessToken).addHeader("Client-ID", clientId).url(url)
				.build()).execute()) {
			String responseBody = response.body().string();
			log.info("Twitch Get Streamsを実行しました。 リクエストURL = {}, ステータスコード = {}, レスポンスボディ = {}", url, response.code(),
					responseBody);
			converted = new Gson().fromJson(responseBody, TwitchGetStreamsResponse.class);
		} catch (Exception exc) {
			log.error("Twitch Get Streamsの実行に失敗しました。 {}", exc);
		}

		return converted;
	}

}
