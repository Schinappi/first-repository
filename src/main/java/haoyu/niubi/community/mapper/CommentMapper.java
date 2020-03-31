package haoyu.niubi.community.mapper;

import haoyu.niubi.community.dto.CommentDTO;
import haoyu.niubi.community.model.Comment;
import haoyu.niubi.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(parent_id,gmt_modified,gmt_create,type,like_count,commentator,content)values(#{parentId},#{gmtModified},#{gmtCreate},#{type},#{likeCount},#{commentator},#{content})")
    void insert(Comment comment) ;
    @Select("select * from comment where id=#{parentId}")
    Comment selectByPrimaryKey(Integer parentId);
    @Select("select * from comment where parent_Id=#{parentId} order by gmt_create desc")
    List<Comment> selectByCommentDTO(CommentDTO commentDTO);
    @Update("update comment set comment_count=#{commentCount}  where id = #{id}")
    void incCommentCount(Comment comment);
    @Select("select  comment_count from comment where id=#{id}")
    Integer selectCommentCount(Integer id);
}
