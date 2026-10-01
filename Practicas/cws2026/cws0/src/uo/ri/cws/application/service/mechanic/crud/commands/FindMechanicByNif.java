package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.jdbc.Jdbc;

public class FindMechanicByNif {

    private static final String TMECHANICS_FINDBYNIF = "select * from TMechanics where nif = ?";
    private String mechanicNif;

    public FindMechanicByNif (String nif) {
        ArgumentChecks.isNotNull(nif , "Nif can not be null to update a mechanic ");
        
        this.mechanicNif = nif;
    }
    
    public Optional<MechanicDto> execute(){
        Optional<MechanicDto> result = Optional.empty();
        
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_FINDBYNIF)) {
                pst.setString(1, mechanicNif);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        MechanicDto dto = new MechanicDto();
                        dto.id= rs.getString("id");
                        dto.name = rs.getString("name");
                        dto.surname = rs.getString("surname");

                        result = Optional.of(dto) ;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
}
