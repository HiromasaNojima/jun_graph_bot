package app.juntrack.common.data.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import app.juntrack.common.data.domain.TweetRecord;

@Mapper
public interface TweetRecordMapper {

	@Select("select * from tweet_record where content_id = #{contentId}")
	public TweetRecord selectByContentId(@Param("contentId") String contentId);

	@Insert("insert into tweet_record (content_id, tweeted_at) values(#{contentId}, #{tweetedAt})")
	public void insert(TweetRecord tweetRecord);

}
