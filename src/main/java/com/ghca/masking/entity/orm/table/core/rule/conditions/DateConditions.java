package com.ghca.masking.entity.orm.table.core.rule.conditions;

public class DateConditions {

    private String starttime;

    private String endtime;

    //关联的规则ID
    private String ruleid;


    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }
}
