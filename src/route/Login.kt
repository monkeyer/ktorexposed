package fan.zheyuan.ktorexposed.route

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.login() {
    route("/login") {
        get {
            call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "fanzheuan"), ""))
        }
        post {
            val posts = call.receiveParameters()
            call.respondRedirect("index")
        }
    }
}