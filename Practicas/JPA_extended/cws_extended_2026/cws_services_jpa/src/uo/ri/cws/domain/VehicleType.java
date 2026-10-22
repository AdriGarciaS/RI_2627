package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

import java.util.Objects;

public class VehicleType {
    private String name;
    private Money pricePerHour;

    @Override
    public String toString() {
        return "VehicleType{" + "name='" + name + '\'' + ", pricePerHour=" + pricePerHour + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleType that = (VehicleType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public String getName() {
        return name;
    }

    public Money getPricePerHour() {
        return pricePerHour;
    }

    public VehicleType(String name, Money pricePerHour) {
        ArgumentChecks.isNotBlank(name, "name can not be blank");
        ArgumentChecks.isNotNull(pricePerHour, "pricePerHour can not be null");
        ArgumentChecks.isTrue(pricePerHour.isNonNegative(), "pricePerHour must be non-negative");

        this.name = name;
        this.pricePerHour = pricePerHour.roundToCents();
    }
}
