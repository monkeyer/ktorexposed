package fan.zheyuan.ktorexposed.domain.repository

import fan.zheyuan.ktorexposed.domain.dao.Follows
import fan.zheyuan.ktorexposed.domain.dao.Users
import fan.zheyuan.ktorexposed.domain.exceptions.NotFoundUserException
import fan.zheyuan.ktorexposed.domain.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    init {
        transaction {
            SchemaUtils.create(Follows)
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findByUsername(username: String): User? {
        return transaction {
            Users.select { Users.username eq username }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(user: User) : Long? {
        return transaction {
            Users.insertAndGetId { row ->
                row[email] = user.email
                row[username] = user.username!!
                row[passwordHash] = user.lpassword!!
                row[bio] = user.bio
                row[image] = user.image
            }.value
        }
    }

    fun update(email: String, user: User): User? {
        transaction {
            Users.update({ Users.email eq email }) { row ->
                row[Users.email] = user.email
                if (user.username != null) {
                    row[username] = user.username
                }
                if (user.lpassword != null) {
                    row[passwordHash] = user.lpassword
                }
                if (user.bio != null) {
                    row[bio] = user.bio
                }
                if (user.image != null) {
                    row[image] = user.image
                }
            }
        }
        return findByEmail(user.email)
    }

    fun findIsFollowUser(email: String, userIdToFollow: Long): Boolean {
        return transaction {
            Users.join(Follows, JoinType.INNER,
                additionalConstraint = {
                    Follows.user eq Users.id and (Follows.follower eq userIdToFollow)
                })
                .select { Users.email eq email }
                .count() > 0
        }
    }

    fun follow(email: String, usernameToFollow: String): User {
        val user = findByEmail(email) ?: throw NotFoundUserException("Email not found to follow")
        val userToFollow = findByUsername(usernameToFollow) ?: throw NotFoundUserException("Username not found to follow")
        transaction {
            Follows.insert { row ->
                row[Follows.user] = userToFollow.id!!
                row[follower] = user.id!!
            }
        }
        return userToFollow
    }

    fun unfollow(email: String, usernameToUnFollow: String): User {
        val user = findByEmail(email) ?: throw NotFoundUserException("Email not found to unfollow")
        val userToUnFollow = findByUsername(usernameToUnFollow) ?: throw NotFoundUserException("Username not found to unfollow")
        transaction {
            Follows.deleteWhere {
                Follows.user eq user.id!! and (Follows.follower eq userToUnFollow.id!!)
            }
        }
        return userToUnFollow
    }
}