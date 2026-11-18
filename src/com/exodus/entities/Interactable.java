package com.exodus.entities;

import com.exodus.engine.GamePanel;

public interface Interactable {
    void onInteract(Player player, GamePanel gp);
}