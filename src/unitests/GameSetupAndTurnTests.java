package unitests;

import card.ICard;
import card.SaladCard;
import card.Vegetable;
import game.SaladGame;
import game.SaladUtils;
import org.junit.jupiter.api.Test;
import piles.IPile;
import piles.SaladPileInitializer;
import player.HumanPlayer;
import player.IPlayer;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for game setup and turn mechanics.
 * Covers requirements 5-10 from the game rules.
 */
public class GameSetupAndTurnTests {

    /**
     * Tests Rule 5: Vegetable market creation with two cards per pile.
     */
    @Test
    void testVegetableMarketCreation_HasTwoVeggieCardsPerPile() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(2);
        ArrayList<IPile> piles = pileInitializer.getPiles();

        assertEquals(3, piles.size());

        for (IPile pile : piles) {
            ICard veggieCard0 = pile.getVeggieCard(0);
            ICard veggieCard1 = pile.getVeggieCard(1);

            assertNotNull(veggieCard0, "First veggie card should not be null");
            assertNotNull(veggieCard1, "Second veggie card should not be null");

            assertFalse(veggieCard0.getCriteriaSideUp(),
                    "Veggie cards should have criteria side down");
            assertFalse(veggieCard1.getCriteriaSideUp(),
                    "Veggie cards should have criteria side down");

            assertNotNull(veggieCard0.getVegetable(), "Veggie cards should have vegetable type");
            assertNotNull(veggieCard1.getVegetable(), "Veggie cards should have vegetable type");
        }
    }

    /**
     * Tests Rule 7: Player drafting point cards.
     */
    @Test
    void testPlayerDraftsPointCard() throws IOException {
        SaladGame game = new SaladGame(new String[]{"1", "1"});
        IPlayer player = game.getPlayers().get(0);
        IPile pile = game.getGamePiles().get(0);
        int initialHandSize = player.getHand().size();
        int initialPileSize = pile.getCards().size();

        ICard drawnCard = pile.buyPointCard(game.getGamePiles());
        player.getHand().add(drawnCard);

        assertEquals(initialHandSize + 1, player.getHand().size(),
                "Hand size should increase by 1");
        assertEquals(initialPileSize - 1, pile.getCards().size(),
                "Pile size should decrease by 1");
        assertSame(drawnCard, player.getHand().get(player.getHand().size() - 1),
                "Drawn card should be in player's hand");
        assertTrue(drawnCard.getCriteriaSideUp(),
                "Drawn card should have criteria side up");
    }

    /**
     * Tests Rule 7: Player drafting vegetable cards.
     */
    @Test
    void testPlayerDraftsVeggieCards() throws IOException {
        SaladGame game = new SaladGame(new String[]{"1", "1"});
        IPlayer player = game.getPlayers().get(0);
        IPile pile = game.getGamePiles().get(0);
        int initialHandSize = player.getHand().size();

        assertNotNull(pile.getVeggieCard(0),"First veggie card should exist");
        assertNotNull(pile.getVeggieCard(1),"Second veggie card should exist");

        ICard veggieCard1 = pile.buyVeggieCard(0, game.getGamePiles());
        ICard veggieCard2 = pile.buyVeggieCard(1, game.getGamePiles());
        player.getHand().add(veggieCard1);
        player.getHand().add(veggieCard2);

        assertEquals(initialHandSize + 2, player.getHand().size(), "Hand size should increase by 2");
        assertFalse(veggieCard1.getCriteriaSideUp(), "Veggie cards should have criteria side down");
        assertFalse(veggieCard2.getCriteriaSideUp(), "Veggie cards should have criteria side down");
    }

    /**
     * Tests Rule 8: Converting criteria cards to vegetables.
     */
    @Test
    void testPlayerMayOptToConvertCriteriaCard() throws IOException {
        IPlayer player = new HumanPlayer(1, false, false, null, null, null);
        SaladCard card = new SaladCard(Vegetable.PEPPER, "TOTAL VEGETABLES=5");
        player.getHand().add(card);

        assertTrue(card.getCriteriaSideUp(), "Card should start with criteria side up");

        card.setCriteriaSideUp(false);
        assertFalse(card.getCriteriaSideUp(), "Player should be able to flip to vegetable side");

        SaladUtils utils = new SaladUtils(new SaladGame(new String[]{"1", "1"}));
        String handDisplay = utils.displayHand(player.getHand());
        assertTrue(handDisplay.contains("PEPPER"), "Vegetable should remain visible");
        assertFalse(handDisplay.contains("TOTAL VEGETABLES=5"), "Criteria should remain hidden");
    }

    /**
     * Tests Rule 9: Showing hand to other players.
     */
    @Test
    public void testDisplayHand_EmptyHand() throws IOException {
        IPlayer player = new HumanPlayer(1, false, false, null, null, null);
        SaladUtils utils = new SaladUtils(new SaladGame(new String[]{"1", "1"}));

        String handDisplay = utils.displayHand(player.getHand());

        assertNotNull("Hand display should not be null", handDisplay);
        String normalizedDisplay = handDisplay.replace("\r\n", "\n").trim();
        assertEquals("Criteria:\t\nVegetables:", normalizedDisplay);
    }

    @Test
    public void testDisplayHand_ShowsCriteriaWhenSideUp() throws IOException {
        SaladCard card = new SaladCard(Vegetable.CARROT, "MOST CABBAGE = 10");
        IPlayer player = new HumanPlayer(1, false, false, null, null, null);
        SaladUtils utils = new SaladUtils(new SaladGame(new String[]{"1", "1"}));
        card.setCriteriaSideUp(true);
        player.getHand().add(card);

        String handDisplay = utils.displayHand(player.getHand());
        assertTrue(handDisplay.contains("MOST CABBAGE = 10"));
        assertTrue(handDisplay.contains("(CARROT)"));
    }

    @Test
    public void testDisplayHand_ShowsVegetables() throws IOException {
        IPlayer player = new HumanPlayer(1, false, false, null, null, null);
        SaladCard carrotCard = new SaladCard(Vegetable.CARROT, "MOST CABBAGE = 10");
        carrotCard.setCriteriaSideUp(false);
        player.getHand().add(carrotCard);

        SaladUtils utils = new SaladUtils(new SaladGame(new String[]{"1", "1"}));
        String handDisplay = utils.displayHand(player.getHand());

        assertFalse(handDisplay.contains("MOST CABBAGE = 10"));
        assertTrue(handDisplay.contains("Vegetables:\tCARROT: 1"));
    }

    /**
     * Tests Rule 10: Market card replacement.
     */
    @Test
    void testMarketReplacement() throws IOException {
        SaladGame game = new SaladGame(new String[]{"1", "1"});
        IPile pile = game.getGamePiles().get(0);

        ICard initialVeggieCard = pile.getVeggieCard(0);
        assertNotNull(initialVeggieCard);

        ICard boughtCard = pile.buyVeggieCard(0, game.getGamePiles());
        assertEquals(initialVeggieCard, boughtCard);

        ICard newVeggieCard = pile.getVeggieCard(0);
        assertNotNull(newVeggieCard);
        assertNotEquals(initialVeggieCard, newVeggieCard);
    }
}