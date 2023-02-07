package com.kewen.spring.context;
/**
 * @descrpition 应用事件
 * @author kewen
 * @since 2023-02-07 16:07
 */
public abstract class ApplicationEvent {
    protected transient final Object source;

    public ApplicationEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
