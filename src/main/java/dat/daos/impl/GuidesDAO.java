package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.GuidesDTO;
import dat.entities.Guides;
import dat.entities.Trip;
import dat.exceptions.JpaOperationException;
import dat.security.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuidesDAO implements IDAO<GuidesDTO> {

    private static final Logger logger = LoggerFactory.getLogger(GuidesDAO.class);
    private static GuidesDAO instance;
    private static EntityManagerFactory emf;

    public static GuidesDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuidesDAO();
        }
        return instance;
    }

    @Override
    public List<GuidesDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuidesDTO> query = em.createQuery("SELECT new dat.dtos.GuidesDTO(g) FROM Guides g", GuidesDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching all Guides", e);
            throw new JpaOperationException("An error occurred while fetching all Guides", e);
        }
    }

    @Override
    public GuidesDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Guides guides = em.find(Guides.class, id);
            return guides != null ? new GuidesDTO(guides) : null;
        } catch (Exception e) {
            logger.error("Error fetching Guides by ID: {}", id, e);
            throw new JpaOperationException("An error occurred while fetching the Guides by ID: " + id, e);
        }
    }

    @Override
    public GuidesDTO create(GuidesDTO guidesDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guides guides = new Guides(guidesDTO);
            em.persist(guides);
            em.getTransaction().commit();
            return new GuidesDTO(guides);
        } catch (Exception e) {
            logger.error("Error creating Guide", e);
            throw new JpaOperationException("An error occurred while creating a Guide", e);
        }
    }

    @Override
    public GuidesDTO update(int id, GuidesDTO guidesDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guides d = em.find(Guides.class, id);
            if (d == null) {
                throw new ApiException(404, "Doctor not found");
            }
            d.setFirstname(guidesDTO.getFirstname());
            d.setLastname(guidesDTO.getLastname());
            d.setEmail(guidesDTO.getEmail());
            d.setPhone(guidesDTO.getPhone());
            d.setYearsOfExperience(guidesDTO.getYearsOfExperience());
            Guides mergedGuides = em.merge(d);
            em.getTransaction().commit();
            return new GuidesDTO(mergedGuides);
        } catch (Exception e) {
            logger.error("Error updating Guide with ID: {}", id, e);
            throw new JpaOperationException("An error occurred while updating the Guide with ID: " + id, e);
        }
    }

    @Override
    public GuidesDTO delete(int id) {
        GuidesDTO deletedGuides = null;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guides guides = em.find(Guides.class, id);
            if (guides != null) {
                deletedGuides = new GuidesDTO(guides);
                for (Trip trip : guides.getTrips()) {
                    em.remove(trip);
                }
                em.remove(guides);
            } else {
                throw new ApiException(404, "Guide not found");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting guide with ID: {}", id, e);
            throw new JpaOperationException("An error occurred while deleting the guide with ID: " + id, e);
        }
        logger.info("Deleted guide: {}", deletedGuides);
        return deletedGuides;
    }
}
