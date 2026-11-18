package com.exodus.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {
    public static final int TILE_SIZE = 48; // Base 16px * Scale 3[cite: 1]
    private static final int HITBOX_OFFSET = 8;
    private static final int HITBOX_SIZE = 32;

    private String name;
    private int worldX;
    private int worldY;
    private int speed;
    private int hp;
    private int maxHp;
    private int atk;
    private int def;

    public Entity(String name, int col, int row, int speed, int maxHp, int atk, int def) {
        setName(name);
        setPosition(col, row);
        setSpeed(speed);
        setMaxHp(maxHp);
        setHp(maxHp);
        setAtk(atk);
        setDef(def);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nama entity tidak boleh kosong.");
        }
        this.name = name;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = Math.max(0, worldX);
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = Math.max(0, worldY);
    }

    public void setPosition(int col, int row) {
        setWorldX(col * TILE_SIZE);
        setWorldY(row * TILE_SIZE);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = Math.max(0, speed);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(this.maxHp, hp));
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = Math.max(0, atk);
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = Math.max(0, def);
    }

    public void takeDamage(int rawDamage) {
        int netDamage = Math.max(0, rawDamage - this.def);
        this.hp = Math.max(0, this.hp - netDamage);
    }

    public void heal(int amount) {
        if (amount <= 0) {
            return;
        }
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public Rectangle getHitbox() {
        return new Rectangle(
            this.worldX + HITBOX_OFFSET,
            this.worldY + HITBOX_OFFSET,
            HITBOX_SIZE,
            HITBOX_SIZE
        );
    }

    public abstract void update();

    public abstract void render(Graphics2D g);
}