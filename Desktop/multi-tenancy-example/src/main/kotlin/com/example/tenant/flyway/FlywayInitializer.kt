package com.example.tenant.flyway

import com.example.tenant.service.TenantCreatorService
import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes

@Startup
@ApplicationScoped
class FlywayInitializer(private val tenantCreatorService: TenantCreatorService) {

    fun initializeFlyway(@Observes startupEvent: StartupEvent) {
        tenantCreatorService.applyFlywayMigrationsOnKnownTenants()
    }
}