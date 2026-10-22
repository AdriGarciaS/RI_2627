package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Charge {

	// Natural attributes
    private Money amount; // cents

    // Accidental attributes
    private Invoice invoice;
    private PaymentMean paymentMean;

    // ------ Constructors -----
    
    /* package */ Charge(Invoice invoice, PaymentMean paymentMean, Money amount) {
    	// validates the arguments
		// store the amount rounded to cents
		// increments the paymentMean accumulated -> paymentMean.charge( amount )
		// links invoice, this and paymentMean
    	ArgumentChecks.isNotNull(invoice, "invoice can not be null");
    	ArgumentChecks.isNotNull(paymentMean, "paymentMean can not be null");
    	ArgumentChecks.isNotNull(amount, "amount can not be null");

    	this.amount = PaymentMean.normalizePositive(amount);
    	paymentMean.charge(this.amount);
    	Associations.SettlesWithdraws.atomicLink(invoice, this, paymentMean);
    }

    // ---- Getters ----

    public Money getAmount() {
        return amount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    // ---- Bussiness methods  ----

    /**
     * Restores and the value to the payment mean and unlinks the charge from the invoice and the payment mean.
     */
	/* package */ void rewind() {
		// decrements the payment mean accumulated ( paymentMean.rewind( amount) )
		StateChecks.isNotNull(invoice, "Charge already rewound");

		paymentMean.rewind(amount);
		Associations.SettlesWithdraws.atomicUnlink(this);
    }

    // ---- Association helpers ----

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    void _setPaymentMean(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }

    // ---- toString ----
    @Override
    public String toString() {
		return "Charge [invoice=" + invoice + ", paymentMean=" + paymentMean + ", amount=" + amount + "]";
    }
}
