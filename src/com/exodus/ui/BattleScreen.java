package com.exodus.ui;

import com.exodus.combat.CombatManager;
import com.exodus.entities.Player;
import com.exodus.entities.enemies.Enemy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BattleScreen {

    private static final int SCREEN_WIDTH = 800;

    public void render(Graphics2D g, Player p, Enemy e, CombatManager cm) {
        drawArenaBackground(g);
        drawPlayerSection(g, p);
        drawEnemySection(g, e);
        drawBattleLogBox(g, cm);
        drawActionCards(g, p);
    }

    private void drawArenaBackground(Graphics2D g) {
        g.setColor(new Color(10, 14, 24));
        g.fillRect(0, 0, SCREEN_WIDTH, 210);

        g.setColor(new Color(22, 30, 46));
        g.fillRect(40, 30, 720, 140);
        g.setColor(new Color(0, 210, 230, 80));
        g.drawRect(40, 30, 720, 140);

        g.setColor(new Color(16, 22, 34));
        g.fillRect(0, 210, SCREEN_WIDTH, 390);

        g.setColor(new Color(0, 240, 255, 35));
        for (int y = 210; y <= 355; y += 24) {
            g.drawLine(0, y, SCREEN_WIDTH, y);
        }
        for (int i = -4; i <= 12; i++) {
            int topX = 120 + (i * 70);
            int bottomX = -80 + (i * 120);
            g.drawLine(topX, 210, bottomX, 355);
        }

        g.setColor(new Color(0, 240, 255, 45));
        g.fillOval(125, 282, 105, 24);
        g.setColor(new Color(239, 68, 68, 45));
        g.fillOval(545, 246, 105, 24);

        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(new Color(140, 170, 195));
        g.drawString("[Tekan 1-4: Uji Aksi | R: Keluar Battle]", 525, 20);
    }

    private void drawPlayerSection(Graphics2D g, Player p) {
        int px = 140;
        int py = 220;

        if (p != null) {
            Graphics2D gPlayer = (Graphics2D) g.create();
            gPlayer.translate(px, py);
            gPlayer.scale(1.5, 1.5);
            gPlayer.translate(-p.getWorldX(), -p.getWorldY());
            p.render(gPlayer);
            gPlayer.dispose();
        }

        int panelX = 70;
        int panelY = 95;
        int panelW = 220;
        int panelH = 88;

        g.setColor(new Color(12, 18, 32, 230));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 8, 8);
        g.setColor(new Color(0, 240, 255));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(panelX, panelY, panelW, panelH, 8, 8);
        g.setStroke(new BasicStroke(1));

        String name = (p != null && p.getName() != null) ? p.getName() : "PLAYER";
        int hp = (p != null) ? p.getHp() : 100;
        int maxHp = (p != null && p.getMaxHp() > 0) ? p.getMaxHp() : 100;
        int en = (p != null) ? p.getEnergy() : 50;
        int maxEn = (p != null && p.getMaxEnergy() > 0) ? p.getMaxEnergy() : 50;

        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString(name.toUpperCase(), panelX + 12, panelY + 20);

        double hpRatio = Math.max(0.0, Math.min(1.0, (double) hp / maxHp));
        Color hpColor = (hpRatio > 0.30) ? new Color(16, 185, 129) : new Color(239, 68, 68);
        drawStatBar(g, panelX + 12, panelY + 30, panelW - 24, 16, hpRatio, hpColor, "HP: " + hp + "/" + maxHp);

        double enRatio = Math.max(0.0, Math.min(1.0, (double) en / maxEn));
        drawStatBar(g, panelX + 12, panelY + 56, panelW - 24, 16, enRatio, new Color(6, 182, 212), "EN: " + en + "/" + maxEn);
    }

    private void drawEnemySection(Graphics2D g, Enemy e) {
        int ex = 560;
        int ey = 180;

        if (e != null) {
            Graphics2D gEnemy = (Graphics2D) g.create();
            gEnemy.translate(ex, ey);
            gEnemy.scale(1.6, 1.6);
            gEnemy.translate(-e.getWorldX(), -e.getWorldY());
            e.render(gEnemy);
            gEnemy.dispose();
        }

        int panelX = 490;
        int panelY = 75;
        int panelW = 230;
        int panelH = 66;

        g.setColor(new Color(24, 12, 18, 230));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 8, 8);
        g.setColor(new Color(239, 68, 68));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(panelX, panelY, panelW, panelH, 8, 8);
        g.setStroke(new BasicStroke(1));

        String name = (e != null && e.getName() != null) ? e.getName() : "HOSTILE UNIT";
        int hp = (e != null) ? e.getHp() : 50;
        int maxHp = (e != null && e.getMaxHp() > 0) ? e.getMaxHp() : 50;

        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(new Color(255, 180, 180));
        g.drawString(name.toUpperCase(), panelX + 12, panelY + 20);

        double hpRatio = Math.max(0.0, Math.min(1.0, (double) hp / maxHp));
        drawStatBar(g, panelX + 12, panelY + 32, panelW - 24, 18, hpRatio, new Color(220, 38, 38), "HP: " + hp + "/" + maxHp);
    }

    private void drawBattleLogBox(Graphics2D g, CombatManager cm) {
        int boxX = 20;
        int boxY = 360;
        int boxW = 760;
        int boxH = 70;

        g.setColor(new Color(10, 15, 28, 240));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 8, 8);
        g.setColor(new Color(0, 210, 230));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 8, 8);
        g.setStroke(new BasicStroke(1));

        String log = (cm != null && cm.getBattleLog() != null)
            ? cm.getBattleLog()
            : "Menunggu perintah taktis...";

        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.setColor(new Color(235, 245, 255));
        List<String> lines = wrapText(log, g.getFontMetrics(), boxW - 32);
        for (int i = 0; i < Math.min(lines.size(), 2); i++) {
            g.drawString("> " + lines.get(i), boxX + 16, boxY + 26 + (i * 22));
        }
    }

    private void drawActionCards(Graphics2D g, Player p) {
        int startX = 20;
        int y = 445;
        int cardW = 181;
        int cardH = 135;
        int gap = 12;

        boolean hasEnoughEnergy = (p == null || p.getEnergy() >= 20);

        String[] titles = {
            "[1] BASIC ATTACK",
            "[2] TECH SKILL",
            "[3] DEFEND",
            "[4] USE ITEM"
        };
        String[] costs = {
            "Cost: 0 EN (+5 EN)",
            "Cost: 20 EN (Armor Pierce)",
            "Cost: 0 EN (+15 EN)",
            "Cost: 0 EN (Consume)"
        };
        String[] descs = {
            "Serangan standar Stun Baton.",
            "Overload Strike 1.6x ATK.",
            "Kurangi 50% damage musuh.",
            "Medkit / Battery / EMP."
        };
        Color[] accents = {
            new Color(0, 240, 255),
            hasEnoughEnergy ? new Color(250, 204, 21) : new Color(100, 116, 139),
            new Color(52, 211, 153),
            new Color(192, 132, 252)
        };

        for (int i = 0; i < 4; i++) {
            int cx = startX + i * (cardW + gap);

            g.setColor(new Color(15, 23, 42, 245));
            g.fillRoundRect(cx, y, cardW, cardH, 10, 10);

            g.setColor(accents[i]);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(cx, y, cardW, cardH, 10, 10);
            g.setStroke(new BasicStroke(1));

            g.setFont(new Font("Monospaced", Font.BOLD, 14));
            g.setColor(accents[i]);
            g.drawString(titles[i], cx + 10, y + 26);

            g.setFont(new Font("Monospaced", Font.BOLD, 11));
            g.setColor(Color.WHITE);
            g.drawString(costs[i], cx + 10, y + 48);

            g.setColor(new Color(71, 85, 105));
            g.drawLine(cx + 10, y + 58, cx + cardW - 10, y + 58);

            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g.setColor(new Color(180, 200, 220));
            List<String> descLines = wrapText(descs[i], g.getFontMetrics(), cardW - 20);
            for (int lineIdx = 0; lineIdx < Math.min(descLines.size(), 3); lineIdx++) {
                g.drawString(descLines.get(lineIdx), cx + 10, y + 78 + (lineIdx * 16));
            }
        }
    }

    private void drawStatBar(Graphics2D g, int x, int y, int w, int h, double ratio, Color fill, String label) {
        g.setColor(new Color(30, 41, 59));
        g.fillRect(x, y, w, h);

        int fillW = (int) (w * ratio);
        g.setColor(fill);
        g.fillRect(x, y, fillW, h);

        g.setColor(new Color(148, 163, 184));
        g.drawRect(x, y, w, h);

        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(Color.WHITE);
        g.drawString(label, x + 6, y + h - 4);
    }

    private List<String> wrapText(String rawText, FontMetrics fm, int maxWidth) {
        List<String> result = new ArrayList<>();
        if (rawText == null || rawText.isEmpty()) return result;

        String[] words = rawText.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) result.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) result.add(currentLine.toString());
        return result;
    }
}