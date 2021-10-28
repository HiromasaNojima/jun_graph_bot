package app.juntrack.common;

import lombok.Getter;
import lombok.ToString;

@ToString
public class RecordDto {

	@Getter
	private final String contentId;

	@Getter
	private final StreamingSiteType streamingSiteType;

	@Getter
	private final String title;

	@Getter
	private final String videoId;

	private RecordDto(Builder builder) {
		this.contentId = builder.contentId;
		this.streamingSiteType = builder.streamingSiteType;
		this.title = builder.title;
		this.videoId = builder.videoId;
	}

	public static class Builder {

		private String contentId;

		private StreamingSiteType streamingSiteType;

		private String title;

		private String videoId;

		public RecordDto build() {
			return new RecordDto(this);
		}

		public Builder setContentId(String contentId) {
			this.contentId = contentId;
			return this;
		}

		public Builder setStreamingSiteType(StreamingSiteType streamingSiteType) {
			this.streamingSiteType = streamingSiteType;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setVideoId(String videoId) {
			this.videoId = videoId;
			return this;
		}

	}

}
