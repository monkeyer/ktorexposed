package fan.zheyuan.ktorexposed
import fan.zheyuan.ktorexposed.config.configureApplication
import fan.zheyuan.ktorexposed.config.configureRoute
import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.routing.Route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import java.io.File
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.main() {
    configureApplication()
//    configureDatabase()
    configureRoute()
}

@KtorExperimentalAPI
fun Route.hasFunction() = { s: String -> hash(s) }

@KtorExperimentalAPI
private fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
@KtorExperimentalAPI
val hashKey = hex("6819b57a326945c1968f45236589")
@KtorExperimentalAPI
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")
val dir = File("build/db")

@KtorExperimentalAPI
fun Application.property(name: String) = this.environment.config.property(name).getString()