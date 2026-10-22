package uo.ri.cws.application.persistence.workorder;

import java.util.List;

import uo.ri.util.exception.PersistenceException;

public interface WorkOrderGateway {

    List<String> findIdsByMechanicId(String mechanicId) throws PersistenceException;

}
