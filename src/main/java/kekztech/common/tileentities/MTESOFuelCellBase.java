package kekztech.common.tileentities;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public abstract class MTESOFuelCellBase<T extends MTESOFuelCellBase<T>> extends MTEEnhancedMultiBlockBase<T>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private final int mSteamPerTick;
    private final int mOxygenPerSecond;
    private final int mEUPerTick;

    protected int mGlassTier = -1;
    protected int mCasing = 0;
    protected static final String STRUCTURE_PIECE_MAIN = "main";

    protected MTESOFuelCellBase(int aID, String aName, String aNameRegional, int aSteamPerTick, int aOxygenPerSecond,
        int aEUPerTick) {
        super(aID, aName, aNameRegional);
        this.mSteamPerTick = aSteamPerTick;
        this.mOxygenPerSecond = aOxygenPerSecond;
        this.mEUPerTick = aEUPerTick;
    }

    public MTESOFuelCellBase(String aName, int aSteamPerTick, int aOxygenPerSecond, int aEUPerTick) {
        super(aName);
        this.mSteamPerTick = aSteamPerTick;
        this.mOxygenPerSecond = aOxygenPerSecond;
        this.mEUPerTick = aEUPerTick;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    public abstract float getFuelEffiencyMultiplier(GTRecipe aFuel);

    public abstract FluidStack getSteamOutput(long aStackSize);

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        Collection<GTRecipe> recipeList = RecipeMaps.gasTurbineFuels.getAllRecipes();

        for (FluidStack hatchFluid : storedFluids) {
            for (GTRecipe aFuel : recipeList) {
                FluidStack liquid;
                if ((liquid = GTUtility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null
                    && hatchFluid.isFluidEqual(liquid)) {

                    liquid.amount = (int) ((this.mEUPerTick * 20)
                        / (aFuel.mSpecialValue * getFuelEffiencyMultiplier(aFuel)));

                    if (super.depleteInput(liquid)) {

                        if (!super.depleteInput(Materials.Oxygen.getGas(this.mOxygenPerSecond))) {
                            super.mEUt = 0;
                            super.mEfficiency = 0;
                            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                        }

                        super.mEUt = this.mEUPerTick;
                        super.mMaxProgresstime = 1;
                        super.mEfficiencyIncrease = 10;
                        if (super.mEfficiency > 0) {
                            float steam_efficiency = (float) super.mEfficiency / getMaxEfficiency(null);
                            super.addOutputPartial(getSteamOutput((int) (this.mSteamPerTick * steam_efficiency)));
                        }
                        return CheckRecipeResultRegistry.GENERATING;
                    }
                }
            }
        }

        super.mEUt = 0;
        super.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_HEAT_EXCHANGER,
            OVERLAY_FRONT_HEAT_EXCHANGER_GLOW,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW);
    }

    @Override
    public void checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem, List<StructureError> errors) {
        this.mCasing = 0;
        this.mGlassTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors)) return;
        checkCasingMin(errors, this.mCasing, 12);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);

        if (this.mGlassTier < VoltageIndex.EV) {
            errors.add(StructureErrors.glassTierNotEnough(VoltageIndex.EV));
        }
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }
}
