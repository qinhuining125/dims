package com.hengtianyi.dims.feature;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.StringUtil;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 全局控制器
 *
 * @author BBF
 */
@ControllerAdvice
public class BaseControllerAdvice {

  private static final int PAGE_MIN = 10;
  private static final int PAGE_MAX = 50;
  private static final IllegalArgumentException ILLEGAL = new IllegalArgumentException();
  private static final String[] PARSE_PATTERNS =
      new String[]{BaseConstant.DATE_FORMAT1, BaseConstant.DATE_FORMAT2, BaseConstant.DATE_FORMAT3};

  /**
   * 初始化数据绑定
   * <ol><li>将所有传递进来的String进行HTML编码，防止XSS攻击</li>
   * <li>将字段中Date类型转换为String类型</li></ol>
   *
   * @param binder WebDataBinder
   */
  @InitBinder
  public void initBinder(final WebDataBinder binder) {
    // 全局处理列表的最大页数，防止大集合
    binder.registerCustomEditor(Integer.class, "rowCount", new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        if (StringUtil.isNoneBlank(text)) {
          int rowCount = NumberUtils.toInt(text, PAGE_MIN);
          if (rowCount > PAGE_MAX) {
            throw ILLEGAL;
          }
          if (rowCount < PAGE_MIN) {
            rowCount = PAGE_MIN;
          }
          setValue(rowCount);
        }
      }
    });

    // Date 类型转换，参考 org.springframework.beans.propertyeditors.CustomDateEditor
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        try {
          setValue(DateUtils.parseDate(text, PARSE_PATTERNS));
        } catch (ParseException e) {
          throw ILLEGAL;
        }
      }

      @Override
      public String getAsText() {
        Date value = (Date) this.getValue();
        return value != null ? DateFormatUtils.format(value, BaseConstant.DATE_FORMAT2)
            : StringUtil.EMPTY;
      }
    });
  }
}
