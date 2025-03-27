package piles;

import card.ICard;
import card.SaladCard;
import card.Vegetable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Initializes and manages the game piles at the start of a PointSalad game.
 * Implements IPileInitializer interface.
 */
public class SaladPileInitializer implements IPileInitializer {
    private ArrayList<IPile> piles = new ArrayList<>();

    /**
     * Constructs a new SaladPileInitializer for the specified number of players.
     *
     * @param numberOfPlayers The number of players in the game
     * @throws IOException if the card manifest can't be loaded
     */
    public SaladPileInitializer(int numberOfPlayers) throws IOException {
        JSONArray cardsArray = loadCards();
        ArrayList<ArrayList<ICard>> decks = createDecks(cardsArray);
        ArrayList<ICard> combinedDeck = combineDecks(decks, numberOfPlayers);
        divideIntoPiles(combinedDeck);
    }

    /**
     * Loads card data from the PointSaladManifest.json file.
     *
     * @return JSONArray containing all card data
     * @throws IOException if the file can't be read
     */
    private JSONArray loadCards() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("PointSaladManifest.json")) {
            if (inputStream == null) {
                throw new IOException("PointSaladManifest.json not found in the classpath.");
            }

            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            String jsonString = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getJSONArray("cards");
        } catch (IOException e) {
            throw new IOException("Couldn't load PointSaladManifest.json", e);
        }
    }

    /**
     * Creates separate decks for each vegetable type from the JSON data.
     *
     * @param cardsArray JSONArray containing card data
     * @return ArrayList of vegetable-specific decks
     */
    private ArrayList<ArrayList<ICard>> createDecks(JSONArray cardsArray) {
        ArrayList<ArrayList<ICard>> decks = new ArrayList<>();
        // Initialize decks for each vegetable type
        ArrayList<ICard> deckPepper = new ArrayList<>();
        ArrayList<ICard> deckLettuce = new ArrayList<>();
        ArrayList<ICard> deckCarrot = new ArrayList<>();
        ArrayList<ICard> deckCabbage = new ArrayList<>();
        ArrayList<ICard> deckOnion = new ArrayList<>();
        ArrayList<ICard> deckTomato = new ArrayList<>();

        decks.add(deckPepper);
        decks.add(deckLettuce);
        decks.add(deckCarrot);
        decks.add(deckCabbage);
        decks.add(deckOnion);
        decks.add(deckTomato);

        // Populate each deck
        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject cardJson = cardsArray.getJSONObject(i);
            JSONObject criteriaObj = cardJson.getJSONObject("criteria");

            deckPepper.add(new SaladCard(Vegetable.PEPPER, criteriaObj.getString("PEPPER")));
            deckLettuce.add(new SaladCard(Vegetable.LETTUCE, criteriaObj.getString("LETTUCE")));
            deckCarrot.add(new SaladCard(Vegetable.CARROT, criteriaObj.getString("CARROT")));
            deckCabbage.add(new SaladCard(Vegetable.CABBAGE, criteriaObj.getString("CABBAGE")));
            deckOnion.add(new SaladCard(Vegetable.ONION, criteriaObj.getString("ONION")));
            deckTomato.add(new SaladCard(Vegetable.TOMATO, criteriaObj.getString("TOMATO")));
        }

        // Shuffle each deck
        for(ArrayList<ICard> deck : decks) {
            shuffleDeck(deck);
        }

        return decks;
    }

    /**
     * Combines vegetable decks into a single deck based on player count.
     *
     * @param decks The vegetable-specific decks
     * @param numberOfPlayers The number of players in the game
     * @return Combined and shuffled deck
     */
    private ArrayList<ICard> combineDecks(ArrayList<ArrayList<ICard>> decks, int numberOfPlayers) {
        int cardsPerVeggie;
        switch (numberOfPlayers) {
            case 2: cardsPerVeggie = 6; break;
            case 3: cardsPerVeggie = 9; break;
            case 4: cardsPerVeggie = 12; break;
            case 5: cardsPerVeggie = 15; break;
            case 6: cardsPerVeggie = 18; break;
            default: throw new IllegalArgumentException("Invalid number of players");
        }

        ArrayList<ICard> combinedDeck = new ArrayList<>();
        System.out.println("Number of players: " + numberOfPlayers + " Number of cards " + cardsPerVeggie);
        for (int i = 0; i < cardsPerVeggie; i++) {
            for (ArrayList<ICard> veggieDeck : decks) {
                if (!veggieDeck.isEmpty()) {
                    combinedDeck.add(veggieDeck.remove(0));
                }
            }
        }
        shuffleDeck(combinedDeck);
        return combinedDeck;
    }

    /**
     * Divides the combined deck into three roughly equal piles.
     *
     * @param deck The combined deck to divide
     */
    private void divideIntoPiles(ArrayList<ICard> deck) {
        int numberOfPiles = 3;
        ArrayList<ArrayList<ICard>> listOfPiles = new ArrayList<>();

        for (int i = 0; i < numberOfPiles; i++) {
            listOfPiles.add(new ArrayList<>());
        }

        for (int i = 0; i < deck.size(); i++) {
            int pileIndex = i % numberOfPiles;
            listOfPiles.get(pileIndex).add(deck.get(i));
        }

        for (ArrayList<ICard> pile : listOfPiles) {
            ArrayList<ICard> pileCopy = new ArrayList<>(pile);
            piles.add(new SaladPile(pileCopy));
        }
    }

    /**
     * Shuffles a deck of cards.
     *
     * @param deck The deck to shuffle
     */
    private void shuffleDeck(ArrayList<ICard> deck) {
        Collections.shuffle(deck);
    }

    /**
     * Gets all initialized piles.
     *
     * @return ArrayList of IPile objects
     */
    public ArrayList<IPile> getPiles() {
        return piles;
    }
}