package graphics;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage player1, player2;

    public static void init() {
        SpriteSheet spriteSheet = new SpriteSheet(ImageLoader.load("/images/player.png"));

        player1 = spriteSheet.crop(0, 0, 95, 130);
        player2 = spriteSheet.crop(3 * 95, 2 * 130, 95, 130);
    }
}
