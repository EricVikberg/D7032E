package unitests;

import card.ICard;
import card.SaladCard;
import card.Vegetable;
import game.SaladGame;
import game.SaladGameLoop;
import game.SaladResultHandler;
import org.junit.jupiter.api.Test;
import piles.IPile;
import player.HumanPlayer;
import player.IPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for game end conditions and scoring functionality.
 * Covers requirements 11-14 from the game rules.
 */
public class GameEndAndScoringTests {

    /**
     * Tests Rule 11: Drawing from largest pile when one is exhausted.
     */
    @Test
    void testDrawFromLargestInGameContext() throws IOException {
        SaladGame game = new SaladGame(new String[]{"1", "1"});
        ArrayList<IPile> piles = game.getGamePiles();

        // Force one pile to be empty
        piles.get(0).getCards().clear();
        piles.get(0).buyVeggieCard(0, piles);
        piles.get(0).buyVeggieCard(1, piles);

        // Find largest remaining pile
        int largestIndex = 1;
        if (piles.get(2).getCards().size() > piles.get(1).getCards().size()) {
            largestIndex = 2;
        }
        int expectedSize = piles.get(largestIndex).getCards().size() - 1;

        // Test
        ICard card = piles.get(0).buyPointCard(piles);

        assertNotNull(card);
        assertEquals(expectedSize, piles.get(largestIndex).getCards().size());
    }

    /**
     * Tests Rule 12: Game ends when market is empty.
     */
    @Test
    public void testGameEndsWhenMarketIsEmpty() throws IOException {
        // Setup game with controlled state
        SaladGame game = new SaladGame(new String[]{"1", "1"});

        // Completely empty all piles
        for (IPile pile : game.getGamePiles()) {
            pile.getCards().clear();
            while (pile.getVeggieCard(0) != null) {
                pile.buyVeggieCard(0, game.getGamePiles());
            }
            while (pile.getVeggieCard(1) != null) {
                pile.buyVeggieCard(1, game.getGamePiles());
            }
        }

        assertTrue(isMarketCompletelyEmpty(game));

        // Create and run game loop
        SaladGameLoop gameLoop = new SaladGameLoop(game);
        gameLoop.runLoop();

        assertTrue(game.getGamePiles().stream().allMatch(IPile::isEmpty),
                "All piles should be empty at game end");
    }

    private boolean isMarketCompletelyEmpty(SaladGame game) {
        return game.getGamePiles().stream().allMatch(pile ->
                pile.getCards().isEmpty() &&
                        pile.getVeggieCard(0) == null &&
                        pile.getVeggieCard(1) == null
        );
    }

    /**
     * Tests Rules 13-14: Score calculation and winner announcement.
     */
    @Test
    public void testScoreCalculationAndWinnerAnnouncement() throws IOException {
        // Setup game with 2 human players (local)
        SaladGame game = new SaladGame(new String[]{"1", "1"});

        // Get player references
        IPlayer player1 = game.getPlayers().get(0);
        IPlayer player2 = game.getPlayers().get(1);

        // Create test hands with known scoring
        SaladCard p1Card1 = new SaladCard(Vegetable.PEPPER, "MOST PEPPER = 10");
        p1Card1.setCriteriaSideUp(true);
        SaladCard p1Card2 = new SaladCard(Vegetable.PEPPER, null);
        p1Card2.setCriteriaSideUp(false);
        player1.getHand().addAll(Arrays.asList(p1Card1, p1Card2));

        SaladCard p2Card1 = new SaladCard(Vegetable.CARROT, "2 / CARROT");
        p2Card1.setCriteriaSideUp(true);
        SaladCard p2Card2 = new SaladCard(Vegetable.CARROT, null);
        p2Card2.setCriteriaSideUp(false);
        SaladCard p2Card3 = new SaladCard(Vegetable.CARROT, null);
        p2Card3.setCriteriaSideUp(false);
        player2.getHand().addAll(Arrays.asList(p2Card1, p2Card2, p2Card3));

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            SaladResultHandler resultHandler = new SaladResultHandler(game);
            resultHandler.handleResult();

            String output = outContent.toString();

            assertEquals(10, player1.getScore(), "Player 1 should have 10 points");
            assertEquals(4, player2.getScore(), "Player 2 should have 4 points");

            assertTrue(output.contains("Player " + player1.getPlayerID() + "'s score is: 10"),
                    "Should show player 1's score of 10");
            assertTrue(output.contains("Player " + player2.getPlayerID() + "'s score is: 4"),
                    "Should show player 2's score of 4");
            assertTrue(output.contains("Congratulations! You are the winner") ||
                            output.contains("winner.*Player " + player1.getPlayerID()),
                    "Should announce player 1 as winner");

        } finally {
            System.setOut(originalOut);
        }
    }
}