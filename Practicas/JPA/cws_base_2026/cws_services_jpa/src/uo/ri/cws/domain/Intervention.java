package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

/**
 * Represents an intervention performed by a mechanic on a work order.
 * Internal aggregate class of WorkOrder. Only WorkOrder can create Interventions.
 */
public class Intervention {
	// Natural attributes
	private LocalDateTime date;
	private int minutes;

	// Accidental attributes
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Set<Substitution> substitutions = new HashSet<>();

	// ---------------------------- Constructors -----------------------------
	/**
	 * All these constructors are package-private 
	 * Only WorkOrder (aggregate root) can create Interventions
	 */
	/* package */ Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		this(mechanic, workOrder, LocalDateTime.now(), minutes);
	}

	/* package */ Intervention(Mechanic mechanic, WorkOrder workOrder, LocalDateTime date, int minutes) {
		this(mechanic, workOrder, date, minutes, Map.of());
	}

	/**
	 * Creates a new Intervention with the given parameters.
	 * @param mechanic The mechanic performing the intervention.
	 * @param workOrder The work order associated with the intervention.
	 * @param date The date and time of the intervention.
	 * @param minutes The duration of the intervention in minutes.
	 * @param substitutions A map of spare parts and their quantities used in the intervention.
	 */
	/* package */ Intervention(
			Mechanic mechanic,
			WorkOrder workOrder,
			LocalDateTime date,
			int minutes,
			Map<SparePart, Integer> substitutions) {
		
		// Validate input parameters
		// Truncate the date to milliseconds to avoid precision issues
		// links the intervention with the work order and mechanic (atomiclink)
		// For each substitution in the map, add it to the intervention
		ArgumentChecks.isNotNull(mechanic, "mechanic can not be null");
		ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
		ArgumentChecks.isNotNull(date, "date can not be null");
		ArgumentChecks.isTrue(minutes >= 0, "minutes can not be negative");
		validateSubstitutions(substitutions);

		this.date = date.truncatedTo(ChronoUnit.MILLIS);
		this.minutes = minutes;
		Associations.RecordsIntervenes.atomicLink(workOrder, this, mechanic);

		for (Map.Entry<SparePart, Integer> entry : substitutions.entrySet()) {
			SparePart sparePart = entry.getKey();
			Integer value = entry.getValue();
			addSubstitution(sparePart, value);
		}
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	public int getMinutes() {
		return minutes;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Intervention that = (Intervention) o;
		return Objects.equals(date, that.date) && Objects.equals(workOrder,
				that.workOrder) && Objects.equals(mechanic, that.mechanic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, workOrder, mechanic);
	}

	@Override
	public String toString() {
		return "Intervention{" + "date=" + date + ", minutes=" + minutes + ", workOrder=" + workOrder + ", mechanic=" + mechanic + '}';
	}

	// ---------------------------- Association helpers -----------------------------
	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	void _setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>(substitutions);
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	// ---------------------------- Business methods -----------------------------

	/* package */ void removeSubstitutions() {
		new HashSet<>(substitutions).forEach(Associations.PerformsSubstitutes::atomicUnlink);
	}

	/**
	 * Calculates the total amount of the intervention, including work and spare parts.
	 * @return The total amount of the intervention rounded to cents as a Money object.
	 */
	public Money getTotalAmount() {
		return Money.calc()
				.add( calculateWorkAmount() )
				.add( calculateSparesAmount() )
				.toMoneyRoundedToCents();
	}

	private Money calculateWorkAmount() {
		Money pricePerHour = workOrder.getPricePerHour();
		
		return Money.calc(pricePerHour)
                .times( minutes )
                .div( 60 ) // mins/hour
                .toMoneyExact();
	}

	private Money calculateSparesAmount() {
		Money.MoneyCalculator calculator = Money.calc();
		substitutions.forEach(s -> calculator.add(s.getTotalAmount()));
		return calculator.toMoneyExact();
	}

	private void addSubstitution(SparePart sparePart, int quantity) {
		new Substitution(sparePart, this, quantity);
	}

	private static void validateSubstitutions(Map<SparePart, Integer> substitutions) {
		ArgumentChecks.isNotNull(substitutions, "substitutions can not be null");
		for (Map.Entry<SparePart, Integer> entry : substitutions.entrySet()) {
			ArgumentChecks.isNotNull(entry.getKey(), "spare part can not be null");
			ArgumentChecks.isNotNull(entry.getValue(), "quantity can not be null");
			ArgumentChecks.isTrue(entry.getValue() > 0, "quantity must be greater than zero");
		}
	}

}
