package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemStackData;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GMTE_AmazonPackager extends GregtechMeta_MultiBlockBase<GMTE_AmazonPackager>
        implements ISurvivalConstructable {

    private long mVoltage;
    private byte mTier;
    private int mCasing;

    private static IStructureDefinition<GMTE_AmazonPackager> STRUCTURE_DEFINITION = null;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GMTE_AmazonPackager(mName);
    }

    public GMTE_AmazonPackager(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GMTE_AmazonPackager(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Packager";
    }

    @Override
    public IStructureDefinition<GMTE_AmazonPackager> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GMTE_AmazonPackager>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GMTE_AmazonPackager.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.getIndexFromPage(2, 9)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 9))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Amazon Warehouse")
                .addInfo("This Multiblock is used for EXTREME packaging requirements")
                .addInfo("Dust Schematics are inserted into the input busses")
                .addInfo("If inserted into the controller, it is shared across all busses")
                .addInfo("1x, 2x, 3x & Other Schematics are to be placed into the controller GUI slot")
                .addInfo("500% faster than using single block machines of the same voltage")
                .addInfo("Only uses 75% of the EU/t normally required").addInfo("Processes 16 items per voltage tier")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front center").addCasingInfoMin("Supply Depot Casings", 10, false)
                .addInputBus("Any casing", 1).addOutputBus("Any casing", 1).addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1).addMufflerHatch("Any casing", 1).toolTipFinisher("GT++");
        return tt;
    }

    private final void initFields() {
        mVoltage = getMaxInputVoltage();
        mTier = (byte) Math.max(1, GT_Utility.getTier(mVoltage));
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
        return TAE.getIndexFromPage(2, 9);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes;
    }

    private static final FluidStack[] sNoFluids = new FluidStack[] {};

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        // Just the best place to check this~
        initFields();

        ArrayList<ItemStack> tItems = getStoredInputs();
        if (this.getGUIItemStack() != null) {
            tItems.add(this.getGUIItemStack());
        }
        ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
        boolean state = checkRecipeGeneric(tItemInputs, sNoFluids, getMaxParallelRecipes(), 75, 500, 10000);

        if (state) {
            return true;
        } else {
            tItems = getStoredInputs();
            AutoMap<ItemStackData> mCompleted = new AutoMap<ItemStackData>();
            AutoMap<ItemStackData> mSchematics = new AutoMap<ItemStackData>();
            for (ItemStack tInputItem : tItems) {
                if (tInputItem != null) {
                    if (ItemList.Schematic_1by1.isStackEqual((Object) tInputItem)
                            || ItemList.Schematic_2by2.isStackEqual((Object) tInputItem)
                            || ItemList.Schematic_3by3.isStackEqual((Object) tInputItem)) {
                        mSchematics.put(new ItemStackData(tInputItem));
                    }
                }
            }
            if (mSchematics.size() > 0) {
                for (ItemStackData g : mSchematics) {
                    for (ItemStack tInputItem : tItems) {
                        if (tInputItem != null) {
                            mCompleted.put(new ItemStackData(tInputItem));
                            checkRecipe(tInputItem, g.getStack());
                        }
                    }
                }
            }

            return mCompleted != null && mCompleted.size() > 0;
        }
    }

    public boolean checkRecipe(ItemStack inputStack, ItemStack schematicStack) {
        if (GT_Utility.isStackValid((Object) inputStack) && GT_Utility.isStackValid((Object) schematicStack)
                && GT_Utility.getContainerItem(inputStack, true) == null) {
            ItemStack tOutputStack;
            if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack) && inputStack.stackSize >= 1) {
                tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[] { inputStack });
                if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
                    final ItemStack input = inputStack;
                    --input.stackSize;
                    this.lEUt = 32 * (1L << this.mTier - 1) * (1L << this.mTier - 1);
                    // this.mMaxProgresstime = 16 / (1 << this.mTier - 1);
                    this.mMaxProgresstime = 2;
                    this.addOutput(tOutputStack);
                    updateSlots();
                    return true;
                }
                return false;
            } else if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack) && inputStack.stackSize >= 4) {
                tOutputStack = GT_ModHandler
                        .getRecipeOutput(new ItemStack[] { inputStack, inputStack, null, inputStack, inputStack });
                if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
                    final ItemStack input2 = inputStack;
                    input2.stackSize -= 4;
                    this.lEUt = 32 * (1L << this.mTier - 1) * (1L << this.mTier - 1);
                    // this.mMaxProgresstime = 32 / (1 << this.mTier - 1);
                    this.mMaxProgresstime = 4;
                    this.addOutput(tOutputStack);
                    updateSlots();
                    return true;
                }
                return false;
            } else if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack) && inputStack.stackSize >= 9) {
                tOutputStack = GT_ModHandler.getRecipeOutput(
                        new ItemStack[] { inputStack, inputStack, inputStack, inputStack, inputStack, inputStack,
                                inputStack, inputStack, inputStack });
                if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
                    final ItemStack input3 = inputStack;
                    input3.stackSize -= 9;
                    this.lEUt = 32 * (1L << this.mTier - 1) * (1L << this.mTier - 1);
                    // this.mMaxProgresstime = 64 / (1 << this.mTier - 1);
                    this.mMaxProgresstime = 6;
                    this.addOutput(tOutputStack);
                    updateSlots();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean allowPutStack(final ItemStack aStack, ItemStack schematicStack) {
        // If Schematic Static is not 1x1, 2x2, 3x3
        if (!ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)
                && !ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
                && !ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)) {
            return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.containsInput(aStack);
        }
        // Something
        if (GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.findRecipe(
                (IHasWorldObjectAndCoords) this.getBaseMetaTileEntity(),
                true,
                GT_Values.V[this.mTier],
                (FluidStack[]) null,
                new ItemStack[] { GT_Utility.copyAmount(64L, new Object[] { aStack }), schematicStack }) != null) {
            return true;
        }
        // 1x1
        if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)
                && GT_ModHandler.getRecipeOutput(new ItemStack[] { aStack }) != null) {
            return true;
        }
        // 2x2
        if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
                && GT_ModHandler.getRecipeOutput(new ItemStack[] { aStack, aStack, null, aStack, aStack }) != null) {
            return true;
        }
        // 3x3
        if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack) && GT_ModHandler.getRecipeOutput(
                new ItemStack[] { aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack }) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }

    @Override
    public int getMaxEfficiency(ItemStack p0) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack arg0) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiPackager;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
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
}
