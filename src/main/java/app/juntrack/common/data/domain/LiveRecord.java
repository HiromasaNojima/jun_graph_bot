package app.juntrack.common.data.domain;

import java.util.Date;

import lombok.Data;

@Data
public class LiveRecord {

	private String contentId;

	private Integer viewers;

	private Integer commnents;

	private String streamingSite;

	private Date registeredAt;

}
