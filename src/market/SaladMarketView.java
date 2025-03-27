package market;

import card.ICard;
import piles.IPile;
import java.util.ArrayList;

/**
 * Provides a view of the game market for display purposes.
 * Implements IMarketView interface.
 */
public class SaladMarketView implements IMarketView {
    private IMarket market;

    /**
     * Constructs a new SaladMarketView with the specified market.
     *
     * @param market The market to display
     */
    public SaladMarketView(IMarket market) {
        this.market = market;
    }

    /**
     * Generates a formatted string representation of the market.
     * Shows both point cards and vegetable cards with their indices.
     *
     * @return Formatted string displaying the current market state
     */
    public String printMarket() {
        String pileString = "Point Cards:\t";
        ArrayList<IPile> piles = market.getMarketPiles();

        // Display point cards
        for (int p = 0; p < piles.size(); p++) {
            ICard pointCard = piles.get(p).getPointCard(piles);
            if (pointCard == null) {
                pileString += "[" + p + "]" + String.format("%-43s", "Empty") + "\t";
            } else {
                pileString += "[" + p + "]" + String.format("%-43s", pointCard) + "\t";
            }
        }

        // Display vegetable cards
        pileString += "\nVeggie Cards:\t";
        char veggieCardIndex = 'A';

        // First row of veggies
        for (IPile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getVeggieCard(0)) + "\t";
            veggieCardIndex++;
        }

        // Second row of veggies
        pileString += "\n\t\t";
        for (IPile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getVeggieCard(1)) + "\t";
            veggieCardIndex++;
        }

        return pileString;
    }
}