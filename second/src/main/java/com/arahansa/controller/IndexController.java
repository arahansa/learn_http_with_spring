package com.arahansa.controller;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.*;

// second
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }


    @GetMapping("/plain")
    public String plain(){
        return "plain";
    }

    @GetMapping("/plain-cash")
    public String indexCash(WebRequest webRequest) {
        if(webRequest.checkNotModified("1")){
            return null;
        }
        return "plain";
    }
    @GetMapping("/plain2")
    public String redirect(){
        return "redirect:/plain";
    }


    @Autowired
    ApplicationContext applicationContext;

    @ResponseBody
    @GetMapping("/bean-list")
    public List<String> beanList() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        return Arrays.asList(beanNames);
    }


    @ResponseBody
    @GetMapping("/view-resolvers")
    public Set<String> viewResolvers() throws Exception {
        Map<String, ViewResolver> matchingBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ViewResolver.class, true, false);
        Set<String> strings = matchingBeans.keySet();
        for(String key : strings){
            ViewResolver viewResolver = matchingBeans.get(key);
            System.out.println(viewResolver.getClass().getName());
            View index = viewResolver.resolveViewName("index", Locale.KOREA);

            System.out.println(index);
        }
        return strings;
    }


}
