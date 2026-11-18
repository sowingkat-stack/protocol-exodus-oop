package com.exodus.entities;

public interface Combatant {
    String executeTurn(Player player, boolean playerDefending);
    void takeDamage(int rawDamage);
    boolean isAlive();
}