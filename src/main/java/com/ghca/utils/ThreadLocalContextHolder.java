package com.ghca.utils;

import java.sql.DatabaseMetaData;

public class ThreadLocalContextHolder {
    /**
     * 不同业务设置不同的业务场景，如：业务A设置值为1，业务B设置值为2...
     */
    private static ThreadLocal<DatabaseMetaData> sceneThreadLocal = new ThreadLocal<>();


    public static DatabaseMetaData getScene() {
        return sceneThreadLocal.get();
    }

    public static void initScene(DatabaseMetaData metaData) {
        if (ThreadLocalContextHolder.sceneThreadLocal == null) {
            ThreadLocalContextHolder.sceneThreadLocal = new ThreadLocal<>();
        }
        ThreadLocalContextHolder.sceneThreadLocal.set(metaData);
    }

    public static void clearScene() {
        initScene(null);
    }

}