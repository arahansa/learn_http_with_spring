package com.arahansa.chapter2

import java.time.LocalDateTime
import java.time.ZoneOffset
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.filter.ShallowEtagHeaderFilter

/**
 * 리얼HTTP 책 92 페이지
 * https://www.baeldung.com/etags-for-rest-with-spring
 * https://medium.com/@bbirec/http-%EC%BA%90%EC%89%AC%EB%A1%9C-api-%EC%86%8D%EB%8F%84-%EC%98%AC%EB%A6%AC%EA%B8%B0-2effb1bfab12
 * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/ResponseEntity.HeadersBuilder.html
 */
@Controller
@RequestMapping("/chap2")
class HttpCashController {

    @GetMapping("/etag")
    fun chap2ETag(): String {
        return "chap2/etag"
    }

    @ResponseBody
    @GetMapping("/state-nocash")
    fun noCash(): List<String> {
        val list = ArrayList<String>()
        for (x in 0 until 10000)
            list.add("아라한사" + x)
        return list
    }

    @ResponseBody
    @GetMapping("/state")
    fun eTag(): ResponseEntity<List<String>> {
        val list = ArrayList<String>()
        for (x in 0 until 10000)
            list.add("아라한사" + x)
        return ResponseEntity.ok()
                .eTag(list.hashCode().toString())
                .body(list)
    }

    @ResponseBody
    @GetMapping("/state-last-modified")
    fun lastModified(): ResponseEntity<List<String>> {
        val list = ArrayList<String>()
        for (x in 0 until 10000)
            list.add("아라한사" + x)
        return ResponseEntity.ok()
                .lastModified(LocalDateTime.of(2019, 11, 5, 0, 4, 0).toEpochSecond(ZoneOffset.UTC))
                .body(list)
    }

    @Bean
    fun shallowEtagHeaderFilterApply(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val filterRegistrationBean = FilterRegistrationBean(ShallowEtagHeaderFilter())
        filterRegistrationBean.addUrlPatterns("/chap2/**", "/practice/**")
        filterRegistrationBean.setName("etagFilter")
        return filterRegistrationBean
    }
}
