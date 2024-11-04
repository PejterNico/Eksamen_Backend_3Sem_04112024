package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.enums.Categories;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripRouteTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final TripDAO tripDAO = new TripDAO();
    private static final String BASE_URL = "http://localhost:7070/api";
    private Javalin app;
    private TripDTO d1, d2, d3;
    private List<TripDTO> tripDTOS;
    private String jwtToken;

    @BeforeAll
    void init() {
        app = ApplicationConfig.startServer(7070);
        HibernateConfig.setTest(true);

        // Her registrerer vi en bruger
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"user\", \"password\": \"test123\"}")
                .when()
                .post(BASE_URL + "/auth/register/")
                .then()
                .statusCode(201);  // Forventet 201 Created

        // Her logger vi brugeren ind for at få et JWT-token
        jwtToken = given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"user\", \"password\": \"test123\"}")
                .when()
                .post(BASE_URL + "/auth/login/")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        // Her tilføjer vi rollen superman til brugeren men vi kunne også have givet ham admin rollen så han har
        // adgang til alle CRUD-operationer
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body("{\"role\": \"superman\"}")  // her giver vi useren superman rollen så han kan det hele
                .when()
                .post(BASE_URL + "/auth/user/addrole/")
                .then()
                .statusCode(200);
    }

    @BeforeEach
    void setUp() {
        // Populate data and test objects
        d1 = new TripDTO("Spain", "300", "2022-01-01", "2022-01-10", "12345", "123456", Categories.BEACH);
        d2 = new TripDTO("Grease", "300", "2022-01-01", "2022-01-10", "12345", "123456", Categories.CITY);
        d3 = new TripDTO("France", "300", "2022-01-01", "2022-01-10", "12345", "123456", Categories.SEA);

        d1 = tripDAO.create(d1);
        d2 = tripDAO.create(d2);
        d3 = tripDAO.create(d3);

        tripDTOS = new ArrayList<>(List.of(d1, d2, d3));
    }

    @AfterEach
    void tearDown() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("DELETE FROM Trip ").executeUpdate();
            em.createQuery("DELETE FROM Guides ").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error clearing database", e);
        } finally {
            em.close();
        }
    }

    @AfterAll
    void closeDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testGetAllTrips() {
        TripDTO[] tripDTOS =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtToken)  // Include JWT token for authorization
                        .when()
                        .get(BASE_URL + "/trips/")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO[].class);
        // Sammenlign de individuelle felter for at undgå referenceproblemer
        assertThat(tripDTOS, arrayContainingInAnyOrder(d1, d2, d3));
    }

    @Test
    void testGetById() {
        TripDTO tripDTO =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtToken)  // Include JWT token for authorization
                        .when()
                        .get(BASE_URL + "/trips/" + d1.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);
        // Sammenlign de individuelle felter for at undgå referenceproblemer
        assertThat(tripDTO, equalTo(d1));
    }


    @Test
    void testAddDoctor() {
        TripDTO d4 = new TripDTO("Bali", "300", "2022-01-01", "2022-01-10","12345","123456", Categories.BEACH);

        TripDTO tripDTO =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtToken)  // Include JWT token for authorization
                        .body(d4)
                        .when()
                        .post(BASE_URL + "/trips/")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(TripDTO.class);
        // Sammenlign de individuelle felter for at undgå referenceproblemer
        assertThat(tripDTO.getName(), equalTo(d4.getName()));

    }

    @Test
    void testUpdate() {

        TripDTO d4 = new TripDTO("Mallorca", "300", "2022-01-01", "2022-01-10","12345","123456", Categories.BEACH);

        TripDTO tripDTO =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + jwtToken)  // Include JWT token for authorization
                        .body(d4)
                        .when()
                        .put(BASE_URL + "/doctors/" + d1.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);
        // Sammenlign de individuelle felter for at undgå referenceproblemer
        assertThat(tripDTO.getName(), equalTo(d4.getName()));

    }

    @Test
    void testDeleteDoctor() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)  // Include JWT token for authorization
                .when()
                .delete(BASE_URL + "/trips/" + d1.getId())
                .then()
                .log().all()
                .statusCode(200);

        List<Trip> trips = tripDAO.getAll().stream().map(TripDTO::toEntity).toList();
        assertEquals(2, trips.size());
    }
}