package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class SparePart {
	
	// Natural attributes
	private String code;
	private String description;
	private Money unitPrice;

	public SparePart(String code) {
		this( code, "no-description", Money.ZERO );
	}

	public SparePart(String code, String description, Money price) {
		// Argument checks
		// Round the price to cents

		// TODO VALIDATION : FOR OBJECTS -> NOT NULL , FOR STRINGS -> NOT BLANK , FOR PRICES ->
		// NON NEGATIVE
		ArgumentChecks.isNotBlank(code, "code can not be blank");
		ArgumentChecks.isNotBlank(description, "description can not be blank");
		ArgumentChecks.isNotNull(price, "price can not be null");
		ArgumentChecks.isTrue(price.isNonNegative(), "price must be non-negative");

		this.code = code;
		this.description = description;
		this.unitPrice = price.roundToCents();
		

	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Money getUnitPrice() {
		return unitPrice;
	}

	public Money getPrice() {
		return unitPrice;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SparePart sparePart = (SparePart) o;
		return Objects.equals(code, sparePart.code);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(code);
	}

	@Override
	public String toString() {
		return "SparePart{" + "code='" + code + '\'' + ", description='" + description + '\'' + ", unitPrice=" + unitPrice + '}';
	}
}

