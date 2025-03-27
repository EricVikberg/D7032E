package game;

import card.ICard;
import card.ICardCounter;
import card.SaladCardCounter;
import card.Vegetable;

import java.util.ArrayList;

/**
 * Provides utility methods for displaying game information.
 * Implements IGameUtils interface.
 */
public class SaladUtils implements IGameUtils {
    private IGame game;
    private ICardCounter cardCounter;

    /**
     * Constructs a new SaladUtils instance with the specified game.
     *
     * @param game The current game instance
     */
    public SaladUtils(IGame game) {
        this.game = game;
        this.cardCounter = new SaladCardCounter();
    }

    /**
     * Formats and displays a player's hand showing:
     * - All criteria cards with their rules
     * - Count of each vegetable type
     *
     * @param hand The player's hand to display
     * @return Formatted string representation of the hand
     */
    public String displayHand(ArrayList<ICard> hand) {
        String handString = "Criteria:\t";
        // Display criteria cards
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getCriteriaSideUp() && hand.get(i).getVegetable() != null) {
                handString += "["+i+"] "+hand.get(i).getCriteria() + " ("+hand.get(i).getVegetable().toString()+")"+"\t";
            }
        }

        handString += "\nVegetables:\t";
        // Display vegetable counts
        for (Vegetable vegetable : Vegetable.values()) {
            int count = cardCounter.countVegetables(hand, vegetable);
            if(count > 0) {
                handString += vegetable + ": " + count + "\t";
            }
        }
        return handString;
    }
}