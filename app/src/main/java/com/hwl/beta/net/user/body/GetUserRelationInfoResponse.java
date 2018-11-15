package com.hwl.beta.net.user.body;

public class GetUserRelationInfoResponse {
    private boolean IsFriend;
    private boolean IsInBlackList;

    public boolean getIsFriend() {
        return IsFriend;
    }

    public void setIsFriend(boolean IsFriend) {
        this.IsFriend = IsFriend;
    }

    public boolean getIsInBlackList() {
        return IsInBlackList;
    }

    public void setIsInBlackList(boolean IsInBlackList) {
        this.IsInBlackList = IsInBlackList;
    }

}
