package com.arahansa.chapter1

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/chap1/url")
class K7_UrlController {

    @GetMapping
    @ResponseBody
    fun url(req: HttpServletRequest) : String{
        return "scheme: ${req.scheme} , host : ${req.remoteHost} , path : ${req.servletPath}"
    }
}