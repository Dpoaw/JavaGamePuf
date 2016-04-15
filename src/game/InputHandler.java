package game;

import display.Display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public InputHandler(Display display) {
        display.getCanvas().addKeyListener(this);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (Game.turn) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Player01.isMovingLeft01 = true;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Player01.isMovingRight01 = true;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Player01.isMovingUp01 = true;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Player01.isMovingDown01 = true;
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = true;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Player01.powerUp = true;
            } else if(keyCode == KeyEvent.VK_PAGE_DOWN) {
                Player01.powerDown = true;
            }
        } else {
            if (keyCode == KeyEvent.VK_LEFT) {
                Player02.isMovingLeft02 = true;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Player02.isMovingRight02 = true;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Player02.isMovingUp02 = true;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Player02.isMovingDown02 = true;
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = true;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Player02.powerUp = true;
            } else if(keyCode == KeyEvent.VK_PAGE_DOWN) {
                Player02.powerDown = true;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (Game.turn) {
            if (keyCode == KeyEvent.VK_LEFT) {
                Player01.isMovingLeft01 = false;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Player01.isMovingRight01 = false;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Player01.isMovingUp01 = false;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Player01.isMovingDown01 = false;
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = false;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Player01.powerUp = false;
            } else if(keyCode == KeyEvent.VK_PAGE_DOWN) {
                Player01.powerDown = false;
            }
        } else {
            if (keyCode == KeyEvent.VK_LEFT) {
                Player02.isMovingLeft02 = false;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                Player02.isMovingRight02 = false;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Player02.isMovingUp02 = false;
            } else if (keyCode == KeyEvent.VK_DOWN) {
                Player02.isMovingDown02 = false;
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                Game.fire = false;
            }
            if (keyCode == KeyEvent.VK_PAGE_UP) {
                Player02.powerUp = false;
            } else if(keyCode == KeyEvent.VK_PAGE_DOWN) {
                Player02.powerDown = false;
            }
        }
    }
}
