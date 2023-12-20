package com.example.domain

import com.example.dto.FruitsDto
import jakarta.persistence.*

@Entity
@NamedQuery(name = "Fruits.findByName", query = "SELECT f FROM FruitsEntity f WHERE f.name=:name")
class FruitsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var name: String = " "

    fun toDto(): FruitsDto {
        val dto = FruitsDto()
        dto.name = this.name
        return dto
    }
}
