package unitests;

import game.SaladGame;
import network.SaladNetwork;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import piles.IPile;
import piles.SaladPile;
import piles.SaladPileInitializer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for player setup and deck initialization.
 * Covers requirements 1-3 from the game rules.
 */
public class PlayerAndDeckTests {
    private SaladNetwork game;

    @AfterEach
    public void tearDown() {
        if (game != null) {
            game.close();
        }
    }

    /**
     * Tests Rule 1: Invalid number of players throws exception.
     */
    @Test
    public void testInvalidNumberOfPlayers() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new SaladGame(new String[]{"1", "0"}));
        assertEquals("You've entered too few players and bots", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new SaladGame(new String[]{"7", "0"}));
        assertEquals("You've entered too many players and bots", exception.getMessage());
    }

    /**
     * Tests Rule 3: Deck size for 2 players.
     */
    @Test
    void testDeckSizeFor2Players() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(2);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        int totalCards = piles.stream()
                .mapToInt(pile -> {
                    SaladPile saladPile = (SaladPile) pile;
                    return pile.getCards().size()
                            + (saladPile.getVeggieCard(0) != null ? 1 : 0)
                            + (saladPile.getVeggieCard(1) != null ? 1 : 0);
                })
                .sum();
        assertEquals(36, totalCards);
    }

    /**
     * Tests Rule 3: Deck size for 3 players.
     */
    @Test
    void testDeckSizeFor3Players() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(3);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        int totalCards = piles.stream()
                .mapToInt(pile -> {
                    SaladPile saladPile = (SaladPile) pile;
                    return pile.getCards().size()
                            + (saladPile.getVeggieCard(0) != null ? 1 : 0)
                            + (saladPile.getVeggieCard(1) != null ? 1 : 0);
                })
                .sum();
        assertEquals(54, totalCards);
    }

    /**
     * Tests Rule 3: Deck size for 4 players.
     */
    @Test
    void testDeckSizeFor4Players() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(4);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        int totalCards = piles.stream()
                .mapToInt(pile -> {
                    SaladPile saladPile = (SaladPile) pile;
                    return pile.getCards().size()
                            + (saladPile.getVeggieCard(0) != null ? 1 : 0)
                            + (saladPile.getVeggieCard(1) != null ? 1 : 0);
                })
                .sum();
        assertEquals(72, totalCards);
    }

    /**
     * Tests Rule 3: Deck size for 5 players.
     */
    @Test
    void testDeckSizeFor5Players() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(5);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        int totalCards = piles.stream()
                .mapToInt(pile -> {
                    SaladPile saladPile = (SaladPile) pile;
                    return pile.getCards().size()
                            + (saladPile.getVeggieCard(0) != null ? 1 : 0)
                            + (saladPile.getVeggieCard(1) != null ? 1 : 0);
                })
                .sum();
        assertEquals(90, totalCards);
    }

    /**
     * Tests Rule 3: Deck size for 6 players.
     */
    @Test
    void testDeckSizeFor6Players() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(6);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        int totalCards = piles.stream()
                .mapToInt(pile -> {
                    SaladPile saladPile = (SaladPile) pile;
                    return pile.getCards().size()
                            + (saladPile.getVeggieCard(0) != null ? 1 : 0)
                            + (saladPile.getVeggieCard(1) != null ? 1 : 0);
                })
                .sum();
        assertEquals(108, totalCards);
    }

    /**
     * Tests Rule 3: Draw pile creation with roughly equal sizes.
     */
    @Test
    void testDrawPileCreation() throws Exception {
        SaladPileInitializer pileInitializer = new SaladPileInitializer(5);
        ArrayList<IPile> piles = pileInitializer.getPiles();
        assertEquals(3, piles.size());

        int pile1Size = piles.get(0).getCards().size();
        int pile2Size = piles.get(1).getCards().size();
        int pile3Size = piles.get(2).getCards().size();
        assertTrue(Math.abs(pile1Size - pile2Size) <= 1);
        assertTrue(Math.abs(pile2Size - pile3Size) <= 1);
    }
}