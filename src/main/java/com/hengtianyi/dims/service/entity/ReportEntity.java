package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/**
 * Report实体类
 * <p>Table: REPORT</p>
 *
 * @author LY
 */
public class ReportEntity extends BaseEntity {

  private static final long serialVersionUID = -422178481211627582L;
  private Long id;

  private String areaCode;

  private String serialNo;

  private String reportNo;

  public ReportEntity() {
  }

  public ReportEntity(String areaCode, String serialNo, String reportNo) {
    this.areaCode = areaCode;
    this.serialNo = serialNo;
    this.reportNo = reportNo;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getSerialNo() {
    return this.serialNo;
  }

  public void setSerialNo(String serialNo) {
    this.serialNo = serialNo;
  }

  public String getReportNo() {
    return this.reportNo;
  }

  public void setReportNo(String reportNo) {
    this.reportNo = reportNo;
  }

  @Override
  public String toString() {
    return "ReportEntity";
  }
}
