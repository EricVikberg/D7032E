package scoring;

import card.ICard;
import card.ICardCounter;
import card.Vegetable;
import player.IPlayer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implements the scoring calculation logic for PointSalad.
 * Handles all scoring criteria defined in the game rules.
 * Implements IScoreCalculator interface.
 */
public class SaladScoreCalc implements IScoreCalculator {
    private ICardCounter cardCounter;

    /**
     * Constructs a new SaladScoreCalc with the specified card counter.
     *
     * @param cardCounter The card counter utility to use
     */
    public SaladScoreCalc(ICardCounter cardCounter) {
        this.cardCounter = cardCounter;
    }

    /**
     * Calculates the total score for a player's hand based on all criteria cards.
     *
     * @param hand The player's hand of cards
     * @param thisPlayer The player being scored
     * @param players All players in the game (for relative scoring)
     * @return The calculated total score
     */
    public int calculateScore(ArrayList<ICard> hand, IPlayer thisPlayer, ArrayList<IPlayer> players) {
        int totalScore = 0;

        for(ICard criteriaCard : hand) {
            if(criteriaCard.getCriteriaSideUp()) {
                String criteria = criteriaCard.getCriteria();
                String[] parts = criteria.split(",");

                if (criteria.contains("TOTAL") || criteria.contains("TYPE") || criteria.contains("SET")) {
                    totalScore += evaluateTotalTypeSet(criteria, hand, thisPlayer, players);
                } else if (criteria.contains("MOST") || criteria.contains("FEWEST")) {
                    totalScore += handleMostFewest(criteria, hand, thisPlayer, players);
                } else if (parts.length > 1 || criteria.indexOf("+")>=0 || parts[0].indexOf("/")>=0) {
                    totalScore += evaluateComplexCriteria(criteria, hand, parts);
                }
            }
        }
        return totalScore;
    }

    /**
     * Handles scoring for criteria involving totals, types, or complete sets.
     *
     * @param criteria The scoring criteria text
     * @param hand The player's hand
     * @param thisPlayer The current player
     * @param players All players
     * @return The calculated score for these criteria
     */
    private int evaluateTotalTypeSet(String criteria, ArrayList<ICard> hand, IPlayer thisPlayer, ArrayList<IPlayer> players) {
        int score = 0;

        if (criteria.contains("TOTAL")) {
            score += handleTotalVegetables(criteria, hand, thisPlayer, players);
        }

        if (criteria.contains("TYPE")) {
            score += handleTypeCriteria(criteria, hand);
        }

        if (criteria.contains("SET")) {
            score += handleSetCriteria(hand);
        }
        return score;
    }

    /**
     * Handles scoring for complex criteria with multiple parts.
     *
     * @param criteria The scoring criteria text
     * @param hand The player's hand
     * @param parts The split criteria parts
     * @return The calculated score for these criteria
     */
    private int evaluateComplexCriteria(String criteria, ArrayList<ICard> hand, String[] parts) {
        int score = 0;

        if (criteria.contains("+")) {
            score += handlePlusCriteria(criteria, hand);
        }

        if (criteria.contains("EVEN") || criteria.contains("ODD")) {
            score += handleEvenOddCriteria(criteria, hand, parts);
        }

        if (criteria.contains("/")) {
            score += handleSlashCriteria(hand, parts);
        }
        return score;
    }

    /**
     * Scores criteria based on total vegetable counts.
     *
     * @param criteria The scoring criteria
     * @param hand The player's hand
     * @param thisPlayer The current player
     * @param players All players
     * @return The calculated score
     */
    private int handleTotalVegetables(String criteria, ArrayList<ICard> hand, IPlayer thisPlayer, ArrayList<IPlayer> players) {
        int countVeg = cardCounter.countTotalVegetables(hand);
        int thisHandCount = countVeg;
        for(IPlayer p : players) {
            if(p.getPlayerID() != thisPlayer.getPlayerID()) {
                int playerVeg = cardCounter.countTotalVegetables(p.getHand());
                if((criteria.indexOf("MOST")>=0) && (playerVeg > countVeg)) {
                    countVeg = cardCounter.countTotalVegetables(p.getHand());
                }
                if((criteria.indexOf("FEWEST")>=0) && (playerVeg < countVeg)) {
                    countVeg = cardCounter.countTotalVegetables(p.getHand());
                }
            }
        }
        if(countVeg == thisHandCount) {
            return Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
        }
        return 0;
    }

    /**
     * Scores criteria based on vegetable types.
     *
     * @param criteria The scoring criteria
     * @param hand The player's hand
     * @return The calculated score
     */
    private int handleTypeCriteria(String criteria, ArrayList<ICard> hand) {
        String[] expr = criteria.split("/");
        int addScore = Integer.parseInt(expr[0].trim());
        if(expr[1].indexOf("MISSING")>=0) {
            int missing = 0;
            for (Vegetable vegetable : Vegetable.values()) {
                if(cardCounter.countVegetables(hand, vegetable) == 0) {
                    missing++;
                }
            }
            return missing * addScore;
        }
        else {
            int atLeastPerVegType = Integer.parseInt(expr[1].substring(expr[1].indexOf(">=")+2).trim());
            int totalType = 0;
            for(Vegetable vegetable : Vegetable.values()) {
                int countVeg = cardCounter.countVegetables(hand, vegetable);
                if(countVeg >= atLeastPerVegType) {
                    totalType++;
                }
            }
            return totalType * addScore;
        }
    }

    /**
     * Scores complete set criteria (one of each vegetable).
     *
     * @param hand The player's hand
     * @return The calculated score
     */
    private int handleSetCriteria(ArrayList<ICard> hand) {
        int addScore = 12;
        for (Vegetable vegetable : Vegetable.values()) {
            int countVeg = cardCounter.countVegetables(hand, vegetable);
            if(countVeg == 0) {
                addScore = 0;
                break;
            }
        }
        return addScore;
    }

    /**
     * Handles "MOST" and "FEWEST" scoring criteria.
     *
     * @param criteria The scoring criteria
     * @param hand The player's hand
     * @param thisPlayer The current player
     * @param players All players
     * @return The calculated score
     */
    private int handleMostFewest(String criteria, ArrayList<ICard> hand, IPlayer thisPlayer, ArrayList<IPlayer> players) {
        int vegIndex = criteria.indexOf("MOST")>=0 ? criteria.indexOf("MOST")+5 : criteria.indexOf("FEWEST")+7;
        String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();
        int countVeg = cardCounter.countVegetables(hand, Vegetable.valueOf(veg));
        int nrVeg = countVeg;
        for(IPlayer p : players) {
            if(p.getPlayerID() != thisPlayer.getPlayerID()) {
                int playerVeg = cardCounter.countVegetables(p.getHand(), Vegetable.valueOf(veg));
                if((criteria.indexOf("MOST")>=0) && (playerVeg > nrVeg)) {
                    nrVeg = cardCounter.countVegetables(p.getHand(), Vegetable.valueOf(veg));
                }
                if((criteria.indexOf("FEWEST")>=0) && (playerVeg < nrVeg)) {
                    nrVeg = cardCounter.countVegetables(p.getHand(), Vegetable.valueOf(veg));
                }
            }
        }
        if(nrVeg == countVeg) {
            return Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
        }
        return 0;
    }

    /**
     * Handles "+" criteria (combinations of vegetables).
     *
     * @param criteria The scoring criteria
     * @param hand The player's hand
     * @return The calculated score
     */
    private int handlePlusCriteria(String criteria, ArrayList<ICard> hand) {
        String expr = criteria.split("=")[0].trim();
        String[] vegs = expr.split("\\+");
        int[] nrVeg = new int[vegs.length];
        int countSameKind = 1;
        for (int j = 1; j < vegs.length; j++) {
            if (vegs[0].trim().equals(vegs[j].trim())) {
                countSameKind++;
            }
        }
        if (countSameKind > 1) {
            return ((int) cardCounter.countVegetables(hand, Vegetable.valueOf(vegs[0].trim())) / countSameKind) *
                    Integer.parseInt(criteria.split("=")[1].trim());
        } else {
            for (int i = 0; i < vegs.length; i++) {
                nrVeg[i] = cardCounter.countVegetables(hand, Vegetable.valueOf(vegs[i].trim()));
            }
            int min = nrVeg[0];
            for (int x = 1; x < nrVeg.length; x++) {
                if (nrVeg[x] < min) {
                    min = nrVeg[x];
                }
            }
            return min * Integer.parseInt(criteria.split("=")[1].trim());
        }
    }

    /**
     * Handles EVEN/ODD criteria.
     *
     * @param criteria The scoring criteria
     * @param hand The player's hand
     * @param parts The split criteria parts
     * @return The calculated score
     */
    private int handleEvenOddCriteria(String criteria, ArrayList<ICard> hand, String[] parts) {
        String veg = parts[0].substring(0, parts[0].indexOf(":"));
        int countVeg = cardCounter.countVegetables(hand, Vegetable.valueOf(veg));
        return (countVeg%2==0)?7:3;
    }

    /**
     * Handles "/" criteria (points per vegetable).
     *
     * @param hand The player's hand
     * @param parts The split criteria parts
     * @return The calculated score
     */
    private int handleSlashCriteria(ArrayList<ICard> hand, String[] parts) {
        int score = 0;
        for(int i = 0; i < parts.length; i++) {
            String[] veg = parts[i].split("/");
            score += Integer.parseInt(veg[0].trim()) * cardCounter.countVegetables(hand, Vegetable.valueOf(veg[1].trim()));
        }
        return score;
    }
}