package com.kewen.spring.web.servlet.mvc.condition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
public class PatternsRequestCondition {
    private final Set<String> patterns=new HashSet<>();

    public PatternsRequestCondition(String... patterns) {
        this.patterns.addAll(Arrays.asList(patterns));
    }

    public Set<String> getPatterns() {
        return patterns;
    }
}
