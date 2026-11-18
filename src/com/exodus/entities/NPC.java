package com.exodus.entities;

import com.exodus.combat.Item;
import com.exodus.main.GamePanel;
import com.exodus.world.StoryState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

public class NPC extends Entity implements Interactable {
    private final StoryState story;

    public NPC(int col, int row, StoryState story) {
        super("Dr. Aris", col, row, 0, 60, 0, 5);
        this.story = Objects.requireNonNull(story, "StoryState tidak boleh null.");
    }

    public NPC(String name, int col, int row, StoryState story) {
        super(name, col, row, 0, 60, 0, 5);
        this.story = Objects.requireNonNull(story, "StoryState tidak boleh null.");
    }

    @Override
    public void onInteract(Player player, GamePanel gp) {
        if (story.isDrArisSaved()) {
            gp.openDialogue(
                getName(),
                "Terima kasih, Agen! Gunakan Keycard dariku untuk membuka pintu inti dan hentikan T-900.",
                "Siap, Dok",
                "Tutup",
                choice -> gp.setGameState(GamePanel.GameState.EXPLORE)
            );
            return;
        }

        gp.openDialogue(
            getName(),
            "Uhuk... Aku terluka parah saat serangan android. Punya 1 Medkit? Akan kuberikan Master Keycard milikku.",
            "[1] Berikan 1 Medkit",
            "[2] Tinggalkan",
            choice -> {
                if (choice == 1) {
                    if (player.hasItem(Item.MEDKIT)) {
                        player.removeItem(Item.MEDKIT);
                        story.setDrArisSaved(true);
                        story.setHasKeycard(true);
                        gp.openDialogue(
                            getName(),
                            "Luka ini jauh membaik! Ini Master Keycard akses ruang kontrol. Berhati-hatilah!",
                            "Terima Keycard",
                            "Lanjutkan",
                            c -> gp.setGameState(GamePanel.GameState.EXPLORE)
                        );
                    } else {
                        gp.openDialogue(
                            "Sistem",
                            "Kamu tidak memiliki Medkit di inventaris! Cari Medkit di peti suplai terlebih dahulu.",
                            "Kembali",
                            "Tutup",
                            c -> gp.setGameState(GamePanel.GameState.EXPLORE)
                        );
                    }
                } else {
                    gp.setGameState(GamePanel.GameState.EXPLORE);
                }
            }
        );
    }

    @Override
    public void update() {
        // NPC diam di Ruang Server
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala 16x16 -> 48x48[cite: 1]

        Color shadowColor = new Color(10, 14, 20, 140);
        Color hairGray = new Color(185, 192, 204);
        Color skinTone = new Color(232, 188, 152);
        Color glassesCyan = new Color(90, 210, 235);
        Color labCoatWhite = new Color(235, 240, 246);
        Color labCoatShade = new Color(195, 205, 218);
        Color shirtBlue = new Color(48, 92, 145);
        Color pantsDark = new Color(36, 44, 58);

        // 1. Bayangan kaki
        g.setColor(shadowColor);
        g.fillRect(x + 3 * s, y + 14 * s, 10 * s, 2 * s);

        // 2. Celana & sepatu
        g.setColor(pantsDark);
        g.fillRect(x + 5 * s, y + 12 * s, 2 * s, 3 * s);
        g.fillRect(x + 9 * s, y + 12 * s, 2 * s, 3 * s);

        // 3. Jas lab putih ilmuwan
        g.setColor(labCoatWhite);
        g.fillRect(x + 3 * s, y + 7 * s, 10 * s, 5 * s);
        g.setColor(labCoatShade);
        g.fillRect(x + 2 * s, y + 7 * s, 2 * s, 4 * s);
        g.fillRect(x + 12 * s, y + 7 * s, 2 * s, 4 * s);

        // Kemeja dalam & ID Card
        g.setColor(shirtBlue);
        g.fillRect(x + 7 * s, y + 7 * s, 2 * s, 4 * s);
        g.setColor(new Color(0, 220, 255));
        g.fillRect(x + 10 * s, y + 8 * s, 1 * s, 2 * s);

        // 4. Kepala, rambut beruban & kacamata
        g.setColor(skinTone);
        g.fillRect(x + 4 * s, y + 2 * s, 8 * s, 5 * s);
        g.setColor(hairGray);
        g.fillRect(x + 4 * s, y + 1 * s, 8 * s, 2 * s);
        g.fillRect(x + 4 * s, y + 2 * s, 1 * s, 3 * s);
        g.fillRect(x + 11 * s, y + 2 * s, 1 * s, 3 * s);

        // Kacamata ilmuwan
        g.setColor(glassesCyan);
        g.fillRect(x + 5 * s, y + 4 * s, 2 * s, 1 * s);
        g.fillRect(x + 9 * s, y + 4 * s, 2 * s, 1 * s);
    }
}