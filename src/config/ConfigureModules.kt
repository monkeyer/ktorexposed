package fan.zheyuan.ktorexposed.config

import fan.zheyuan.ktorexposed.domain.repository.UserRepository
import fan.zheyuan.ktorexposed.domain.service.UserService
import fan.zheyuan.ktorexposed.utils.JwtProvider
import fan.zheyuan.ktorexposed.web.controllers.UserController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object ConfigureModules {
    private val userModule = Kodein.Module("USER") {
        bind() from singleton { UserController(instance()) }
        bind() from singleton { UserService(JwtProvider, instance()) }
        bind() from singleton { UserRepository() }
    }

    internal val kodein = Kodein {
        import(userModule)
    }
}