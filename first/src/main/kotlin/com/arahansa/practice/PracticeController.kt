package com.arahansa.practice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/practice")
class PracticeController {

    @GetMapping("/simpledto")

    fun simpleDto() : SimpleDto {
        return SimpleDto("arahansa")
    }

    @GetMapping("/simplecashdto")
    fun simpleCachDto() : SimpleCachDto {
        return SimpleCachDto("arahansa", LocalDateTime.of(2010, 11, 16, 8, 16));
    }

}
