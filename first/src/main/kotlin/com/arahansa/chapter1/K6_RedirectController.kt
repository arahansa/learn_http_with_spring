package com.arahansa.chapter1

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

// https://www.baeldung.com/spring-redirect-and-forward
/**
 * 리다이렉트에는 다섯가지가 있다고 한다.
 */
@Controller
@RequestMapping("/chap1/redirect")
class K6_RedirectController {

    @GetMapping("/301")
    fun redirect301(): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY)
        return redirectView
    }

    @GetMapping("/302")
    fun redirect302(): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.FOUND)
        return redirectView
    }

    @GetMapping("/303")
    fun redirect303(): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.SEE_OTHER)
        return redirectView
    }

    @GetMapping("/307 ")
    fun redirect307(): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT)
        return redirectView
    }

    @GetMapping("/308 ")
    fun redirect308(): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY)
        return redirectView
    }


}