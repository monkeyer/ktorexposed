package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.route.*
import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Application.configureRoute() {
    routing {
        statics()
        login()
        index()
        video()
    }
}