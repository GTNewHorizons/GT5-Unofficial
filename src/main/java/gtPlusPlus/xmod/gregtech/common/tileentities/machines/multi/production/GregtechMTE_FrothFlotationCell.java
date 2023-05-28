package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

public class GregtechMTE_FrothFlotationCell extends GregtechMeta_MultiBlockBase<GregtechMTE_FrothFlotationCell>
        implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMTE_FrothFlotationCell> STRUCTURE_DEFINITION = null;

    public GregtechMTE_FrothFlotationCell(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_FrothFlotationCell(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_FrothFlotationCell(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Flotation Cell";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Process that milled ore!")
                .addInfo("You can only ever process one type of material per controller")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(7, 9, 7, true)
                .addController("Front Center").addCasingInfoMin("Inconel Reinforced Casing", 68, false)
                .addCasingInfoMin("Flotation Casing", 52, false).addInputBus("Bottom Casing", 1)
                .addInputHatch("Bottom Casing", 1).addOutputHatch("Bottom Casing", 1).addEnergyHatch("Bottom Casing", 1)
                .addMaintenanceHatch("Bottom Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public String getSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP.toString();
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
        return TAE.getIndexFromPage(2, 1);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    @Override
    public IStructureDefinition<GregtechMTE_FrothFlotationCell> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_FrothFlotationCell>builder().addShape(
                    mName,
                    new String[][] { { "       ", "       ", "   X   ", "  X~X  ", "   X   ", "       ", "       " },
                            { "       ", "   F   ", "  FFF  ", " FF FF ", "  FFF  ", "   F   ", "       " },
                            { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                            { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                            { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                            { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                            { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                            { "  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  " },
                            { "  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  " }, })
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMTE_FrothFlotationCell.class)
                                    .atLeast(InputBus, InputHatch, OutputHatch, Maintenance, Energy)
                                    .casingIndex(getCasingTextureId()).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 1))))
                    .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 9))
                    .addElement('X', ofBlock(ModBlocks.blockCasings3Misc, 1)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 3, 3, 0) && mCasing >= 68 - 4 && checkHatch();
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiFrothFlotationCell;
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Fix GT bug
        if (this.getBaseMetaTileEntity().getFrontFacing() != ForgeDirection.UP) {
            log("Fixing Bad Facing. (GT Bug)");
            this.getBaseMetaTileEntity().setFrontFacing(ForgeDirection.UP);
        }
    }

    @Override
    public boolean checkRecipe(ItemStack arg0) {
        return super.checkRecipeGeneric();
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {
        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = findRecipe(
                getBaseMetaTileEntity(),
                mLastRecipe,
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            return false;
        }

        /*
         * Material checks Makes sure we can only ever use one type of material in this flotation cell. We used to
         * depend on Alk's hash, but it's unreliable and user-hostile So we're using unlocalized name of material now.
         */
        Material foundMaterial = FlotationRecipeHandler
                .getMaterialOfMilledProduct(FlotationRecipeHandler.findMilledStack(tRecipe));
        String foundMaterialName = null;
        if (foundMaterial != null) {
            foundMaterialName = foundMaterial.getUnlocalizedName();
        }

        if (foundMaterialName == null) {
            return false;
        }

        // Set material locked for this controller
        if (lockedMaterialName == null) {
            lockedMaterialName = foundMaterialName;
        }

        // Check material match
        if (!Objects.equals(lockedMaterialName, foundMaterialName)) {
            return false;
        }

        return super.checkRecipeGeneric(
                aItemInputs,
                aFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll,
                tRecipe);
    }

    /*
     * Handle NBT
     */

    private String lockedMaterialName = null;

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("lockedMaterialName", Constants.NBT.TAG_STRING)) {
            lockedMaterialName = aNBT.getString("lockedMaterialName");
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("lockedMaterialName")) {
            tooltip.add(
                    StatCollector.translateToLocal("tooltip.flotationCell.lockedTo") + " "
                            + StatCollector.translateToLocal(stack.getTagCompound().getString("lockedMaterialName")));
        }
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "Locked material: " + lockedMaterialName };
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return lockedMaterialName != null && !lockedMaterialName.equals("");
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.StringSyncer(() -> lockedMaterialName, val -> lockedMaterialName = val));
    }
}
