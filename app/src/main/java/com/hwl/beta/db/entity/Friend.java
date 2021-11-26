package com.hwl.beta.db.entity;

import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/28.
 */
@Entity
public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private long id;
    private String symbol;
    private String name;
    private String remark;
    private String groupRemark;
    private String firstLetter;
    private String headImage;
    private String lifeNotes;
    private int sex;
    private String circleBackImage;
    private String country;
    private String province;
    private boolean isFriend;
    private String updateTime;

    @Transient
    private String messageCount;
    @Transient
    private int imageRes;
    @Transient
    private String showName;

    public String getShowName() {
        if (StringUtils.isNotBlank(this.remark))
            return this.remark;
        else if (StringUtils.isNotBlank(this.name))
            return this.name;

        return showName;
    }

    public String getMessageCount() {
        if (StringUtils.isBlank(messageCount))
            return "0";
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    @Generated(hash = 502452610)
    public Friend(long id, String symbol, String name, String remark, String groupRemark,
            String firstLetter, String headImage, String lifeNotes, int sex, String circleBackImage,
            String country, String province, boolean isFriend, String updateTime) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.remark = remark;
        this.groupRemark = groupRemark;
        this.firstLetter = firstLetter;
        this.headImage = headImage;
        this.lifeNotes = lifeNotes;
        this.sex = sex;
        this.circleBackImage = circleBackImage;
        this.country = country;
        this.province = province;
        this.isFriend = isFriend;
        this.updateTime = updateTime;
    }

    @Generated(hash = 287143722)
    public Friend() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFirstLetter() {
        return this.firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getHeadImage() {
        return this.headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getLifeNotes() {
        return this.lifeNotes;
    }

    public void setLifeNotes(String lifeNotes) {
        this.lifeNotes = lifeNotes;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCircleBackImage() {
        return this.circleBackImage;
    }

    public void setCircleBackImage(String circleBackImage) {
        this.circleBackImage = circleBackImage;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean getIsFriend() {
        return this.isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    @Override
    public String toString() {
        return String.format("%s-%s-%s-%s-%s-%s-%s-%s-%s-%s", this.id, this.symbol, this.name,
                this.remark, this.headImage, this.lifeNotes, this.sex, this.circleBackImage, this
                        .country, this.province);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Friend) {
            Friend f = (Friend) obj;
            return this.getId() == f.getId();
        }
        return super.equals(obj);
    }

    public String getGroupRemark() {
        return this.groupRemark;
    }

    public void setGroupRemark(String groupRemark) {
        this.groupRemark = groupRemark;
    }
}
