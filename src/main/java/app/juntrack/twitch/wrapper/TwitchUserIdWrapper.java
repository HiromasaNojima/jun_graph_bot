package app.juntrack.twitch.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class TwitchUserIdWrapper {

	private final String userId;

	public TwitchUserIdWrapper(@Autowired Environment env) {
		userId = env.getProperty("app.twitch.user.id");
	}

}
