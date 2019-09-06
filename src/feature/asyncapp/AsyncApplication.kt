package fan.zheyuan.ktorexposed.feature.app

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title
import java.util.*
import kotlin.system.measureTimeMillis

@ObsoleteCoroutinesApi
val compute = newFixedThreadPoolContext(4, "compute")
typealias DelayProvider = suspend (ms: Int) -> Unit

@kotlin.jvm.JvmOverloads
fun Application.asyncModule(random: Random = Random(), delayProvider: DelayProvider = { delay(it.toLong()) }) {
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        get("/{...}") {
            val startTime = System.currentTimeMillis()
            call.respondHandlingLongCalculation(random, delayProvider, startTime)
        }
    }
}

@ObsoleteCoroutinesApi
private suspend fun ApplicationCall.respondHandlingLongCalculation(random: Random, delayProvider: DelayProvider, startTime: Long) {
    val queueTime = System.currentTimeMillis() - startTime
    var number = 0
    val computeTime = measureTimeMillis {
        withContext(compute) {
            for (index in 0 until 300) {
                delayProvider(1)
                number += random.nextInt(100)
            }
        }
    }

    respondHtml {
        head {
            title { +"Ktor: async" }
        }
        body {
            p { +"Hello from Ktor Async sample application" }
            p { +"We calculated number $number in $computeTime ms of compute time, spending $queueTime ms in queue." }
        }

    }
}