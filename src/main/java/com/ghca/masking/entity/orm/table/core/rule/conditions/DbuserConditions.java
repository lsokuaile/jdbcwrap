package com.ghca.masking.entity.orm.table.core.rule.conditions;

public class DbuserConditions {
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
}
