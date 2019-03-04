package org.nci.platform.common.consts;

public class ValidConsts {
	public static final String MESSAGES_TOOLONG = "{0}最大长度不能超过{1}个字符！?FieldModifier.label&Size.max";
	public static final String MESSAGES_NOTNULL = "{0}不能为空！?FieldModifier.label";
	public static final String MESSAGES_MAX = "{0}的最大值是{1}！?FieldModifier.label&Max.value";
	public static final String MESSAGES_MIN = "{0}的最小值是{1}！?FieldModifier.label&Min.value";
	
	public static final String MESSAGES_NOTMOBILEPHONE = "不是有效手机号！";
	public static final String MESSAGES_OUTRANGE_JINE = "{0}的取值范围是0～99999.9999，最多4位小数！?FieldModifier.label";
	public static final String MESSAGES_OUTRANGE_SHULIANG = "{0}的取值范围是0～99999的整数！?FieldModifier.label";
	
	
	public static final String PATTERN_MOBILEPHONE = "^1[3|4|5|7|8]\\d{9}$";
	public static final String PATTERN_TELEPHONE = "^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}$";
	public static final String PATTERN_JINE = "^((([1-9][0-9]{0,5})|0)(\\.|(\\.\\d{1,4})?))$";

	public static final String PATTERN_SHULIANG = "^(([1-9][0-9]{0,5})|0)$";
}
