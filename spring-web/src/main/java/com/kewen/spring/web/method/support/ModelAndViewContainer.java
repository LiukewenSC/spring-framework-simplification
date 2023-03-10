package com.kewen.spring.web.method.support;
/**
 * @descrpition 视图容器
 * @author kewen
 * @since 2023-03-08
 */
public class ModelAndViewContainer {

    /**
     * 请求已经被完全处理，默认未完全处理
     */
    private boolean requestHandled = false;

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }
    public boolean isRequestHandled() {
        return this.requestHandled;
    }
}
