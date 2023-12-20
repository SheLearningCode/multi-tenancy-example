package com.example.tenant.service

import io.agroal.api.AgroalDataSource
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import java.lang.Exception
import java.sql.DatabaseMetaData
import java.sql.ResultSet

@Startup
@ApplicationScoped
class TenantCreatorService(private val defaultDataSource: AgroalDataSource) {

    @ConfigProperty(name = "quarkus.flyway.schemas")
    private lateinit var schemas: String

    @ConfigProperty(name = "quarkus.flyway.locations")
    private lateinit var scriptLocation: String

    @ConfigProperty(name = "tenants.excluded-schemas")
    private lateinit var excludedSchemas: String

    fun getAllKnownTenants(): List<String> {
        val knownSchemas: MutableList<String> = emptyList<String>().toMutableList()

        try {
            defaultDataSource.connection.use { connection ->
                val metaData: DatabaseMetaData = connection.metaData
                val resultSet: ResultSet = metaData.schemas
                while (resultSet.next()) {
                    val schemaName: String = resultSet.getString("TABLE_SCHEM")
                    knownSchemas.add(schemaName)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        knownSchemas.removeAll(excludedSchemas.split(","))

        return knownSchemas
    }

    fun applyFlywayMigrationsOnKnownTenants() {
        val migratingSchemas = schemas.split(",").toMutableSet()
        migratingSchemas.addAll(getAllKnownTenants())

        migratingSchemas.forEach {
            createNewTenant(it)
        }
    }

    fun createNewTenant(schema: String) {
        Flyway.configure().dataSource(defaultDataSource).schemas(schema).locations(Location(scriptLocation)).load().migrate()
    }

}