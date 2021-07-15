package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;

/**
 * ClueReportImageEntity实体类
 * <p>Table: IMAGE_CLUE_REPORT</p>
 *
 * @author LY
 */
public class ImageClueReportEntity extends BaseEntity {


  private static final long serialVersionUID = -7528738187238993843L;

  public static final int TYPE_IMAGE=10;

  public static final int TYPE_AUDIO=20;

  public static final int TYPE_VIDEO=30;

  private String id;
  /**
   * 上报线索id
   */
  private String clueId;
  /**
   * 图片路径
   */
  private String imageUrl;
  /**
   * 图片创建时间
   */
  private Date createTime;

  /**
   * 类型
   * 10:图片
   * 20:语音
   * 30:视频
   */
  private int type;


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClueId() {
    return clueId;
  }

  public void setClueId(String clueId) {
    this.clueId = clueId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * 获取createTime属性(任务时间)
   *
   * @return 任务时间
   */
  public Date getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 任务时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "ImageClueReportEntity";
  }
}
