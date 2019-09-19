package fan.zheyuan.ktorexposed.utils

import com.auth0.jwt.algorithms.Algorithm

object Cipher {
    private const val SECRET_KEY = "zAP5MBA4B4Ijz0MZaS48"
    val algorithm = Algorithm.HMAC256(SECRET_KEY)

    fun encrypt(data: String?) : ByteArray =
        algorithm.sign(data?.toByteArray())
}