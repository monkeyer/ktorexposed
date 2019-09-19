package fan.zheyuan.ktorexposed.domain.service

import fan.zheyuan.ktorexposed.domain.exceptions.NotFoundUserException
import fan.zheyuan.ktorexposed.domain.exceptions.UnauthorizedException
import fan.zheyuan.ktorexposed.domain.model.Profile
import fan.zheyuan.ktorexposed.domain.model.User
import fan.zheyuan.ktorexposed.domain.repository.UserRepository
import fan.zheyuan.ktorexposed.utils.Cipher
import fan.zheyuan.ktorexposed.utils.JwtProvider
import java.util.*

class UserService (private val jwtProvider: JwtProvider, private val userRepository: UserRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun create(user: User): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }
        userRepository.create(user.copy(lpassword = String(base64Encoder.encode(Cipher.encrypt(user.lpassword)))))
        return user.copy(token = generateJwtToken(user))
    }

    fun authenticate(user: User): User {
        val userFound = userRepository.findByEmail(user.email)
        if (userFound?.lpassword == String(base64Encoder.encode(Cipher.encrypt(user.lpassword)))) {
            return userFound.copy(token = generateJwtToken(userFound))
        }
        throw UnauthorizedException("email or password invalid!")
    }

    fun getByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        user ?: throw NotFoundUserException("User not found to get.")
        return user.copy(token = generateJwtToken(user))
    }

    fun getProfileByUsername(email: String, usernameFollowing: String): Profile {
        return userRepository.findByUsername(usernameFollowing).let { user ->
            user ?: throw NotFoundUserException("User not found to find.")
            Profile(user.username, user.bio, user.image, userRepository.findIsFollowUser(email, user.id!!))
        }
    }

    fun update(email: String, user: User): User? =userRepository.update(email, user)

    fun follow(email: String, usernameToFollow: String): Profile =
        userRepository.follow(email, usernameToFollow).let { user ->
            Profile(user.username, user.bio, user.image, true)
        }

    fun unfollow(email: String, usernameToUnFollow: String): Profile =
        userRepository.unfollow(email, usernameToUnFollow).let { user ->
            Profile(user.username, user.bio, user.image, false)
        }

    private fun generateJwtToken(user: User): String? = jwtProvider.createJWT(user)

}