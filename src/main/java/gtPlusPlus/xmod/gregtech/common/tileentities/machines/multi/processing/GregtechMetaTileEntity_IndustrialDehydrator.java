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
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialDehydrator extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialDehydrator> implements ISurvivalConstructable {

    private static int CASING_TEXTURE_ID;
    private static String mCasingName = "Vacuum Casing";
    private HeatingCoilLevel mHeatingCapacity;
    private boolean mDehydratorMode = false;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialDehydrator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    public GregtechMetaTileEntity_IndustrialDehydrator(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialDehydrator(mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Factory Grade Vacuum Furnace")
                .addInfo("Can toggle the operation temperature with a Screwdriver")
                .addInfo("All Dehydrator recipes are Low Temp recipes")
                .addInfo("Speed: +120% | EU Usage: 50% | Parallel: 4")
                .addInfo("Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)")
                .addInfo("Each 1800K over the min. Heat Capacity allows for one upgraded overclock")
                .addInfo("Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 5, 3, true)
                .addController("Bottom Center").addCasingInfoMin(mCasingName, 5, false).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialDehydrator>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                                    { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialDehydrator.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(CASING_TEXTURE_ID).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings4Misc, 10))))
                    .addElement(
                            'H',
                            ofCoil(
                                    GregtechMetaTileEntity_IndustrialDehydrator::setCoilLevel,
                                    GregtechMetaTileEntity_IndustrialDehydrator::getCoilLevel))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 4, 0) && mCasing >= 5 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mDehydratorMode ? GTPPRecipeMaps.chemicalDehydratorNonCellRecipes : GTPPRecipeMaps.vacuumFurnaceRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GTPPRecipeMaps.chemicalDehydratorNonCellRecipes, GTPPRecipeMaps.vacuumFurnaceRecipes);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialDehydrator;
    }

    @Override
    public String getMachineType() {
        return "Vacuum Furnace / Dehydrator";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                return recipe.mSpecialValue <= getCoilLevel().getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setHeatOC(true).setHeatDiscount(true)
                        .setRecipeHeat(recipe.mSpecialValue).setMachineHeat((int) getCoilLevel().getHeat());
            }
        }.setSpeedBonus(1F / 2.2F).setEuModifier(0.5F).setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mDehydratorMode = !mDehydratorMode;
        String aMode = mDehydratorMode ? "Dehydrator" : "Vacuum Furnace";
        PlayerUtils.messagePlayer(aPlayer, "Mode: " + aMode);
        mLastRecipe = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mDehydratorMode", mDehydratorMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mDehydratorMode = aNBT.getBoolean("mDehydratorMode");
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }
}
