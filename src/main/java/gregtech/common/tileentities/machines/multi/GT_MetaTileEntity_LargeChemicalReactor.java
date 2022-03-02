package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Single_Recipe_Check;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GT_MetaTileEntity_LargeChemicalReactor extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_LargeChemicalReactor> {
    private static final int CASING_INDEX = 176;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_LargeChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_LargeChemicalReactor>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                    {"ccc", "cxc", "ccc"},
                    {"c~c", "xPx", "cxc"},
                    {"ccc", "cxc", "ccc"},
            }))
            .addElement('P', ofBlock(GregTech_API.sBlockCasings8, 1))
            .addElement('c', ofChain(
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addInputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addOutputToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addMaintenanceToMachineList, CASING_INDEX, 1),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addEnergyInputToMachineList, CASING_INDEX, 1),
                    onElementPass(GT_MetaTileEntity_LargeChemicalReactor::onCasingAdded, ofBlock(GregTech_API.sBlockCasings8, 0))
            ))
            .addElement('x', ofChain(
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addInputToMachineList, CASING_INDEX, 2),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addOutputToMachineList, CASING_INDEX, 2),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addMaintenanceToMachineList, CASING_INDEX, 2),
                    ofHatchAdder(GT_MetaTileEntity_LargeChemicalReactor::addEnergyInputToMachineList, CASING_INDEX, 2),
                    onElementPass(GT_MetaTileEntity_LargeChemicalReactor::onCoilAdded, ofBlock(GregTech_API.sBlockCasings5, 0)),
                    onElementPass(GT_MetaTileEntity_LargeChemicalReactor::onCasingAdded, ofBlock(GregTech_API.sBlockCasings8, 0))
            ))
            .build();

    private int mCasingAmount;
    private int mCoilAmount;

    public GT_MetaTileEntity_LargeChemicalReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeChemicalReactor(this.mName);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Chemical Reactor")
                .addInfo("Controller block for the Large Chemical Reactor")
                .addInfo("Does not lose efficiency when overclocked")
                .addInfo("Accepts fluids instead of fluid cells")
                .addSeparator()
                .beginStructureBlock(3, 3, 3, false)
                .addController("Front center")
                .addCasingInfo("Chemically Inert Machine Casing", 8)
                .addOtherStructurePart("PTFE Pipe Machine Casing", "Center")
                .addOtherStructurePart("Cupronickel Coil Block", "Adjacent to the PTFE Pipe Machine Casing", 1)
                .addEnergyHatch("Any casing", 1, 2)
                .addMaintenanceHatch("Any casing", 1, 2)
                .addInputBus("Any casing", 1, 2)
                .addInputHatch("Any casing", 1, 2)
                .addOutputBus("Any casing", 1, 2)
                .addOutputHatch("Any casing", 1, 2)
                .addStructureInfo("You can have multiple hatches/busses")
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive,
                                 boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[]{
                    casingTexturePages[1][48],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                    casingTexturePages[1][48],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{casingTexturePages[1][48]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeChemicalReactor.png");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        long tVoltage = getMaxInputVoltage();
        byte tier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe;

        if (mLockedToSingleRecipe && mSingleRecipeCheck != null) {
            if (!mSingleRecipeCheck.checkRecipeInputsSingleStack(true)) {
                return false;
            }

            tRecipe = mSingleRecipeCheck.getRecipe();
        } else {
            ArrayList<ItemStack> tInputList = getStoredInputs();
            int tInputList_sS = tInputList.size();
            for (int i = 0; i < tInputList_sS - 1; i++) {
                for (int j = i + 1; j < tInputList_sS; j++) {
                    if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                        if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                            tInputList.remove(j--);
                            tInputList_sS = tInputList.size();
                        } else {
                            tInputList.remove(i--);
                            tInputList_sS = tInputList.size();
                            break;
                        }
                    }
                }
            }
            ItemStack[] inputs = tInputList.toArray(new ItemStack[0]);

            ArrayList<FluidStack> tFluidList = getStoredFluids();
            int tFluidList_sS = tFluidList.size();
            for (int i = 0; i < tFluidList_sS - 1; i++) {
                for (int j = i + 1; j < tFluidList_sS; j++) {
                    if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                        if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                            tFluidList.remove(j--);
                            tFluidList_sS = tFluidList.size();
                        } else {
                            tFluidList.remove(i--);
                            tFluidList_sS = tFluidList.size();
                            break;
                        }
                    }
                }
            }
            FluidStack[] fluids = tFluidList.toArray(new FluidStack[0]);

            if (inputs.length == 0 && fluids.length == 0) {
                return false;
            }

            GT_Single_Recipe_Check.Builder tSingleRecipeCheckBuilder = null;
            if (mLockedToSingleRecipe) {
                // We're locked to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                tSingleRecipeCheckBuilder = GT_Single_Recipe_Check.builder(this).setBefore();
            }

            tRecipe = GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.findRecipe(getBaseMetaTileEntity(), false,
                    false, gregtech.api.enums.GT_Values.V[tier], fluids, inputs);

            if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, fluids, inputs)) {
                return false;
            }

            if (mLockedToSingleRecipe) {
                mSingleRecipeCheck = tSingleRecipeCheckBuilder.setAfter().setRecipe(tRecipe).build();
            }
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        calculatePerfectOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
        //In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }

        this.mOutputItems = tRecipe.mOutputs;
        this.mOutputFluids = tRecipe.mFluidOutputs;
        this.updateSlots();
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private void onCoilAdded() {
        mCoilAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mCoilAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) &&
                mCasingAmount >= 8 && mCoilAmount == 1 &&
                !mEnergyHatches.isEmpty() && mMaintenanceHatches.size() == 1;
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }
}
