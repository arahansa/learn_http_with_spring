package com.arahansa.chapter2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.ServletWebRequest
import javax.servlet.http.HttpServletRequest

/**
 * 참고소스 : https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-etag-header.html
 */
@Controller
@RequestMapping("/chap2")
class AcceptLanguageController {

    @Autowired
    lateinit var messageSource: MessageSource


    fun getETag(): String {
        return "version1"
    }

    @GetMapping("/html")
    fun citiesHtml(swr: ServletWebRequest, model: Model) : String?{
        if(swr.isNotModified) return null
        model.addAttribute("cities", City.values())
        return "chap2/cities"
    }


    @GetMapping("/cities")
    @ResponseBody
    fun cities(req: HttpServletRequest): ResponseEntity<List<String>> {


        val locale = LocaleContextHolder.getLocale()
        println("locale : "+locale)
        val cityList = City.values().toList().map { messageSource.getMessage(it.key, null, locale) }
        return ResponseEntity.ok()
                .eTag(cityList.hashCode().toString())
                .body(cityList)
    }

}