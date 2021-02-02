package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * TaskInfo实体类
 * <p>Table: TASK_INFO</p>
 *
 * @author LY
 */
public class TaskImageEntity extends BaseEntity {


  private static final long serialVersionUID = -7528738187238993843L;
  private String id;
  /**
   * 任务id
   */
  private String taskId;
  /**
   * 图片路径
   */
  private String imageURL;
  /**
   * 图片创建时间
   */
  private Date createTime;


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public String getImageURL() {
    return imageURL;
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



  @Override
  public String toString() {
    return "TaskImageEntity";
  }
}
