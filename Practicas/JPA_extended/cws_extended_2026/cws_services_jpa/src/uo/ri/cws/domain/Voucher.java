package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;

public class Voucher extends PaymentMean {

    private VoucherCode code;
    private String description;
    private Money availableAmount; // cents

    // ---- Constructors ----
    public Voucher(VoucherCode code, Money available) {
        this(code, "no-description", available);
    }

    public Voucher(VoucherCode code, String description, Money available) {
        ArgumentChecks.isNotNull(code);
        ArgumentChecks.isNotBlank(description);
        ArgumentChecks.isNotNull(available);
        ArgumentChecks.isTrue(available.isNonNegative(), "Available amount must be non-negative");

        this.code = code;
        this.description = description;
		this.availableAmount = Money.calc(available).toMoneyRoundedToCents();
    }

    // ------ Getters and setters (if needed) ----

    public Money getAvailableAmount() {
        return availableAmount;
    }

    public String getDescription() {
        return description;
    }

    public void changeDescription(String description) {
        ArgumentChecks.isNotBlank(description);
        this.description = description;
    }

    public VoucherCode getCode() {
        return code;
    }

    // ---- Business methods ----

    @Override
    /* package */ void charge(Money amount) {
    	// Charges the amount to the payment mean and subtracts it from the available amount
    	super.charge(amount);
    	availableAmount = Money.calc(availableAmount)
    			.subtract(normalizePositive(amount))
    			.toMoneyRoundedToCents();
    }

	@Override
	/* package */ void rewind(Money amount) {
		// Rewinds the amount to the payment mean and adds it to the available amount
		super.rewind(amount);
		availableAmount = Money.calc(availableAmount)
				.add(normalizePositive(amount))
				.toMoneyRoundedToCents();
	}

    @Override
    public boolean canBeCharged(Money amount) {
    	checkPositive(amount);
    	return availableAmount.greaterOrEqual(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Voucher voucher = (Voucher) o;
        return Objects.equals(code, voucher.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return "Voucher{" + "availableAmount=" + availableAmount + ", code=" + code + ", description='" + description + '\'' + '}';
    }
}
