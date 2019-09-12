package com.games.flipit;

public class PlayerObject {
    boolean isTurn = false;
    int playerScore = 0;
    int totalOpenedTiles = 0;
    int[] openedPlaces = new int[2];
    int[] openedTypes = new int[2];
    int totalAttempts = 0;
}
