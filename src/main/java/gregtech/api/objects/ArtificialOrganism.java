package gregtech.api.objects;

public class ArtificialOrganism {

    private int intelligence;
    private int strength;
    private int reproduction;

    private int count;
    private int sentience;

    private boolean immortal;

    public ArtificialOrganism(int intelligence, int strength, int reproduction) {
        this.intelligence = intelligence;
        this.strength = strength;
        this.reproduction = reproduction;
        count = 500;
        sentience = 0;
    }

    /**
     * Try to use some number of AOs. Returns the number of AOs that were actually consumed, or -1 if the
     * operation should fail.
     */
    public int consumeAOs(int number) {
        if (immortal) return 0;
        if (count - number >= 0) {
            count -= number;
            return number;
        }
        return -1;
    }

    /**
     * Try to restore some number of AOs. Returns the number of AOs that were actually restored, or -1 if the
     * operation should fail.
     */
    public int replenishAOs(int number) {
        count += number;
        return number;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getStrength() {
        return strength;
    }

    /**
     * Returns current number of AOs, used in recipe validation
     */
    public int getCount() {
        return count;
    }

    /**
     * Default AO for testing.
     */
    public ArtificialOrganism() {
        intelligence = 10;
        strength = 10;
        reproduction = 10;
        count = 50;
        sentience = 0;
    }

    public ArtificialOrganism(int intelligence, int strength, int reproduction, int count, int sentience) {
        this.intelligence = intelligence;
        this.strength = strength;
        this.reproduction = reproduction;
        this.count = count;
        this.sentience = sentience;
    }

    @Override
    public String toString() {
        return "Intelligence " + intelligence
            + " Strength: "
            + strength
            + " Reproduction: "
            + reproduction
            + " Count: "
            + count;
    }
}
