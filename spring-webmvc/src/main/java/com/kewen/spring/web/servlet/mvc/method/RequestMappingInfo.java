package com.kewen.spring.web.servlet.mvc.method;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.servlet.mvc.condition.PatternsRequestCondition;

/**
 * @descrpition 请求映射信息
 * @author kewen
 * @since 2023-03-07
 */
public class RequestMappingInfo {
    @Nullable
    private final String name;

    private final PatternsRequestCondition patternsCondition;

    public RequestMappingInfo(String name, PatternsRequestCondition patternsCondition) {
        this.name = name;
        this.patternsCondition = patternsCondition;
    }

    public String getName() {
        return name;
    }

    public PatternsRequestCondition getPatternsCondition() {
        return patternsCondition;
    }
}
