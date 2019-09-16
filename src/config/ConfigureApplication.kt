package fan.zheyuan.ktorexposed.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.GsonBuilder
import fan.zheyuan.ktorexposed.config.Auth.makeJwtVerifier
import fan.zheyuan.ktorexposed.hashKey
import fan.zheyuan.ktorexposed.property
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.content.TextContent
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import org.apache.commons.codec.binary.Base64
import org.slf4j.event.Level
import java.nio.charset.Charset
import java.util.*


@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.configureApplication() {
//    val issuer = property("jwt.domain")
//    val audience = property("jwt.audience")
    val realm = property("jwt.realm")
    val verifier = Auth.makeJwtVerifier()
//    val validityInMs = property("jwt.expiration").toInt()
//    val secret_key = property("jwt.secret_key")

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(FreeMarkerContent("404.ftl", mapOf("userId" to "fanzheyuan"), ""))
        }
        status(HttpStatusCode.Unauthorized) {
            call.respondRedirect("/login")
        }
        exception<AuthorizationException> {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    install(Sessions) {
        cookie<SiteSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }
    //support video
    install(PartialContent) { }
    install(Compression) {
        default()
        excludeContentType(ContentType.Video.Any)
    }

    install(Authentication) {
        //        val jwtVerifier = makeJwtVerifier(issuer, audience)
        val jwtVerifier = makeJwtVerifier()
        jwt {
            verifier(jwtVerifier)
            validate { credential ->
                JWTPrincipal(SubjectDecodingPayload(credential.payload))
            }
            /*verifier(verifier)
            this.realm = realm
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }*/
//            validate { credential ->
//                if (credential.payload.audience.contains(audience))
//                    JWTPrincipal(credential.payload)
//                else null
//            }
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


/*private val algorithm = Algorithm.HMAC256("default_secret")
private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier =
    JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

private fun makeToken(name: String, issuer: String, validityInMs: Int): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer(issuer)
    .withClaim("name", name)
    .withExpiresAt(getExpiration(validityInMs))
    .sign(algorithm)

private fun getExpiration(validityInMs: Int) = Date(System.currentTimeMillis() + validityInMs)

fun sign(name: String, issuer: String, validityInMs: Int): Map<String, String> = mapOf("token" to makeToken(name, issuer, validityInMs))*/
data class SiteSession(val userId: String = "")
class AuthorizationException : RuntimeException()
class SubjectDecodingPayload(private val delegate: Payload) : Payload by delegate {
    private data class Provider(val providerID: String, val providerKey: String)

    private val decodedSubject: String by lazy {
        val encodedSubject = delegate.subject
        val decodedSubject = Base64.decodeBase64(encodedSubject).toString(Charset.forName("UTF-8"))
        val provider = GsonBuilder().create().fromJson<Provider>(decodedSubject, Provider::class.java)
        provider.providerKey
    }

    override fun getSubject(): String = decodedSubject
}