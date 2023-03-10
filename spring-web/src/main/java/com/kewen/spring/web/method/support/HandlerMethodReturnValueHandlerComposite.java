package com.kewen.spring.web.method.support;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler{

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    public HandlerMethodReturnValueHandlerComposite(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        this.returnValueHandlers.addAll(returnValueHandlers);
    }

    public List<HandlerMethodReturnValueHandler> getHandlers() {
        return Collections.unmodifiableList(this.returnValueHandlers);
    }
    @Nullable
    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return getReturnValueHandler(returnType) !=null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);

        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
    @Nullable
    private HandlerMethodReturnValueHandler selectHandler(@Nullable Object value, MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }
}
