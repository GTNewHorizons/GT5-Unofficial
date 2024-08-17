package gregtech.api.objects;

public class ArtificialOrganism {

    private int intelligence;
    private int strength;
    private int reproduction;

    private int count;
    private int sentience;

    public ArtificialOrganism(int intelligence, int strength, int reproduction) {
        this.intelligence = intelligence;
        this.strength = strength;
        this.reproduction = reproduction;
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

    @Override
    public String toString() {
        return "Intelligence " + intelligence
            + "\nStrength: "
            + strength
            + "\nReproduction: "
            + reproduction
            + "\nCount: "
            + count;
    }
}
