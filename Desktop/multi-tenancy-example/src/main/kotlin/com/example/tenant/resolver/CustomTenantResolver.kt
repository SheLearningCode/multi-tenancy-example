package com.example.tenant.resolver

import com.example.tenant.service.TenantCreatorService
import io.quarkus.hibernate.orm.PersistenceUnitExtension
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.RequestScoped

@PersistenceUnitExtension
@RequestScoped
class CustomTenantResolver(
    private val context: RoutingContext,
    private val tenantCreatorService: TenantCreatorService
) : TenantResolver {

    override fun getDefaultTenantId(): String {
        return "public"
    }

    override fun resolveTenantId(): String {
        val receivedTenant: String? = context.request().getHeader("tenant")

        val knownTenants = tenantCreatorService.getAllKnownTenants()

        if (receivedTenant != null) {
            if (knownTenants.contains(receivedTenant)) {
                return receivedTenant
            }
            tenantCreatorService.createNewTenant(receivedTenant)
            return receivedTenant
        }

        return defaultTenantId
    }
}