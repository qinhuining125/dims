package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import java.util.Date;

/**
 * TaskFlow实体类
 * <p>Table: task_flow</p>
 *
 * @author LY
 */
public class TaskFlowEntity extends BaseEntity {


  private static final long serialVersionUID = 3056894414245085812L;
  /**
   * id
   */
  private String id;

  private String taskId;

  /**
   * 接受人员Id
   */
  private String receiveId;

  /**
   * 接收角色id
   */
  private Integer receiveRoleId;

  /**
   * 接受人员
   */
  private String receiveName;

  /**
   * 办结内容
   */
  private String remark;

  /**
   * 状态 0未受理，1已受理，2办结
   */
  private Integer state;

  /**
   * 时间
   */
  private Date createTime;


  /**
   * 获取id属性(id)
   *
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * 设置id属性
   *
   * @param id id
   */
  public void setId(String id) {
    this.id = id;
  }

  public String getTaskId() {
    return this.taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  /**
   * 获取receiveId属性(接受人员Id)
   *
   * @return 接受人员Id
   */
  public String getReceiveId() {
    return this.receiveId;
  }

  /**
   * 设置receiveId属性
   *
   * @param receiveId 接受人员Id
   */
  public void setReceiveId(String receiveId) {
    this.receiveId = receiveId;
  }

  /**
   * 获取receiveRoleId属性(接收角色id)
   *
   * @return 接收角色id
   */
  public Integer getReceiveRoleId() {
    return this.receiveRoleId;
  }

  /**
   * 设置receiveRoleId属性
   *
   * @param receiveRoleId 接收角色id
   */
  public void setReceiveRoleId(Integer receiveRoleId) {
    this.receiveRoleId = receiveRoleId;
  }

  /**
   * 获取remark属性(办结内容)
   *
   * @return 办结内容
   */
  public String getRemark() {
    return this.remark;
  }

  /**
   * 设置remark属性
   *
   * @param remark 办结内容
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

  /**
   * 获取state属性(状态)
   *
   * @return 状态
   */
  public Integer getState() {
    return this.state;
  }

  /**
   * 设置state属性
   *
   * @param state 状态
   */
  public void setState(Integer state) {
    this.state = state;
  }

  /**
   * 获取createTime属性(时间)
   *
   * @return 时间
   */
  public Date getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getReceiveName() {
    return receiveName;
  }

  public void setReceiveName(String receiveName) {
    this.receiveName = receiveName;
  }

  @Override
  public String toString() {
    return "TaskFlowEntity";
  }
}
