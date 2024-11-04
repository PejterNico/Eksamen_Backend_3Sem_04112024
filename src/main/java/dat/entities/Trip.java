package dat.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dat.dtos.TripDTO;
import dat.enums.Categories;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false, unique = true)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "starttime", nullable = false)
    private String starttime;

    @Column(name = "endtime", nullable = false)
    private String endtime;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "category", nullable = false)
    private Categories category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "guides_id", nullable = false)
    @JsonBackReference // Ignorerer doctor i JSON-serialisering for at undgå cirkulære referencer
    private Guides guides;

    //constructor
    public Trip(String name, String price, String starttime, String endtime, String longitude, String latitude, Categories category) {
        this.name = name;
        this.price = price;
        this.starttime = starttime;
        this.endtime = endtime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
    }

    // Constructor fra DTO
    public Trip(TripDTO tripDTO) {
        this.id = tripDTO.getId();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.starttime = tripDTO.getStarttime();
        this.endtime = tripDTO.getEndtime();
        this.longitude = tripDTO.getLongitude();
        this.latitude = tripDTO.getLatitude();
        this.category = tripDTO.getCategory();
    }

    // Metode til at tilknytte en guide til en tur
    public void addGuide(Guides guides) {
        this.guides = guides;
    }
}
