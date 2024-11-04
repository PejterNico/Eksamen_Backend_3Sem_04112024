package dat.routes;

import dat.controllers.impl.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoute {

    private final TripController tripController = new TripController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/populate", tripController::populate, Role.ANYONE);
            get("/", tripController::getAll, Role.ANYONE);
            get("/{id}", tripController::getById, Role.ANYONE);
            get("/guides/{guidesId}", tripController::getTripsByGuide, Role.ANYONE);
            get("/categories/{category}", tripController::getByCategory, Role.ANYONE);
            get("/packing-items/{category}", tripController::getPackingItems);
            post("/", tripController::create, Role.ANYONE);
            post("/{guideId}/trip/{tripId}", tripController::addTripToGuides, Role.ANYONE);
            put("/{id}", tripController::update, Role.ANYONE);
            delete("/{id}", tripController::delete, Role.ANYONE);
        };
    }
}
