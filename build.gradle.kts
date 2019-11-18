import org.springframework.boot.gradle.tasks.bundling.BootJar

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        "classpath"(group = "org.jlleitschuh.gradle", name = "ktlint-gradle", version = "9.1.0")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.kapt") version "1.3.21"
    id("org.springframework.boot") version "2.1.4.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21" apply false
    id("com.gorylenko.gradle-git-properties") version "1.5.1" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.1.0"
}

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin") // 요부분을 apply { plugin("kotlin")} -> apply(plugin="kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    group = "com.arahansa"
    version = "1.0.0"

    dependencies {
        compile("com.fasterxml.jackson.module:jackson-module-kotlin")
        compile("org.jetbrains.kotlin:kotlin-reflect")
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        compile("org.springframework.boot:spring-boot-starter-logging")

        // spring
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.4.1")

        kapt("org.springframework.boot:spring-boot-configuration-processor")
        compileOnly("org.springframework.boot:spring-boot-configuration-processor")

        testCompile("org.springframework.boot:spring-boot-starter-test")
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
            dependsOn(processResources) // kotlin 에서 ConfigurationProperties
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
        }
    }
}

project(":common") {
    dependencies {
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project(":first") {
    dependencies {
        compile(project(":common"))
    }
}

project(":second") {
    dependencies {
        compile(project(":common"))
    }
}
