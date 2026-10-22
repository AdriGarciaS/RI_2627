package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto>{
    

    private MechanicDto m = new MechanicDto();
    
    private MechanicGateway mg  = Factories.persistence.forMechanic();


    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Dto can not be null to add a mechanic");
       ArgumentChecks.isNotBlank(dto.nif, "nif can not be blank to add a mechanic");
       ArgumentChecks.isNotBlank(dto.name, "name can not be blank to add a mechanic");
       ArgumentChecks.isNotBlank(dto.surname, "Surname can not be blank to add a mechanic");
       m.name= dto.name;
       m.nif= dto.nif;
       m.surname= dto.surname;
        m.id = UUID.randomUUID().toString();
       m.version = 1;

    }

   
    
    @Override
    public MechanicDto execute() throws BusinessException {
        
        BusinessChecks.doesNotExist(mg.findByNif(m.nif),
            "There is already a mechanic with that nif");
        
        MechanicRecord record = MechanicDtoAssembler.toRecord(m);
        mg.add(record );
        
        m.id = record.id;
        m.version = record.version;
        
        
        return m;
    }
    
    
}
