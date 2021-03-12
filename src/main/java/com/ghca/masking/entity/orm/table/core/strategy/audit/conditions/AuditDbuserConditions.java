package com.ghca.masking.entity.orm.table.core.strategy.audit.conditions;




public class AuditDbuserConditions  {
    //主键
    private String id;
    //匹配符号 = <> =% <>%
    private String matchsymbol;
    //配置的值
    private String matchval;
    //关联的规则ID
    private String ruleid;

    public String getMatchsymbol() {
        return matchsymbol;
    }

    public void setMatchsymbol(String matchsymbol) {
        this.matchsymbol = matchsymbol;
    }

    public String getMatchval() {
        return matchval;
    }

    public void setMatchval(String matchval) {
        this.matchval = matchval;
    }

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
