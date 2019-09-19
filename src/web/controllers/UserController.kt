package fan.zheyuan.ktorexposed.web.controllers

import fan.zheyuan.ktorexposed.domain.model.User
import fan.zheyuan.ktorexposed.domain.model.UserDTO
import fan.zheyuan.ktorexposed.domain.service.UserService
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.request.receive
import io.ktor.response.respond

class UserController (private val userService: UserService) {
    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.authenticate(this.validLogin()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }

    suspend fun register(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.create(this.validRegister()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }

    fun getUserByEmail(email: String?): User = email.let {
        require(!it.isNullOrBlank()) { "User not logged or with invalid email." }
        userService.getByEmail(it)
    }

    suspend fun getCurrent(ctx: ApplicationCall) = ctx.respond(UserDTO(ctx.authentication.principal()))

    suspend fun update(ctx: ApplicationCall) {
        val email = ctx.authentication.principal<User>()?.email
        require(!email.isNullOrBlank()) { "User not logged." }
        ctx.receive<UserDTO>().also { userDTO ->
            userService.update(email, userDTO.validToUpdate()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }
}