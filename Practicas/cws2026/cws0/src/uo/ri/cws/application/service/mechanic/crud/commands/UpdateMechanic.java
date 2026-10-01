package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {
    
    private static final String TMECHANICS_UPDATE =
        "UPDATE TMechanics SET name = ?, surname = ? WHERE id = ?";

    private final MechanicDto dto;

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Dto can not be null to update a mechanic");
        ArgumentChecks.isNotBlank(dto.id, "Mechanic id can not be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "Mechanic name can not be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "Mechanic surname can not be null or blank");
    
        this.dto = dto;
    }

    public void execute() throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection();
             PreparedStatement pst = c.prepareStatement(TMECHANICS_UPDATE)) {
    
            pst.setString(1, dto.name);
            pst.setString(2, dto.surname);
            pst.setString(3, dto.id);
    
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 0) {
                throw new BusinessException("The mechanic does not exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
    }
}

}
