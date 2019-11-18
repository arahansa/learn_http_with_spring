package com.arahansa.practice

import org.springframework.web.servlet.mvc.LastModified
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.servlet.http.HttpServletRequest


data class SimpleCachDto(val name:String, val modified: LocalDateTime) : LastModified{
    override fun getLastModified(request: HttpServletRequest): Long {
        return modified.toEpochSecond(ZoneOffset.UTC);
    }
}
