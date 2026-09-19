package gregtech.api.objects;

import static gregtech.api.enums.Mods.GregTech;

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
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class ArtificialOrganism {

    private static final int STAT_MAX = 30;

    /** Each level of Reproduction adds base recovery rate by 20% additively. */
    public static final float REPRODUCTION_BONUS_PER_LEVEL = 0.20f;

    /** Each level of Strength adds crafting speed by 10% additively. */
    public static final float STRENGTH_SPEED_PER_LEVEL = 0.10f;

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
    public boolean crystalline;
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
     * Try to restore some number of AOs. Returns the number of AOs that were actually restored.

     */
    public int replenishAOs(int number) {
        number = Math.max(0, Math.min(maxAOs - count, number));
        count += number;
        return number;
    }

    /**
     * Calculates the AO recovery for one maintenance cycle before the population-cap clamp is applied.
     */
    public int calculateReproduction(int baseRegen) {
        if (immortal || decaying) return 0;
        return Math
            .round(baseRegen * (1f + REPRODUCTION_BONUS_PER_LEVEL * reproduction) * (float) getReproductionModifier());
    }

    /**
     * Simulate one cycle of AO reproduction.
     */
    public void doReproduction(int baseRegen) {
        replenishAOs(calculateReproduction(baseRegen));
    }

    /**
     * Simulate one cycle of AO deaths from starvation.
     */
    public void doDeath(int baseDecay) {
        if (immortal || decaying) return;
        count = Math.max(
            0,
            count - Math.round(
                baseDecay * (1f + REPRODUCTION_BONUS_PER_LEVEL * reproduction) * (float) getReproductionModifier()));
    }

    /**
     * Calculate nutrient fluid consumption multiplier of all traits.
     */
    public double getNutritionModifier() {
        double modifier = 1.0;
        for (Trait trait : traits) modifier *= trait.nutritionModifier;
        return modifier;
    }

    /**
     * Calculate maintenance power multiplier of all traits.
     */
    public double getPowerModifier() {
        double modifier = 1.0;
        for (Trait trait : traits) modifier *= trait.powerModifier;
        return modifier;
    }

    /**
     * Calculate reproduction rate multiplier of all traits.
     */
    public double getReproductionModifier() {
        double modifier = 1.0;
        for (Trait trait : traits) modifier *= trait.reproductionModifier;
        return modifier;
    }

    /**
     * Calculate maximum AO capacity multiplier of all traits.
     */
    public double getMaxAOsModifier() {
        double modifier = 1.0;
        for (Trait trait : traits) modifier *= trait.maxAOsModifier;
        return modifier;
    }

    /**
     * Calculate AO discount multiplier of all traits.
     */
    public double getAOConsumptionMultiplier() {
        if (cooperative) return 0.7;
        return 1.0;
    }

    /**
     * Nutrient fluid this organism consumes(defaults to Nutrient Broth).
     */
    public Fluid getNutritionFluid() {
        for (Trait trait : traits) {
            if (trait.nutritionFluid != Materials.NutrientBroth.mFluid) return trait.nutritionFluid;
        }
        return Materials.NutrientBroth.mFluid;
    }

    /**
     * Kill all AOs.
     */
    public void purgeAOs() {
        count = 0;
    }

    /**
     * Calculates the default speed bonus given to AO Units based on strength and traits.
     */
    public float calculateSpeedBonus() {
        float durationModifier = 1f / (1f + STRENGTH_SPEED_PER_LEVEL * strength);
        durationModifier *= 2;

        return durationModifier;
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
        this.maxAOs = Math.max(0, (int) Math.round(maxAOs * getMaxAOsModifier()));
        count = Math.min(count, this.maxAOs);
    }

    public int getMaxAOs() {
        return maxAOs;
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
            case Crystalline -> crystalline = true;
            case Immortal -> immortal = true;
        }
    }

    public void finalize(int maxAOs) {
        setMaxAOs(maxAOs);
        finalized = true;
        if (decaying || immortal) count = this.maxAOs;
        else count = Math.min(50, this.maxAOs);
    }

    /**
     * Wipes the entire population and resets the organism back to a blank, unfinalized state so its traits can be
     * re-selected from GUI. Used when the HMC injects sterilization fluid.
     */
    public void sterilize() {
        intelligence = 0;
        strength = 0;
        reproduction = 0;
        count = 0;
        sentience = 0;
        maxAOs = 0;
        finalized = false;
        traits.clear();
        photosynthetic = false;
        hiveMind = false;
        laborer = false;
        cooperative = false;
        decaying = false;
        genius = false;
        cancerous = false;
        crystalline = false;
        immortal = false;
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
            addTrait(Trait.valueOf(((NBTTagString) t).func_150285_a_()), true);
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
            this.cooperative, this.decaying, this.genius, this.cancerous, this.crystalline, this.immortal };
        boolean[] organismBools = new boolean[] { organism.finalized, organism.photosynthetic, organism.hiveMind,
            organism.laborer, organism.cooperative, organism.decaying, organism.genius, organism.cancerous,
            organism.crystalline, organism.immortal };
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
            crystalline,
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

        Photosynthetic(ItemList.IC2_Plantball.get(1), 5, 5, 0, 1, "GT5U.artificialorganisms.traitname.photosynthetic",
            "GT5U.artificialorganisms.traitdesc.photosynthetic", 0.25, 0.25, 0.25, 1.0, Materials.NutrientBroth.mFluid),
        HiveMind(new ItemStack(Blocks.red_mushroom, 1), 8, 8, 8, 2, "GT5U.artificialorganisms.traitname.hivemind",
            "GT5U.artificialorganisms.traitdesc.hivemind", 1.0, 1.0, 1.0, 1.0, Materials.NeuralFluid.mFluid),
        Laborer(new ItemStack(Items.beef, 1), 8, 0, 5, 3, "GT5U.artificialorganisms.traitname.laborer",
            "GT5U.artificialorganisms.traitdesc.laborer", 1.0, 1.0, 1.0, 8.0, Materials.NutrientBroth.mFluid),
        Cooperative(new ItemStack(Items.diamond_sword, 1), 5, 3, 5, 4, "GT5U.artificialorganisms.traitname.cooperative",
            "GT5U.artificialorganisms.traitdesc.cooperative", 1.0, 1.0, 1.0, 1.0, Materials.NutrientBroth.mFluid),
        Decaying(new ItemStack(Items.rotten_flesh, 1), 10, 8, 0, 5, "GT5U.artificialorganisms.traitname.decaying",
            "GT5U.artificialorganisms.traitdesc.decaying", 1.0, 1.0, 1.0, 1.0, Materials.NutrientBroth.mFluid),
        Genius(ItemList.Neuron_Cell_Cluster.get(1), 10, 2, 1, 6, "GT5U.artificialorganisms.traitname.genius",
            "GT5U.artificialorganisms.traitdesc.genius", 1.0, 1.0, 1.0, 1.0, Materials.NutrientBroth.mFluid),
        Cancerous(new ItemStack(Items.poisonous_potato, 1), 4, 9, 9, 7, "GT5U.artificialorganisms.traitname.cancerous",
            "GT5U.artificialorganisms.traitdesc.cancerous", 2.0, 16.0, 1.0, 1.0, Materials.NutrientBroth.mFluid),
        Crystalline(Materials.MysteriousCrystal.getDust(1), 5, 5, 0, 8,
            "GT5U.artificialorganisms.traitname.crystalline", "GT5U.artificialorganisms.traitdesc.crystalline", 1.0,
            1.0, 1.0, 1.0, Materials.NutrientBroth.mFluid),
        Immortal(new ItemStack(Items.nether_star, 1), 10, 10, 10, 10, "GT5U.artificialorganisms.traitname.immortal",
            "GT5U.artificialorganisms.traitdesc.immortal", 0.0, 0.0, 1.0, 1.0, Materials.NutrientBroth.mFluid);

        public final ItemStack cultureItem;
        public final int baseInt, baseStr, baseRep;
        public final int id;
        public final String nameLocKey, descLocKey;
        public final double powerModifier;
        public final double nutritionModifier;
        public final double reproductionModifier;
        public final double maxAOsModifier;
        public final Fluid nutritionFluid;
        public final UITexture texture;

        Trait(ItemStack cultureItem, int baseInt, int baseStr, int baseRep, int id, String nameLocKey,
            String descLocKey, double powerModifier, double nutritionModifier, double reproductionModifier,
            double maxAOsModifier, Fluid nutritionFluid) {
            this.cultureItem = cultureItem;
            this.baseInt = baseInt;
            this.baseStr = baseStr;
            this.baseRep = baseRep;
            this.id = id;
            this.nameLocKey = nameLocKey;
            this.descLocKey = descLocKey;
            this.powerModifier = powerModifier;
            this.nutritionModifier = nutritionModifier;
            this.reproductionModifier = reproductionModifier;
            this.maxAOsModifier = maxAOsModifier;
            this.nutritionFluid = nutritionFluid;
            this.texture = UITexture.builder()
                .location(GregTech.ID, "gui/picture/artificial_organisms/trait_" + this.id)
                .imageSize(10, 10)
                .build();
        }
    }
}
