package at.htlleonding.control;

import at.htlleonding.entity.Farm;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class InitBean {
    @Inject
    EntityManager em;

    @Transactional
    void startUp(@Observes StartupEvent event){
        Log.info("it is working");

        Farm farm = new Farm("Hahnweg", "3", 4055, "Pucking");
        em.persist(farm);
    }
}
