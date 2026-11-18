package com.exodus.entities.enemies;

import com.exodus.entities.Combatant;
import com.exodus.entities.Entity;
import com.exodus.entities.Player;

public abstract class Enemy extends Entity implements Combatant {
    private int stunTurns;

    public Enemy(String name, int col, int row, int speed, int maxHp, int atk, int def) {
        super(name, col, row, speed, maxHp, atk, def);
        this.stunTurns = 0;
    }

    public int getStunTurns() {
        return stunTurns;
    }

    public void setStunTurns(int stunTurns) {
        this.stunTurns = Math.max(0, stunTurns);
    }

    public void applyStun(int turns) {
        if (turns > 0) {
            this.stunTurns += turns;
        }
    }

    public boolean isStunned() {
        return this.stunTurns > 0;
    }

    protected String consumeStunTurn() {
        if (this.stunTurns > 0) {
            this.stunTurns--;
        }
        return getName() + " lumpuh akibat efek EMP dan melewatkan giliran!";
    }

    public void reduceDefPercent(double percent) {
        if (percent <= 0) {
            return;
        }
        double ratio = percent > 1.0 ? percent / 100.0 : percent;
        int newDef = (int) Math.round(getDef() * (1.0 - Math.min(1.0, ratio)));
        setDef(newDef);
    }

    public void boostAtkPercent(double percent) {
        if (percent <= 0) {
            return;
        }
        double ratio = percent > 1.0 ? percent / 100.0 : percent;
        int newAtk = (int) Math.round(getAtk() * (1.0 + ratio));
        setAtk(newAtk);
    }

    @Override
    public void update() {
        // Default idle behavior untuk enemy statis/patroli
    }

    @Override
    public abstract String executeTurn(Player player, boolean playerDefending);
}