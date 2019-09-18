package fan.zheyuan.ktorexposed.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fan.zheyuan.ktorexposed.dao.People
import fan.zheyuan.ktorexposed.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    lateinit var db: Database
    fun init() {
        db = Database.connect(hikari())
        db.useNestedTransactions = true
        transaction {
            create(Widgets, Cities, Users)
//            drop(Cities, Users)
        }
//        testDB()
    }

    private fun NodeToNodes() {
        val root = Nodes.new { name = "root" }
        val child1 = Nodes.new { name = "child1" }
        child1.parents = SizedCollection(root)

        val child2 = Nodes.new { name = "child2" }
        root.children = SizedCollection(listOf(child1, child2))
    }
    private fun testDB() {
        transaction {
            val saintPetersburgId = Cities.insert {
                it[name] = "St. Petersburg"
            } get Cities.id

            val munichId = Cities.insert {
                it[name] = "Munich"
            } get Cities.id

            Cities.insert {
                it[name] = "Prague"
            }

            Users.insert {
                it[id] = "andrey"
                it[name] = "Andrey"
                it[cityId] = saintPetersburgId
            }

            Users.insert {
                it[id] = "sergey"
                it[name] = "Sergey"
                it[cityId] = munichId
            }

            Users.insert {
                it[id] = "eugene"
                it[name] = "Eugene"
                it[cityId] = munichId
            }

            Users.insert {
                it[id] = "alex"
                it[name] = "Alex"
                it[cityId] = null
            }

            Users.insert {
                it[id] = "smth"
                it[name] = "Something"
                it[cityId] = null
            }

            Users.update({Users.id eq "alex"}) {
                it[name] = "Alexey"
            }

            Users.deleteWhere{Users.name like "%thing"}

            println("All cities:")

            for (city in Cities.selectAll()) {
                println("${city[Cities.id]}: ${city[Cities.name]}")
            }

            println("Manual join:")
            (Users innerJoin Cities).slice(Users.name, Cities.name).
                select {(Users.id.eq("andrey") or Users.name.eq("Sergey")) and
                        Users.id.eq("sergey") and Users.cityId.eq(Cities.id)}.forEach {
                println("${it[Users.name]} lives in ${it[Cities.name]}")
            }

            println("Join with foreign key:")


            (Users innerJoin Cities).slice(Users.name, Users.cityId, Cities.name).
                select {Cities.name.eq("St. Petersburg") or Users.cityId.isNull()}.forEach {
                if (it[Users.cityId] != null) {
                    println("${it[Users.name]} lives in ${it[Cities.name]}")
                }
                else {
                    println("${it[Users.name]} lives nowhere")
                }
            }

            println("Functions and group by:")

            ((Cities innerJoin Users).slice(Cities.name, Users.id.count()).selectAll().groupBy(Cities.name)).forEach {
                val cityName = it[Cities.name]
                val userCount = it[Users.id.count()]

                if (userCount > 0) {
                    println("$userCount user(s) live(s) in $cityName")
                } else {
                    println("Nobody lives in $cityName")
                }
            }
//            drop (Users, Cities)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "com.mysql.jdbc.Driver"
        config.jdbcUrl = "jdbc:mysql://localhost/ktortest?useSSL=false"
        config.username = "fan"
        config.password = "fangzhou"
        config.maximumPoolSize = 3
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.isAutoCommit = false
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}