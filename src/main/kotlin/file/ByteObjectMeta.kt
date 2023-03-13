package file


import database.DataEntity
import jakarta.persistence.*
import java.util.*


data class ByteObjectMeta(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: String = "",

    @Column(name = "name")
    val name: String = "",

    @Column(name = "type")
    val type: String = "",

    @Column(name = "extension")
    val extension: String = "",

    @Column(name = "created_date")
    val createdDate: Date = Date(),

    @Column(name = "updated_date")
    val updatedDate: Date = Date(),

    @Column(name = "deleted")
    val deleted: Boolean = false,

    @Column(name = "deleted_date")
    val deletedDate: Date? = null


): DataEntity()