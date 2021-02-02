package com.hengtianyi.dims.constant;

/**
 * 角色枚举类
 *
 * @author LY
 */
public enum RoleEnum {

  /**
   *
   */
  WGY("村（社）网格员", 1001),

  WGLLY("纪检监察网格联络员", 1002),

  XZJWGLY("乡镇纪委管理员", 1003),

  XJWLXS("县纪委联系室/巡察办", 1004),

  FGLD("分管领导", 1005),

  XJLDBZ("县级领导班子", 1006),

  ADMIN("超级管理员", 1007),

  XCBADMIN("巡察办主任", 1008),

  XC1GROUP("巡察一组", 1009),

  XC2GROUP("巡察二组", 1010),

  XC3GROUP("巡察三组", 1011),

  MASSES("群众", 2000);

  /**
   * 监测分类
   */
  private final String name;
  /**
   * 编号
   */
  private final Integer roleId;

  RoleEnum(String name, Integer roleId) {
    this.name = name;
    this.roleId = roleId;
  }

  /**
   * 根据code获取name
   *
   * @param roleId id
   * @return name 值
   */
  public static String getNameByRoleId(Integer roleId) {
    for (RoleEnum roleEnum : RoleEnum.values()) {
      if (roleEnum.getRoleId().equals(roleId)) {
        return roleEnum.name;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public Integer getRoleId() {
    return roleId;
  }
}
