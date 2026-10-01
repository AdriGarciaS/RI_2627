package uo.ri.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {

	@Override
	public void execute() throws BusinessException {

		// Ask the user for data
		MechanicDto dto = new MechanicDto();
		dto.nif = Console.readString("Nif");
		dto.name = Console.readString("Name");
		dto.surname = Console.readString("Surname");

		// Invoke the service
		MechanicCrudService as = Factories.service.forMechanicCrudService();
		dto = as.create( dto );

		// Show result
		Console.println("New mechanic added: " + dto.id);
	}

}
