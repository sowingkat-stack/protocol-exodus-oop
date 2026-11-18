package com.exodus.entities;

import com.exodus.combat.Item;
import com.exodus.engine.KeyHandler;
import com.exodus.world.RoomMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Player extends Entity implements Combatant {
    private static final int INITIAL_HP = 100;
    private static final int INITIAL_ENERGY = 50;
    private static final int INITIAL_ATK = 15;
    private static final int INITIAL_DEF = 6;
    private static final int INITIAL_SPEED = 4;

    private static final int HITBOX_OFFSET = 8;
    private static final int HITBOX_SIZE = 32;
    private static final int MAX_COLS = 16;
    private static final int MAX_ROWS = 12;

    private final KeyHandler keyH;
    private final RoomMap map;
    private final List<Item> inventory;

    private int energy;
    private int maxEnergy;

    public Player(KeyHandler keyH, RoomMap map) {
        super("Agent", 2, 2, INITIAL_SPEED, INITIAL_HP, INITIAL_ATK, INITIAL_DEF);
        this.keyH = Objects.requireNonNull(keyH, "KeyHandler tidak boleh null.");
        this.map = Objects.requireNonNull(map, "RoomMap tidak boleh null.");
        this.maxEnergy = INITIAL_ENERGY;
        this.energy = INITIAL_ENERGY;
        this.inventory = new ArrayList<>();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(this.maxEnergy, energy));
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = Math.max(1, maxEnergy);
        if (this.energy > this.maxEnergy) {
            this.energy = this.maxEnergy;
        }
    }

    public Boolean useEnergy(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (this.energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }

    public void restoreEnergy(int amount) {
        if (amount <= 0) {
            return;
        }
        this.energy = Math.min(this.maxEnergy, this.energy + amount);
    }

    public void addAtk(int bonus) {
        if (bonus > 0) {
            setAtk(getAtk() + bonus);
        }
    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public boolean hasItem(Item item) {
        return item != null && inventory.contains(item);
    }

    public boolean removeItem(Item item) {
        return item != null && inventory.remove(item);
    }

    @Override
    public void update() {
        int dx = 0;
        int dy = 0;

        if (keyH.upPressed) {
            dy -= getSpeed();
        }
        if (keyH.downPressed) {
            dy += getSpeed();
        }
        if (keyH.leftPressed) {
            dx -= getSpeed();
        }
        if (keyH.rightPressed) {
            dx += getSpeed();
        }

        if (dx != 0) {
            int nextX = getWorldX() + dx;
            if (canMoveTo(nextX, getWorldY())) {
                setWorldX(nextX);
            }
        }

        if (dy != 0) {
            int nextY = getWorldY() + dy;
            if (canMoveTo(getWorldX(), nextY)) {
                setWorldY(nextY);
            }
        }
    }

    private boolean canMoveTo(int nextWorldX, int nextWorldY) {
        int left = nextWorldX + HITBOX_OFFSET;
        int right = left + HITBOX_SIZE - 1;
        int top = nextWorldY + HITBOX_OFFSET;
        int bottom = top + HITBOX_SIZE - 1;

        if (left < 0 || top < 0) {
            return false;
        }

        int leftCol = left / TILE_SIZE;
        int rightCol = right / TILE_SIZE;
        int topRow = top / TILE_SIZE;
        int bottomRow = bottom / TILE_SIZE;

        if (rightCol >= MAX_COLS || bottomRow >= MAX_ROWS) {
            return false;
        }

        return !map.isTileSolid(leftCol, topRow)
            && !map.isTileSolid(rightCol, topRow)
            && !map.isTileSolid(leftCol, bottomRow)
            && !map.isTileSolid(rightCol, bottomRow);
    }

    @Override
    public String executeTurn(Player player, boolean playerDefending) {
        return getName() + " bersiap melakukan aksi taktis.";
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala pixel art (16x16 -> 48x48)[cite: 1]

        // Palet warna agen sci-fi
        Color shadowColor = new Color(10, 14, 24, 150);
        Color helmetDark = new Color(24, 34, 56);
        Color armorDarkBlue = new Color(30, 58, 110);
        Color armorMidBlue = new Color(46, 86, 158);
        Color armorTrim = new Color(120, 148, 196);
        Color visorCyan = new Color(0, 238, 255);
        Color visorGlow = new Color(180, 252, 255);
        Color bootDark = new Color(18, 24, 38);

        // 1. Bayangan kaki (grid 16x16)
        g.setColor(shadowColor);
        g.fillRect(x + 3 * s, y + 14 * s, 10 * s, 2 * s);

        // 2. Kaki & sepatu bot taktis
        g.setColor(bootDark);
        g.fillRect(x + 4 * s, y + 12 * s, 3 * s, 3 * s);
        g.fillRect(x + 9 * s, y + 12 * s, 3 * s, 3 * s);

        // 3. Armor badan (biru gelap & aksen bahu)
        g.setColor(armorDarkBlue);
        g.fillRect(x + 3 * s, y + 7 * s, 10 * s, 5 * s);

        // Lengan & pelindung bahu
        g.setColor(armorMidBlue);
        g.fillRect(x + 2 * s, y + 7 * s, 2 * s, 4 * s);
        g.fillRect(x + 12 * s, y + 7 * s, 2 * s, 4 * s);

        // Detail dada & core energi cyan
        g.setColor(armorTrim);
        g.fillRect(x + 5 * s, y + 8 * s, 6 * s, 1 * s);
        g.setColor(visorCyan);
        g.fillRect(x + 7 * s, y + 9 * s, 2 * s, 2 * s);

        // 4. Helm sci-fi
        g.setColor(helmetDark);
        g.fillRect(x + 4 * s, y + 1 * s, 8 * s, 6 * s);
        g.setColor(armorMidBlue);
        g.fillRect(x + 5 * s, y + 1 * s, 6 * s, 2 * s);

        // 5. Visor cyan menyala + kilau pixel
        g.setColor(visorCyan);
        g.fillRect(x + 5 * s, y + 3 * s, 6 * s, 2 * s);
        g.setColor(visorGlow);
        g.fillRect(x + 6 * s, y + 3 * s, 2 * s, 1 * s);
    }
}