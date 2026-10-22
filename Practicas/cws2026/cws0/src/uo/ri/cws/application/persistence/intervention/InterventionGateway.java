package uo.ri.cws.application.persistence.intervention;

import java.util.List;

import uo.ri.util.exception.PersistenceException;

public interface InterventionGateway {

    List<String> findIdsByMechanicId(String mechanicId) throws PersistenceException;

}
