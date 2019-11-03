package com.arahansa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @Autowired
    ApplicationContext applicationContext;

    @ResponseBody
    @GetMapping("/bean-list")
    public List<String> beanList() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        return Arrays.asList(beanNames);
    }


}
