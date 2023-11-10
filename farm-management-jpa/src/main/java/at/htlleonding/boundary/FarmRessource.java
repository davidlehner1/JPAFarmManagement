package at.htlleonding.boundary;

import at.htlleonding.control.FarmRepository;
import at.htlleonding.entity.Farm;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/farm")
public class FarmRessource {

    @Inject
    FarmRepository farmRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Farm> farms = farmRepository.getAllFarms();
        return Response.ok(farms.toArray()).build();
    }

    @GET
    @Path("/{zip}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByZip(@PathParam("zip") int zip) {
        List<Farm> farmStream = farmRepository.getFarmByZip(zip);
        return Response.ok(farmStream).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteByIndex(@PathParam("id") int id) {
        farmRepository.deleteFarmById(id);
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewFarm(Farm newFarm) {
        return farmRepository.createNewFarm(newFarm);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFarmFully(@PathParam("id") int id, Farm updatedFarm) {
        Farm updated = farmRepository.updateFarmById(id, updatedFarm);
        if(updated == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.accepted(updated).build();
    }

    @GET
    @Path("/myresponse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response myCustomResponse(@QueryParam("no") Integer no) {
        if (no == 1) {
            return Response.ok(new Farm("test Straße", "2", 4055, "Pucking")).header("MY_HEADER_ENTRY", "java is cool").build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/myjson")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject myCustomJsonObject() {
        JsonObject myObject = Json.createObjectBuilder()
                .add("first-name", "Bertl")
                .add("last-name", "Balasz")
                .build();
        return myObject;
    }

    @GET
    @Path("/myJsonArray")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray myJsonArray() {
        JsonArray myJsonArray = farmRepository.getJsonArray();
        return myJsonArray;
    }
}
