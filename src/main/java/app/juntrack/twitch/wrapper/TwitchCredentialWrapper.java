package app.juntrack.twitch.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class TwitchCredentialWrapper {

	private final String accessToken;

	private final String clientId;

	public TwitchCredentialWrapper(@Autowired Environment env) {
		accessToken = env.getProperty("app.twitch.access.token");
		clientId = env.getProperty("app.twitch.client.id");
	}

}
