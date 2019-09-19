package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.domain.service.DatabaseFactory
import io.ktor.application.Application

fun Application.configureDatabase() {
    DatabaseFactory.init()
}