package fan.zheyuan.ktorexposed.config

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.response.cacheControl
import io.ktor.response.respondText
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
fun Application.ConfigureSSE() {
    val channels = produce { // this: ProducerScope<SseEvent> ->
        var n = 0
        while (true) {
            send(SseEvent("demo$n"))
            delay(1000)
            n++
        }
    }.broadcast()

    routing {
        get("/sse") {
            val events = channels.openSubscription()
            try {
                call.respondSse(events)
            } finally {
              events.cancel()
            }
        }
        get("/sse_html") {
            call.respondText(
                """
                    <html>
                            <head></head>
                            <body>
                                <ul id="events">
                                </ul>
                                <script type="text/javascript">
                                    var source = new EventSource('/sse');
                                    var eventsUl = document.getElementById('events');
                                    function logEvent(text) {
                                        var li = document.createElement('li')
                                        li.innerText = text;
                                        eventsUl.appendChild(li);
                                    }
                                    source.addEventListener('message', function(e) {
                                        logEvent('message:' + e.data);
                                    }, false);
                                    source.addEventListener('open', function(e) {
                                        logEvent('open');
                                    }, false);
                                    source.addEventListener('error', function(e) {
                                        if (e.readyState == EventSource.CLOSED) {
                                            logEvent('closed');
                                        } else {
                                            logEvent('error');
                                            console.log(e);
                                        }
                                    }, false);
                                </script>
                            </body>
                        </html>
                """.trimIndent(),
                contentType = ContentType.Text.Html
            )
        }
    }
}


suspend fun ApplicationCall.respondSse(events: ReceiveChannel<SseEvent>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondTextWriter(contentType = ContentType.Text.EventStream) {
        for (event in events) {
            if (event.id != null) {
                write("id: ${event.id}\n")
            }
            if (event.event != null) {
                write("event: ${event.event}\n")
            }
            for (dataLine in event.data.lines()) {
                write("data: $dataLine\n")
            }
            write("\n")
            flush()
        }
    }
}
data class SseEvent(val data: String, val event: String? = null, val id: String? = null)