package com.ghca.masking.tools.permission;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthUtil {

    private static String fileName = "permission.properties";

    /**
     * @Description: 根据用户名查询规则权限
     * @return: Map
     */
    public static Map<String, String> queryByUsername(String username) {
        Map<String, String> ret = new HashMap<String, String>();
        Setting setting = new Setting(fileName, CharsetUtil.CHARSET_UTF_8, false);
        String content = setting.get(username);
        ret.put(username, content);
        return ret;
    }

    /**
     * @Description: 保存用户的规则权限
     * @return: ResultVO
     */
    public static void save(List<String> users, String ruleJson) {
        Setting setting = new Setting(fileName, CharsetUtil.CHARSET_UTF_8, false);
        users.forEach(user -> {
            setting.put(user, ruleJson);
        });
        setting.store(fileName);
    }

    /**
     * @Description: 删除用户的规则权限
     * @return: ResultVO
     */
    public static void delete(List<String> users) {
        Setting setting = new Setting(fileName, CharsetUtil.CHARSET_UTF_8, false);
        users.forEach(user -> {
            setting.remove(user);
        });
        setting.store(fileName);
    }

    public static void main(String[] args) {
        ArrayList arr = new ArrayList<String>();
        arr.add("张三");
        arr.add("李四");
        arr.add("王五");
        String val = "{\"code\":\"POP_00014\",\"msg\":\"成功22。\",\"flag\":true,\"data\":{\"total\":5,\"list\":[{\"id\":\"8c7874559c9d41e785e23955327e7f86\",\"note\":\"2\",\"name\":\"11111\",\"type\":\"2\",\"sysname\":\"数据源7111\",\"business\":\"系统名称2\",\"dataAreaList\":null,\"dataRuleList\":null,\"areanum\":1,\"rulenum\":1},{\"id\":\"02488a4eb7dd46e081f5d0241a2ed4b1\",\"note\":\"2\",\"name\":\"11\",\"type\":\"2\",\"sysname\":\"数据源7\",\"business\":\"系统名称2\",\"dataAreaList\":null,\"dataRuleList\":null,\"areanum\":1,\"rulenum\":1},{\"id\":\"c874e5f455174c89b6cb2be58dd0ee9c\",\"note\":\"2\",\"name\":\"资产1222\",\"type\":\"2\",\"sysname\":\"数据源7\",\"business\":\"系统名称2\",\"dataAreaList\":null,\"dataRuleList\":null,\"areanum\":2,\"rulenum\":2},{\"id\":\"393ad07d3ed14285b409ec2fde03c9bb\",\"note\":\"2\",\"name\":\"资产122\",\"type\":\"2\",\"sysname\":\"数据源7\",\"business\":\"系统名称2\",\"dataAreaList\":null,\"dataRuleList\":null,\"areanum\":1,\"rulenum\":1},{\"id\":\"6c9164cd241944d69c9148b7bf2e8323\",\"note\":\"2\",\"name\":\"资产1\",\"type\":\"2\",\"sysname\":\"数据源7\",\"business\":\"系统名称2\",\"dataAreaList\":null,\"dataRuleList\":null,\"areanum\":1,\"rulenum\":1}]},\"total\":5,\"pages\":0,\"pageSize\":0,\"pageNum\":0}";
        save(arr, val);
        System.out.println(queryByUsername("张三"));
    }

}
