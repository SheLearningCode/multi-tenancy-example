package com.example.domain.repository

import com.example.domain.FruitsEntity
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
class FruitsRepository : PanacheRepositoryBase<FruitsEntity, Long>