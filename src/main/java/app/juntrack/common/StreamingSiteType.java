package app.juntrack.common;

public enum StreamingSiteType {

	YOUTUBE("youtube");

	private String value;

	StreamingSiteType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
