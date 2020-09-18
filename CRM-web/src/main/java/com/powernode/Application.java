package com.powernode;

import com.powernode.util.MD5Util;
import com.powernode.util.UUIDUtil;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
public class Application {
    public static void main(String[] args) {
        System.out.println(UUIDUtil.getUUID());
        System.out.println(MD5Util.getMD5("123"));
    }
}
