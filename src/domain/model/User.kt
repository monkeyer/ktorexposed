package fan.zheyuan.ktorexposed.domain.model

import io.ktor.auth.Principal

data class UserDTO(val user: User? = null) {
    fun validRegister(): User {
        require(
            user == null ||
                    user.username.isNullOrBlank() ||
                    user.lpassword.isNullOrBlank()
        ) {
            "User is invalid."
        }
        return user!!
    }

    fun validLogin(): User {
        require(
            user == null ||
                    user.username.isNullOrBlank() ||
                    user.lpassword.isNullOrBlank()
        ) {
            "Username or Password is invalid."
        }
        return user!!
    }

    fun validToUpdate(): User {
        require(
            user == null ||
                    user.username.isNullOrBlank() ||
                    user.lpassword.isNullOrBlank()
        ) {
            "User is invalid."
        }
        return user!!
    }
}

data class User(
    val id: Long? = null,
    val email: String,
    val token: String? = null,
    val username: String? = null,
    val lpassword: String? = null,
    val bio: String? = null,
    val image: String? = null
) : Principal