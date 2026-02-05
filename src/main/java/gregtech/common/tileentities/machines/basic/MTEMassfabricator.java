package gregtech.common.tileentities.machines.basic;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
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

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.primitives.Ints;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.objects.overclockdescriber.EUOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.config.MachineStats;

public class MTEMassfabricator extends MTEBasicMachine {

    public static int sUUAperUUM = 1;
    public static int sUUASpeedBonus = 4;
    public static int sDurationMultiplier = 3215;
    public static boolean sRequiresUUA = false;
    public static int BASE_EUT = 256;
    public static GTRecipe nonUUARecipe;
    public static GTRecipe uuaRecipe;

    public MTEMassfabricator(int aID, String aName, String aNameRegional, int aTier) {
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

    public MTEMassfabricator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 8, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMassfabricator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
    public void onConfigLoad() {
        super.onConfigLoad();
        sDurationMultiplier = MachineStats.massFabricator.durationMultiplier;
        sUUAperUUM = MachineStats.massFabricator.UUAPerUUM;
        sUUASpeedBonus = MachineStats.massFabricator.UUASpeedBonus;
        sRequiresUUA = MachineStats.massFabricator.requiresUUA;
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
            this.mOutputFluids[0] = Materials.UUMatter.getFluid(1L);
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
    public RecipeMap<?> getRecipeMap() {
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
        public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
            return super.createCalculator(template, recipe).setEUt(Ints.saturatedCast(V[tier] * amperage))
                .setEUtIncreasePerOC(2.0)
                .setMaxOverclocks(tier - 1);
        }

        @Override
        protected boolean shouldShowAmperage(OverclockCalculator calculator) {
            return true;
        }

        @Override
        protected String getVoltageString(OverclockCalculator calculator) {
            // standard amperage calculation doesn't work here
            long voltage = V[mTier];
            String voltageString = StatCollector.translateToLocalFormatted(
                "GT5U.nei.display.voltage",
                formatNumber(voltage),
                GTUtility.getTierNameWithParentheses(voltage));

            if (wasOverclocked(calculator)) {
                voltageString += StatCollector.translateToLocal("GT5U.nei.display.overclock");
            }
            return voltageString;
        }

        @Override
        protected String getAmperageString(OverclockCalculator calculator) {
            int amperage = this.amperage;
            String amperageValue;
            int denominator = 1;
            for (int i = 1; i < mTier; i++) {
                amperage >>= 1;
                if (amperage == 0) {
                    denominator <<= 1;
                }
            }
            if (amperage > 0) {
                amperageValue = formatNumber(amperage);
            } else {
                amperageValue = "1/" + denominator;
            }
            return StatCollector.translateToLocalFormatted("GT5U.nei.display.amperage", amperageValue);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_REPLICATOR;
    }

}
