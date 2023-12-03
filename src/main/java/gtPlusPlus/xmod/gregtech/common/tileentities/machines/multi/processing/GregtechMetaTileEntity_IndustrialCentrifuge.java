package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class GregtechMetaTileEntity_IndustrialCentrifuge extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialCentrifuge> implements ISurvivalConstructable {

    private boolean mIsAnimated;
    private static final CustomIcon frontFaceActive = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
    private static final CustomIcon frontFace = new CustomIcon("iconsets/LARGECENTRIFUGE5");
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialCentrifuge> STRUCTURE_DEFINITION = null;
    // public static double recipesComplete = 0;

    public GregtechMetaTileEntity_IndustrialCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        mIsAnimated = true;
    }

    public GregtechMetaTileEntity_IndustrialCentrifuge(final String aName) {
        super(aName);
        mIsAnimated = true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialCentrifuge(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Centrifuge";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Centrifuge")
                .addInfo("125% faster than using single block machines of the same voltage")
                .addInfo("Disable animations with a screwdriver").addInfo("Only uses 90% of the EU/t normally required")
                .addInfo("Processes six items per voltage tier").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 3, 3, true).addController("Front Center")
                .addCasingInfoMin("Centrifuge Casings", 6, false).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialCentrifuge>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialCentrifuge.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 0))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        if (usingAnimations()) {
            return frontFaceActive;
        } else {
            return frontFace;
        }
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return frontFace;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(0);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.centrifugeNonCellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setEuModifier(0.9F).setSpeedBonus(1F / 2.25F)
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasingsMisc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(0);
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCentrifuge;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mIsAnimated = !mIsAnimated;
        Logger.INFO("Is Centrifuge animated " + this.mIsAnimated);
        if (this.mIsAnimated) {
            PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture. ");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture. ");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mIsAnimated", mIsAnimated);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mIsAnimated")) {
            mIsAnimated = aNBT.getBoolean("mIsAnimated");
        } else {
            mIsAnimated = true;
        }
    }

    public boolean usingAnimations() {
        // Logger.INFO("Is animated? "+this.mIsAnimated);
        return this.mIsAnimated;
    }
}
