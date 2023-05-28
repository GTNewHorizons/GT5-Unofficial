package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_CubicMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_VacuumFreezer
    extends GT_MetaTileEntity_CubicMultiBlockBase<GT_MetaTileEntity_VacuumFreezer> {

    public GT_MetaTileEntity_VacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_VacuumFreezer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_VacuumFreezer(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Vacuum Freezer")
            .addInfo("Controller Block for the Vacuum Freezer")
            .addInfo("Cools hot ingots and cells")
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoRange("Frost Proof Machine Casing", 16, 24, false)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addOutputHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { casingTexturePages[0][17] };
        }
        return rTexture;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sVacuumRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ItemStack[] tInputList = getCompactedInputs();
        FluidStack[] tFluidList = getCompactedFluids();

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe = getRecipeMap()
            .findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidList, tInputList);
        if (tRecipe != null) {
            if (!canOutputAll(tRecipe)) return false;
            if (tRecipe.isRecipeInputEqual(true, tFluidList, tInputList)) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;

                calculateOverclockedNessMultiInternal(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage, false);
                // In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                this.mOutputItems = new ItemStack[] { tRecipe.getOutput(0) };
                this.mOutputFluids = new FluidStack[] { tRecipe.getFluidOutput(0) };
                updateSlots();
                return true;
            }
        }
        return false;
    }

    @Override
    protected IStructureElement<GT_MetaTileEntity_CubicMultiBlockBase<?>> getCasingElement() {
        return StructureUtility.ofBlock(GregTech_API.sBlockCasings2, 1);
    }

    @Override
    protected int getHatchTextureIndex() {
        return 17;
    }

    @Override
    protected int getRequiredCasingCount() {
        return 16;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
