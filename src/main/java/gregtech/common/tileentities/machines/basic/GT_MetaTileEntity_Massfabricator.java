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

import gregtech.api.enums.MachineType;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.power.BasicMachineEUPower;
import gregtech.common.power.Power;

public class GT_MetaTileEntity_Massfabricator extends GT_MetaTileEntity_BasicMachine {

    public static int sUUAperUUM = 1;
    public static int sUUASpeedBonus = 4;
    public static int sDurationMultiplier = 3215;
    public static boolean sRequiresUUA = false;
    protected final long EUt;

    public GT_MetaTileEntity_Massfabricator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.MATTER_FABRICATOR.tooltipDescription(),
            1,
            1,
            "Massfabricator.png",
            "",
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
        EUt = V[1] * (long) Math.pow(2, mTier + 2);
    }

    public GT_MetaTileEntity_Massfabricator(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
        EUt = V[1] * (long) Math.pow(2, mTier + 2);
    }

    public GT_MetaTileEntity_Massfabricator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
        EUt = V[1] * (long) Math.pow(2, mTier + 2);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Massfabricator(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mGUIName,
            this.mNEIName);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    protected Power buildPower() {
        return new MassfabricatorPower(mTier, mAmperage);
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
            calculateOverclockedNess(
                (int) EUt,
                containsUUA(getFillableStack()) ? sDurationMultiplier / sUUASpeedBonus : sDurationMultiplier);
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
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes;
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

    protected class MassfabricatorPower extends BasicMachineEUPower {

        protected MassfabricatorPower(byte tier, int amperage) {
            super(tier, amperage);
        }

        @Override
        public void computePowerUsageAndDuration(int euPerTick, int duration) {
            originalVoltage = computeVoltageForEuRate(euPerTick);

            if (mTier == 0) {
                // Long time calculation
                long xMaxProgresstime = ((long) duration) << 1;
                if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                    // make impossible if too long
                    recipeEuPerTick = Integer.MAX_VALUE - 1;
                    recipeDuration = Integer.MAX_VALUE - 1;
                } else {
                    recipeEuPerTick = (int) (V[1] << 2); // 2^2=4 so shift <<2
                    recipeDuration = (int) xMaxProgresstime;
                }
            } else {
                // Long EUt calculation
                long xEUt = EUt;

                long tempEUt = V[1];

                recipeDuration = duration;

                while (tempEUt <= V[mTier - 1]) {
                    tempEUt <<= 2; // this actually controls overclocking
                    recipeDuration >>= 1; // this is effect of overclocking
                    if (recipeDuration == 0) xEUt = (long) (xEUt / 1.1D); // U know, if the time is less than 1 tick
                                                                          // make the machine use less power
                }
                if (xEUt > Integer.MAX_VALUE - 1) {
                    recipeEuPerTick = Integer.MAX_VALUE - 1;
                    recipeDuration = Integer.MAX_VALUE - 1;
                } else {
                    recipeEuPerTick = (int) xEUt;
                    if (recipeEuPerTick == 0) recipeEuPerTick = 1;
                    if (recipeDuration == 0) recipeDuration = 1; // set time to 1 tick
                }
            }
            wasOverclocked = checkIfOverclocked();
        }

        @Override
        public String getVoltageString() {
            long voltage = V[1];
            String voltageDescription = GT_Utility.formatNumbers(voltage) + " EU";
            voltageDescription += GT_Utility.getTierNameWithParentheses(voltage);
            return voltageDescription;
        }

        @Override
        public String getAmperageString() {
            long amperage = originalVoltage / V[1];
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
