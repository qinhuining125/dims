package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;
import java.util.List;

/**
 * @author LY
 */
public class TaskInfoDto extends BaseBean {

  private static final long serialVersionUID = -9006156916467848256L;

  /**
   * 上传图片
   */
  private String images;
  /**
   * 内容
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

  public String getImages() {
    return images;
  }
  public void setImages(String images) {
    this.images = images;
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
}
