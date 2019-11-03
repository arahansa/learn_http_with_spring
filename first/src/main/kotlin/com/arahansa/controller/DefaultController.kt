package com.arahansa.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class DefaultController {

    @GetMapping("/")
    fun idx(): String {
        return "index"
    }
}