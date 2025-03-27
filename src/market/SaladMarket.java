package market;

import piles.IPile;
import java.util.ArrayList;

/**
 * Represents the game market containing available card piles.
 * Implements IMarket interface.
 */
public class SaladMarket implements IMarket {
    private ArrayList<IPile> piles;

    /**
     * Constructs a new SaladMarket with the specified piles.
     *
     * @param piles The initial set of card piles for the market
     */
    public SaladMarket(ArrayList<IPile> piles) {
        this.piles = piles;
    }

    /**
     * Gets all market piles.
     *
     * @return ArrayList of IPile objects representing the market piles
     */
    public ArrayList<IPile> getMarketPiles() {
        return this.piles;
    }
}