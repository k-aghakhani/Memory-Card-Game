package com.aghakhani.memorycardgame;

public class MemoryCard {
    private int imageId;
    private boolean isFlipped;
    private boolean isMatched;

    public MemoryCard(int imageId) {
        this.imageId = imageId;
        this.isFlipped = false;
        this.isMatched = false;
    }

    public int getImageId() { return imageId; }
    public boolean isFlipped() { return isFlipped; }
    public void setFlipped(boolean flipped) { isFlipped = flipped; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) { isMatched = matched; }
}
