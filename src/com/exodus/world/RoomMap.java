package com.exodus.world;

import com.exodus.entities.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class RoomMap {

    public static final int TILE_SIZE = 48;
    public static final int MAX_COLS = 16;
    public static final int MAX_ROWS = 12;

    public static final int TILE_FLOOR = 0;
    public static final int TILE_WALL = 1;
    public static final int TILE_DOOR = 2;
    public static final int TILE_DECOR_SOLID = 3;
    public static final int TILE_HAZARD = 4;

    private int currentRoomId = 0;

    private static final String[] ROOM_NAMES = {
        "ROOM 0: CRYO-BAY (TITIK AWAL)",
        "ROOM 1: KORIDOR UTAMA (CENTRAL HUB)",
        "ROOM 2: GUDANG LOGISTIK",
        "ROOM 3: RUANG SERVER (TITIK KEPUTUSAN)",
        "ROOM 4: LIFT PERMUKAAN (ARENA BOSS)"
    };

    private final int[][][] maps = {
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 0, 3, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 2},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 0, 3, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        {
            {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        {
            {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 1},
            {1, 0, 0, 3, 3, 0, 0, 3, 0, 0, 0, 4, 0, 4, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 3, 3, 0, 0, 3, 0, 0, 0, 0, 3, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 4, 0, 4, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1}
        }
    };

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void changeRoom(int targetRoomId, int spawnCol, int spawnRow, Player player) {
        if (targetRoomId >= 0 && targetRoomId < maps.length) {
            this.currentRoomId = targetRoomId;
            if (player != null) {
                player.setPosition(spawnCol, spawnRow);
            }
        }
    }

    public boolean isTileSolid(int col, int row) {
        if (col < 0 || col >= MAX_COLS || row < 0 || row >= MAX_ROWS) {
            return true;
        }
        int tile = maps[currentRoomId][row][col];
        return tile == TILE_WALL || tile == TILE_DECOR_SOLID;
    }

    public int getTileEvent(int col, int row) {
        if (col < 0 || col >= MAX_COLS || row < 0 || row >= MAX_ROWS) {
            return -1;
        }
        return maps[currentRoomId][row][col];
    }

    public void render(Graphics2D g) {
        int[][] currentGrid = maps[currentRoomId];

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                drawTile(g, currentGrid[row][col], col * TILE_SIZE, row * TILE_SIZE);
            }
        }

        g.setColor(new Color(10, 15, 26));
        g.fillRect(0, 576, 800, 24);
        g.fillRect(768, 0, 32, 600);

        g.setColor(new Color(0, 210, 230));
        g.drawLine(0, 576, 768, 576);
        g.drawLine(768, 0, 768, 576);

        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.setColor(new Color(0, 240, 255));
        g.drawString(ROOM_NAMES[currentRoomId], 12, 593);

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.setColor(new Color(160, 190, 210));
        g.drawString("[1-4: Peta | E: Uji UI | R: Room 0]", 500, 593);
    }

    private void drawTile(Graphics2D g, int tile, int x, int y) {
        switch (tile) {
            case TILE_FLOOR:
                g.setColor(new Color(22, 29, 41));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(32, 42, 58));
                g.drawRect(x, y, TILE_SIZE - 1, TILE_SIZE - 1);
                g.setColor(new Color(45, 58, 78));
                g.fillRect(x + 4, y + 4, 3, 3);
                g.fillRect(x + TILE_SIZE - 7, y + TILE_SIZE - 7, 3, 3);
                break;

            case TILE_WALL:
                g.setColor(new Color(45, 55, 72));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(74, 85, 104));
                g.fillRect(x + 3, y + 3, TILE_SIZE - 6, 6);
                g.setColor(new Color(26, 32, 44));
                g.drawRect(x, y, TILE_SIZE - 1, TILE_SIZE - 1);
                g.drawRect(x + 6, y + 12, TILE_SIZE - 13, TILE_SIZE - 19);
                break;

            case TILE_DOOR:
                g.setColor(new Color(12, 45, 58));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(0, 240, 255));
                g.setStroke(new BasicStroke(2));
                g.drawRect(x + 2, y + 2, TILE_SIZE - 5, TILE_SIZE - 5);
                g.setStroke(new BasicStroke(1));
                g.fillRect(x + 8, y + 12, TILE_SIZE - 16, 4);
                g.fillRect(x + 8, y + 22, TILE_SIZE - 16, 4);
                g.fillRect(x + 8, y + 32, TILE_SIZE - 16, 4);
                break;

            case TILE_DECOR_SOLID:
                g.setColor(new Color(22, 29, 41));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(30, 41, 59));
                g.fillRect(x + 6, y + 4, TILE_SIZE - 12, TILE_SIZE - 8);
                g.setColor(new Color(14, 165, 233));
                g.fillRect(x + 10, y + 10, TILE_SIZE - 20, 14);
                g.setColor(new Color(16, 185, 129));
                g.fillRect(x + 12, y + 30, 6, 4);
                g.setColor(new Color(239, 68, 68));
                g.fillRect(x + 22, y + 30, 6, 4);
                break;

            case TILE_HAZARD:
                g.setColor(new Color(38, 34, 24));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(new Color(234, 179, 8, 170));
                for (int i = 4; i < TILE_SIZE - 4; i += 12) {
                    g.fillRect(x + i, y + 4, 6, TILE_SIZE - 8);
                }
                g.setColor(new Color(55, 50, 35));
                g.drawRect(x, y, TILE_SIZE - 1, TILE_SIZE - 1);
                break;
        }
    }
}