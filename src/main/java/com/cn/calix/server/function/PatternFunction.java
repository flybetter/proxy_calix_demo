package com.cn.calix.server.function;

import com.cn.calix.server.dto.ProxyResult;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/5/15
 * Time: 下午3:23
 */
@FunctionalInterface
public interface PatternFunction {
    public ProxyResult check (Pattern pattern, String command);
}
