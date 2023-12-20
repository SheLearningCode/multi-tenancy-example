package com.example.dto

import com.example.domain.FruitsEntity


class FruitsDto {
    var name: String = " "

    fun toDomain(): FruitsEntity {
        val domain = FruitsEntity()
        domain.name = this.name

        return domain
    }
}