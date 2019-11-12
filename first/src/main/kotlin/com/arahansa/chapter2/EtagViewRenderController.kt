package com.arahansa.chapter2

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.WebRequest

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#filters-cors
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-caching-etag-lastmodified
 */
@Controller
@RequestMapping("/chap2")
class EtagViewRenderController {

    fun getEtag(): Long {
        return 1L
    }

    @GetMapping("/etagview")
    fun eTagTest(webRequest: WebRequest, name: String?, model: Model): String? {
        val eTag: Long = getEtag()
        if (webRequest.checkNotModified(eTag)) {
            println("여기서 eTag널처리하는 부분이 들어가긴하겠죠..")
            return null
        }
        model.addAttribute("name", name)
        return "chap2/etagview"
    }

    @GetMapping("/originalview")
    fun eTagTestNo(webRequest: WebRequest, name: String?, model: Model): String {
        return "chap2/etagview"
    }
}
