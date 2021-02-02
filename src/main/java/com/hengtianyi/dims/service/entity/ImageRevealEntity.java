package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;


/**
 * ImageReveal实体类
 * <p>Table: IMAGE_REVEAL</p>
 */
public class ImageRevealEntity extends BaseEntity {


    private static final long serialVersionUID = 114610873081789600L;

    /**
     * id
     */
    private String id;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 关联举报的表单id
     */
    private String revealId;


    /**
     * 上传图片的时间-系统自动生成
     */
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRevealId() {
        return revealId;
    }

    public void setRevealId(String revealId) {
        this.revealId = revealId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ImageRevealEntity";
    }
}
