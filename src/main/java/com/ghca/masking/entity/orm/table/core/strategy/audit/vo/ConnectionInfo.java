package com.ghca.masking.entity.orm.table.core.strategy.audit.vo;

public class ConnectionInfo {

    // 客户端ip
    private String clientIp;
    //数据库url
    private String url;
    //数据库用户名
    private String userName;
    // 数据库ip
    private String dbIp;
    // 数据库schema
    private String schema;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
