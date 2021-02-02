package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author
 */
public class RevealInfoDto extends BaseBean {

  private static final long serialVersionUID = 5017373809545239291L;



  /**
   * 举报人姓名
   */
  private String userName;

  private String userId;

  /**
   * 联系方式
   */
  private String contact;

  /**
   * 举报类型：0匿名，1实名
   */
  private Short type;

  /**
   * 乡镇
   */
  private String townCode;

  /**
   * 村
   */
  private String villageCode;

  /**
   * 巡察轮次的id
   */
  private String inspectionId;

  /**
   * 举报对象
   */
  private String revealTarget;

  /**
   * 举报内容
   */
  private String content;
  /**
   * 接受人员id
   */
  private List<String> receiveId;

  /**
   * 接受人员角色id
   */
  private List<Integer> receiveRoleId;

  //图片接收
  private List<String> imageArray;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public Short getType() {
    return type;
  }

  public void setType(Short type) {
    this.type = type;
  }

  public String getTownCode() {
    return townCode;
  }

  public void setTownCode(String townCode) {
    this.townCode = townCode;
  }

  public String getVillageCode() {
    return villageCode;
  }

  public void setVillageCode(String villageCode) {
    this.villageCode = villageCode;
  }

  public String getInspectionId() {
    return inspectionId;
  }

  public void setInspectionId(String inspectionId) {
    this.inspectionId = inspectionId;
  }

  public String getRevealTarget() {
    return revealTarget;
  }

  public void setRevealTarget(String revealTarget) {
    this.revealTarget = revealTarget;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getReceiveId() {
    return receiveId;
  }

  public void setReceiveId(List<String> receiveId) {
    this.receiveId = receiveId;
  }

  public List<Integer> getReceiveRoleId() {
    return receiveRoleId;
  }

  public void setReceiveRoleId(List<Integer> receiveRoleId) {
    this.receiveRoleId = receiveRoleId;
  }

  public List<String> getImageArray() {
    return imageArray;
  }

  public void setImageArray(List<String> imageArray) {
    this.imageArray = imageArray;
  }
}
