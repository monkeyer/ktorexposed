package fan.zheyuan.ktorexposed
import fan.zheyuan.ktorexposed.config.configureApplication
import fan.zheyuan.ktorexposed.config.configureDatabase
import fan.zheyuan.ktorexposed.config.configureRoute
import io.ktor.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    configureApplication()
//    configureDatabase()
    configureRoute()
}