package market;

import piles.IPile;
import java.util.ArrayList;

/**
 * Provides a view of the market for display purposes.
 */
public interface IMarketView {
    /**
     * Generates a formatted string representation of the market.
     * Shows:
     * - Available point cards
     * - Available vegetable cards with their indices
     *
     * @return Formatted string showing current market state
     */
    String printMarket();
}