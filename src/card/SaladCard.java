package card;

/**
 * Represents a game card in PointSalad with two sides:
 * - Criteria side (scoring rules)
 * - Vegetable side (one of six vegetable types)
 * Implements the ICard interface.
 */
public class SaladCard implements ICard {
    private Vegetable vegetable;
    private String criteria;
    private boolean criteriaSideUp = true;

    /**
     * Creates a new SaladCard with the specified vegetable and criteria.
     *
     * @param vegetable The vegetable type on the card
     * @param criteria The scoring rule text for the criteria side
     */
    public SaladCard(Vegetable vegetable, String criteria) {
        this.vegetable = vegetable;
        this.criteria = criteria;
    }

    /**
     * Sets which side of the card is face up.
     *
     * @param criteriaUp true to show criteria side, false to show vegetable side
     */
    public void setCriteriaSideUp(boolean criteriaUp) {
        this.criteriaSideUp = criteriaUp;
    }

    /**
     * Checks which side of the card is currently face up.
     *
     * @return true if criteria side is up, false if vegetable side is up
     */
    public boolean getCriteriaSideUp() {
        return this.criteriaSideUp;
    }

    /**
     * Gets the scoring criteria text.
     *
     * @return The criteria text string
     */
    public String getCriteria() {
        return this.criteria;
    }

    /**
     * Gets the vegetable type of this card.
     *
     * @return The Vegetable enum value
     */
    public Vegetable getVegetable() {
        return this.vegetable;
    }

    /**
     * Returns a string representation of the card.
     * Shows criteria and vegetable if criteria side is up,
     * otherwise just shows the vegetable.
     *
     * @return Formatted string representation of the card
     */
    @Override
    public String toString() {
        if(criteriaSideUp) {
            return criteria + " (" + vegetable + ")";
        } else {
            return vegetable.toString();
        }
    }
}
