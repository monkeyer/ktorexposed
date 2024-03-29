package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.domain.repository.PersonRepository
import fan.zheyuan.ktorexposed.route.*
import fan.zheyuan.ktorexposed.web.controllers.UserController
import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

@KtorExperimentalAPI
fun Application.configureRoute() {
    val personRepository by kodein().instance<PersonRepository>()
    val userController by ConfigureModules.kodein.instance<UserController>()
    println(personRepository.VERSION)
    routing {
        statics()
        login()
        index()
        video()
        people(personRepository)
        users(userController)
    }
}