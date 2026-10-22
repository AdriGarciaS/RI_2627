package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;


public abstract class PaymentMean {

	// Natural attributes
    private Money accumulated = Money.ZERO; // cents

    // Accidental attributes
    private Client client;

    // ---- Association helpers ----

    void _setClient(Client client) {
        this.client = client;
    }

    // ---- Business methods ----

    public abstract boolean canBeCharged(Money amount);

    /**
     * Increments the accumulated amount of the payment mean by the given amount.
     * package-private Only called from Charge constructor.
     * @param amount
     */
    /* package */ void charge(Money amount) {
    	// Normalize the amount to cents 
    	// check if it can be charged
    	// increment the accumulated amount
    	Money normalized = normalizePositive(amount);
    	StateChecks.isTrue(canBeCharged(normalized), "Payment mean can not be charged");

    	accumulated = Money.calc(accumulated).add(normalized).toMoneyRoundedToCents();
    }

    /**
	 * Decrements the accumulated amount of the payment mean by the given amount.
	 * package-private Only called from Charge.rewind().
	 * @param amount
	 */
	/* package */ void rewind(Money amount) {
		Money normalized = normalizePositive(amount);
		StateChecks.isTrue(accumulated.greaterOrEqual(normalized), "Can not rewind more than accumulated");

		accumulated = Money.calc(accumulated).subtract(normalized).toMoneyRoundedToCents();
	}

	public Client getClient() {
		return client;
	}

	public Money getAccumulated() {
		return accumulated;
	}

	protected static void checkPositive(Money amount) {
		ArgumentChecks.isNotNull(amount, "Amount cannot be null");
		ArgumentChecks.isTrue(amount.greaterThan(Money.ZERO), "Amount must be positive");
	}

	protected static Money normalizePositive(Money amount) {
		ArgumentChecks.isNotNull(amount, "Amount cannot be null");
		Money result = Money.calc(amount).toMoneyRoundedToCents();
		checkPositive(result);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PaymentMean that = (PaymentMean) o;
		return Objects.equals(client, that.client);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(client);
	}

	@Override
	public String toString() {
		return "PaymentMean{" + "accumulated=" + accumulated + '}';
	}
}

