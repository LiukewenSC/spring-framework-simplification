package com.kewen.spring.beans;

import cn.hutool.core.bean.BeanUtil;
import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 属性值
 * @author kewen
 * @since 2023-02-12 14:40
 */
public class PropertyValue {
    @Nullable
    private Object source;

    private final String name;

    /**
     * 配置的值，此处有两种类型的值，
     * 第一种，从xml中解析的 property标签中的value值或ref值
     * 第二种，在填充属性population中得到属性对应的bean之后将其替换为bean
     *
     */
    @Nullable
    private final Object value;

    public PropertyValue(String name, @Nullable Object value) {
        this.name = name;
        this.value = value;
    }

    public Object getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}