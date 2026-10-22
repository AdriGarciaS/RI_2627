package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindMechanicById implements Command<Optional<MechanicDto>>{

    private String mechanicId;
    private MechanicGateway mg = Factories.persistence.forMechanic();

    public FindMechanicById (String id) {
        ArgumentChecks.isNotBlank(id , "Id can not be blank to find a mechanic");
        this.mechanicId = id;
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return mg.findById(mechanicId)
                .map(MechanicDtoAssembler::toDto);
    }

}
