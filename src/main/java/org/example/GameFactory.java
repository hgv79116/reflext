package org.example;

import org.example.survi.Game;

public class GameFactory {
    private final int numCategory = 10;
    Game newGame() {
        return new Game(numCategory);
    }
}
