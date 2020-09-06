package app.juntrack.youtube.http.endpoint;

public enum YoutubeEndpoint {

	WATCH("https://www.youtube.com/watch?v=", null),

	VIDEOS_LIST("https://www.googleapis.com/youtube/v3/videos?part=snippet,liveStreamingDetails&id=", "Videos: list"),

	SEARCH_LIST("https://www.googleapis.com/youtube/v3/search?part=id, snippet&type=video&eventType=live&channelId=",
			"Search: list");

	private final String url;

	private final String apiName;

	private YoutubeEndpoint(String url, String apiName) {
		this.url = url;
		this.apiName = apiName;
	}

	public String getUrl() {
		return url;
	}

	public String getApiName() {
		return apiName;
	}

}
