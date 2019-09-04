package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.route.index
import fan.zheyuan.ktorexposed.route.login
import fan.zheyuan.ktorexposed.route.statics
import io.ktor.application.Application
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing

fun Application.configureRoute() {
    routing {
        statics()
        login()
        index()
    }
}