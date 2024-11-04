package dat.dtos;

import dat.entities.Trip;
import dat.enums.Categories;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class TripDTO {

    private int id;
    private String name;
    private String price;
    private String starttime;
    private String endtime;
    private String longitude;
    private String latitude;
    private Categories category;
    private Set<GuidesDTO> guides = new HashSet<>();


    public TripDTO(int id, String name, String price, String starttime, String endtime, String longitude, String latitude, Categories category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.starttime = starttime;
        this.endtime = endtime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
    }

    public TripDTO(String name, String price, String starttime, String endtime, String longitude, String latitude, Categories category) {
        this.name = name;
        this.price = price;
        this.starttime = starttime;
        this.endtime = endtime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
    }

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.starttime = trip.getStarttime();
        this.endtime = trip.getEndtime();
        this.longitude = trip.getLongitude();
        this.latitude = trip.getLatitude();
        this.category = trip.getCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDTO tripDTO = (TripDTO) o;
        return Objects.equals(id, tripDTO.id) &&
                Objects.equals(name, tripDTO.name) &&
                Objects.equals(price, tripDTO.price) &&
                Objects.equals(starttime, tripDTO.starttime) &&
                Objects.equals(endtime, tripDTO.endtime) &&
                Objects.equals(longitude, tripDTO.longitude) &&
                Objects.equals(latitude, tripDTO.latitude) &&
                category == tripDTO.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, starttime, endtime, longitude, latitude, category);
    }

    public Trip toEntity() {
        return new Trip(this);
    }
}
