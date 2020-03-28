package haoyu.niubi.community.mapper;


        import haoyu.niubi.community.model.User;
        import haoyu.niubi.community.model.UserExample;
        import org.apache.ibatis.annotations.*;

        import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user (ACCOUNT_ID,NAME,TOKEN,GMT_CREATE,GMT_MODIFIED,AVATAR_URL)values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
    @Select("select * from user where ACCOUNT_ID = #{accountId}")
    User findByAccountId(@Param("accountId") Integer accountId);
    @Select("select * from user where ID = #{Id}")
    User findById(@Param("Id") Integer Id);
    @Update("update user set GMT_MODIFIED=#{gmtModified} ,TOKEN=#{token},NAME=#{name},AVATAR_URL=#{avatarUrl} where ACCOUNT_ID=#{accountId}")
    void update(User user1);
    @Select("select * from user where id = id order by id ")
    List<User> selectByUser(UserExample user);

}