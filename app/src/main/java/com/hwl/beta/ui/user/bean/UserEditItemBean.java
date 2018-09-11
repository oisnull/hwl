package com.hwl.beta.ui.user.bean;

/**
 * Created by Administrator on 2018/4/3.
 */

public class UserEditItemBean {

    public final static int ACTIONTYPE_SYMBOL = 1;
    public final static int ACTIONTYPE_NAME = 2;
    public final static int ACTIONTYPE_REMARK = 3;
    public final static int ACTIONTYPE_SEX = 4;
    public final static int ACTIONTYPE_LIFENOTES = 5;

    private int actionType;
    private String editContent;
    private String edtiDesc;
    private String sex;
    private long friendId;

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public UserEditItemBean() {
    }

    public UserEditItemBean(int actionType, String editContent) {
        this.actionType = actionType;
        this.editContent = editContent;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getEditContent() {
        return editContent;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }

    public String getEdtiDesc() {
        switch (this.actionType) {
            case ACTIONTYPE_SYMBOL:
                return "注：添加后不能修改\n标识是指你在系统中的身份，其它人只能通过标识来查找你的信息.";
            case ACTIONTYPE_NAME:
                return "个人昵称";
            case ACTIONTYPE_REMARK:
                return "设置好友备注";
            default:
                return edtiDesc;
        }
    }

//    public void setEdtiDesc(String edtiDesc) {
//        this.edtiDesc = edtiDesc;
//    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
