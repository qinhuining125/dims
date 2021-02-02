package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import java.util.Date;

/**
 * ClueFlow实体类
 * <p>Table: clue_flow</p>
 *
 * @author LY
 */
public class ClueFlowEntity extends BaseEntity {


  private static final long serialVersionUID = 1141827256491046361L;
  private String id;

  /**
   * 上报线索id
   */
  private String clueId;

  /**
   * 状态
   */
  private Integer state;

  /**
   * 受理人id
   */
  private String receiveId;

  /**
   * 受理人名
   */
  private String receiveName;
  /**
   * 备注
   */
  private String remark;

  /**
   * 时间
   */
  private Date createTime;


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * 获取clueId属性(上报线索id)
   *
   * @return 上报线索id
   */
  public String getClueId() {
    return this.clueId;
  }

  /**
   * 设置clueId属性
   *
   * @param clueId 上报线索id
   */
  public void setClueId(String clueId) {
    this.clueId = clueId;
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
   * 获取reveiveId属性(受理人id)
   *
   * @return 受理人id
   */
  public String getReceiveId() {
    return this.receiveId;
  }

  /**
   * 设置reveiveId属性
   *
   * @param receiveId 受理人id
   */
  public void setReceiveId(String receiveId) {
    this.receiveId = receiveId;
  }

  /**
   * 获取remark属性(备注)
   *
   * @return 备注
   */
  public String getRemark() {
    return this.remark;
  }

  /**
   * 设置remark属性
   *
   * @param remark 备注
   */
  public void setRemark(String remark) {
    this.remark = remark;
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
    return "ClueFlowEntity";
  }
}
