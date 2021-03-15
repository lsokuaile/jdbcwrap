package com.ghca.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 请求工具类
 * Created by macro on 2020/10/8.
 */
public class RequestUtil {

    /**
     * 获取请求真实IP地址
     */
    public static String getRequestIp(HttpServletRequest request) {
        //通过HTTP代理服务器转发时添加
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            // 从本地访问时根据网卡取本机配置的IP
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inetAddress.getHostAddress();
            }
        }
        // 通过多个代理转发的情况，第一个IP为客户端真实IP，多个IP会按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**

     * 判断IP是否在指定范围

     * @param ipStart

     * @param ipEnd

     * @param ip

     * @return

     */

    public static boolean ipIsValid(String ipStart,String ipEnd, String ip) {

        if (StringUtils.isEmpty(ipStart)) {

            throw new NullPointerException("起始IP不能为空！");

        }

        if (StringUtils.isEmpty(ipEnd)) {

            throw new NullPointerException("结束IP不能为空！");

        }

        if (StringUtils.isEmpty(ip)) {

            throw new NullPointerException("IP不能为空！");

        }

        ipStart = ipStart.trim();

        ipEnd = ipEnd.trim();

        ip = ip.trim();

        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";

        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;

        if (!ipStart.matches(REGX_IP) || !ip.matches(REGX_IP) || !ipEnd.matches(REGX_IP)) {

            return false;

        }

        String[] sips = ipStart.split("\\.");

        String[] sipe = ipEnd.split("\\.");

        String[] sipt = ip.split("\\.");

        long ips = 0L, ipe = 0L, ipt = 0L;

        for (int i = 0; i < 4; ++i) {

            ips = ips << 8 | Integer.parseInt(sips[i]);

            ipe = ipe << 8 | Integer.parseInt(sipe[i]);

            ipt = ipt << 8 | Integer.parseInt(sipt[i]);

        }

        if (ips > ipe) {

            long t = ips;

            ips = ipe;

            ipe = t;

        }

        return ips <= ipt && ipt <= ipe;

    }

}
