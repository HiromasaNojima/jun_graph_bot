package app.juntrack.youtube.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class YoutubeChannelIdWrapper {

	private final String channelId;

	@Autowired
	Environment env;

	public YoutubeChannelIdWrapper(@Autowired Environment env) {
		channelId = env.getProperty("app.youtube.channel.id");
	}

	public String getChannelId() {
		return channelId;
	}

}
