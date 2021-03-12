package com.ghca.masking.entity.orm.table.core.strategy.audit.conditions;




public class AuditIpConditions  {
    //主键
    private String id;
    //匹配符号 = <> =% <>%
    private String matchsymbol;
    //配置的值
    private String matchval;
    //IP段 起始段
    private String startval;
    //IP段终止段
    private String endval;
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

    public String getStartval() {
        return startval;
    }

    public void setStartval(String startval) {
        this.startval = startval;
    }

    public String getEndval() {
        return endval;
    }

    public void setEndval(String endval) {
        this.endval = endval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
