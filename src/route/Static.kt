package fan.zheyuan.ktorexposed.route

import io.ktor.http.content.*
import io.ktor.routing.Route

fun Route.statics() {
    static("upload") {
        resources("files/upload")
    }
    static("/") {
        resources("files/favicon")
        resources("files/web")
    }
}