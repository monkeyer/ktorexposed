package fan.zheyuan.ktorexposed.domain.dao

import org.jetbrains.exposed.sql.Table

object People : Table() {
    val id = uuid("id").primaryKey()
    val firstName = text("first_name")
    var lastName = text("last_name")
    val age = integer("age")
    val job = integer("job")
}