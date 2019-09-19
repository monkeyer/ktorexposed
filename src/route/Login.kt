package fan.zheyuan.ktorexposed.route

import fan.zheyuan.ktorexposed.config.Auth
import fan.zheyuan.ktorexposed.web.controllers.UserController
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Route.login() {

    route("/login") {

        get {
            call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "fanzheyuan"), ""))
        }
        post {
            val user = call.receive<Parameters>()
//            val signed = sign(user["lname"].toString(), application.property("jwt.domain"), application.property("jwt.expiration").toInt())
            val signed = Auth.sign(user["lname"].toString())
//            call.respond(signed)
            call.respondRedirect("index")
        }
    }

    authenticate {
        route("/secret") {
            get {
                val user = call.authentication.principal<UserIdPrincipal>()
                call.respondText("hi ${user?.name}, you are authenticated.", contentType = ContentType.Text.Plain)
            }
        }
    }
}

fun Route.users(userController: UserController) {
    route("users") {
        post { userController.register(this.context) }
        post("login") { userController.login(this.context) }
    }

    route("user") {
        authenticate {
            get { userController.getCurrent(this.context) }
            put { userController.update(this.context) }
        }
    }
}
