package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.jdbc.Jdbc;

public class FindAllMechanic {

    private static final String TMECHANICS_FIND_ALL =
        "SELECT id, version, nif, name, surname FROM TMechanics";

    public List<MechanicDto> execute() {
        List<MechanicDto> result = new ArrayList<>();
    
        try (Connection c = Jdbc.createThreadConnection();
             PreparedStatement pst = c.prepareStatement(TMECHANICS_FIND_ALL);
             ResultSet rs = pst.executeQuery()) {
    
            while (rs.next()) {
                result.add(toDto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
    private MechanicDto toDto(ResultSet rs) throws SQLException {
        MechanicDto dto = new MechanicDto();
        dto.id = rs.getString("id");
        dto.version = rs.getLong("version");
        dto.nif = rs.getString("nif");
        dto.name = rs.getString("name");
        dto.surname = rs.getString("surname");
        return dto;
    }

}
