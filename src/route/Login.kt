package fan.zheyuan.ktorexposed.route

import fan.zheyuan.ktorexposed.config.sign
import fan.zheyuan.ktorexposed.model.User
import fan.zheyuan.ktorexposed.property
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import kotlin.math.sign

@KtorExperimentalAPI
fun Route.login() {

    route("/login") {

        get {
            call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "fanzheuan"), ""))
        }
        post {
            val user = call.receive<User>()
            val signed = sign(user.name, application.property("jwt.domain"), application.property("jwt.expiration").toInt())
            application.log.info(user.name)
            call.respond(user)
//            call.respondRedirect("index")
        }
    }
}
