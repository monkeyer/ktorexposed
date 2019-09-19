package fan.zheyuan.ktorexposed.domain.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Widgets : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 255)
    val quantity = integer("quantity")
    val dateUpdated = long("dateUpdated")
}

object WidgetUsers : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
    val cityId = (integer("city_id") references Cities.id).nullable() // Column<Int?>
}

object Cities : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val name = varchar("name", 50) // Column<String>
}

data class Widget(
    val id: Int,
    val name: String,
    val quantity: Int,
    val dateUpdated: Long
)

data class NewWidget(
    val id: Int?,
    val name: String,
    val quantity: Int
)
