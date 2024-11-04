package dat.routes;

import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.enums.Categories;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class Populator {

    private TripDAO tripDAO;
    private EntityManagerFactory emf;

    public Populator(TripDAO tripDAO, EntityManagerFactory emf) {
        this.tripDAO = tripDAO;
        this.emf = emf;
    }

    public List<TripDTO> populateTrips() {

        TripDTO d1, d2, d3;

        // Populate data and test objects
        d1 = new TripDTO("Spain", "300", "2022-01-01", "2022-01-10","12345","123456", Categories.BEACH);
        d2 = new TripDTO("Grease", "300", "2022-01-01", "2022-01-10","12345","123456", Categories.CITY);
        d3 = new TripDTO("France", "300", "2022-01-01", "2022-01-10","12345","123456", Categories.SEA);


        d1 = tripDAO.create(d1);
        d2 = tripDAO.create(d2);
        d3 = tripDAO.create(d3);
        return new ArrayList<>(List.of(d1, d2, d3));

    }
}
