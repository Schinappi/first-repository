package haoyu.niubi.community.mapper;


import haoyu.niubi.community.dto.QuestionQueryDTO;
import haoyu.niubi.community.model.Question;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag)values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question order by gmt_modified desc limit #{offset},#{size} ")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select(value = "select count(*) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size} ")
    List<Question> listByUserId(@Param(value = "userId") Integer userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from question where  id= #{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set title = #{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id = #{id}")
    int update(Question question);

    @Update("update question set view_count=#{viewCount} where id = #{id}")
    void updateView(Question question1);

    @Update("update question set comment_count=#{commentCount} where id = #{id}")
    void incCommentCount(Question question1);
     @Select("select *  from question where  id != #{id} and tag regexp #{tag}")
    List<Question> selectRelated(Question question);
    @Select(value = "select count(*) from question where title regexp #{search}")
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    @Select("select * from question where title regexp #{search} order by gmt_create desc   limit #{page},#{size} ")
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
