package market;

import piles.IPile;
import java.util.ArrayList;

/**
 * Represents the game market containing available cards.
 */
public interface IMarket {
    /**
     * Gets all piles that make up the market.
     *
     * @return List of market piles
     */
    ArrayList<IPile> getMarketPiles();
}