
package app.juntrack.twitch.client.http.response.streams;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.ToString;

@Generated("jsonschema2pojo")
@ToString
public class TwitchGetStreamsResponse {

	@SerializedName("data")
	@Expose
	private List<Data> data = null;
	@SerializedName("pagination")
	@Expose
	private Pagination pagination;

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
