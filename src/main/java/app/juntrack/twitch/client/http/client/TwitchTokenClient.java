package app.juntrack.twitch.client.http.client;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import app.juntrack.twitch.client.http.endpoint.TwitchEndpoint;
import app.juntrack.twitch.client.http.response.token.TwitchTokenResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@Slf4j
public class TwitchTokenClient {

	public TwitchTokenResponse getToken(String clientId, String clientSecret) {
		String url = MessageFormat.format(TwitchEndpoint.TOKEN.getUrl(), clientId, clientSecret);
		TwitchTokenResponse converted = null;
		try (Response response = new OkHttpClient().newCall(new Request.Builder()
				.url(url).post(RequestBody.create("{}", null)).build()).execute()) {
			String responseBody = response.body().string();
			log.info("execute get twitch token. url = {}, status_code = {}, response_body = {}", url, response.code(),
					responseBody);
			converted = new Gson().fromJson(responseBody, TwitchTokenResponse.class);
		} catch (Exception exc) {
			log.error("failed to get twich token {}", exc);
		}

		return converted;
	}

}
