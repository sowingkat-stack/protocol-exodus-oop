package com.exodus.entities;

import com.exodus.combat.Item;
import com.exodus.engine.GamePanel;
import com.exodus.world.StoryState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

public class Chest extends Entity implements Interactable {
    private final boolean weaponVault;
    private final Item rewardItem;
    private final StoryState story;
    private boolean opened;

    // Konstruktor untuk Peti Suplai Biasa
    public Chest(String name, int col, int row, Item rewardItem) {
        super(name, col, row, 0, 1, 0, 999);
        this.weaponVault = false;
        this.rewardItem = Objects.requireNonNull(rewardItem, "Reward item tidak boleh null.");
        this.story = null;
        this.opened = false;
    }

    // Konstruktor untuk Brankas Senjata di Ruang Server
    public Chest(String name, int col, int row, StoryState story) {
        super(name, col, row, 0, 1, 0, 999);
        this.weaponVault = true;
        this.rewardItem = null;
        this.story = Objects.requireNonNull(story, "StoryState tidak boleh null.");
        this.opened = false;
    }

    public boolean isWeaponVault() {
        return weaponVault;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    @Override
    public void onInteract(Player player, GamePanel gp) {
        if (opened) {
            gp.openDialogue(
                getName(),
                weaponVault ? "Brankas senjata ini sudah kosong." : "Peti suplai ini sudah dibuka dan kosong.",
                "Tutup",
                "Selesai",
                choice -> gp.setGameState(GamePanel.GameState.EXPLORE)
            );
            return;
        }

        if (weaponVault) {
            gp.openDialogue(
                "Brankas Senjata",
                "Terdapat prototipe Plasma Rifle (+18 ATK) & Master Keycard di balik kaca pengaman. Bobol paksa?",
                "[1] Bobol Brankas",
                "[2] Biarkan",
                choice -> {
                    if (choice == 1) {
                        opened = true;
                        story.setTookPlasmaRifle(true);
                        story.setHasKeycard(true);
                        player.addAtk(18);
                        gp.openDialogue(
                            "Sistem",
                            "Brankas terbuka! Kamu memperoleh Plasma Rifle (+18 ATK) dan Master Keycard!",
                            "Lanjutkan",
                            "Tutup",
                            c -> gp.setGameState(GamePanel.GameState.EXPLORE)
                        );
                    } else {
                        gp.setGameState(GamePanel.GameState.EXPLORE);
                    }
                }
            );
        } else {
            opened = true;
            player.addItem(rewardItem);
            gp.openDialogue(
                getName(),
                "Kamu membuka peti suplai dan mendapatkan: " + rewardItem.getDisplayName() + "!",
                "Ambil",
                "Tutup",
                choice -> gp.setGameState(GamePanel.GameState.EXPLORE)
            );
        }
    }

    @Override
    public void update() {
        // Peti bersifat statis
    }

    @Override
    public void render(Graphics2D g) {
        int x = getWorldX();
        int y = getWorldY();
        int s = 3; // Skala 16x16 -> 48x48[cite: 1]

        Color shadowColor = new Color(10, 12, 18, 150);
        Color bodyDark = weaponVault ? new Color(42, 46, 58) : new Color(58, 68, 84);
        Color bodyMain = weaponVault ? new Color(74, 80, 98) : new Color(96, 112, 134);
        Color trimColor = weaponVault ? new Color(218, 165, 32) : new Color(156, 174, 196);
        Color latchGlow = opened
            ? new Color(70, 75, 85)
            : (weaponVault ? new Color(255, 75, 75) : new Color(0, 230, 180));

        // 1. Bayangan bawah
        g.setColor(shadowColor);
        g.fillRect(x + 2 * s, y + 13 * s, 12 * s, 2 * s);

        // 2. Badan peti / brankas
        g.setColor(bodyDark);
        g.fillRect(x + 2 * s, y + 4 * s, 12 * s, 10 * s);
        g.setColor(bodyMain);
        g.fillRect(x + 3 * s, y + 5 * s, 10 * s, 8 * s);

        // 3. Lis pinggir logam / emas
        g.setColor(trimColor);
        g.fillRect(x + 2 * s, y + 4 * s, 12 * s, 1 * s);
        g.fillRect(x + 2 * s, y + 8 * s, 12 * s, 1 * s);
        g.fillRect(x + 2 * s, y + 13 * s, 12 * s, 1 * s);

        // 4. Kunci indikator (mati jika sudah dibuka)
        g.setColor(latchGlow);
        g.fillRect(x + 7 * s, y + 7 * s, 2 * s, 3 * s);
    }
}