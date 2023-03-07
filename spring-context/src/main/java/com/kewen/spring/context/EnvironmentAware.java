package com.kewen.spring.context;

import com.kewen.spring.beans.factory.Aware;
import com.kewen.spring.core.env.Environment;

/**
 * @descrpition 环境变量加载
 * @author kewen
 * @since 2023-03-07
 */
public interface EnvironmentAware extends Aware {
    void setEnvironment(Environment environment);
}
