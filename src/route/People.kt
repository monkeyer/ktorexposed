package fan.zheyuan.ktorexposed.route

import fan.zheyuan.ktorexposed.domain.model.Person
import fan.zheyuan.ktorexposed.domain.repository.PersonRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import java.util.*

fun Route.people(personRepository: PersonRepository) {
    route("/people") {
        get {
//            println(Person(UUID.randomUUID(), "dark", "knight", 18, "kotliner"))
            call.respond(HttpStatusCode.OK, personRepository.findAll())
        }
        get("/{id}") {
          val id = UUID.fromString(call.parameters["id"]!!)
            personRepository.find(id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondText(status = HttpStatusCode.NotFound) {
                "There is no record with id: $id"
            }
        }

        post {
            val person = call.receive<Person>()
            val result = personRepository.save(person.copy(id = UUID.randomUUID()))
            call.respond(result)
        }

        put {
            val person = call.receive<Person>()
            when {
                person.id == null -> call.respondText(status = HttpStatusCode.BadRequest) { "Id is missing" }
                personRepository.exists(person.id) -> call.respond(HttpStatusCode.OK, personRepository.save(person))
                else -> call.respondText(status = HttpStatusCode.NotFound) {
                    "There is no record with id : ${person.id}"
                }
            }
        }

        delete("/{id}") {
            val id = UUID.fromString(call.parameters["id"]!!)
            if(personRepository.exists(id)) {
                call.respond(HttpStatusCode.NoContent, personRepository.delete(id))
            } else {
                call.respondText(status = HttpStatusCode.NotFound) {
                    "There is no record with id: $id"
                }
            }
        }
    }
}