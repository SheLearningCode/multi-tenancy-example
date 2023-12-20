package com.example.controller
import com.example.domain.FruitsEntity
import com.example.domain.repository.FruitsRepository
import com.example.dto.FruitsDto
import com.example.tenant.service.TenantCreatorService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType


@Path("/fruit-api/")
class FruitsResource (
    private val fruitsRepository: FruitsRepository,
    private val tenantCreatorService: TenantCreatorService,
    private val logger: org.jboss.logging.Logger
){
    @GET
    @Path("fruits")
    @Produces(MediaType.APPLICATION_JSON)
    fun listFruits(): MutableList<FruitsEntity>? = fruitsRepository.listAll()

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getFruitById(@PathParam("id") id: Long): FruitsDto = fruitsRepository.findById(id).toDto()

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createFruit(fruitsDto: FruitsDto): FruitsDto {
        return try {
            fruitsDto.toDomain().apply {
                fruitsRepository.persist(this)
            }.toDto()
        } catch (ex: Exception) {
            logger.error("Error in createParticipant function: ${ex.message}")
            throw ex
        }
    }

    @GET
    @Path("/schema")
    fun getAllSchemas(): List<String> = tenantCreatorService.getAllKnownTenants()
}