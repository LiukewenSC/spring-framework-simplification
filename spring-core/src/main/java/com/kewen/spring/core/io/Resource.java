package com.kewen.spring.core.io;

import java.io.InputStream;

/**
 * @descrpition 资源
 * @author kewen
 * @since 2023-02-06 14:45
 */
public interface Resource {

    InputStream getInputStream();
}
