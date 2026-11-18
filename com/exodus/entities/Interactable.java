package com.exodus.entities;

import com.exodus.main.GamePanel;

public interface Interactable {
    void onInteract(Player player, GamePanel gp);
}