package dat.routes;


import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;


public class Routes {

    public final TripRoute DBRoute = new TripRoute();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/trips", DBRoute.getRoutes());
        };
    }
}
