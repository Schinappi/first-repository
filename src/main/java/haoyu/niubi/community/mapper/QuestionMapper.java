package haoyu.niubi.community.mapper;


import haoyu.niubi.community.dto.QuestionDTO;
import haoyu.niubi.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_modified,creator,tag)values(#{title},#{description},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select(value = "select count(1) from question")
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
    void updateComment(Question question1);
}
