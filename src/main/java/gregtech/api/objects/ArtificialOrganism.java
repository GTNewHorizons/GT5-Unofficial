package gregtech.api.objects;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import gregtech.api.enums.ItemList;

public class ArtificialOrganism {

    private int intelligence;
    private int strength;
    private int reproduction;

    private int count;
    private int sentience;

    // Feature traits
    public boolean photosynthetic;
    public boolean hiveMind;
    public boolean laborer;
    public boolean cooperative;
    public boolean decaying;
    public boolean genius;
    public boolean cancerous;
    public boolean immortal;

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

    /**
     * Simulate one cycle of AO reproduction.
     */
    public void doReproduction() {
        if (immortal || decaying) return;
        replenishAOs((count / 10) * reproduction);
    }

    /**
     * Kill all AOs.
     */
    public void purgeAOs() {
        count = 0;
    }

    private final Random rng = new Random();

    /**
     * Calculates the default speed bonus given to AO Units based on sentience, strength, and traits.
     * Pass this directly into setSpeedBonus() on the multiblock.
     */
    public float calculateSpeedBonus() {
        float speedBonus = 1;

        // TODO: some increase based on strength

        // At this threshold, AOs have a chance to slow down recipes.
        if (sentience > 5) {
            if (rng.nextInt(10) == 0) {
                speedBonus = 2;
            }
        }

        return speedBonus;
    }

    public void increaseSentience(int amount) {
        sentience += amount;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getStrength() {
        return strength;
    }

    public int getSentience() {
        return sentience;
    }

    /**
     * Returns current number of AOs, used in recipe validation
     */
    public int getCount() {
        return count;
    }

    public void addTrait(Trait trait) {
        intelligence += trait.baseInt;
        strength += trait.baseStr;
        reproduction += trait.baseRep;

        switch (trait) {
            case Photosynthetic -> photosynthetic = true;
            case HiveMind -> hiveMind = true;
            case Laborer -> laborer = true;
            case Cooperative -> cooperative = true;
            case Decaying -> decaying = true;
            case Genius -> genius = true;
            case Cancerous -> cancerous = true;
            case Immortal -> immortal = true;
        }
    }

    public void finalize(int maxAOs) {
        if (decaying || immortal) count = maxAOs;
        else count = 50;
    }

    /**
     * Default AO
     */
    public ArtificialOrganism() {
        intelligence = 0;
        strength = 0;
        reproduction = 0;
        count = 0;
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

    public enum Trait {

        Photosynthetic(ItemList.IC2_Plantball.getItem(), 6, 3, 1, 1),
        HiveMind(Item.getItemFromBlock(Blocks.red_mushroom), 5, 5, 5, 2),
        Laborer(Items.beef, 3, 8, 5, 3),
        Cooperative(Items.poisonous_potato, 5, 5, 5, 4),
        Decaying(Items.rotten_flesh, 10, 10, 10, 5),
        Genius(Items.poisonous_potato, 5, 5, 5, 6),
        Cancerous(Items.poisonous_potato, 5, 5, 5, 7),
        Immortal(Items.nether_star, 10, 10, 10, 10);

        public final Item cultureItem;
        public final int baseInt, baseStr, baseRep;
        public final int id;

        Trait(Item cultureItem, int baseInt, int baseStr, int baseRep, int id) {
            this.cultureItem = cultureItem;
            this.baseInt = baseInt;
            this.baseStr = baseStr;
            this.baseRep = baseRep;
            this.id = id;
        }
    }
}
