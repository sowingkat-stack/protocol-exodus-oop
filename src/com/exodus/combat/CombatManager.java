package com.exodus.combat;

import com.exodus.entities.Player;
import com.exodus.entities.enemies.Enemy;

import java.util.Objects;

public class CombatManager {
    private static final int PLASMA_STRIKE_COST = 15;
    private static final int MEDKIT_HEAL = 45;
    private static final int BATTERY_RESTORE = 35;
    private static final int EMP_STUN_TURNS = 1;

    private Player player;
    private Enemy enemy;
    private String battleLog;
    private boolean battleOver;
    private boolean playerVictory;

    public CombatManager() {
        this.battleLog = "";
        this.battleOver = false;
        this.playerVictory = false;
    }

    public void initBattle(Player player, Enemy enemy) {
        this.player = Objects.requireNonNull(player, "Player tidak boleh null.");
        this.enemy = Objects.requireNonNull(enemy, "Enemy tidak boleh null.");
        this.battleOver = false;
        this.playerVictory = false;
        this.battleLog = "Ancaman terdeteksi: " + enemy.getName()
            + " (HP: " + enemy.getHp() + "/" + enemy.getMaxHp() + ")!";
    }

    public void executePlayerAction(int choice) {
        if (battleOver || player == null || enemy == null) {
            return;
        }

        boolean playerDefending = false;
        StringBuilder logBuilder = new StringBuilder();

        switch (choice) {
            case 1 -> {
                // [1] Basic Attack
                int enemyHpBefore = enemy.getHp();
                enemy.takeDamage(player.getAtk());
                int dealt = enemyHpBefore - enemy.getHp();
                if (dealt == 0) {
                    logBuilder.append(player.getName()).append(" menyerang, namun ")
                              .append(enemy.getName()).append(" berhasil menghindar!");
                } else {
                    logBuilder.append(player.getName()).append(" menembak ")
                              .append(enemy.getName()).append(" (-").append(dealt).append(" HP).");
                }
            }
            case 2 -> {
                // [2] Overload Blast (15 Energy -> 1.8x ATK + kurangi 15% DEF musuh)
                if (!player.useEnergy(PLASMA_STRIKE_COST)) {
                    this.battleLog = "Energy tidak cukup! Butuh " + PLASMA_STRIKE_COST + " EN untuk Overload Blast.";
                    return;
                }
                int enemyHpBefore = enemy.getHp();
                int overloadRaw = (int) Math.round(player.getAtk() * 1.8);
                enemy.takeDamage(overloadRaw);
                int dealt = enemyHpBefore - enemy.getHp();
                enemy.reduceDefPercent(0.15);

                logBuilder.append(player.getName()).append(" melepaskan OVERLOAD BLAST (-")
                          .append(PLASMA_STRIKE_COST).append(" EN)! ");
                if (dealt == 0) {
                    logBuilder.append(enemy.getName()).append(" menghindar dari ledakan!");
                } else {
                    logBuilder.append(enemy.getName()).append(" menerima -").append(dealt)
                              .append(" HP & DEF turun 15%!");
                }
            }
            case 3 -> {
                // [3] Defend (kurangi damage masuk 50% & pulihkan +8 EN)
                playerDefending = true;
                player.restoreEnergy(8);
                logBuilder.append(player.getName()).append(" mengaktifkan perisai taktis (DMG -50%, +8 EN).");
            }
            case 4 -> {
                // [4] Gunakan Item Taktis Otomatis Sesuai Kebutuhan
                String itemMsg = useBestAvailableItem();
                if (itemMsg == null) {
                    this.battleLog = "Tidak ada item yang dapat digunakan di inventaris!";
                    return;
                }
                logBuilder.append(itemMsg);
            }
            default -> {
                this.battleLog = "Pilihan aksi tidak valid! Pilih 1 - 4.";
                return;
            }
        }

        // Cek apakah musuh tumbang setelah aksi pemain
        if (!enemy.isAlive()) {
            battleOver = true;
            playerVictory = true;
            logBuilder.append(" | ").append(enemy.getName()).append(" berhasil dilumpuhkan!");
            this.battleLog = logBuilder.toString();
            return;
        }

        // Giliran Musuh
        String enemyActionLog = enemy.executeTurn(player, playerDefending);
        logBuilder.append(" | ").append(enemyActionLog);

        // Cek apakah pemain tumbang setelah serangan musuh
        if (!player.isAlive()) {
            battleOver = true;
            playerVictory = false;
            logBuilder.append(" | ").append(player.getName()).append(" tumbang dalam misi!");
        }

        this.battleLog = logBuilder.toString();
    }

    private String useBestAvailableItem() {
        // Prioritas 1: Medkit jika HP <= 65%
        if (player.getHp() <= player.getMaxHp() * 0.65 && player.hasItem(Item.MEDKIT)) {
            player.removeItem(Item.MEDKIT);
            int hpBefore = player.getHp();
            player.heal(MEDKIT_HEAL);
            int healed = player.getHp() - hpBefore;
            return player.getName() + " menggunakan Medkit (+" + healed + " HP).";
        }

        // Prioritas 2: EMP Grenade jika musuh belum terkena stun
        if (!enemy.isStunned() && player.hasItem(Item.EMP_GRENADE)) {
            player.removeItem(Item.EMP_GRENADE);
            enemy.applyStun(EMP_STUN_TURNS);
            return player.getName() + " melempar EMP Grenade! " + enemy.getName() + " lumpuh 1 giliran!";
        }

        // Prioritas 3: Battery Cell jika Energy <= 50%
        if (player.getEnergy() <= player.getMaxEnergy() * 0.5 && player.hasItem(Item.BATTERY_CELL)) {
            player.removeItem(Item.BATTERY_CELL);
            int enBefore = player.getEnergy();
            player.restoreEnergy(BATTERY_RESTORE);
            int restored = player.getEnergy() - enBefore;
            return player.getName() + " menggunakan Battery Cell (+" + restored + " EN).";
        }

        // Fallback: gunakan item apa pun yang tersisa di inventaris
        if (player.hasItem(Item.MEDKIT)) {
            player.removeItem(Item.MEDKIT);
            int hpBefore = player.getHp();
            player.heal(MEDKIT_HEAL);
            return player.getName() + " menggunakan Medkit (+" + (player.getHp() - hpBefore) + " HP).";
        }
        if (player.hasItem(Item.BATTERY_CELL)) {
            player.removeItem(Item.BATTERY_CELL);
            int enBefore = player.getEnergy();
            player.restoreEnergy(BATTERY_RESTORE);
            return player.getName() + " menggunakan Battery Cell (+" + (player.getEnergy() - enBefore) + " EN).";
        }
        if (player.hasItem(Item.EMP_GRENADE)) {
            player.removeItem(Item.EMP_GRENADE);
            enemy.applyStun(EMP_STUN_TURNS);
            return player.getName() + " melempar EMP Grenade! " + enemy.getName() + " lumpuh 1 giliran!";
        }

        return null;
    }

    public String getBattleLog() {
        return battleLog;
    }

    public boolean isBattleOver() {
        return battleOver;
    }

    public boolean isPlayerVictory() {
        return playerVictory;
    }
}