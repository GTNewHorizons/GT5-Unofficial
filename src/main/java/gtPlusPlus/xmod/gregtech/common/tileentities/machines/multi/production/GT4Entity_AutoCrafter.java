package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.helpers.CraftingHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.autocrafter.AC_Helper_Utils;

public class GT4Entity_AutoCrafter extends GregtechMeta_MultiBlockBase<GT4Entity_AutoCrafter>
        implements ISurvivalConstructable {

    private byte mTier = 1;
    protected GT_Recipe lastRecipeToBuffer;
    private int casing;
    private static IStructureDefinition<GT4Entity_AutoCrafter> STRUCTURE_DEFINITION = null;

    public CraftingHelper inventoryCrafter;

    public void onRightclick(EntityPlayer player) {}

    public GT4Entity_AutoCrafter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT4Entity_AutoCrafter(String mName) {
        super(mName);
    }

    @Override
    public String getMachineType() {
        return "Assembler";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new GT4Entity_AutoCrafter(this.mName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return super.onRunningTick(aStack);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiAutoCrafter;
    }

    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Highly Advanced Assembling Machine")
                .addInfo("200% faster than using single block machines of the same voltage")
                .addInfo("Processes two items per voltage tier").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 3, 3, true).addController("Front Center")
                .addCasingInfo("Bulk Production Frame", 10).addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(0, 10);
    }

    @Override
    public IStructureDefinition<GT4Entity_AutoCrafter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT4Entity_AutoCrafter>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GT4Entity_AutoCrafter.class)
                                    .atLeast(InputBus, OutputBus, InputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.getIndexFromPage(0, 10)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(mName, itemStack, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, itemStack, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity baseMetaTileEntity, ItemStack itemStack) {
        casing = 0;
        if (checkPiece(mName, 1, 1, 0) && casing >= 10 && checkHatch()) {
            setTier();
            return true;
        } else return false;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
    }

    private void setTier() {
        long tVoltage = getMaxInputVoltage();
        this.mTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
    }

    @Override
    public boolean checkRecipe(final ItemStack itemStack) {
        FluidStack[] properArray = getStoredFluids().toArray(new FluidStack[] {});

        for (GT_MetaTileEntity_Hatch_InputBus bus : mInputBusses) {
            ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
            if (isValidMetaTileEntity(bus)) {
                for (int i = bus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (bus.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                        tBusItems.add(bus.getBaseMetaTileEntity().getStackInSlot(i));
                    }
                }
            }

            return checkRecipeGeneric(
                    tBusItems.toArray(new ItemStack[] {}),
                    properArray,
                    getMaxParallelRecipes(),
                    100,
                    200,
                    10_000);

        }
        return false;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * (Math.max(1, GT_Utility.getTier(getMaxInputVoltage())));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 100;
    }

    @Override
    public String[] getExtraInfoData() {
        final String running = (this.mMaxProgresstime > 0 ? "Auto-Crafter running" : "Auto-Crafter stopped");
        final String maintenance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintenance issues"
                : "Needs Maintenance");
        String tSpecialText;

        if (lastRecipeToBuffer != null && lastRecipeToBuffer.mOutputs[0].getDisplayName() != null) {
            tSpecialText = "Currently processing: " + lastRecipeToBuffer.mOutputs[0].getDisplayName();
        } else {
            tSpecialText = "Currently processing: Nothing";
        }

        return new String[] { "Large Scale Auto-Assembler v1.01c", running, maintenance, tSpecialText };
    }

    @Override
    public void explodeMultiblock() {
        AC_Helper_Utils.removeCrafter(this);
        super.explodeMultiblock();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        AC_Helper_Utils.removeCrafter(this);
        super.doExplosion(aExplosionPower);
    }
}
