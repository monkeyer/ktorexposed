package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.service.DatabaseFactory
import io.ktor.application.Application

fun Application.configureDatabase() {
    DatabaseFactory.init()
}