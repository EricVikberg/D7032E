package game;

import card.ICard;
import piles.IPile;
import player.IPlayer;

/**
 * Handles turn logic for human players in PointSalad.
 * Implements ITurnHandler to provide human-specific turn behavior.
 */
public class SaladHumanTurnHandler implements ITurnHandler {
    private IGame game;
    private IGameUtils handDisplayer;

    /**
     * Constructs a new SaladHumanTurnHandler with the specified game instance.
     *
     * @param game The current game instance
     */
    public SaladHumanTurnHandler(IGame game) {
        this.game = game;
        this.handDisplayer = new SaladUtils(this.game);
    }

    /**
     * Executes a human player's turn by:
     * 1. Displaying current hand and market
     * 2. Processing card selection
     * 3. Handling criteria card conversion if applicable
     *
     * @param thisPlayer The human player whose turn is being handled
     */
    public void handleTurn(IPlayer thisPlayer) {
        thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
        thisPlayer.sendMessage(handDisplayer.displayHand(thisPlayer.getHand()));
        thisPlayer.sendMessage("\nThe piles are: ");
        thisPlayer.sendMessage(game.getMarketView().printMarket());

        boolean validChoice = false;
        while(!validChoice) {
            thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
            String pileChoice = thisPlayer.readMessage();

            if(pileChoice.matches("\\d")) {
                validChoice = takePointCard(thisPlayer, pileChoice);
            } else {
                validChoice = takeVeggieCard(thisPlayer, pileChoice);
            }
        }

        // Check for criteria card conversion
        if (hasCriteriaCard(thisPlayer)) {
            convertCriteriaCard(thisPlayer);
        }

        thisPlayer.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
        game.sendToAllPlayers("Player " + thisPlayer.getPlayerID()+ "'s hand is now: \n"+handDisplayer.displayHand(thisPlayer.getHand())+"\n");
    }

    /**
     * Attempts to take a point card from the specified pile.
     *
     * @param thisPlayer The player taking the card
     * @param pileChoice String input representing pile selection
     * @return true if card was successfully taken, false otherwise
     */
    public boolean takePointCard(IPlayer thisPlayer, String pileChoice) {
        int pileIndex;

        try {
            pileIndex = Integer.parseInt(pileChoice);
        } catch (NumberFormatException e) {
            thisPlayer.sendMessage("\nInvalid input. Please enter a valid pile number.\n");
            return false;
        }

        int numberOfPiles = game.getGamePiles().size();
        if (pileIndex < 0 || pileIndex >= numberOfPiles) {
            thisPlayer.sendMessage("\nInvalid pile index. Please choose a valid pile between 0 and " + (numberOfPiles - 1) + ".\n");
            return false;
        }

        IPile chosenPile = game.getGamePiles().get(pileIndex);
        ICard pointCard = chosenPile.getPointCard(game.getGamePiles());

        if (pointCard == null) {
            thisPlayer.sendMessage("\nThis pile is empty. Please choose another pile.\n");
            return false;
        }

        thisPlayer.getHand().add(chosenPile.buyPointCard(game.getGamePiles()));
        thisPlayer.sendMessage("\nYou took a card from pile " + pileIndex + " and added it to your hand.\n");
        return true;
    }

    /**
     * Attempts to take vegetable cards based on player input.
     *
     * @param thisPlayer The player taking the cards
     * @param pileChoice String input representing card selections (A-F)
     * @return true if at least one card was taken, false otherwise
     */
    public boolean takeVeggieCard(IPlayer thisPlayer, String pileChoice) {
        int takenVeggies = 0;
        for (int charIndex = 0; charIndex < pileChoice.length(); charIndex++) {
            char chosenChar = Character.toUpperCase(pileChoice.charAt(charIndex));

            if (chosenChar < 'A' || chosenChar > 'F') {
                thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
                return false;
            }

            int choice = chosenChar - 'A';
            int pileIndex = choice % 3; // 0, 1, or 2
            int veggieIndex = choice < 3 ? 0 : 1;

            IPile chosenPile = game.getGamePiles().get(pileIndex);
            ICard veggieCard = chosenPile.getVeggieCard(veggieIndex);

            if (veggieCard == null) {
                thisPlayer.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
                return false;
            }

            if (takenVeggies == 2) {
                return true; // Max of two veggies taken
            } else {
                thisPlayer.getHand().add(chosenPile.buyVeggieCard(veggieIndex, game.getGamePiles()));
                takenVeggies++;
            }
        }
        return takenVeggies > 0;
    }

    /**
     * Checks if player has any criteria cards in their hand.
     *
     * @param player The player to check
     * @return true if any criteria cards found, false otherwise
     */
    private boolean hasCriteriaCard(IPlayer player) {
        for (ICard card : player.getHand()) {
            if (card.getCriteriaSideUp()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles conversion of criteria cards to vegetable cards.
     *
     * @param player The player converting cards
     */
    private void convertCriteriaCard(IPlayer player) {
        player.sendMessage("\n" + handDisplayer.displayHand(player.getHand()) +
                "\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");

        String choice = player.readMessage();

        if (choice.matches("\\d+")) {
            int cardIndex = Integer.parseInt(choice);
            if (cardIndex >= 0 && cardIndex < player.getHand().size()) {
                player.getHand().get(cardIndex).setCriteriaSideUp(false);
                player.sendMessage("\nYou turned a criteria card into a veggie card.\n");
            } else {
                player.sendMessage("\nInvalid choice. No card was converted.\n");
            }
        }
    }
}