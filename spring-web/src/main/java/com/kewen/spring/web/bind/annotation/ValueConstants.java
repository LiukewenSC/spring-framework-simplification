package com.kewen.spring.web.bind.annotation;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public interface ValueConstants {

    /**
     * 常量定义了一个没有默认值的值-作为null的替换，我们不能在注释属性中使用null。
     * 这是16个unicode字符的人工排列，其唯一目的是永远不匹配用户声明的值。
     */
    String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";
}
