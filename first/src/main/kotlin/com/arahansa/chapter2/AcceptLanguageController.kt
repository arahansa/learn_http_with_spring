package com.arahansa.chapter2

import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView

/**
 * 참고소스 : https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-etag-header.html
 */
@Controller
@RequestMapping("/chap2")
class AcceptLanguageController {

    @Autowired
    lateinit var messageSource: MessageSource

    fun getEtag(): Long {
        return 1L
    }

    @GetMapping("/html")
    fun citiesHtml(webRequest: WebRequest, model: Model): String? {
        val eTag: Long = getEtag()
        if (webRequest.checkNotModified(eTag)) return null
        model.addAttribute("cities", City.values())
        return "chap2/cities"
    }

    // 웹을 지탱하는 기술 책 492페이지
    @GetMapping("/html-gateway")
    fun citiesHtml(locale: Locale, request: HttpServletRequest, response: HttpServletResponse) {
        println("display lang : $locale.language")
        val redirectView = RedirectView("/chap2/html.${locale.language}")
        redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT)
        response.setHeader("Vary", "Accept-Language")
        response.setHeader("Content-Language", locale.language)
        redirectView.render(null, request, response)
    }

    @GetMapping("/html.{lang}")
    fun citiesHtmlByLang(webRequest: WebRequest, @PathVariable lang: String): ModelAndView? {
        if (webRequest.checkNotModified(getEtag())) return null
        val supportLangs = listOf("ko", "en")
        val mav = ModelAndView()
        // 욕심코드.. 그냥 404 때려도..^0^?
        if (!supportLangs.contains(lang)) {
            mav.addObject("langs", supportLangs.map { Locale(it) })
            mav.addObject("lang", lang)
            mav.status = HttpStatus.MULTIPLE_CHOICES
            mav.viewName = "chap2/multiple"
        } else {
            mav.addObject("lang", lang)
            mav.addObject("cities", City.values())
            mav.viewName = "chap2/cities-ajax"
            mav.status = HttpStatus.OK
        }
        return mav
    }

    @GetMapping("/cities.{lang}")
    @ResponseBody
    fun citiesByLang(req: HttpServletRequest, @PathVariable lang: String): ResponseEntity<List<String>> {
        val cityList = City.values().toList().map { messageSource.getMessage(it.key, null, Locale(lang)) }
        return ResponseEntity.ok()
            .eTag(cityList.hashCode().toString())
            .body(cityList)
    }

    @GetMapping("/cities")
    @ResponseBody
    fun cities(req: HttpServletRequest): ResponseEntity<List<String>> {
        val locale: Locale = LocaleContextHolder.getLocale()
        println("locale : " + locale)
        val cityList = City.values().toList().map { messageSource.getMessage(it.key, null, locale) }
        return ResponseEntity.ok()
            .eTag(cityList.hashCode().toString())
            .body(cityList)
    }

    @GetMapping("/cities-gate")
    fun citiesGate(locale: Locale): RedirectView {
        val redirectView: RedirectView = RedirectView("/")
        redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT)
        return redirectView
    }
}
