package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.route.index
import fan.zheyuan.ktorexposed.route.login
import fan.zheyuan.ktorexposed.route.statics
import fan.zheyuan.ktorexposed.route.video
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