package haoyu.niubi.community.exception;

public enum CustomizeErrorCode implements  ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001,"你找的问题不在了"),
   TARGET_PARM_NOT_FOUND(2002,"未选中问题或评论进行回复"),
   NO_LOGIN(2003,"未登录不能进行评论"),
     SYS_ERROR (2004,"服务器冒烟了") ,
    TYPE_PARM_WRONG (2005,"评论类型错误或不存在") ,
    COMMENT_NOT_FOUND (2006,"回复的评论不存在了" ),
    CONTENT_IS_EMPTY (2007,"输入内容不能为空" );

    private String message;
    private  Integer code;
    CustomizeErrorCode(Integer code, String message){
        this.message=message;
        this.code =code;
    }
  @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
