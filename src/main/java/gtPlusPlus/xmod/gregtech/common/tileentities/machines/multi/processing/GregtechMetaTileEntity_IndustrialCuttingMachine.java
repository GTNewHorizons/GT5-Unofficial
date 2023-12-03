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
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

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
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialCuttingMachine extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialCuttingMachine> implements ISurvivalConstructable {

    private boolean mCuttingMode = true;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialCuttingMachine> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialCuttingMachine(final int aID, final String aName,
            final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialCuttingMachine(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialCuttingMachine(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Cutting Machine / Slicing Machine";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Cutting Factory")
                .addInfo("200% faster than using single block machines of the same voltage")
                .addInfo("Only uses 75% of the EU/t normally required").addInfo("Processes four items per voltage tier")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 5, true)
                .addController("Front Center").addCasingInfoMin("Cutting Factory Frames", 14, false)
                .addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialCuttingMachine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialCuttingMachine>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC", "CCC", "CCC" },
                                            { "C~C", "C-C", "C-C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialCuttingMachine.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 13))))
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
        return checkPiece(mName, 1, 1, 0) && mCasing >= 14 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(29);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mCuttingMode ? RecipeMaps.cutterRecipes : RecipeMaps.slicerRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.cutterRecipes, RecipeMaps.slicerRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F).setEuModifier(0.75F)
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCuttingMachine;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta() {
        return 13;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(29);
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mCuttingMode = !mCuttingMode;
        String aMode = mCuttingMode ? "Cutting" : "Slicing";
        PlayerUtils.messagePlayer(aPlayer, "Mode: " + aMode);
        mLastRecipe = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mCuttingMode", mCuttingMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mCuttingMode")) {
            mCuttingMode = aNBT.getBoolean("mCuttingMode");
        } else {
            mCuttingMode = true;
        }
    }
}
