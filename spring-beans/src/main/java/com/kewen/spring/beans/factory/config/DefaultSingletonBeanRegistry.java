package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.factory.ObjectFactory;
import com.kewen.spring.core.lang.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 默认的单例注册容器
 * @author kewen
 * @since 2023-02-07 16:23
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** Maximum number of suppressed exceptions to preserve. */
    private static final int SUPPRESSED_EXCEPTIONS_LIMIT = 100;


    /** Cache of singleton objects: bean name to bean instance. */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** Cache of singleton factories: bean name to ObjectFactory. */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /** Cache of early singleton objects: bean name to bean instance. */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /** Set of registered singletons, containing the bean names in registration order. */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    /** Names of beans that are currently in creation. */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** Names of beans currently excluded from in creation checks. */
    private final Set<String> inCreationCheckExclusions =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** Collection of suppressed Exceptions, available for associating related causes. */
    @Nullable
    private Set<Exception> suppressedExceptions;

    /** Flag that indicates whether we're currently within destroySingletons. */
    private boolean singletonsCurrentlyInDestruction = false;

    /** Disposable bean instances: bean name to disposable instance. */
    private final Map<String, Object> disposableBeans = new LinkedHashMap<>();

    /** Map between containing bean names: bean name to Set of bean names that the bean contains. */
    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    /** Map between dependent bean names: bean name to Set of dependent bean names. */
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    /** Map between depending bean names: bean name to Set of bean names for the bean's dependencies. */
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName,singletonObject);

    }

    @Override
    public Object getSingleton(String beanName) {

        return singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        Set<String> strings = singletonObjects.keySet();
        return strings.toArray(new String[0]);
    }

    @Override
    public int getSingletonCount() {
        return singletonObjects.size();
    }
}
