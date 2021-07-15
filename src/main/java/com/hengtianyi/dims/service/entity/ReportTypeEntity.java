package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import java.util.Date;

/** 
 * ReportType实体类
 * <p>Table: REPORT_TYPE</p>
 * @author LY
 */
public class ReportTypeEntity extends BaseEntity {


  private static final long serialVersionUID = 5716972485067868383L;
  /**
   * id
   */
  private Integer id;

  /** 
   * 角色Id
   */
  private Integer roleId;

  /** 
   * 内容
   */
  private String content;
  /**
   * 状态
   */
  private String state;

  /**
   * 是否是城区（目前符合条件的只有，滨河城区管委会的网格员，联络员视为城区，其他的视为农村）
   * 0：否
   * 1：是
   */
  private Integer flag;

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  /**
   * 顺序
   */
  private Integer sortNo;

  /** 
   * 时间
   */
  private Date createTime;


  /** 
   * 获取id属性(id)
   * @return id
   */
  public Integer getId(){
    return this.id;
  }

  /** 
   * 设置id属性
   * @param id id
   */
  public void setId(Integer id){
    this.id = id;
  }

  /** 
   * 获取roleId属性(角色Id)
   * @return 角色Id
   */
  public Integer getRoleId(){
    return this.roleId;
  }

  /** 
   * 设置roleId属性
   * @param roleId 角色Id
   */
  public void setRoleId(Integer roleId){
    this.roleId = roleId;
  }

  /** 
   * 获取content属性(内容)
   * @return 内容
   */
  public String getContent(){
    return this.content;
  }

  /** 
   * 设置content属性
   * @param content 内容
   */
  public void setContent(String content){
    this.content = content;
  }

  /** 
   * 获取sortNo属性(顺序)
   * @return 顺序
   */
  public Integer getSortNo(){
    return this.sortNo;
  }

  /** 
   * 设置sortNo属性
   * @param sortNo 顺序
   */
  public void setSortNo(Integer sortNo){
    this.sortNo = sortNo;
  }

  /** 
   * 获取createTime属性(时间)
   * @return 时间
   */
  public Date getCreateTime(){
    return this.createTime;
  }

  /** 
   * 设置createTime属性
   * @param createTime 时间
   */
  public void setCreateTime(Date createTime){
    this.createTime = createTime;
  }


  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  @Override
  public String toString() {
    return "ReportTypeEntity";
  }
}
