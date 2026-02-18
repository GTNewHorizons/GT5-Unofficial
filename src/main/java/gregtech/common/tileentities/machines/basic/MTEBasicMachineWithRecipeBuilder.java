package gregtech.common.tileentities.machines.basic;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.SoundResource;
import gregtech.api.recipe.RecipeMap;
import gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipe.SpecialEffects;

public final class MTEBasicMachineWithRecipeBuilder {

    private MTEBasicMachineWithRecipeBuilder() {}

    public static MTEBasicMachineWithRecipeBuilder builder() {
        return new MTEBasicMachineWithRecipeBuilder();
    }

    private int id;
    private String unlocalizedName;
    private String englishName;
    private int tier;
    private String[] description;
    private RecipeMap<?> recipes;
    private int inputSlotCount;
    private int outputSlotCount;
    private boolean hasInputFluidSlot = false;
    private boolean hasOutputFluidSlot = false;
    private int fluidTankCapacity = 0;
    private SoundResource sound;
    private SpecialEffects specialEffect = SpecialEffects.NONE;
    private String overlays;

    public MTEBasicMachineWithRecipe build() {
        if (fluidTankCapacity == 0) {
            return new MTEBasicMachineWithRecipe(
                id,
                unlocalizedName,
                englishName,
                tier,
                description,
                recipes,
                inputSlotCount,
                outputSlotCount,
                hasInputFluidSlot || hasOutputFluidSlot,
                sound,
                specialEffect,
                overlays);
        }

        return new MTEBasicMachineWithRecipe(
            id,
            unlocalizedName,
            englishName,
            tier,
            description,
            recipes,
            inputSlotCount,
            outputSlotCount,
            fluidTankCapacity,
            sound,
            specialEffect,
            overlays);
    }

    public MTEBasicMachineWithRecipeBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setName(String unlocalizedName, String englishName) {
        this.unlocalizedName = unlocalizedName;
        this.englishName = englishName;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        this.englishName = StatCollector.translateToFallback(unlocalizedName);
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setTier(int tier) {
        this.tier = tier;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setDescription(String description) {
        this.description = new String[] { description };
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setDescription(String[] descriptions) {
        this.description = descriptions;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setRecipes(RecipeMap<?> recipes) {
        this.recipes = recipes;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setSlotsCount(int inputSlotCount, int outputSlotCount) {
        this.inputSlotCount = inputSlotCount;
        this.outputSlotCount = outputSlotCount;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setFluidSlots(boolean hasInput, boolean hasOutput) {
        this.hasInputFluidSlot = hasInput;
        this.hasOutputFluidSlot = hasOutput;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setFluidSlots(boolean hasInput, boolean hasOutput, int capacity) {
        this.hasInputFluidSlot = hasInput;
        this.hasOutputFluidSlot = hasOutput;
        this.fluidTankCapacity = capacity;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setSound(SoundResource sound) {
        this.sound = sound;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setSpecialEffect(SpecialEffects specialEffect) {
        this.specialEffect = specialEffect;
        return this;
    }

    public MTEBasicMachineWithRecipeBuilder setOverlays(String overlays) {
        this.overlays = overlays;
        return this;
    }
}
