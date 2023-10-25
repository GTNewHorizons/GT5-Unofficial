package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MASSFAB;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_MASSFAB;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_MASSFAB;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_MASSFAB_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_MASSFAB_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_MASSFAB_GLOW;

import java.util.Arrays;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraftforge.fluids.FluidStack;

import com.google.common.primitives.Ints;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.overclockdescriber.EUOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

public class GT_MetaTileEntity_Massfabricator extends GT_MetaTileEntity_BasicMachine {

    public static int sUUAperUUM = 1;
    public static int sUUASpeedBonus = 4;
    public static int sDurationMultiplier = 3215;
    public static boolean sRequiresUUA = false;
    public static int BASE_EUT = 256;
    public static GT_Recipe nonUUARecipe;
    public static GT_Recipe uuaRecipe;

    public GT_MetaTileEntity_Massfabricator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            8,
            MachineType.MATTER_FABRICATOR.tooltipDescription(),
            1,
            1,
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_MASSFAB),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_MASSFAB_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_MASSFAB),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MASSFAB_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_MASSFAB),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_MASSFAB_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_MASSFAB),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_MASSFAB_GLOW)
                    .glow()
                    .build()));
    }

    public GT_MetaTileEntity_Massfabricator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Massfabricator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    protected OverclockDescriber createOverclockDescriber() {
        return new MassfabricatorOverclockDescriber(mTier, mAmperage);
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        super.onConfigLoad(aConfig);
        sDurationMultiplier = aConfig
            .get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
        sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
        sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
        sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
        Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
    }

    @Override
    public long maxAmperesIn() {
        return 10;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 512L;
    }

    @Override
    public int checkRecipe() {
        FluidStack tFluid = getDrainableStack();
        if ((tFluid == null) || (tFluid.amount < getCapacity())) {
            this.mOutputFluid = Materials.UUMatter.getFluid(1L);
            calculateCustomOverclock(containsUUA(getFillableStack()) ? uuaRecipe : nonUUARecipe);
            // In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            if (containsUUA(tFluid = getFillableStack())) {
                tFluid.amount -= sUUAperUUM;
                return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
            }
            return sRequiresUUA || Arrays.stream(getAllInputs())
                .anyMatch(s -> ItemList.Circuit_Integrated.isStackEqual(s, true, true))
                    ? FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS
                    : FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    public RecipeMap<?> getRecipeList() {
        return RecipeMaps.massFabFakeRecipes;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUAmplifier.getFluid(1L));
    }

    @Override
    public int getCapacity() {
        return Math.max(sUUAperUUM, 1000);
    }

    private boolean containsUUA(FluidStack aFluid) {
        return aFluid != null && aFluid.amount >= sUUAperUUM && aFluid.isFluidEqual(Materials.UUAmplifier.getFluid(1L));
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    protected class MassfabricatorOverclockDescriber extends EUOverclockDescriber {

        protected MassfabricatorOverclockDescriber(byte tier, int amperage) {
            super(tier, amperage);
        }

        @Override
        public GT_OverclockCalculator createCalculator(GT_OverclockCalculator template, GT_Recipe recipe) {
            return super.createCalculator(template, recipe).setEUt(Ints.saturatedCast(V[tier] * amperage))
                .setEUtIncreasePerOC(1)
                .limitOverclockCount(tier - 1)
                .setOneTickDiscount(false);
        }

        @Override
        protected boolean shouldShowAmperage(GT_OverclockCalculator calculator) {
            return true;
        }

        @Override
        protected String getVoltageString(GT_OverclockCalculator calculator) {
            // standard amperage calculation doesn't work here
            return decorateWithOverclockLabel(GT_Utility.formatNumbers(V[mTier]) + " EU/t", calculator)
                + GT_Utility.getTierNameWithParentheses(V[mTier]);
        }

        @Override
        protected String getAmperageString(GT_OverclockCalculator calculator) {
            int amperage = this.amperage;
            int denominator = 1;
            for (int i = 1; i < mTier; i++) {
                amperage >>= 1;
                if (amperage == 0) {
                    denominator <<= 1;
                }
            }
            if (amperage > 0) {
                return GT_Utility.formatNumbers(amperage);
            } else {
                return "1/" + denominator;
            }
        }
    }
}
