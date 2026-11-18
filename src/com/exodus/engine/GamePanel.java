package com.exodus.engine;

import com.exodus.combat.CombatManager;
import com.exodus.entities.Player;
import com.exodus.entities.enemies.Enemy;
import com.exodus.ui.BattleScreen;
import com.exodus.ui.DialogueBox;
import com.exodus.world.RoomMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int BASE_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = BASE_TILE_SIZE * SCALE;
    public static final int MAX_COLS = 16;
    public static final int MAX_ROWS = 12;
    public static final int FPS = 60;

    public enum GameState {
        TITLE, EXPLORE, DIALOGUE, BATTLE, ENDING
    }

    private GameState currentState;
    private final KeyHandler keyH;
    private final RoomMap roomMap;
    private final DialogueBox dialogueBox;
    private final BattleScreen battleScreen;
    private final CombatManager combatManager;
    private final Player player;
    private final Timer gameTimer;
    private int frameCounter = 0;

    private Enemy currentEnemy;

    public GamePanel() {
        this.keyH = new KeyHandler();
        this.roomMap = new RoomMap();
        this.dialogueBox = new DialogueBox();
        this.battleScreen = new BattleScreen();
        this.combatManager = new CombatManager();
        this.player = new Player(keyH, roomMap);
        this.currentState = GameState.TITLE;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(10, 14, 24));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyH);

        this.gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });
        this.gameTimer.start();
    }

    public void setGameState(GameState state) {
        this.currentState = state;
        keyH.consumeKeys();
    }

    public void openDialogue(String speaker, String text, String opt1, String opt2, Consumer<Integer> callback) {
        dialogueBox.show(speaker, text, opt1, opt2, callback);
        setGameState(GameState.DIALOGUE);
    }

    public void startBattle(Enemy enemy) {
        this.currentEnemy = enemy;
        combatManager.initBattle(player, enemy);
        setGameState(GameState.BATTLE);
    }

    public GameState getGameState() { return currentState; }
    public KeyHandler getKeyHandler() { return keyH; }
    public RoomMap getRoomMap() { return roomMap; }
    public DialogueBox getDialogueBox() { return dialogueBox; }
    public BattleScreen getBattleScreen() { return battleScreen; }

    private void update() {
        frameCounter++;

        switch (currentState) {
            case TITLE:
                if (keyH.interactPressed) {
                    keyH.consumeKeys();
                    roomMap.changeRoom(0, 3, 5, player);
                    setGameState(GameState.EXPLORE);
                }
                break;

            case EXPLORE:
                if (keyH.restartPressed) {
                    roomMap.changeRoom(0, 3, 5, player);
                    keyH.consumeKeys();
                } else if (keyH.numberPressed >= 1 && keyH.numberPressed <= 4) {
                    roomMap.changeRoom(keyH.numberPressed, 7, 5, player);
                    keyH.consumeKeys();
                } else if (keyH.interactPressed) {
                    keyH.consumeKeys();
                    openDialogue(
                        "TRACK A DIAGNOSTIC",
                        "Pilih komponen UI Track A yang ingin kamu uji coba secara langsung:",
                        "Uji DialogueBox (Dialog Dr. Aris)",
                        "Uji BattleScreen (Arena Pertarungan Turn-Based)",
                        choice -> {
                            if (choice == 1) {
                                openDialogue(
                                    "Dr. Aris",
                                    "AETHER mengunci lift utama! Kakiku terjepit reruntuhan... Jika kau punya Medkit, tolong obati aku.",
                                    "Berikan 1 Medkit & Tolong Aris",
                                    "Tinggalkan Aris",
                                    null
                                );
                            } else {
                                startBattle(new Enemy() {});
                            }
                        }
                    );
                }
                break;

            case DIALOGUE:
                dialogueBox.update(keyH);
                // Hanya kembali ke EXPLORE jika callback tidak memindahkan state ke BATTLE/ENDING
                if (!dialogueBox.isOpen() && currentState == GameState.DIALOGUE) {
                    setGameState(GameState.EXPLORE);
                }
                break;

            case BATTLE:
                if (keyH.numberPressed >= 1 && keyH.numberPressed <= 4) {
                    combatManager.executePlayerAction(keyH.numberPressed);
                    keyH.consumeKeys();
                } else if (keyH.restartPressed) {
                    keyH.consumeKeys();
                    setGameState(GameState.EXPLORE);
                }
                break;

            case ENDING:
                if (keyH.restartPressed) {
                    keyH.consumeKeys();
                    setGameState(GameState.TITLE);
                }
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        switch (currentState) {
            case TITLE:
                renderTitleScreen(g2);
                break;
            case EXPLORE:
                roomMap.render(g2);
                break;
            case DIALOGUE:
                roomMap.render(g2);
                dialogueBox.render(g2);
                break;
            case BATTLE:
                battleScreen.render(g2, player, currentEnemy, combatManager);
                break;
            case ENDING:
                renderEndingPlaceholder(g2);
                break;
        }

        g2.dispose();
    }

    private void renderTitleScreen(Graphics2D g2) {
        g2.setColor(new Color(8, 12, 20));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g2.setColor(new Color(0, 255, 220, 15));
        for (int y = 0; y < SCREEN_HEIGHT; y += 4) {
            g2.drawLine(0, y, SCREEN_WIDTH, y);
        }

        g2.setColor(new Color(0, 210, 230));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(36, 36, SCREEN_WIDTH - 72, SCREEN_HEIGHT - 72);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(44, 44, SCREEN_WIDTH - 88, SCREEN_HEIGHT - 88);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(new Color(0, 200, 170));
        drawCenteredString(g2, "SYSTEM BOOT // AETHER-OS v4.09 // SECTOR-7", 110);

        g2.setFont(new Font("Monospaced", Font.BOLD, 52));
        g2.setColor(new Color(0, 240, 255));
        drawCenteredString(g2, "PROTOCOL: EXODUS", 245);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2.setColor(new Color(170, 200, 220));
        drawCenteredString(g2, "2D TOP-DOWN TACTICAL SCI-FI RPG", 295);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(new Color(120, 150, 175));
        drawCenteredString(g2, "[WASD / ARROWS] Move   |   [E / ENTER] Interact   |   [1-4] Action", 390);

        if ((frameCounter / 30) % 2 == 0) {
            g2.setFont(new Font("Monospaced", Font.BOLD, 22));
            g2.setColor(new Color(255, 215, 0));
            drawCenteredString(g2, "> PRESS [ENTER] TO START <", 475);
        }
    }

    private void renderEndingPlaceholder(Graphics2D g2) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 24));
        g2.setColor(new Color(0, 255, 170));
        drawCenteredString(g2, "[STATE: ENDING] SIMULATION COMPLETE", 270);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, "Tekan [R] untuk kembali ke Title Screen", 315);
    }

    private void drawCenteredString(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}