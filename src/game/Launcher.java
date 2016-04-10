package game;

import display.Display;

public class Launcher {

    public static void main(String[] args) {
        Game game = new Game("JavaGamePuf", 1200, 700);//calling Game, which calls display
        game.start();
    }
}
//1 hour of video