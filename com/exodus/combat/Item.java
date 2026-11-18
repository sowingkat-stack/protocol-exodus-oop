package com.exodus.combat;

public enum Item {
    MEDKIT("Medkit (+45 HP)"),
    BATTERY_CELL("Battery Cell (+35 EN)"),
    EMP_GRENADE("EMP Grenade (Stun 1 Turn)");

    private final String displayName;

    Item(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}