
package app.juntrack.twitch.client.http.response.streams;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.ToString;

@ToString
@Generated("jsonschema2pojo")
public class Data {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("user_id")
	@Expose
	private String userId;
	@SerializedName("user_login")
	@Expose
	private String userLogin;
	@SerializedName("user_name")
	@Expose
	private String userName;
	@SerializedName("game_id")
	@Expose
	private String gameId;
	@SerializedName("game_name")
	@Expose
	private String gameName;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("viewer_count")
	@Expose
	private Integer viewerCount;
	@SerializedName("started_at")
	@Expose
	private String startedAt;
	@SerializedName("language")
	@Expose
	private String language;
	@SerializedName("thumbnail_url")
	@Expose
	private String thumbnailUrl;
	@SerializedName("tag_ids")
	@Expose
	private List<String> tagIds = null;
	@SerializedName("is_mature")
	@Expose
	private Boolean isMature;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getViewerCount() {
		return viewerCount;
	}

	public void setViewerCount(Integer viewerCount) {
		this.viewerCount = viewerCount;
	}

	public String getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public List<String> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<String> tagIds) {
		this.tagIds = tagIds;
	}

	public Boolean getIsMature() {
		return isMature;
	}

	public void setIsMature(Boolean isMature) {
		this.isMature = isMature;
	}

}
