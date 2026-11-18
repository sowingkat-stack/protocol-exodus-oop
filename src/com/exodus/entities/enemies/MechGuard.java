package com.exodus.entities.enemies;

import com.exodus.entities.Player;

import java.awt.Color;
import java.awt.Graphics2D;

public class MechGuard extends Enemy {
    private boolean isCharging;

    public MechGuard(int col, int row) {
        super("T-900 Mech Guard", col, row, 1, 150, 22, 14);
        this.isCharging = false;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        this.isCharging = charging;
    }

    @Override
    public String executeTurn(Player player, boolean playerDefending) {
        if (isStunned()) {
            return consumeStunTurn();
        }

        if (!isCharging) {
            isCharging = true;
            return "T-900 CHARGING HYPER BEAM! Gunakan Defend!";
        }

        isCharging = false;
        int hpBefore = player.getHp();
        int hyperBeamRaw = (int) Math.round(getAtk() * 2.2);
        int effectiveRaw = playerDefending ? Math.max(1, hyperBeamRaw / 2) : hyperBeamRaw;
        player.takeDamage(effectiveRaw);
        int dealt = hpBefore - player.getHp();

        return "T-900 MENEMBAKKAN HYPER BEAM! (-" + dealt + " HP)";
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala 16x16 -> 48x48

        Color shadowColor = new Color(8, 10, 14, 170);
        Color armorBlack = new Color(26, 28, 34);
        Color armorCrimson = new Color(175, 24, 36);
        Color armorRedLight = new Color(225, 52, 64);
        Color cannonMetal = new Color(74, 80, 92);
        Color coreGlow = isCharging ? new Color(255, 230, 60) : new Color(255, 40, 40);

        // 1. Bayangan berat mech raksasa
        g.setColor(shadowColor);
        g.fillRect(x + 1 * s, y + 14 * s, 14 * s, 2 * s);

        // 2. Kaki hidrolik tebal hitam-merah
        g.setColor(armorBlack);
        g.fillRect(x + 2 * s, y + 11 * s, 4 * s, 4 * s);
        g.fillRect(x + 10 * s, y + 11 * s, 4 * s, 4 * s);
        g.setColor(armorCrimson);
        g.fillRect(x + 3 * s, y + 12 * s, 2 * s, 2 * s);
        g.fillRect(x + 11 * s, y + 12 * s, 2 * s, 2 * s);

        // 3. Meriam bahu ganda (kiri & kanan atas)
        g.setColor(cannonMetal);
        g.fillRect(x + 0 * s, y + 1 * s, 3 * s, 5 * s);
        g.fillRect(x + 13 * s, y + 1 * s, 3 * s, 5 * s);
        g.setColor(coreGlow);
        g.fillRect(x + 1 * s, y + 1 * s, 1 * s, 1 * s);
        g.fillRect(x + 14 * s, y + 1 * s, 1 * s, 1 * s);

        // 4. Chassis raksasa merah-hitam
        g.setColor(armorBlack);
        g.fillRect(x + 2 * s, y + 4 * s, 12 * s, 7 * s);
        g.setColor(armorCrimson);
        g.fillRect(x + 3 * s, y + 5 * s, 10 * s, 5 * s);
        g.setColor(armorRedLight);
        g.fillRect(x + 4 * s, y + 5 * s, 8 * s, 1 * s);

        // 5. Lengan berat & reaktor dada
        g.setColor(armorBlack);
        g.fillRect(x + 0 * s, y + 6 * s, 2 * s, 5 * s);
        g.fillRect(x + 14 * s, y + 6 * s, 2 * s, 5 * s);
        g.setColor(coreGlow);
        g.fillRect(x + 7 * s, y + 7 * s, 2 * s, 2 * s);

        // 6. Kepala & visor tempur
        g.setColor(armorBlack);
        g.fillRect(x + 5 * s, y + 1 * s, 6 * s, 3 * s);
        g.setColor(coreGlow);
        g.fillRect(x + 6 * s, y + 2 * s, 4 * s, 1 * s);
    }
}