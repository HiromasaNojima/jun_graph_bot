package app.juntrack.twitch.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import app.juntrack.twitch.client.http.client.TwitchTokenClient;
import lombok.Getter;

@Component
@Getter
public class TwitchCredentialWrapper {

	private final String clientId;

	private final String clientSecret;

	@Autowired
	private TwitchTokenClient tokenClient;

	public TwitchCredentialWrapper(@Autowired Environment env) {
		clientId = env.getProperty("app.twitch.client.id");
		clientSecret = env.getProperty("app.twitch.client.secret");
	}

	public String getAccessToken() {
		return tokenClient.getToken(clientId, clientSecret).getAccessToken();
	}

}
