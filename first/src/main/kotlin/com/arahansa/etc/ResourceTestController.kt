package com.arahansa.etc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/test")
class ResourceTestController {

    @GetMapping("/resource")
    fun resourceTestPage(): String{
        return "etc/resource"
    }

}