package fan.zheyuan.ktorexposed.route

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

private const val video_url = "/upload/test.mp4"

fun Route.video() {
    route("/video") {
        get {
            call.respond(FreeMarkerContent("video.ftl", mapOf("videoUrl" to video_url), ""))
        }
    }
}