package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/**
 * 业务表实体的基类
 * <p>主要放置创建修改人、时间和备注字段</p>
 *
 * @author BBF
 */
public class AbstractEntity extends BaseEntity {

  private static final long serialVersionUID = -2084316436029226097L;

  /**
   * 备注说明
   */
  private String remark;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 创建时间
   */
  private Long createTime;

  /**
   * 修改人
   */
  private String updateBy;

  /**
   * 修改时间
   */
  private Long updateTime;

  /**
   * 获取remark属性(备注说明)
   *
   * @return 备注说明
   */
  public String getRemark() {
    return this.remark;
  }

  /**
   * 设置remark属性
   *
   * @param remark 备注说明
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }

  /**
   * 获取createBy属性(创建人)
   *
   * @return 创建人
   */
  public String getCreateBy() {
    return this.createBy;
  }

  /**
   * 设置createBy属性
   *
   * @param createBy 创建人
   */
  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  /**
   * 获取createTime属性(创建时间)
   *
   * @return 创建时间
   */
  public Long getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 创建时间
   */
  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  /**
   * 获取updateBy属性(修改人)
   *
   * @return 修改人
   */
  public String getUpdateBy() {
    return this.updateBy;
  }

  /**
   * 设置updateBy属性
   *
   * @param updateBy 修改人
   */
  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  /**
   * 获取updateTime属性(修改时间)
   *
   * @return 修改时间
   */
  public Long getUpdateTime() {
    return this.updateTime;
  }

  /**
   * 设置updateTime属性
   *
   * @param updateTime 修改时间
   */
  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    return "AbstractEntity{" +
        "remark='" + remark + '\'' +
        ", createBy='" + createBy + '\'' +
        ", createTime=" + createTime +
        ", updateBy='" + updateBy + '\'' +
        ", updateTime=" + updateTime +
        '}';
  }
}
