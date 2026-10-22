package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void>{

    
    private String id;
    private MechanicGateway mg = Factories.persistence.forMechanic();
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    private InterventionGateway ig = Factories.persistence.forIntervention();

    public DeleteMechanic(String id) {
        ArgumentChecks.isNotBlank(id, "Id can not be blank to delete a mechanic");
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {
        BusinessChecks.exists(mg.findById(id), "The mechanic does not exist");

        BusinessChecks.isTrue(wg.findIdsByMechanicId(id).isEmpty(),
                "The mechanic has work orders assigned");
        BusinessChecks.isTrue(ig.findIdsByMechanicId(id).isEmpty(),
                "The mechanic has interventions");

        mg.remove(id);
        return null;
    }
}
