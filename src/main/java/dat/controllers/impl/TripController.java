package dat.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.enums.Categories;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static dat.config.Populate.populateDatabase;

public class TripController implements IController<TripDTO> {

    private final TripDAO tripDAO;
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final String BASE_URL_PACKING = "https://packingapi.cphbusinessapps.dk/packinglist/";


    public TripController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.tripDAO = TripDAO.getInstance(emf);
    }

    public void create(Context ctx) {
        try {
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO savedTripDTO = tripDAO.create(tripDTO);
            ctx.status(201).json(savedTripDTO);
        } catch (Exception e) {
            logger.error("Invalid Trip data", e);
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "Invalid Trip data",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Throwable e) {
            logger.error("Internal server error", e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = tripDAO.getById(id);
            if (tripDTO != null) {
                ctx.json(tripDTO);
            } else {
                throw new NotFoundResponse("Trip with ID " + id + " not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid Trip ID format: {}", ctx.pathParam("id"), e);
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "Status 400: Invalid Trip ID format: " + ctx.pathParam("id"),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (NotFoundResponse e) {
            logger.error("Status 404: Not Found: Trip with ID: {}", ctx.pathParam("id"), e);
            ctx.status(404).json(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "message", "Trip with ID " + ctx.pathParam("id") + " not found",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Exception e) {
            logger.error("An unknown error occurred while retrieving Trip with ID: {}", ctx.pathParam("id"), e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "An unknown error occurred. Please try again later.",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void getAll(Context ctx) {
        try {
            List<TripDTO> tripDTOS = tripDAO.getAll();
            ctx.json(tripDTOS);
        } catch (NotFoundResponse e) {
            logger.error("Trip not found", e);
            ctx.status(404).json(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "message", "Trip not found",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Exception e) {
            logger.error("Unknown error occurred", e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void addTripToGuides(Context ctx) {
        try {
            int tripId = Integer.parseInt(ctx.pathParam("tripId"));
            int guidesId = Integer.parseInt(ctx.pathParam("guidesId"));
            Trip trip = tripDAO.addGuideToTrip(tripId, guidesId);
            ctx.json(trip);
        } catch (NumberFormatException e) {
            logger.error("Invalid trip or guide ID format", e);
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "Invalid trip or guide ID format",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (IllegalArgumentException e) {
            logger.error("Trip or Guide not found", e);
            ctx.status(404).json(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Exception e) {
            logger.error("Failed to add guide to trip: {}", e.getMessage(), e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Failed to add guide to trip: " + e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTripDTO = tripDAO.update(id, tripDTO);
            if (updatedTripDTO != null) {
                ctx.json(updatedTripDTO);
            } else {
                throw new NotFoundResponse("Trip not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid Trip ID", e);
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "Invalid Trip ID",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (NotFoundResponse e) {
            logger.error("Doctor not found", e);
            ctx.status(404).json(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Throwable e) {
            logger.error("Internal server error", e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO deletedTripDTO = tripDAO.delete(id);
            if (deletedTripDTO != null) {
                String jsonResponse = String.format("{\"Message\": \"Trip deleted\", \"deletedTripDTO\": %s}",
                        OBJECT_MAPPER.writeValueAsString(deletedTripDTO));
                ctx.status(200).json(jsonResponse);
            } else {
                throw new NotFoundResponse("Trip not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid Trip ID", e);
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "message", "Invalid Trip ID",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (NotFoundResponse e) {
            logger.error("Plant not found", e);
            ctx.status(404).json(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Throwable e) {
            logger.error("Internal server error", e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void getTripsByGuide (Context ctx) {
        try {
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            ctx.json(tripDAO.getTripsByGuide(guideId));
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "message", "Invalid Guide ID format.",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }


    public void populate(Context ctx) {
        try {
            if(!tripDAO.getAll().isEmpty()) {
                ctx.status(400).json(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", "The Database is already populated",
                        "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));
            } else {
                populateDatabase();
                ctx.res().setStatus(200);
                ctx.json("{ \"Message\": \"The Database has been populated\" }");
            }
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage(), e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error: " + e.getMessage(),
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }

    public void getByCategory(Context ctx) {
        try {
            String specialityParam = ctx.pathParam("category");
            if (specialityParam.isEmpty()) {
                ctx.status(400).json(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Category parameter is required",
                        "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));
                return;
            }
            Categories categories;
            try {
                categories = Categories.valueOf(specialityParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid category: {}", specialityParam,e);
                ctx.status(400).json(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Invalid Category",
                        "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));
                return;
            }List<TripDTO> tripDTOS = tripDAO.getByCategory(categories);
            ctx.json(tripDTOS);
        } catch (Exception e) {
            logger.error("Unknown error occurred", e);
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Internal server error",
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        }
    }


    //Try fetching data
    public void getPackingItems(Context ctx) {
        String category = ctx.pathParam("category").toLowerCase();
        String url = BASE_URL_PACKING + category;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ctx.json(OBJECT_MAPPER.readTree(response.body()));
            } else {
                ctx.status(HttpStatus.BAD_REQUEST).result("Failed to fetch packing items");
            }
        } catch (IOException | InterruptedException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error fetching packing items");
        }
    }

}
