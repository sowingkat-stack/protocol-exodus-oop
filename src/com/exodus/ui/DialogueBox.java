package com.exodus.ui;

import com.exodus.engine.KeyHandler;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DialogueBox {

    private static final int BOX_X = 20;
    private static final int BOX_Y = 400;
    private static final int BOX_WIDTH = 760;
    private static final int BOX_HEIGHT = 180;

    private boolean open = false;
    private String speaker = "";
    private String text = "";
    private String opt1 = "";
    private String opt2 = null;
    private Consumer<Integer> callback;

    public void show(String speaker, String text, String opt1, String opt2, Consumer<Integer> cb) {
        this.speaker = (speaker != null) ? speaker : "SYSTEM";
        this.text = (text != null) ? text : "";
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.callback = cb;
        this.open = true;
    }

    public void update(KeyHandler kh) {
        if (!open || kh == null) {
            return;
        }

        int chosenOption = 0;

        if (kh.numberPressed == 1) {
            chosenOption = 1;
        } else if (kh.numberPressed == 2 && opt2 != null) {
            chosenOption = 2;
        } else if (opt2 == null && kh.interactPressed) {
            chosenOption = 1;
        }

        if (chosenOption != 0) {
            kh.consumeKeys();
            this.open = false;
            if (callback != null) {
                callback.accept(chosenOption);
            }
        }
    }

    public void render(Graphics2D g) {
        if (!open) {
            return;
        }

        g.setColor(new Color(10, 15, 28, 235));
        g.fillRoundRect(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, 12, 12);

        g.setColor(new Color(0, 240, 255));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, 12, 12);

        g.setColor(new Color(0, 160, 190, 180));
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(BOX_X + 5, BOX_Y + 5, BOX_WIDTH - 10, BOX_HEIGHT - 10, 8, 8);

        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(new Color(255, 215, 0));
        g.drawString("[ " + speaker.toUpperCase() + " ]", BOX_X + 22, BOX_Y + 30);

        g.setColor(new Color(0, 210, 230, 110));
        g.drawLine(BOX_X + 20, BOX_Y + 38, BOX_X + BOX_WIDTH - 20, BOX_Y + 38);

        g.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g.setColor(new Color(230, 240, 250));
        List<String> lines = wrapText(text, g.getFontMetrics(), BOX_WIDTH - 44);
        int textY = BOX_Y + 62;
        for (int i = 0; i < Math.min(lines.size(), 3); i++) {
            g.drawString(lines.get(i), BOX_X + 22, textY + (i * 22));
        }

        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        if (opt2 == null) {
            String label1 = (opt1 != null && !opt1.isEmpty()) ? opt1 : "Lanjutkan / Tutup";
            g.setColor(new Color(0, 255, 180));
            g.drawString("[1] " + label1 + "  (atau tekan ENTER/E)", BOX_X + 22, BOX_Y + 156);
        } else {
            String label1 = (opt1 != null) ? opt1 : "Opsi 1";
            g.setColor(new Color(0, 255, 180));
            g.drawString("[1] " + label1, BOX_X + 22, BOX_Y + 136);

            g.setColor(new Color(255, 180, 70));
            g.drawString("[2] " + opt2, BOX_X + 22, BOX_Y + 160);
        }
    }

    public boolean isOpen() {
        return open;
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