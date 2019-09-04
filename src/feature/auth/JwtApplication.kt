package fan.zheyuan.ktorexposed.feature.auth

import fan.zheyuan.ktorexposed.views.templates.ThemeColor
import fan.zheyuan.ktorexposed.views.templates.total
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlin.random.Random

fun Application.jwtApplication() {
    install(Routing) {
        route("/") {




            get {
                call.respondText("demo")
            }
            authenticate {
                route("/who") {
                    handle {
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val subjectString = principal!!.payload.subject.removePrefix("auth0|")
                        call.respondText("Success, $subjectString")
                    }
                }
            }
        }
    }
}