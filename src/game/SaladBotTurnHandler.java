package game;

import card.ICard;
import piles.IPile;
import player.IPlayer;

import java.util.ArrayList;

/**
 * Handles turn logic for bot players in the PointSalad game.
 * Implements ITurnHandler to provide bot-specific turn behavior.
 */
public class SaladBotTurnHandler implements ITurnHandler {
    private IGame game;
    private IGameUtils handDisplayer;

    /**
     * Constructs a new SaladBotTurnHandler with the specified game instance.
     *
     * @param game The current game instance
     */
    public SaladBotTurnHandler(IGame game) {
        this.game = game;
        this.handDisplayer = new SaladUtils(this.game);
    }

    /**
     * Executes a bot player's turn by either taking point cards or vegetable cards.
     * The bot makes random but strategic decisions to maximize its score.
     *
     * @param thisPlayer The bot player whose turn is being handled
     */
    public void handleTurn(IPlayer thisPlayer) {
        boolean emptyPiles = false;
        int choice = (int) (Math.random() * 2);
        boolean tookCard = false;

        if(choice == 0) {
            if (takePointCard(thisPlayer)) {
                tookCard = true;
            }

            if(!tookCard) {
                takeVeggieCard(thisPlayer, game.getGamePiles());
            }
        } else if (choice == 1) {
            if (takeVeggieCard(thisPlayer, game.getGamePiles())) {
                tookCard = true;
            }

            if (!tookCard) {
                takePointCard(thisPlayer);
            }
        }

        game.sendToAllPlayers("Bot " + thisPlayer.getPlayerID() +
                "'s hand is now: \n"+handDisplayer.displayHand(thisPlayer.getHand())+"\n");
    }

    /**
     * Attempts to take a point card from the piles that provides the highest score.
     *
     * @param thisPlayer The bot player taking the card
     * @return true if a point card was successfully taken, false otherwise
     */
    private boolean takePointCard(IPlayer thisPlayer) {
        int highestPointCardIndex = -1;
        int highestPointCardScore = 0;

        for (int i = 0; i < game.getGamePiles().size(); i++) {
            if (game.getGamePiles().get(i).getPointCard(game.getGamePiles()) != null) {
                ArrayList<ICard> tempHand = new ArrayList<>(thisPlayer.getHand());
                tempHand.add(game.getGamePiles().get(i).getPointCard(game.getGamePiles()));
                int score = game.getScoreCalculator().calculateScore(tempHand, thisPlayer, game.getPlayers());

                if (score > highestPointCardScore) {
                    highestPointCardScore = score;
                    highestPointCardIndex = i;
                }
            }
        }

        if (highestPointCardIndex != -1 &&
                game.getGamePiles().get(highestPointCardIndex).getPointCard(game.getGamePiles()) != null) {
            thisPlayer.getHand().add(game.getGamePiles().get(highestPointCardIndex).buyPointCard(game.getGamePiles()));
            return true;
        }
        return false;
    }

    /**
     * Attempts to take up to two vegetable cards from available piles.
     *
     * @param thisPlayer The bot player taking the cards
     * @param piles The available game piles
     * @return true if at least one vegetable card was taken, false otherwise
     */
    private boolean takeVeggieCard(IPlayer thisPlayer, ArrayList<IPile> piles) {
        int cardsPicked = 0;
        boolean cardTaken = false;

        for(IPile pile : piles) {
            if(pile.getVeggieCard(0) != null && cardsPicked < 2) {
                thisPlayer.getHand().add(pile.buyVeggieCard(0, piles));
                cardsPicked++;
                cardTaken = true;
            }
            if(pile.getVeggieCard(1) != null && cardsPicked < 2) {
                thisPlayer.getHand().add(pile.buyVeggieCard(1, piles));
                cardsPicked++;
                cardTaken = true;
            }
        }
        return cardTaken;
    }
}
