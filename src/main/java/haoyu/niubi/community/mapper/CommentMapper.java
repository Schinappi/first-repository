package haoyu.niubi.community.mapper;

import haoyu.niubi.community.dto.CommentDTO;
import haoyu.niubi.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(parent_id,gmt_modified,gmt_create,type,like_count,commentator,content)values(#{parentId},#{gmtModified},#{gmtCreate},#{type},#{likeCount},#{commentator},#{content})")
    void insert(Comment comment) ;
    @Select("select * from comment where parentId=#{parentId}")
    Comment selectByPrimaryKey(Integer parentId);
    @Select("select * from comment where parent_Id=#{parentId} order by gmt_create desc")
    List<Comment> selectByCommentDTO(CommentDTO commentDTO);
}
