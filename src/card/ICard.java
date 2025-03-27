package card;

/**
 * Represents a game card with two displayable sides (criteria and vegetable).
 * Defines the core behavior that all game cards must implement.
 */
public interface ICard {
    /**
     * Sets which side of the card is currently face up.
     *
     * @param criteriaUp true to show the criteria side (scoring rules),
     *                   false to show the vegetable side
     */
    void setCriteriaSideUp(boolean criteriaUp);

    /**
     * Gets the current face-up side of the card.
     *
     * @return true if criteria side is up, false if vegetable side is up
     */
    boolean getCriteriaSideUp();

    /**
     * Gets the vegetable type displayed on the card.
     *
     * @return The Vegetable enum value, or null if not a vegetable card
     */
    Vegetable getVegetable();

    /**
     * Gets the scoring criteria text displayed on the card.
     *
     * @return The criteria text string, or null if not a criteria card
     */
    String getCriteria();

    /**
     * Returns a string representation of the card's current state.
     *
     * @return String showing either the criteria text (with vegetable in parentheses)
     *         or just the vegetable name, depending on current side up
     */
    String toString();
}