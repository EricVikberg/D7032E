package piles;

import java.util.ArrayList;

/**
 * Defines the interface for initializing and managing game piles at the start of a PointSalad game.
 */
public interface IPileInitializer {

    /**
     * Gets all initialized piles.
     *
     * @return ArrayList of IPile objects representing the game piles
     */
    ArrayList<IPile> getPiles();
}