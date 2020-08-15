package app.juntrack.youtube.http.client;

import java.io.FileInputStream;
import java.util.List;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.gson.Gson;

import app.juntrack.youtube.http.response.YoutubeResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class YouubeApiClient<T extends YoutubeResponse> {

	private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/youtube");

	private final String credential;

	private final OkHttpClient httpClient = new OkHttpClient();

	public YouubeApiClient(String credential) {
		this.credential = credential;
	}

	public T sendGetRequest(String url, String apiName, Class<T> classOfT) {
		T converted = null;
		try (Response response = httpClient.newCall(createRequest(url)).execute()) {
			String responseBody = response.body().string();
			log.info(apiName + "を実行しました。 リクエストURL = {}, ステータスコード = {}, レスポンスボディ = {}", url, response.code(),
					responseBody);
			converted = new Gson().fromJson(responseBody, classOfT);
		} catch (Exception exc) {
			log.error(apiName + "の実行に失敗しました。 {}", exc);
		}

		return converted;
	}

	private Request createRequest(String url) {
		ServiceAccountCredentials credentials = null;
		try {
			credentials = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(
					new FileInputStream(credential));
			credentials = (ServiceAccountCredentials) credentials.createScoped(SCOPES);
			credentials.refreshIfExpired();
		} catch (Exception exc) {
			log.error("クレデンシャル情報の生成に失敗しました。 {}", exc);
		}

		return new Request.Builder()
				.addHeader("Authorization", "Bearer " + credentials.getAccessToken().getTokenValue()).url(url)
				.build();
	}

}
