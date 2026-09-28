package minipcsimulator.model;

public class SystemClock {
    private int ticks = 0;

    public void tick() {
        this.ticks++;
    }

    public int getTicks() {
        return this.ticks;
    }

    public void reset() {
        this.ticks = 0;
    }
}
