package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.jdbc.Jdbc;

public class AddMechanic {
    
    private static final String TMECHANICS_ADD = "insert into TMechanics (id, nif, name, surname, version, createdAt, "
        + "updatedAt, entityState) values (?, ?, ?, ?, ?, ?, ?, ?)";

    
    private MechanicDto m = new MechanicDto();;


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

   
    
    public MechanicDto execute() {
       
        try (Connection c = Jdbc.createThreadConnection();) {
            try (PreparedStatement pst = c.prepareStatement(TMECHANICS_ADD)) {
                pst.setString(1, m.id);
                pst.setString(2, m.nif);
                pst.setString(3, m.name);
                pst.setString(4, m.surname);
                pst.setLong(5, m.version);
                pst.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                pst.setString(8, "ENABLED");               

                pst.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return m;
    }
    
    
}
