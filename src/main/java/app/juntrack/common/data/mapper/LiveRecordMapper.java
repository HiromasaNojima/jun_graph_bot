package app.juntrack.common.data.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import app.juntrack.common.data.domain.LiveRecord;

@Mapper
public interface LiveRecordMapper {

	@Select("select * from live_record")
	public List<LiveRecord> selectAll();

	@Select("select * from (select * from live_record where content_id = #{contentId} order by rand() limit 250) as randomSelected order by registered_at asc")
	public List<LiveRecord> selectByContentIdOrderByRegisteredAtAsc(@Param("contentId") String contentId);

	@Insert("insert into live_record (content_id, viewers, streaming_site ,registered_at) values(#{contentId}, #{viewers}, #{streamingSite}, #{registeredAt})")
	public void insert(LiveRecord liveRecord);

}
