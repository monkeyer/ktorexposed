package fan.zheyuan.ktorexposed.model

import java.io.Serializable

//data class User(val userId: String, val email: String, val displayName: String, val passwordHash: String) : Serializable
data class User(val name: String, val password: String)