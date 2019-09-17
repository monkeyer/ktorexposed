package fan.zheyuan.ktorexposed.repository

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder
import fan.zheyuan.ktorexposed.model.Person
import java.util.*

class PersonRepository(private val session: CqlSession) {
    val VERSION = "${this.javaClass.simpleName} 1.0"

    fun findAll(): List<Person> {
        return session.execute("SELECT * FROM people")
            .all()
            .map(this::rowToPerson)
    }

    fun find(id: UUID): Person? {
        return session.execute("SELECT * FROM people WHERE id = $id")
            .one()?.run { rowToPerson(this) }
    }

    fun delete(id: UUID) = session.execute("DELETE FROM people WHERE id = $id")

    fun save(person: Person): Person {
        return person.apply {
            session.execute(
                SimpleStatementBuilder(
                    """
                    INSERT INTO people (id, first_name, last_name, age, job)
                    VALUS (:id, :firstName, :lastName, :age, :job)
                """
                ).addNamedValue("id", id)
                    .addNamedValue("firstName", firstName)
                    .addNamedValue("lastName", lastName)
                    .addNamedValue("age", age)
                    .addNamedValue("job", job)
                    .build()

            )
        }
    }

    fun exists(id: UUID): Boolean = find(id) != null

    private fun rowToPerson(row: Row) = Person(
        row["id", UUID::class.java],
        row["first_name", String::class.java]!!,
        row["last_name", String::class.java]!!,
        row["age", Int::class.java]!!,
        row["job", String::class.java]!!
    )
}