package com.liuyanzhao.framework;

import com.liuyanzhao.common.utils.SecurityUtils;

/**
 * @author 言曌
 * @since 2024/1/5 21:24
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(SecurityUtils.encryptPassword("123456"));
    }
}
