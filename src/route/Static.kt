package fan.zheyuan.ktorexposed.route

import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.statics() {
    static("css") {
        resources("files/css")
    }
    static("js") {
        resources("files/js")
    }
    static("webfonts") {
        resources("files/webfonts")
    }
    static("img") {
        resources("files/img")
    }
    static("upload") {
        resources("files/upload")
    }
}