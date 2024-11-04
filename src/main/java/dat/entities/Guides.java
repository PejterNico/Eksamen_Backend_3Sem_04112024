package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.dtos.GuidesDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guides")
public class Guides {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guides_id", nullable = false, unique = true)
    private int id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "guides")
    @JsonManagedReference  // Ejer af relationen
    @JsonIgnore
    private Set<Trip> trips = new HashSet<>();

    //constructor
    public Guides(String firstname, String lastname, String email, String phone, int yearsOfExperience) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    // // Constructor til oprettelse af en ny Guide med et set af Trips
    public Guides(String firstname, String lastname, String email, String phone, int yearsOfExperience, Set<Trip> trips) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
        this.trips = trips;
    }


    // Constructor fra DTO
    public Guides(GuidesDTO guidesDTO) {
        this.id = guidesDTO.getId();
        this.firstname = guidesDTO.getFirstname();
        this.lastname = guidesDTO.getLastname();
        this.email = guidesDTO.getEmail();
        this.phone = guidesDTO.getPhone();
        this.yearsOfExperience = guidesDTO.getYearsOfExperience();
    }

    public void addTrip(Trip trip) {
        if (this.trips == null) {
            this.trips = new HashSet<>();
        }
        this.trips.add(trip);
    }

    public void setAppointments(Set<Trip> trips) {
        if(trips != null) {
            this.trips = trips;
            for (Trip trip : trips) {
                trip.setGuides(this);
            }
        }
    }


}
