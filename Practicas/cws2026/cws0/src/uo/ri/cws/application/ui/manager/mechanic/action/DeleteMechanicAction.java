package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteMechanicAction implements Action {

    
   
    @Override
    public void execute() throws BusinessException {

        
       String  mechanicid = Console.readString("Type mechanic id ");
        
        Factories
            .service
            .forMechanicCrudService()
            .delete(mechanicid);
            
        //new DeleteMechanic(mechanicid).execute();;

        Console.println("Mechanic deleted");
    }

}