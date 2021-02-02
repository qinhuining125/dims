package com.hengtianyi.dims.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dto.UserDto;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;

public class ExcelTest extends TestDbBase {

  @Resource
  private SysUserService sysUserService;

  @Test
  public void read() {
    ImportParams params = new ImportParams();
    params.setSheetNum(1);
    List<UserDto> list = ExcelImportUtil.importExcel(new File("D:/temp/user.xls"), UserDto.class, params);
    System.out.println(JsonUtil.toJson(list));
    for (UserDto user : list) {
      SysUserEntity entity = new SysUserEntity();
      entity.setId(IdGenUtil.uuid32());
      entity.setUserAccount(user.getAccount());
      entity.setPassword(user.getPassword());
      entity.setUserName(user.getUserName());
      entity.setEnabled(1);
      entity.setSex(user.getSex());
      entity.setAreaCode(user.getAreaCode());
      entity.setAreaName(user.getAreaName());
      entity.setRoleId(1001);
      entity.setIdCard(user.getIdCard());
      entity.setCreateTime(SystemClock.now());
      sysUserService.insertData(entity);
    }
  }

  @Test
  public void read2() {
    ImportParams params = new ImportParams();
    List<UserAccount> list = ExcelImportUtil
        .importExcel(new File("D:/temp/check.xlsx"), UserAccount.class, params);
    System.out.println(JsonUtil.toJson(list));
    for (UserAccount user : list) {
      SysUserEntity entity = new SysUserEntity();
      String account = user.getAccount();
      if (!sysUserService.checkRepeat("", account)) {
        continue;
      }
      System.out.println("新用户-----" + account);
      entity.setId(IdGenUtil.uuid32());
      entity.setUserAccount(account);
      entity.setPassword(user.getPassword());
      entity.setUserName(user.getUserName());
      entity.setEnabled(1);
      if (account.length() <= 5) {
        entity.setRoleId(1004);
      } else if (account.length() == 9) {
        entity.setRoleId(1003);
      } else if (account.length() == 12) {
        entity.setRoleId(1002);
      } else if (account.length() == 15) {
        entity.setRoleId(1001);
      }
      entity.setAreaCode(account);
      entity.setCreateTime(SystemClock.now());
      sysUserService.insertData(entity);
    }
  }

  @Test
  public void insert() {
    SysUserEntity entity = new SysUserEntity();
    entity.setId(IdGenUtil.uuid32());
    entity.setUserAccount("gongjian");
    entity.setPassword("1");
    entity.setUserName("纪委副书记、监委副主任-弓剑");
    entity.setEnabled(1);
    entity.setPhone("13303442566");
    entity.setRoleId(1005);
    entity.setSex("男");
    entity.setCreateTime(SystemClock.now());
    sysUserService.insertData(entity);
  }
}
