package app.juntrack.twitch.client.http.response.token;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.ToString;

@Generated("jsonschema2pojo")
@ToString
@Data
public class TwitchTokenResponse {

	@SerializedName("access_token")
	@Expose
	private String accessToken;
	@SerializedName("expires_in")
	@Expose
	private Integer expiresIn;
	@SerializedName("scope")
	@Expose
	private List<String> scope = null;
	@SerializedName("token_type")
	@Expose
	private String tokenType;

}