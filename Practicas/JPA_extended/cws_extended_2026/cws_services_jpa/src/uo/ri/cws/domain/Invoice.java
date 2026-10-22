package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Invoice {
	private static final LocalDate JULY_1_2012 = LocalDate.of(2012, 7, 1);

	public enum InvoiceState {
		ISSUED, PAID
	}

	// Natural attributes
	private long number;
	private LocalDate date;
	private InvoiceState state = InvoiceState.ISSUED;

	/*
	 * Subtotal amount of the invoice, before applying VAT. This represents the taxable base.
	 */
	private Money subtotal = Money.ZERO;  // cents

	/*
	 * VAT rate applied to the invoice, expressed as a percentage. Example: 21% VAT.
	 */
	private Percentage vatRate = Percentage.ofPercent(21);  // default to 21%

	/*
	 * VAT amount calculated from the subtotal amount. This is the tax the customer must pay.
	 */
	private Money vatAmount = Money.ZERO;  // cents

	/*
	 * Total amount of the invoice including VAT. This is the final amount the customer must pay.
	 */
	private Money total = Money.ZERO; // cents

	private boolean usedForVoucher = false;

	// Accidental attributes
	private Set<WorkOrder> workOrders = new HashSet<>();
	private Set<Charge> charges = new HashSet<>();

	// ----------------- Constructors -------------------------------------
	
	public Invoice(long number) {
		// call full constructor with sensible defaults
		this(number, LocalDate.now());
	}

	public Invoice(long number, LocalDate date) {
		// call full constructor with sensible defaults
		this(number, date, List.of());
	}

	public Invoice(long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	public Invoice(long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		// store the number
		// add every work order calling addWorkOrder( w )
		ArgumentChecks.isTrue(number >= 0, "number can not be negative");
		ArgumentChecks.isNotNull(date, "date can not be null");
		ArgumentChecks.isNotNull(workOrders, "workOrders can not be null");
		workOrders.forEach(w -> ArgumentChecks.isNotNull(w, "workOrder can not be null"));
		workOrders.forEach(w -> StateChecks.isTrue(w.isApproved(), "Work order is not approved"));

		this.number = number;
		this.date = date;
		workOrders.forEach(this::addWorkOrder);
	}

	// ---- Getters ----
	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	public Money getSubtotal() {
		return subtotal;
	}

	public Percentage getVatRate() {
		return vatRate;
	}

	public Money getVatAmount() {
		return vatAmount;
	}

	public Money getTotal() {
		return total;
	}

	// ------------------ Association helpers (package-private) --------------------------
	
	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	// ---- Business methods --------------------------------------------
	
	/**
	 * Adds a work order to the invoice. The work order must be approved and the invoice must not be settled.
	 * @param workOrder
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		// Validations
		// links the work order 
		// compute the invoice amount after adding the work order
		// marks it as invoiced
		ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");
		StateChecks.isTrue(workOrder.isApproved(), "Work order is not approved");

		Associations.Bills.link(this, workOrder);
		computeAmount();
		workOrder.invoice();
	}

	/**
	 * Removes a work order from the invoice. The invoice must not be settled.
	 * @param workOrder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "workOrder can not be null");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");
		ArgumentChecks.isTrue(workOrders.contains(workOrder), "Work order does not belong to this invoice");

		Associations.Bills.unlink(workOrder);
		workOrder.uninvoice();
		computeAmount();
	}

	public boolean isSettled() {
		return state == InvoiceState.PAID;
	}

	public boolean isUsedForVoucher() {
		return usedForVoucher;
	}

	/**
	 * A paid invoice over 500 euro not used yet for a voucher can generate a 30 euro voucher.
	 */
	public boolean canGenerateVoucher30() {
		return isSettled()
				&& total.greaterThan(Money.of("500.00"))
				&& !usedForVoucher;
	}

	public void markAsUsedForVoucher() {
		usedForVoucher = true;
	}

	/**
	 * Settles the invoice by charging the specified payment means with the requested amounts.
	 * The total of the charges must equal the invoice total, and each payment mean must be able to be charged.
	 * @param requestedCharges A map of payment means and their corresponding amounts to charge.
	 */
	public void settle(Map<PaymentMean, Money> requestedCharges) {
		// Validations
		// Normalize the requested charges (round to cents, check positive)
		// Check that the total of requested charges equals the invoice total
		// For each payment mean, create a Charge and link it to the invoice and payment mean
		// Set the invoice state to PAID
		ArgumentChecks.isNotNull(requestedCharges, "charges can not be null");
		ArgumentChecks.isFalse(requestedCharges.isEmpty(), "charges can not be empty");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");

		Map<PaymentMean, Money> charges = normalize(requestedCharges);
		StateChecks.isTrue(total.greaterThan(Money.ZERO), "Invoice total must be positive");
		StateChecks.isTrue(chargesTotal(charges).equals(total), "Charges do not sum the invoice total");
		charges.forEach((paymentMean, amount) ->
				StateChecks.isTrue(paymentMean.canBeCharged(amount), "Payment mean can not be charged"));

		charges.forEach((paymentMean, amount) -> new Charge(this, paymentMean, amount));
		state = InvoiceState.PAID;
	}

	public void unSettle() {
		StateChecks.isTrue(isSettled(), "Invoice is not settled");

		new HashSet<>(charges).forEach(Charge::rewind);
		state = InvoiceState.ISSUED;
	}

	// ---- Calculation ----
	private void computeAmount() {
	    // 1. Sum all work order totals = subtotal
	    // 2. Determine VAT rate based on invoice date
	    // 3. Compute VAT amount
	    // 4. Compute total amount (subtotal + VAT) rounded to cents
		Money.MoneyCalculator calculator = Money.calc();
		workOrders.forEach(w -> calculator.add(w.getAmount()));
		subtotal = calculator.toMoneyRoundedToCents();

		vatRate = getApplicableVatRate();
		vatAmount = Money.calc(subtotal).times(vatRate).toMoneyRoundedToCents();
		total = Money.calc(subtotal).add(vatAmount).toMoneyRoundedToCents();
	}


	private Percentage getApplicableVatRate() {
		return date.isAfter(JULY_1_2012) ? Percentage.ofPercent(21) : Percentage.ofPercent(18);
	}

	private Map<PaymentMean, Money> normalize(Map<PaymentMean, Money> requestedCharges) {
		Map<PaymentMean, Money> result = new LinkedHashMap<>();
		requestedCharges.forEach((paymentMean, amount) -> {
			ArgumentChecks.isNotNull(paymentMean);
			ArgumentChecks.isNotNull(amount);
			Money normalizedAmount = Money.calc(amount).toMoneyRoundedToCents();
			ArgumentChecks.isTrue(normalizedAmount.greaterThan(Money.ZERO), "Charge amount must be positive");
			result.put(paymentMean, normalizedAmount);
		});
		return result;
	}

	private Money chargesTotal(Map<PaymentMean, Money> requestedCharges) {
		Money.MoneyCalculator calculator = Money.calc();
		requestedCharges.values().forEach(calculator::add);
		return calculator.toMoneyRoundedToCents();
	}

}
