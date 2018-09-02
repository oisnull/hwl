package com.hwl.beta.net.group.body;

public class SetGroupNoteRequest {
    private long UserId;
    private String GroupGuid;
    private String GroupNote;

    public String getGroupNote() {
        return GroupNote;
    }

    public void setGroupNote(String groupNote) {
        GroupNote = groupNote;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

}
