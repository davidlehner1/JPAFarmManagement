package at.htlleonding.control;

import at.htlleonding.entity.Farm;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class FarmRepository {

    //<editor-fold desc="Fields">
    @Inject
    EntityManager em;
    private JsonArray jsonArray = Json.createArrayBuilder().build();
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public FarmRepository() {
        try {
            readCSV();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    private void readCSV() throws IOException {
        String csvFilePath = "src/main/resources/farmData.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            String[] header = reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                JsonObject jsonObject = Json.createObjectBuilder()
                        .add("street", nextLine[0])
                        .add("houseNumber", nextLine[1])
                        .add("zip", nextLine[2])
                        .add("city", nextLine[3])
                        .build();
                jsonArray = Json.createArrayBuilder(jsonArray).add(jsonObject).build();
            }
            System.out.println(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    public List<Farm> getAllFarms() {
        TypedQuery<Farm> query = em.createQuery("select f from Farm f", Farm.class);
        return query.getResultList();
    }

    public List<Farm> getFarmByZip(int zip) {
        TypedQuery<Farm> query = em.createQuery("SELECT f FROM Farm f where f.zip = :zipValue", Farm.class);
        query.setParameter("zipValue", zip);
        return query.getResultList();
    }

    @Transactional
    public void deleteFarmById(int id) {
        Farm farm = em.find(Farm.class, id);
        if (farm != null) {
            em.remove(farm);
        }
    }

    @Transactional
    public Response createNewFarm(Farm newFarm) {
        try{
            em.persist(newFarm);
            return Response.ok(newFarm).build();
        } catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    public Farm updateFarmById(int id, Farm updatedFarm) {
        Farm farm = em.find(Farm.class, id);
        if(farm != null){
            farm.setCity(updatedFarm.getCity());
            farm.setId(updatedFarm.getId());
            farm.setZip(updatedFarm.getZip());
            farm.setStreet(updatedFarm.getStreet());
            farm.setHouseNumber(updatedFarm.getHouseNumber());
            return em.merge(farm);
        }
        return null;
    }
    //</editor-fold>
}
