package com.exodus.entities.enemies;

import com.exodus.entities.Player;

import java.awt.Color;
import java.awt.Graphics2D;

public class SecurityAndroid extends Enemy {
    private static final int EMP_DRAIN = 10;
    private int turnCounter;

    public SecurityAndroid(int col, int row) {
        super("Security Android", col, row, 2, 75, 16, 8);
        this.turnCounter = 0;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = Math.max(0, turnCounter);
    }

    @Override
    public String executeTurn(Player player, boolean playerDefending) {
        if (isStunned()) {
            return consumeStunTurn();
        }

        turnCounter++;
        int hpBefore = player.getHp();

        if (turnCounter % 3 == 0) {
            int rawEmp = (int) Math.round(getAtk() * 1.25);
            int effectiveRaw = playerDefending ? Math.max(1, rawEmp / 2) : rawEmp;
            player.takeDamage(effectiveRaw);

            int energyBefore = player.getEnergy();
            player.setEnergy(energyBefore - EMP_DRAIN);
            int energyDrained = energyBefore - player.getEnergy();
            int dealt = hpBefore - player.getHp();

            return getName() + " melancarkan EMP SLASH! (-" + dealt + " HP, -" + energyDrained + " EN)";
        }

        int effectiveRaw = playerDefending ? Math.max(1, getAtk() / 2) : getAtk();
        player.takeDamage(effectiveRaw);
        int dealt = hpBefore - player.getHp();

        return getName() + " menyerang dengan bilah taktis! (-" + dealt + " HP)";
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala 16x16 -> 48x48

        Color shadowColor = new Color(10, 14, 20, 150);
        Color metalDark = new Color(68, 76, 88);
        Color metalMid = new Color(128, 138, 152);
        Color metalHighlight = new Color(182, 192, 206);
        Color opticAmber = new Color(255, 140, 0);
        Color jointDark = new Color(36, 40, 48);

        // 1. Bayangan kaki
        g.setColor(shadowColor);
        g.fillRect(x + 3 * s, y + 14 * s, 10 * s, 2 * s);

        // 2. Kaki robotik
        g.setColor(jointDark);
        g.fillRect(x + 4 * s, y + 12 * s, 3 * s, 3 * s);
        g.fillRect(x + 9 * s, y + 12 * s, 3 * s, 3 * s);

        // 3. Torso humanoid abu-abu metalik
        g.setColor(metalDark);
        g.fillRect(x + 3 * s, y + 6 * s, 10 * s, 6 * s);
        g.setColor(metalMid);
        g.fillRect(x + 4 * s, y + 7 * s, 8 * s, 4 * s);
        g.setColor(metalHighlight);
        g.fillRect(x + 5 * s, y + 7 * s, 6 * s, 1 * s);

        // 4. Lengan mekanis
        g.setColor(metalMid);
        g.fillRect(x + 1 * s, y + 6 * s, 2 * s, 5 * s);
        g.fillRect(x + 13 * s, y + 6 * s, 2 * s, 5 * s);

        // 5. Kepala & visor optik
        g.setColor(metalDark);
        g.fillRect(x + 4 * s, y + 1 * s, 8 * s, 5 * s);
        g.setColor(metalHighlight);
        g.fillRect(x + 5 * s, y + 1 * s, 6 * s, 1 * s);
        g.setColor(opticAmber);
        g.fillRect(x + 5 * s, y + 3 * s, 6 * s, 1 * s);
    }
}