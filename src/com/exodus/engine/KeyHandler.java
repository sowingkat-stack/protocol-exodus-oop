package com.exodus.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean interactPressed;
    public int numberPressed;
    public boolean restartPressed;

    private boolean interactLocked = false;
    private boolean numberLocked = false;
    private boolean restartLocked = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;

        if ((code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) && !interactLocked) {
            interactPressed = true;
            interactLocked = true;
        }

        if (!numberLocked) {
            if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
                numberPressed = 1;
                numberLocked = true;
            } else if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                numberPressed = 2;
                numberLocked = true;
            } else if (code == KeyEvent.VK_3 || code == KeyEvent.VK_NUMPAD3) {
                numberPressed = 3;
                numberLocked = true;
            } else if (code == KeyEvent.VK_4 || code == KeyEvent.VK_NUMPAD4) {
                numberPressed = 4;
                numberLocked = true;
            }
        }

        if (code == KeyEvent.VK_R && !restartLocked) {
            restartPressed = true;
            restartLocked = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;

        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) {
            interactPressed = false;
            interactLocked = false;
        }

        if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1 ||
            code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2 ||
            code == KeyEvent.VK_3 || code == KeyEvent.VK_NUMPAD3 ||
            code == KeyEvent.VK_4 || code == KeyEvent.VK_NUMPAD4) {
            numberPressed = 0;
            numberLocked = false;
        }

        if (code == KeyEvent.VK_R) {
            restartPressed = false;
            restartLocked = false;
        }
    }

    public void consumeKeys() {
        interactPressed = false;
        numberPressed = 0;
        restartPressed = false;
    }
}