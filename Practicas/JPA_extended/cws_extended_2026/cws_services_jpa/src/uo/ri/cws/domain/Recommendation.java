package uo.ri.cws.domain;

import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Recommendation {

	// Natural attributes
	private boolean usedForVoucher = false;

	// Accidental attributes
	private Client recommender;
	private Client recommended;

	// ---- Constructors ----

	public Recommendation(Client recommender, Client recommended) {
		ArgumentChecks.isNotNull(recommender, "recommender can not be null");
		ArgumentChecks.isNotNull(recommended, "recommended can not be null");
		ArgumentChecks.isFalse(recommender.equals(recommended), "A client can not recommend itself");
		StateChecks.isTrue(recommended.getRecommendation() == null, "Client already recommended");

		Associations.Recommends.link(recommender, this, recommended);
	}

	// ---- Getters ----

	public Client getRecommender() {
		return recommender;
	}

	public Client getRecommended() {
		return recommended;
	}

	public boolean isUsedForVoucher() {
		return usedForVoucher;
	}

	// ---- Association helpers ----

	void _setRecommender(Client recommender) {
		this.recommender = recommender;
	}

	void _setRecommended(Client recommended) {
		this.recommended = recommended;
	}

	// ---- Business methods ----

	public void markAsUsedForVoucher() {
		usedForVoucher = true;
	}

	@Override
	public String toString() {
		return "Recommendation{" + "usedForVoucher=" + usedForVoucher + '}';
	}
}
