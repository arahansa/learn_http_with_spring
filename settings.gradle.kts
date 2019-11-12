pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "learn_http_with_spring"

/**
 * raw: 도메인 및 공통 사용 컴포넌트
 * first :
 */
include("common", "first", "second")
