package app.juntrack.twitch.client.http.endpoint;

import lombok.Getter;

@Getter
public enum TwitchEndpoint {

	STREAMS("https://api.twitch.tv/helix/streams", "Get Streams"),

	VIDEOS("https://api.twitch.tv/helix/videos", "Get Videos"),

	WATCH("https://www.twitch.tv/videos/", "");

	private final String url;

	private final String apiName;

	private TwitchEndpoint(String url, String apiName) {
		this.url = url;
		this.apiName = apiName;
	}

}
