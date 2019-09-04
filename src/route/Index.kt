package fan.zheyuan.ktorexposed.route

import fan.zheyuan.ktorexposed.views.templates.ThemeColor
import fan.zheyuan.ktorexposed.views.templates.total
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import kotlin.random.Random

fun Route.index() {
    authenticate {
        route("/who") {
            handle {
                val principal = call.authentication.principal<JWTPrincipal>()
                val subjectString = principal!!.payload.subject.removePrefix("auth0|")
                call.respondText("Success, $subjectString")
            }
        }
    }
    route("/index") {
        get {
            var color: String = ThemeColor.PINK.color
            val random = Random.nextInt(total)
            ThemeColor.values().forEachIndexed { id, theme ->
                if (id == random) color = theme.color
            }
            call.respond(FreeMarkerContent("index.ftl", mapOf("userId" to "fanzheuan", "color" to color), ""))
        }
    }
}