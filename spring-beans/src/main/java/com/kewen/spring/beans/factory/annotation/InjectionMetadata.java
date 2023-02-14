package com.kewen.spring.beans.factory.annotation;

import com.kewen.spring.beans.PropertyValues;
import com.kewen.spring.core.lang.Nullable;

import java.util.List;

/**
 * @descrpition 自动注入的 bean原数据
 * @author kewen
 * @since 2023-02-15
 */
public class InjectionMetadata {

    private List<AutowiredElement> elements;

    public List<AutowiredElement> getElements() {
        return elements;
    }

    public void setElements(List<AutowiredElement> elements) {
        this.elements = elements;
    }
}
