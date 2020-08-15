package app.juntrack.common.data.domain;

import java.util.Date;

import lombok.Data;

@Data
public class TweetRecord {

	private String contentId;

	private Date tweetedAt;

}
