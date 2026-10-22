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
        new UpdateMechanic(dto).execute();

    }

    @Override
    public Optional<MechanicDto> findById(String id) throws BusinessException {
        return new FindMechanicById(id).execute();
    }

    @Override
    public Optional<MechanicDto> findByNif(String nif) throws BusinessException {
        return new FindMechanicByNif(nif).execute();
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
        return new FindAllMechanic().execute();
    }

}
