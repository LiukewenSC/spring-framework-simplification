package com.kewen.spring.web.sample.advance;

import com.kewen.spring.context.annotation.Controller;
import com.kewen.spring.web.bind.annotation.ControllerAdvice;
import com.kewen.spring.web.bind.annotation.ExceptionHandler;
import com.kewen.spring.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-15
 */
@ControllerAdvice
@Controller
@ResponseBody
public class SampleControllerAdvice {


    @ExceptionHandler(RuntimeException.class)
    public Object runtimeException(RuntimeException t){
        System.out.println(t);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("message",t.getMessage());
        return map;
    }
    @ExceptionHandler(Exception.class)
    public Object exception(Exception t){
        System.out.println(t);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success",false);
        map.put("message",t.getMessage());
        return map;
    }
    @ExceptionHandler(Throwable.class)
    public Object throwable(Throwable t){
        System.out.println(t);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("message",t.getMessage());
        return map;
    }


}
