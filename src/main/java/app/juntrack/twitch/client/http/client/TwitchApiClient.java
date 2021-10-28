package app.juntrack.twitch.client.http.client;

import com.google.gson.Gson;

import app.juntrack.twitch.client.http.response.TwitchResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class TwitchApiClient<T extends TwitchResponse> {

	private final String accessToken;

	private final String clientId;

	private final OkHttpClient httpClient = new OkHttpClient();

	public TwitchApiClient(String accessToken, String clientId) {
		this.accessToken = accessToken;
		this.clientId = clientId;
	}

	public T sendGetRequest(String url, String apiName, Class<T> classOfT) {
		T converted = null;
		try (Response response = httpClient.newCall(new Request.Builder()
				.addHeader("Authorization", "Bearer " + accessToken).addHeader("Client-ID", clientId).url(url)
				.build()).execute()) {
			String responseBody = response.body().string();
			log.info("Twitch {} を実行しました。 リクエストURL = {}, ステータスコード = {}, レスポンスボディ = {}", apiName, url, response.code(),
					responseBody);
			converted = new Gson().fromJson(responseBody, classOfT);
		} catch (Exception exc) {
			log.error("Twitch {} の実行に失敗しました。 {}", apiName, exc);
		}

		return converted;
	}

}
