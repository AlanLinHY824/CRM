package com.powernode.myenum;

/**
 * @author Alan Lin
 */
public enum ResultEnum {
    SUCCESS("SUCCESS",1001),
    USER_NOT_FOUND("用户名不存在",1002),
    PWD_ERROR("密码错误",1002),
    IP_lIMITED("IP地址受限",1003),
    ACC_EXPIRED("账户已过期，请联系管理员",1004),
    ACC_LOCKED("账户已被锁定，请联系管理员",1004),
    NOT_FOUND("未找到数据",1005),
    CLUE_NOT_FOUND("该线索已被删除",1005),
    Add_FAILED("添加数据失败",1005),
    RELATION_EXIST("该市场活动已关联，请勿重复关联！",1005),
    PARA_MISSING("必需参数未填写",1005),
    CUSTOMER_MISSING("客户名称未填写",1005),
    CONTACT_MISSING("联系人名称未填写",1005),
    OWNER_MISSING("所有者未填写",1005),
    TRANNAME_MISSING("交易名称未填写",1005),
    STAGE_MISSING("交易阶段未填写",1005),
    EXPECTEDDATE_MISSING("预计成交日期未填写",1005),
    UPDATE_FAILED("修改数据失败",1005),
    DELETE_FAILED("删除数据失败",1005),
    UNKNOWN_ERROR("未知异常",1005),
    VERIFYCODE_ERROR("获取验证码异常",1006),
    NONE_VERIFYCODE("请先获取验证码",1007),
    VERIFYCODE_WRONG("验证码不匹配",1008),

    IDVERIFY_ERROR("身份验证服务异常",2001),
    ID_INCONSISTENT("身份信息不一致",2002);

    private String message;
    private Integer code;

    ResultEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
