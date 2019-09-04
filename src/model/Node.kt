package fan.zheyuan.ktorexposed.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Node : IntIdTable() {
    val name = varchar("name", 50)
}
object NodeToNodes : Table() {
    val parent = reference("parent_node_id", Node)
    val child = reference("child_user_id", Node)
}
class Nodes(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Nodes>(Node)

    var name by Node.name
    var parents by Nodes.via(NodeToNodes.child, NodeToNodes.parent)
    var children by Nodes.via(NodeToNodes.parent, NodeToNodes.child)
}