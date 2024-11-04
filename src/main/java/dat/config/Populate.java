package dat.config;

import dat.entities.*;
import dat.enums.Categories;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Populate {
    public static Logger logger = LoggerFactory.getLogger(Populate.class);
    static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public static void main(String[] args) {
            populateDatabase();
    }


    // Metode til at populere databasen med doctors og appointments
    public static void populateDatabase() {


        Trip trip1 = new Trip("Trip to Miami Beach", "1200", "2024-06-01 08:00", "2024-06-07 20:00", "-80.1918", "25.7617", Categories.BEACH);
        Trip trip2 = new Trip("Trip to New York City", "1500", "2024-07-01 09:00", "2024-07-10 21:00", "-74.0060", "40.7128", Categories.CITY);
        Trip trip3 = new Trip("Trip to Amazon Forest", "2000", "2024-08-01 10:00", "2024-08-15 22:00", "-60.0258", "-3.4653", Categories.FOREST);
        Trip trip4 = new Trip("Trip to Lake Tahoe", "1800", "2024-09-01 11:00", "2024-09-10 23:00", "-120.044", "39.0968", Categories.LAKE);
        Trip trip5 = new Trip("Trip to Mediterranean Sea", "1400", "2024-10-01 12:00", "2024-10-07 19:00", "18.4241", "33.9249", Categories.SEA);
        Trip trip6 = new Trip("Trip to Paris", "1600", "2024-11-01 13:00", "2024-11-10 18:00", "-112.115", "36.1069", Categories.CITY);


        Set<Trip> trips1 = new HashSet<>();
        trips1.add(trip1);
        trips1.add(trip2);

        Set<Trip> trips2 = new HashSet<>();
        trips2.add(trip3);
        trips2.add(trip4);

        Set<Trip> trips3 = new HashSet<>();
        trips3.add(trip5);
        trips3.add(trip6);



        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Make 3 guides
            // Create Guides instances
            Guides guide1 = new Guides("John", "Doe", "john.doe@example.com", "1234567890", 10);
            Guides guide2 = new Guides("Jane", "Smith", "jane.smith@example.com", "0987654321", 8);
            Guides guide3 = new Guides("Emily", "Johnson", "emily.johnson@example.com", "1122334455", 12);


            guide1.setAppointments(trips1);
            guide2.setAppointments(trips2);
            guide3.setAppointments(trips3);


            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            em.getTransaction().commit();
            logger.info("Database populated successfully");
        } catch (PersistenceException e) {
            logger.error("Error populating database", e);
            throw new RuntimeException("Error populating database", e);
        }
    }
}
