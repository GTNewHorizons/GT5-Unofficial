package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import gregtech.api.enums.ItemList;

public class ArtificialOrganism {

    private static final int STAT_MAX = 30;

    private int intelligence;
    private int strength;
    private int reproduction;

    private int count;
    private int sentience;

    private int maxAOs;

    private boolean finalized = false;

    public final ArrayList<Trait> traits = new ArrayList<>();

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
        number = Math.min(maxAOs - count, number);
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
        if (sentience < 100) {
            sentience = Math.min(100, sentience + amount);
        }
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getStrength() {
        return strength;
    }

    public int getReproduction() {
        return reproduction;
    }

    public int getSentience() {
        return sentience;
    }

    public boolean getFinalized() {
        return finalized;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setReproduction(int reproduction) {
        this.reproduction = reproduction;
    }

    public void setMaxAOs(int maxAOs) {
        this.maxAOs = maxAOs;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setSentience(int sentience) {
        this.sentience = sentience;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    /**
     * Returns current number of AOs, used in recipe validation
     */
    public int getCount() {
        return count;
    }

    public void addTrait(Trait trait) {
        addTrait(trait, false);
    }

    public void addTrait(Trait trait, boolean fromSerializer) {
        if (!fromSerializer) {
            intelligence += Math.min(trait.baseInt, STAT_MAX);
            strength += Math.min(trait.baseStr, STAT_MAX);
            reproduction += Math.min(trait.baseRep, STAT_MAX);
        }

        traits.add(trait);

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
        finalized = true;
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

    public NBTTagCompound saveAOToCompound(NBTTagCompound tag) {
        tag.setInteger("intelligence", intelligence);
        tag.setInteger("strength", strength);
        tag.setInteger("reproduction", reproduction);

        tag.setInteger("count", count);
        tag.setInteger("sentience", sentience);

        tag.setBoolean("finalized", finalized);

        NBTTagList traitList = new NBTTagList();
        for (Trait t : traits) {
            traitList.appendTag(new NBTTagString(t.name()));
        }

        tag.setTag("traitlist", traitList);
        return tag;
    }

    /**
     * Constructor to rebuild AO from an NBT tag
     */
    public ArtificialOrganism(NBTTagCompound tag) {
        intelligence = tag.getInteger("intelligence");
        strength = tag.getInteger("strength");
        reproduction = tag.getInteger("reproduction");

        count = tag.getInteger("count");
        sentience = tag.getInteger("sentience");

        finalized = tag.getBoolean("finalized");

        NBTTagList traitList = tag.getTagList("traitlist", Constants.NBT.TAG_STRING);
        for (Object t : traitList.tagList) {
            traits.add(Trait.valueOf(((NBTTagString) t).func_150285_a_()));
        }
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (!(obj instanceof ArtificialOrganism organism)) return false;

        int[] thisInts = new int[] { this.intelligence, this.strength, this.reproduction, this.count, this.sentience };
        int[] organismInts = new int[] { organism.intelligence, organism.strength, organism.reproduction,
            organism.count, organism.sentience };
        if (!Arrays.equals(thisInts, organismInts)) return false;

        boolean[] thisBools = new boolean[] { this.finalized, this.photosynthetic, this.hiveMind, this.laborer,
            this.cooperative, this.decaying, this.genius, this.cancerous, this.immortal };
        boolean[] organismBools = new boolean[] { organism.finalized, organism.photosynthetic, organism.hiveMind,
            organism.laborer, organism.cooperative, organism.decaying, organism.genius, organism.cancerous,
            organism.immortal };
        return Arrays.equals(thisBools, organismBools);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            intelligence,
            strength,
            reproduction,
            count,
            sentience,
            finalized,
            photosynthetic,
            hiveMind,
            laborer,
            cooperative,
            decaying,
            genius,
            cancerous,
            immortal);
    }

    // I tried to do something more optimized, but it gets really nasty when you try to take into account
    // metaitems. This is not at all a hot path, so it should be fine.
    public static Trait getTraitFromItem(ItemStack item) {
        for (Trait t : Trait.values()) {
            if (t.cultureItem.isItemEqual(item)) return t;
        }
        return null;
    }

    public enum Trait {

        Photosynthetic(ItemList.IC2_Plantball.get(1), 6, 3, 1, 1, "GT5U.artificialorganisms.traitname.photosynthetic",
            "GT5U.artificialorganisms.traitdesc.photosynthetic"),
        HiveMind(new ItemStack(Blocks.red_mushroom, 1), 5, 5, 5, 2, "GT5U.artificialorganisms.traitname.hivemind",
            "GT5U.artificialorganisms.traitdesc.hivemind"),
        Laborer(new ItemStack(Items.beef, 1), 3, 8, 5, 3, "GT5U.artificialorganisms.traitname.laborer",
            "GT5U.artificialorganisms.traitdesc.laborer"),
        Cooperative(new ItemStack(Items.diamond_sword, 1), 5, 5, 5, 4, "GT5U.artificialorganisms.traitname.cooperative",
            "GT5U.artificialorganisms.traitdesc.cooperative"),
        Decaying(new ItemStack(Items.rotten_flesh, 1), 10, 10, 10, 5, "GT5U.artificialorganisms.traitname.decaying",
            "GT5U.artificialorganisms.traitdesc.decaying"),
        Genius(ItemList.Neuron_Cell_Cluster.get(1), 5, 5, 5, 6, "GT5U.artificialorganisms.traitname.genius",
            "GT5U.artificialorganisms.traitdesc.genius"),
        Cancerous(new ItemStack(Items.poisonous_potato, 1), 5, 5, 5, 7, "GT5U.artificialorganisms.traitname.cancerous",
            "GT5U.artificialorganisms.traitdesc.cancerous"),
        Immortal(new ItemStack(Items.nether_star, 1), 10, 10, 10, 10, "GT5U.artificialorganisms.traitname.immortal",
            "GT5U.artificialorganisms.traitdesc.immortal");

        public final ItemStack cultureItem;
        public final int baseInt, baseStr, baseRep;
        public final int id;
        public final String nameLocKey, descLocKey;

        Trait(ItemStack cultureItem, int baseInt, int baseStr, int baseRep, int id, String nameLocKey,
            String descLocKey) {
            this.cultureItem = cultureItem;
            this.baseInt = baseInt;
            this.baseStr = baseStr;
            this.baseRep = baseRep;
            this.id = id;
            this.nameLocKey = nameLocKey;
            this.descLocKey = descLocKey;
        }
    }
}
