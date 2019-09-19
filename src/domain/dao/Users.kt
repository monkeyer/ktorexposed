package fan.zheyuan.ktorexposed.domain.dao

import fan.zheyuan.ktorexposed.domain.model.User
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

internal object Users : LongIdTable() {
    val email = varchar("email", 200).uniqueIndex()
    var username = varchar("username", 256).uniqueIndex()
    val passwordHash = varchar("password_hash", 64)
    val bio: Column<String?> = varchar("bio", 1000).nullable()
    val image: Column<String?> = varchar("image", 255).nullable()

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            email = row[email],
            username = row[username],
            lpassword = row[passwordHash],
            bio = row[bio],
            image = row[image]
        )
    }
}

internal object Follows : Table() {
    val user: Column<Long> = long("user").primaryKey()
    val follower: Column<Long> = long("user_follower").primaryKey()
}