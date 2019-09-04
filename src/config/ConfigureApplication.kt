package fan.zheyuan.ktorexposed.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.SerializationFeature
import fan.zheyuan.ktorexposed.temp.MySession
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.slf4j.event.Level

fun Application.configureApplication() {

    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations) {
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

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

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
}

private val algorithm = Algorithm.HMAC256("secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier =
    JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
