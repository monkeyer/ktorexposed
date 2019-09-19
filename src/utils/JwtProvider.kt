package fan.zheyuan.ktorexposed.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import fan.zheyuan.ktorexposed.domain.model.User
import java.util.*

object JwtProvider {
    private const val validityInMs = 36_000_00 * 10
    const val issuer = "ktor-exposed"
    const val audience = "ktor-audience"

    val verifier: JWTVerifier = JWT
        .require(Cipher.algorithm)
        .withIssuer(issuer)
        .build()

    fun decodeJWT(token: String): DecodedJWT = JWT.require(Cipher.algorithm).build().verify(token)

    fun createJWT(user: User): String? =
        JWT.create()
            .withIssuedAt(Date())
            .withSubject("authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("lname", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs)).sign(Cipher.algorithm)

}