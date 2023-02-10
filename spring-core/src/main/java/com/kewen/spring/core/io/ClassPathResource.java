package com.kewen.spring.core.io;

import com.kewen.spring.core.lang.Nullable;

import java.io.InputStream;

/**
 * @descrpition Class解析器
 * @author kewen
 * @since 2023-02-06 14:51
 */
public class ClassPathResource implements Resource {
    private final String path;

    @Nullable
    private ClassLoader classLoader;

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader;
    }

    @Override
    public InputStream getInputStream() {
        //此处用hutool的resource加载，只要保证不用spring的就可以了
        cn.hutool.core.io.resource.ClassPathResource resource = new cn.hutool.core.io.resource.ClassPathResource(path,classLoader);
        return resource.getStream();
    }
}
