package com.kewen.spring.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @descrpition 可变的属性填充值
 * @author kewen
 * @since 2023-02-12 14:03
 */
public class MutablePropertyValues implements PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(propertyName)){
                return propertyValue;
            }
        }
        return null;
    }

    @Override
    public boolean contains(String propertyName) {
        PropertyValue propertyValue = getPropertyValue(propertyName);
        return propertyValue !=null;
    }

    public void add(String name,Object value){
        addOrReplacePropertyValue(new PropertyValue(name,value));
    }

    /**
     * 加载或更新值
     * @param propertyValue
     */
    public void addOrReplacePropertyValue(PropertyValue propertyValue){
        for (int i = 0; i < propertyValueList.size(); i++) {
            PropertyValue oldValue = propertyValueList.get(i);
            if (oldValue.getName().equals(propertyValue.getName())){
                propertyValueList.set(i,propertyValue);
                return;
            }
        }
        propertyValueList.add(propertyValue);
    }
}