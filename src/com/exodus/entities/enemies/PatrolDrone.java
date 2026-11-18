package com.exodus.entities.enemies;

import com.exodus.entities.Player;

import java.awt.Color;
import java.awt.Graphics2D;

public class PatrolDrone extends Enemy {
    private static final double DODGE_CHANCE = 0.20;

    public PatrolDrone(int col, int row) {
        super("Patrol Drone", col, row, 2, 45, 12, 4);
    }

    @Override
    public void takeDamage(int rawDamage) {
        if (Math.random() < DODGE_CHANCE) {
            return; // 20% peluang menghindar (damage = 0)
        }
        super.takeDamage(rawDamage);
    }

    @Override
    public String executeTurn(Player player, boolean playerDefending) {
        if (isStunned()) {
            return consumeStunTurn();
        }

        int hpBefore = player.getHp();
        int effectiveRaw = playerDefending ? Math.max(1, getAtk() / 2) : getAtk();
        player.takeDamage(effectiveRaw);
        int dealt = hpBefore - player.getHp();

        return getName() + " menembakkan laser ringan! (-" + dealt + " HP)";
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala 16x16 -> 48x48

        Color shadowColor = new Color(10, 12, 18, 130);
        Color hullDark = new Color(45, 52, 64);
        Color hullLight = new Color(92, 104, 122);
        Color rotorColor = new Color(140, 160, 185);
        Color sensorRed = new Color(255, 35, 55);
        Color sensorGlow = new Color(255, 170, 170);

        // 1. Bayangan di lantai (menunjukkan drone melayang)
        g.setColor(shadowColor);
        g.fillRect(x + 4 * s, y + 14 * s, 8 * s, 1 * s);

        // 2. Baling-baling / thruster samping
        g.setColor(rotorColor);
        g.fillRect(x + 1 * s, y + 4 * s, 4 * s, 1 * s);
        g.fillRect(x + 11 * s, y + 4 * s, 4 * s, 1 * s);
        g.setColor(hullDark);
        g.fillRect(x + 2 * s, y + 5 * s, 2 * s, 2 * s);
        g.fillRect(x + 12 * s, y + 5 * s, 2 * s, 2 * s);

        // 3. Badan utama drone melayang
        g.setColor(hullDark);
        g.fillRect(x + 4 * s, y + 4 * s, 8 * s, 6 * s);
        g.setColor(hullLight);
        g.fillRect(x + 5 * s, y + 3 * s, 6 * s, 2 * s);
        g.fillRect(x + 5 * s, y + 9 * s, 6 * s, 1 * s);

        // 4. Lampu sensor merah menyala
        g.setColor(sensorRed);
        g.fillRect(x + 6 * s, y + 6 * s, 4 * s, 2 * s);
        g.setColor(sensorGlow);
        g.fillRect(x + 7 * s, y + 6 * s, 1 * s, 1 * s);
    }
}