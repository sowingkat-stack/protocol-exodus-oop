package com.exodus.world;

public class StoryState {
    private boolean hasKeycard;
    private boolean drArisSaved;
    private boolean tookPlasmaRifle;
    private boolean bossDefeated;

    public StoryState() {
        this.hasKeycard = false;
        this.drArisSaved = false;
        this.tookPlasmaRifle = false;
        this.bossDefeated = false;
    }

    public boolean isHasKeycard() {
        return hasKeycard;
    }

    public void setHasKeycard(boolean hasKeycard) {
        this.hasKeycard = hasKeycard;
    }

    public boolean isDrArisSaved() {
        return drArisSaved;
    }

    public void setDrArisSaved(boolean drArisSaved) {
        this.drArisSaved = drArisSaved;
    }

    public boolean isTookPlasmaRifle() {
        return tookPlasmaRifle;
    }

    public void setTookPlasmaRifle(boolean tookPlasmaRifle) {
        this.tookPlasmaRifle = tookPlasmaRifle;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }
}