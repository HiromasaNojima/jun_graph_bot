package app.juntrack.common;

import lombok.Getter;

public class RecordDto {

	@Getter
	private final String contentId;

	@Getter
	private final StreamingSiteType streamingSiteType;

	@Getter
	private final String title;

	private RecordDto(Builder builder) {
		this.contentId = builder.contentId;
		this.streamingSiteType = builder.streamingSiteType;
		this.title = builder.title;
	}

	public static class Builder {

		private String contentId;

		private StreamingSiteType streamingSiteType;

		private String title;

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

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordDto [contentId=");
		builder.append(contentId);
		builder.append(", streamingSiteType=");
		builder.append(streamingSiteType);
		builder.append(", title=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

}
