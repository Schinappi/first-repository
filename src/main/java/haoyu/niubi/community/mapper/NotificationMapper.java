package haoyu.niubi.community.mapper;

import haoyu.niubi.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(id,notifier,receiver,outerId,type,gmt_create,status,outer_title,notifier_name)values(#{id},#{notifier},#{receiver},#{outerId},#{type},#{gmt_create},#{status},#{outerTitle},#{notifierName})")
    void insert(Notification notification);
    @Select("select count(1) from notification where receiver = #{userId}")
    Integer countByUserId(Integer userId);
    @Select("select * from notification where receiver = #{userId} limit #{offset},#{size} ")
    List<Notification> listByUserId(Integer userId, Integer offset, Integer size);
    @Select("select count(1) from notification where receiver = #{receiver} and status =#{status}")
    Integer countByExample(Notification notificationExample);
    @Select("select * from notification where id = #{id}")
    Notification selectByPrimaryKey(Integer id);
    @Update("update notification set status = #{status} where id = #{id}")
    void updateByPrimaryKey(Notification notification);
}
