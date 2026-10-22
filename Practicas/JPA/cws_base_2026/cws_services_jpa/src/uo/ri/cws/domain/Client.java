package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;

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