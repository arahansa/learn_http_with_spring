package com.arahansa.controller


import com.arahansa.practice.SimpleCachDto
import com.arahansa.practice.SimpleDto
import org.springframework.http.ResponseEntity
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

    @GetMapping("/simpledto2")
    fun simpleDto2(): ResponseEntity<SimpleDto>{
        val simpleDto = SimpleDto("arahansa")
        return ResponseEntity.ok().lastModified(1L).body(simpleDto)
    }

    @GetMapping("/simplecashdto")
    fun simpleCachDto() : SimpleCachDto {
        return SimpleCachDto("arahansa", LocalDateTime.of(2010, 11, 16, 8, 16));
    }

}
