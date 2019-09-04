package fan.zheyuan.ktorexposed.feature.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import fan.zheyuan.ktorexposed.views.templates.ThemeColor
import fan.zheyuan.ktorexposed.views.templates.total
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title
import java.io.File
import kotlin.random.Random

fun Application.jwtApplication() {
    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        val jwtVerifier = makeJwtVerifier(issuer, audience)
        jwt {
            verifier(jwtVerifier)
            this.realm = realm
            validate { credential ->
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }

    install(DefaultHeaders)
    install(CallLogging)
    install(AutoHeadResponse)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Routing) {
        route("/") {

            route("/login") {
                get {
                    call.respond(FreeMarkerContent("login.ftl", mapOf("userId" to "fanzheuan"), ""))
                }
                post {
                    val posts = call.receiveParameters()
                    log.info("login ${posts["Name"]} ${posts["Password"]}")
                    call.respondRedirect("index")
                }
            }
            get("index") {
                var color: String = ThemeColor.PINK.color
                val random = Random.nextInt(total)
                ThemeColor.values().forEachIndexed { id, theme ->
                    if (id == random) color = theme.color
                }
                call.respond(FreeMarkerContent("index.ftl", mapOf("userId" to "fanzheuan", "color" to color), ""))
            }

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
        routing {
            static("css") {
                resources("files/css")
            }
            static("js") {
                resources("files/js")
            }
            static("webfonts") {
                resources("files/webfonts")
            }
            static("img") {
                resources("files/img")
            }
        }
    }
}

private val algorithm = Algorithm.HMAC256("secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier =
    JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
