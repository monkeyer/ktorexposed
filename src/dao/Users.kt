package fan.zheyuan.ktorexposed.dao

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex()
    var displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}