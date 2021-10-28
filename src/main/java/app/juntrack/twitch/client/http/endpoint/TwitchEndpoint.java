package app.juntrack.twitch.client.http.endpoint;

import lombok.Getter;

@Getter
public enum TwitchEndpoint {

	VIDEOS("https://www.twitch.tv/videos/");

	private final String url;

	private TwitchEndpoint(String url) {
		this.url = url;
	}

}
