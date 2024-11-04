package dat.dtos;

import dat.entities.Guides;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GuidesDTO {

    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;
    private Set<TripDTO> trips = new HashSet<>();

    public GuidesDTO(int id, String firstname, String lastname, String email, String phone, int yearsOfExperience) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public GuidesDTO(String firstname, String lastname, String email, String phone, int yearsOfExperience) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public GuidesDTO(Guides guides) {
        this.id = guides.getId();
        this.firstname = guides.getFirstname();
        this.lastname = guides.getLastname();
        this.email = guides.getEmail();
        this.phone = guides.getPhone();
        this.yearsOfExperience = guides.getYearsOfExperience();
        if (guides.getTrips() != null) {
            guides.getTrips().forEach(trip -> trips.add(new TripDTO(trip)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuidesDTO guidesDTO = (GuidesDTO) o;
        return Objects.equals(id, guidesDTO.id) &&
                Objects.equals(firstname, guidesDTO.firstname) &&
                Objects.equals(lastname, guidesDTO.lastname) &&
                Objects.equals(email, guidesDTO.email) &&
                Objects.equals(phone, guidesDTO.phone) &&
                Objects.equals(yearsOfExperience, guidesDTO.yearsOfExperience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, phone, yearsOfExperience);
    }
}
