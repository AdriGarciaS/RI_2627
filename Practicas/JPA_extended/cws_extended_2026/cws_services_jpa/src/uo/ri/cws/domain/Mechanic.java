package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;

public class Mechanic {

	// Natural attributes
	private Nif nif;
	private FullName fullName;

	// Accidental attributes
	private Set<WorkOrder> assigned = new HashSet<>();
	
	public Mechanic(Nif nif) {
		// Delegate to the other constructor with meaningful default values for the fullname
		this(nif, new FullName("no-name", "no-surname"));
	}

	public Mechanic(Nif nif, FullName name) {
		ArgumentChecks.isNotNull(nif , "nif can not be null");
		ArgumentChecks.isNotNull(name, "name can not be null");

		this.nif = nif;
		this.fullName = name;

	}

	// ---- Setters (optional, controlled) ----

	public void setFullName(FullName name) {
		ArgumentChecks.isNotNull(name);
		this.fullName = name;
	}
	
	public void setNif(Nif nif) {
		ArgumentChecks.isNotNull(nif);
		this.nif = nif;		
	}

	//getters

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>(assigned);
	}

	public FullName getFullName() {
		return fullName;
	}

	public Nif getNif() {
		return nif;
	}


	// hashcode and equal

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Mechanic mechanic = (Mechanic) o;
		return Objects.equals(nif, mechanic.nif);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nif);
	}

	//tostring

	@Override
	public String toString() {
		return "Mechanic{" + "nif=" + nif + ", fullName=" + fullName + '}';
	}


	// ---- Associations helpers ----

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

}
