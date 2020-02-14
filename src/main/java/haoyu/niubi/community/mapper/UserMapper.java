package haoyu.niubi.community.mapper;


import haoyu.niubi.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
   @Insert("insert into user (ACCOUNT_ID,NAME,TOKEN,GMT_CREATE,GMT_MODIFIED)values(#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified})")
      void insert(User user);
}
