package uo.ri.cws.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class Vehicle {

	// Natural attributes
	private LicensePlate licensePlate;
	private String make;
	private String model;
	
	// Accidental attributes
	private VehicleType vehicleType;
	private Client client;
	private Set<WorkOrder> workOrders = new HashSet<>();

	// ---- Constructors ----

	public Vehicle(LicensePlate licensePlate, String make, String model) {
		ArgumentChecks.isNotNull(licensePlate, "licensePlate can not be null");
		ArgumentChecks.isNotBlank(make, "make can not be blank");
		ArgumentChecks.isNotBlank(model, "model can not be blank");

		this.licensePlate = licensePlate;
		this.make = make;
		this.model = model;

	}

	public Vehicle(LicensePlate plateNumber) {

	}


	public Money getPricePerHour() {
		return vehicleType.getPricePerHour(); // VehicleType must return Money
	}

	public LicensePlate getLicensePlate() {
		return licensePlate;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}


	public Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	public Client getClient() {
		return client;
	}
	void _setClient(Client client) {this.client = client;}

	void _setVehicleType(VehicleType vehicleType) {this.vehicleType = vehicleType;}

	// TODO HASHCODE Y EQUAL SOBRE IDENTIDAD DE LA ENTITY
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(licensePlate, vehicle.licensePlate);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(licensePlate);
	}

	// TODO NO PONER EN EL TOSTRING , ATTR NO NATURALES PQ HACE LLAMDAS RECURSIVAS
	@Override
	public String toString() {
		return "Vehicle{" + "model='" + model + '\'' + ", make='" + make + '\'' + ", licensePlate=" + licensePlate + '}';
	}


}
