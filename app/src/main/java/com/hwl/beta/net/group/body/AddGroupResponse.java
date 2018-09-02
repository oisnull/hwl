package com.hwl.beta.net.group.body;

import java.util.Date;

public class AddGroupResponse {
    private int Status;
    private String GroupGuid;
    private Date BuildTime;

    public int getStatus() {
        return Status;
    }

    public String getGroupGuid() {
        return GroupGuid;
    }

    public Date getBuildTime() {
        return BuildTime;
    }
}
