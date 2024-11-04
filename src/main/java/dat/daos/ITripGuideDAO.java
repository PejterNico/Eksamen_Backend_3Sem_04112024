package dat.daos;

import dat.dtos.TripDTO;
import dat.entities.Trip;

import java.util.Set;

public interface ITripGuideDAO<T> {

    Trip addGuideToTrip(int tripId, int guideId);
    Set<TripDTO> getTripsByGuide(int guideId);

}
