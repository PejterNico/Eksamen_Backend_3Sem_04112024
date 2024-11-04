package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guides;
import dat.entities.Trip;
import dat.enums.Categories;
import dat.exceptions.JpaOperationException;
import dat.security.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO>, ITripGuideDAO<TripDTO>{

    private static final Logger logger = LoggerFactory.getLogger(TripDAO.class);
    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }

    @Override
    public List<TripDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t", TripDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching all trips", e);
            throw new JpaOperationException("An error occurred while fetching all trips", e);
        }
    }

    @Override
    public TripDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            return trip != null ? new TripDTO(trip) : null;
        } catch (Exception e) {
            logger.error("Error fetching Trips by ID: {}", id, e);
            throw new JpaOperationException("An error occurred while fetching the Trips by ID: " + id, e);
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } catch (Exception e) {
            logger.error("Error creating Trip", e);
            throw new JpaOperationException("An error occurred while creating a Trip", e);
        }
    }

    public TripDTO update(int id, TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip d = em.find(Trip.class, id);
            if (d == null) {
                throw new ApiException(404, "Trip not found");
            }
            d.setName(tripDTO.getName());
            d.setPrice(tripDTO.getPrice());
            d.setStarttime(tripDTO.getStarttime());
            d.setEndtime(tripDTO.getEndtime());
            d.setLongitude(tripDTO.getLongitude());
            d.setLatitude(tripDTO.getLatitude());
            d.setCategory(tripDTO.getCategory());
            Trip mergedTrip = em.merge(d);
            em.getTransaction().commit();
            return new TripDTO(mergedTrip);
        } catch (Exception e) {
            logger.error("Error updating trip with ID: {}", id, e);
            throw new JpaOperationException("An error occurred while updating the trip with ID: " + id, e);
        }
    }

    @Override
    public TripDTO delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new RuntimeException("Plant not found");
            }
            em.remove(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting trip", e);
        }
    }

    @Override
    public Trip addGuideToTrip(int tripId, int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guides guides = em.find(Guides.class, guideId);

            if (trip != null && guides != null) {
                trip.addGuide(guides);
                guides.addTrip(trip);
                em.merge(trip);
                em.merge(guides);
                em.getTransaction().commit();
                return trip;
            } else {
                em.getTransaction().rollback();
                throw new ApiException(404, "Trip or Guide not found");
            }
        } catch (Exception e) {
            logger.error("Error adding trip to guide with ID: {}", tripId, e);
            throw new JpaOperationException("An error occurred while adding trip to guide with ID: " + guideId, e);
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            Guides guide = em.find(Guides.class, guideId);
            if (guide != null) {
                return guide.getTrips().stream()
                        .map(TripDTO::new)
                        .collect(Collectors.toSet());
            }
            return null;
        } catch (Exception e) {
            logger.error("Error fetching trips by guide with ID: {}", guideId, e);
            throw new JpaOperationException("An error occurred while fetching trips by guide with ID: " + guideId, e);
        }
    }

    public List<TripDTO> getByCategory(Categories categories) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(r) FROM Trip r WHERE r.category = :categories", TripDTO.class);
            query.setParameter("categories", categories);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching trip by category: {}", categories, e);
            throw new JpaOperationException("An error occurred while fetching doctors by specialty: " + categories, e);
        }
    }

}
