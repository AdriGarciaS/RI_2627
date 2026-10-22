package uo.ri.cws.application.service.mechanic.crud;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicDtoAssembler {
    
    public static MechanicRecord toRecord(MechanicDto dto) {
        MechanicRecord mr = new MechanicRecord();
        
        mr.name = dto.name;
        mr.surname = dto.surname;
        mr.nif = dto.nif;
        
        return mr;
    }

    public static MechanicDto toDto(MechanicRecord mr) {
        MechanicDto dto = new MechanicDto();

        dto.id = mr.id;
        dto.version = mr.version;
        dto.nif = mr.nif;
        dto.name = mr.name;
        dto.surname = mr.surname;

        return dto;
    }

}
