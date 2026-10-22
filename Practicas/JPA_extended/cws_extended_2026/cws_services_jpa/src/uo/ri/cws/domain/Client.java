package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Client {

	// Natural attributes
	private Nif nif;
	private FullName fullName;
	private Email email;
	private PhoneNumber phone;
	private Address address;

	// ACCIDENTAL ATTR
	private Set<Vehicle> vehicles = new HashSet<>();
	private Set<PaymentMean> paymentMeans = new HashSet<>();
	private Set<Recommendation> recommendations = new HashSet<>();
	private Recommendation recommendation;

	// ---- Constructors ----
	public Client(Nif nif) {
		this(nif, 
			new FullName("No name", "No surname"), 
			new Email("default@example.com"),
			new PhoneNumber("+34600000000"), 
			new Address("No street", "No city", "No zip", "No country")
		);
	}

	public Client(Nif nif, FullName fullName) {
	    this(nif,
	        fullName,
	        new Email("default@example.com"),
	        new PhoneNumber("+34600000000"),
			new Address("No street", "No city", "No zip", "No country")
	    );
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Client client = (Client) o;
		return Objects.equals(nif, client.nif);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nif);
	}

	public Address getAddress() {
		return address;
	}

	public Email getEmail() {
		return email;
	}

	public FullName getFullName() {
		return fullName;
	}

	public Nif getNif() {
		return nif;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	public Set<PaymentMean> getPaymentMeans() {return new HashSet<>(paymentMeans);}

	Set<PaymentMean> _getPaymentMeans() {return  paymentMeans;}

	public Set<Recommendation> getRecommendations() {
		return new HashSet<>(recommendations);
	}

	Set<Recommendation> _getRecommendations() {
		return recommendations;
	}

	public Recommendation getRecommendation() {
		return recommendation;
	}

	void _setRecommendation(Recommendation recommendation) {
		this.recommendation = recommendation;
	}

	// ---- Voucher methods ----

	public List<WorkOrder> getWorkOrdersAvailableForVoucher20() {
		return vehicles.stream()
				.flatMap(v -> v.getWorkOrders().stream())
				.filter(w -> w.getInvoice() != null)
				.filter(w -> w.getInvoice().isSettled())
				.filter(w -> !w.isUsedForVoucher())
				.toList();
	}

	public void markThreeWorkOrdersAsUsedForVoucher20() {
		List<WorkOrder> available = getWorkOrdersAvailableForVoucher20();
		StateChecks.isTrue(available.size() >= 3, "Not enough work orders available");

		available.subList(0, 3).forEach(WorkOrder::markAsUsedForVoucher);
	}

	public List<Recommendation> getRecommendationsAvailableForVoucher25() {
		return recommendations.stream()
				.filter(r -> !r.isUsedForVoucher())
				.filter(r -> r.getRecommended().hasPaidInvoice())
				.toList();
	}

	public boolean isEligibleForVoucher25() {
		return hasPaidInvoice() && getRecommendationsAvailableForVoucher25().size() >= 3;
	}

	public void markThreeRecommendationsAsUsedForVoucher25() {
		StateChecks.isTrue(isEligibleForVoucher25(), "Client is not eligible for a voucher");

		getRecommendationsAvailableForVoucher25().subList(0, 3)
				.forEach(Recommendation::markAsUsedForVoucher);
	}

	private boolean hasPaidInvoice() {
		return vehicles.stream()
				.flatMap(v -> v.getWorkOrders().stream())
				.anyMatch(w -> w.getInvoice() != null && w.getInvoice().isSettled());
	}

	public Client(Nif nif, FullName fullName, Email email, PhoneNumber phone, Address address) {
		ArgumentChecks.isNotNull(nif, "nif can not be null");
		ArgumentChecks.isNotNull(fullName, "fullName can not be null");
		ArgumentChecks.isNotNull(email, "email can not be null");
		ArgumentChecks.isNotNull(phone, "phone can not be null");
		ArgumentChecks.isNotNull(address, "address can not be null");

		this.nif = nif;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

}