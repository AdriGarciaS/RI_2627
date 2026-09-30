package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceimpl implements MechanicCrudService {

    @Override
    public MechanicDto create(MechanicDto dto) throws BusinessException {
        return new AddMechanic(dto).execute();
    }

    @Override
    public void delete(String mechanicId) throws BusinessException {
        new DeleteMechanic(mechanicId).execute();

    }

    @Override
    public void update(MechanicDto dto) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<MechanicDto> findById(String id) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<MechanicDto> findByNif(String nif) throws BusinessException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

}
