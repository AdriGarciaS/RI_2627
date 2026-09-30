package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {


    private static final String TMECHANICS_UPDATE = 
            "update TMechanics set name = ?, surname = ?, "
    		+ "version = version + 1, updatedat = ?"
            + "where id = ?";

    @Override
    public void execute() throws BusinessException {

        // Get info
        String id = Console.readString("Type mechahic id to update");

        // check mechanic exists
        Optional<MechanicDto> existing = Factories
            .service
            .forMechanicCrudService()
            .findById(id);
        
        if(existing.isEmpty()) {
            Console.print("The mechanic is not present in the system");
            return;
        }
        
        
        // Ask for new data
        // nif is the identity, cannot be changed
        String name = Console.readString("Name");
        String surname = Console.readString("Surname");

        // update
        updateMechanic(id, name, surname);

        // Print result
        Console.println("Mechanic updated");
    }

    private void updateMechanic(String id, String name, String surname) {

        // Process
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_UPDATE)) {
                pst.setString(1, name);
                pst.setString(2, surname);
                pst.setTimestamp(3, new Timestamp(
						System.currentTimeMillis()));
                pst.setString(4, id);

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
}