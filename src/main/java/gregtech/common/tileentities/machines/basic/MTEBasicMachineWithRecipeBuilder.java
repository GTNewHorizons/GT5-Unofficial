package gregtech.common.tileentities.machines.basic;

import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe.SpecialEffects;
import gregtech.api.recipe.RecipeMap;

public final class MTEBasicMachineWithRecipeBuilder {

    private MTEBasicMachineWithRecipeBuilder() {}

    public static NameStep builder(int id) {
        return new Builder(id);
    }

    private static final class Builder
        implements NameStep, TierStep, DescriptionStep, RecipesStep, SlotsStep, SoundStep, OverlaysStep, OptionalStep {

        private final int id;
        private String unlocalizedName;
        private String englishName;
        private int tier;
        private String[] description;
        private RecipeMap<?> recipes;
        private int inputSlotCount;
        private int outputSlotCount;
        private SoundResource sound;
        private String overlays;
        private boolean hasInputFluidSlot = false;
        private boolean hasOutputFluidSlot = false;
        private int fluidTankCapacityOverride = 0;
        private int machineAmperageOverride = 0;
        private int machineEUtMultiplier = 1;
        private SpecialEffects specialEffect = SpecialEffects.NONE;

        public Builder(int id) {
            this.id = id;
        }

        @Override
        public TierStep setName(String unlocalizedName, String englishName) {
            this.unlocalizedName = unlocalizedName;
            this.englishName = englishName;
            return this;
        }

        @Override
        public TierStep setName(String unlocalizedName) {
            this.unlocalizedName = unlocalizedName;
            this.englishName = StatCollector.translateToFallback(unlocalizedName);
            return this;
        }

        @Override
        public DescriptionStep setTier(int tier) {
            this.tier = tier;
            return this;
        }

        @Override
        public RecipesStep setDescription(String[] descriptions) {
            this.description = descriptions;
            return this;
        }

        @Override
        public SlotsStep setRecipes(RecipeMap<?> recipes) {
            this.recipes = recipes;
            return this;
        }

        @Override
        public SoundStep setSlotsCount(int inputSlotCount, int outputSlotCount) {
            this.inputSlotCount = inputSlotCount;
            this.outputSlotCount = outputSlotCount;
            return this;
        }

        @Override
        public OverlaysStep setSound(SoundResource sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public OptionalStep setOverlays(String overlays) {
            this.overlays = overlays;
            return this;
        }

        @Override
        public OptionalStep setFluidSlots(boolean hasInput, boolean hasOutput) {
            this.hasInputFluidSlot = hasInput;
            this.hasOutputFluidSlot = hasOutput;
            this.fluidTankCapacityOverride = 0;
            return this;
        }

        @Override
        public OptionalStep setFluidSlots(boolean hasInput, boolean hasOutput, int capacityOverride) {
            this.hasInputFluidSlot = hasInput;
            this.hasOutputFluidSlot = hasOutput;
            this.fluidTankCapacityOverride = capacityOverride;
            return this;
        }

        @Override
        public OptionalStep setSpecialEffect(SpecialEffects specialEffect) {
            this.specialEffect = specialEffect;
            return this;
        }

        @Override
        public OptionalStep setMachineAmperage(int amperage) {
            if (amperage < 1) throw new IllegalArgumentException("Machine amperage must be at least 1");
            this.machineAmperageOverride = amperage;
            return this;
        }

        @Override
        public OptionalStep setMachineEUtMultiplier(int multiplier) {
            if (multiplier < 1) throw new IllegalArgumentException("Machine EU/t multiplier must be at least 1");
            this.machineEUtMultiplier = multiplier;
            return this;
        }

        @Override
        public MTEBasicMachineWithRecipe build() {
            String[] finalDescription = description;
            int extraDescriptionLines = (machineAmperageOverride != 0 ? 1 : 0) + (machineEUtMultiplier != 1 ? 1 : 0);
            if (extraDescriptionLines > 0) {
                finalDescription = Arrays.copyOf(description, description.length + extraDescriptionLines);
                int descriptionIndex = description.length;
                if (machineEUtMultiplier != 1) {
                    finalDescription[descriptionIndex++] = EnumChatFormatting.GRAY
                        + StatCollector.translateToLocal("GT5U.MBTT.PowerUsage")
                        + ": "
                        + EnumChatFormatting.RED
                        + (machineEUtMultiplier * 100)
                        + "%"
                        + EnumChatFormatting.RESET;
                }
                if (machineAmperageOverride != 0) {
                    finalDescription[descriptionIndex] = EnumChatFormatting.GRAY
                        + StatCollector.translateToLocal("GT5U.MBTT.BaseAmperage")
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + machineAmperageOverride
                        + "A"
                        + EnumChatFormatting.RESET;
                }
            }
            MTEBasicMachineWithRecipe machine;
            if (fluidTankCapacityOverride == 0) {
                machine = new MTEBasicMachineWithRecipe(
                    id,
                    unlocalizedName,
                    englishName,
                    tier,
                    finalDescription,
                    recipes,
                    inputSlotCount,
                    outputSlotCount,
                    hasInputFluidSlot,
                    hasOutputFluidSlot,
                    sound,
                    specialEffect,
                    overlays);
            } else {
                machine = new MTEBasicMachineWithRecipe(
                    id,
                    unlocalizedName,
                    englishName,
                    tier,
                    finalDescription,
                    recipes,
                    inputSlotCount,
                    outputSlotCount,
                    hasInputFluidSlot,
                    hasOutputFluidSlot,
                    fluidTankCapacityOverride,
                    sound,
                    specialEffect,
                    overlays);
            }
            if (machineAmperageOverride != 0) {
                machine.setMachineAmperage(machineAmperageOverride);
            }
            return machine.setMachineEUtMultiplier(machineEUtMultiplier);
        }
    }

    public interface NameStep {

        TierStep setName(String unlocalizedName, String englishName);

        TierStep setName(String unlocalizedName);
    }

    public interface TierStep {

        DescriptionStep setTier(int tier);
    }

    public interface DescriptionStep {

        RecipesStep setDescription(String[] descriptions);
    }

    public interface RecipesStep {

        SlotsStep setRecipes(RecipeMap<?> recipes);
    }

    public interface SlotsStep {

        SoundStep setSlotsCount(int inputSlotCount, int outputSlotCount);
    }

    public interface SoundStep {

        OverlaysStep setSound(SoundResource sound);
    }

    public interface OverlaysStep {

        OptionalStep setOverlays(String overlays);
    }

    public interface OptionalStep {

        OptionalStep setFluidSlots(boolean hasInput, boolean hasOutput);

        OptionalStep setFluidSlots(boolean hasInput, boolean hasOutput, int capacityOverride);

        OptionalStep setSpecialEffect(SpecialEffects specialEffect);

        OptionalStep setMachineAmperage(int amperage);

        OptionalStep setMachineEUtMultiplier(int multiplier);

        MTEBasicMachineWithRecipe build();
    }
}
