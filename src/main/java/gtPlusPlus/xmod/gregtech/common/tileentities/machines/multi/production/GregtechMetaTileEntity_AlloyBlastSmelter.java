package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_AlloyBlastSmelter extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_AlloyBlastSmelter> implements ISurvivalConstructable {

    private int mMode = 0;
    private boolean isUsingControllerCircuit = false;
    private static Item circuit;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_AlloyBlastSmelter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_AlloyBlastSmelter(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_AlloyBlastSmelter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Fluid Alloy Cooker";
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("isBussesSeparate")) {
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Alloy Blast Smelter")
                .addInfo("20% Faster than the Electric Blast Furnace")
                .addInfo("Allows Complex GT++ alloys to be created").addInfo("Accepts only one Energy Hatch")
                .addInfo("Circuit for recipe goes in the Input Bus or GUI slot")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 4, 3, true)
                .addController("Bottom Center").addCasingInfo("Blast Smelter Casings", 5)
                .addCasingInfo("Blast Smelter Heat Containment Coils", 16).addInputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_AlloyBlastSmelter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_AlloyBlastSmelter>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_AlloyBlastSmelter.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(15)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 15))))
                    .addElement('H', ofBlock(ModBlocks.blockCasingsMisc, 14)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 3, 0) && mCasing >= 5 && mEnergyHatches.size() == 1 && checkHatch();
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(Integer.valueOf(208));
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
        return TAE.GTPP_INDEX(15);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        if (this.getBaseMetaTileEntity().isServerSide()) {
            // Get Controller Circuit
            if (circuit == null) {
                circuit = CI.getNumberedCircuit(0).getItem();
            }
            if (aStack != null && aStack.getItem() == circuit) {
                this.mMode = aStack.getItemDamage();
                return this.isUsingControllerCircuit = true;
            } else {
                if (aStack == null) {
                    this.isUsingControllerCircuit = false;
                    return true; // Allowed empty
                }
                Logger.WARNING("Not circuit in GUI inputs.");
                return this.isUsingControllerCircuit = false;
            }
        }
        Logger.WARNING("No Circuit, clientside.");
        return this.isUsingControllerCircuit = false;
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {

        if (this.getBaseMetaTileEntity().isServerSide()) {

            ArrayList<ItemStack> tInputList = null;
            // Get Controller Circuit
            this.isUsingControllerCircuit = isCorrectMachinePart(aStack);

            final long tVoltage = this.getMaxInputVoltage();
            final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            ItemStack[] tInputs = null;
            final FluidStack[] tFluids = getCompactedFluids();
            GT_Recipe tRecipe = null;

            if (inputSeparation) {
                for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
                    tInputList = new ArrayList<>();
                    tBus.mRecipeMap = getRecipeMap();

                    if (isValidMetaTileEntity(tBus)) {
                        for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                            if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                                tInputList.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
                            }
                        }
                    }
                    tInputs = tInputList.toArray(new ItemStack[0]);
                    tRecipe = GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.findRecipe(
                            this.getBaseMetaTileEntity(),
                            false,
                            gregtech.api.enums.GT_Values.V[tTier],
                            tFluids,
                            tInputs);
                    if ((tRecipe != null)) {
                        break;
                    }
                }
            } else {
                tInputList = this.getStoredInputs();
                for (int i = 0; i < (tInputList.size() - 1); i++) {
                    for (int j = i + 1; j < tInputList.size(); j++) {
                        if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                            if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                                tInputList.remove(j--);
                            } else {
                                tInputList.remove(i--);
                                break;
                            }
                        }
                    }
                }
                tInputs = tInputList.toArray(new ItemStack[0]);
                tRecipe = GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.findRecipe(
                        this.getBaseMetaTileEntity(),
                        false,
                        gregtech.api.enums.GT_Values.V[tTier],
                        tFluids,
                        tInputs);
            }

            // Validity check
            if ((isUsingControllerCircuit && tInputList.size() < 1)
                    || (!isUsingControllerCircuit && tInputList.size() < 2)) {
                Logger.WARNING("Not enough inputs.");
                return false;
            } else if (isUsingControllerCircuit && tInputList.size() >= 1) {
                tInputList.add(CI.getNumberedCircuit(this.mMode));
            }

            if (tInputList.size() > 1) {
                if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
                    Logger.WARNING("Found some Valid Inputs.");
                    this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
                    this.mEfficiencyIncrease = 10000;
                    if (tRecipe.mEUt <= 16) {
                        this.lEUt = (tRecipe.mEUt * (1L << (tTier - 1)) * (1L << (tTier - 1)));
                        this.mMaxProgresstime = (tRecipe.mDuration / (1 << (tTier - 1)));
                    } else {
                        this.lEUt = tRecipe.mEUt;
                        this.mMaxProgresstime = tRecipe.mDuration;
                        while (this.lEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                            this.lEUt *= 4;
                            this.mMaxProgresstime /= 2;
                        }
                    }
                    if (this.lEUt > 0) {
                        this.lEUt = (-this.lEUt);
                    }
                    this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                    this.mOutputFluids = new FluidStack[] { tRecipe.getFluidOutput(0) };
                    List<ItemStack> tOutPutItems = new ArrayList<ItemStack>();
                    for (ItemStack tOut : tRecipe.mOutputs) {
                        if (ItemUtils.checkForInvalidItems(tOut)) {
                            tOutPutItems.add(tOut);
                        }
                    }
                    if (tOutPutItems.size() > 0)
                        this.mOutputItems = tOutPutItems.toArray(new ItemStack[tOutPutItems.size()]);
                    this.updateSlots();
                    return true;
                }
            }
        }
        Logger.WARNING("Failed to find some Valid Inputs or Clientside.");
        return false;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
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
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiABS;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public int getAmountOfOutputs() {
        return 2;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    protected boolean isInputSeparationButtonEnabled() {
        return true;
    }
}
