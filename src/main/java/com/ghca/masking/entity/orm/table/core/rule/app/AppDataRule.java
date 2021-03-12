package com.ghca.masking.entity.orm.table.core.rule.app;


public class AppDataRule {

    //规则名称
    private String rulename;
    //规则类型 1：脱敏规则 2：审计规则 3：加/解密规则
    private String type;
    //规则所属分组
    private String belongtogroup;
    //规则描述
    private String remark;
    //脱敏表达式
    private String desensitizationexpression;
    //审计开关 0:关 1：开
    private String auditswitch;

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBelongtogroup() {
        return belongtogroup;
    }

    public void setBelongtogroup(String belongtogroup) {
        this.belongtogroup = belongtogroup;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDesensitizationexpression() {
        return desensitizationexpression;
    }

    public void setDesensitizationexpression(String desensitizationexpression) {
        this.desensitizationexpression = desensitizationexpression;
    }

    public String getAuditswitch() {
        return auditswitch;
    }

    public void setAuditswitch(String auditswitch) {
        this.auditswitch = auditswitch;
    }
}
