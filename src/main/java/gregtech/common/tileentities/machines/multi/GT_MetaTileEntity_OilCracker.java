package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

public class GT_MetaTileEntity_OilCracker extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_OilCracker>
    implements ISurvivalConstructable {

    private static final byte CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_OilCracker> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_OilCracker>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "lcmcr", "lcmcr", "lcmcr" }, { "lc~cr", "l---r", "lcmcr" },
                    { "lcmcr", "lcmcr", "lcmcr" }, }))
        .addElement('c', ofCoil(GT_MetaTileEntity_OilCracker::setCoilLevel, GT_MetaTileEntity_OilCracker::getCoilLevel))
        .addElement(
            'l',
            buildHatchAdder(GT_MetaTileEntity_OilCracker.class)
                .atLeast(
                    GT_HatchElement.InputHatch.withAdder(GT_MetaTileEntity_OilCracker::addLeftHatchToMachineList),
                    GT_HatchElement.Energy,
                    GT_HatchElement.Maintenance)
                .dot(2)
                .casingIndex(CASING_INDEX)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_OilCracker::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
        .addElement(
            'r',
            buildHatchAdder(GT_MetaTileEntity_OilCracker.class)
                .atLeast(
                    GT_HatchElement.OutputHatch.withAdder(GT_MetaTileEntity_OilCracker::addRightHatchToMachineList),
                    GT_HatchElement.Energy,
                    GT_HatchElement.Maintenance)
                .dot(3)
                .casingIndex(CASING_INDEX)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_OilCracker::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
        .addElement(
            'm',
            buildHatchAdder(GT_MetaTileEntity_OilCracker.class)
                .atLeast(
                    GT_HatchElement.InputHatch.withAdder(GT_MetaTileEntity_OilCracker::addMiddleInputToMachineList)
                        .withCount(t -> t.mMiddleInputHatches.size()),
                    GT_HatchElement.Energy,
                    GT_HatchElement.Maintenance)
                .dot(1)
                .casingIndex(CASING_INDEX)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_OilCracker::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
        .build();
    private HeatingCoilLevel heatLevel;
    protected final List<GT_MetaTileEntity_Hatch_Input> mMiddleInputHatches = new ArrayList<>();
    // 0 -> left, 1 -> right, any other -> not found
    protected int mInputOnSide;
    protected int mOutputOnSide;
    protected int mCasingAmount;

    public GT_MetaTileEntity_OilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilCracker(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Cracker")
            .addInfo("Controller block for the Oil Cracking Unit")
            .addInfo("Thermally cracks heavy hydrocarbons into lighter fractions")
            .addInfo("More efficient than the Chemical Reactor")
            .addInfo("Gives different benefits whether it hydro or steam-cracks:")
            .addInfo("Hydro - Consumes 20% less Hydrogen and outputs 25% more cracked fluid")
            .addInfo("Steam - Outputs 50% more cracked fluid")
            .addInfo("(Values compared to cracking in the Chemical Reactor)")
            .addInfo("Place the appropriate circuit in the controller")
            .addSeparator()
            .beginStructureBlock(5, 3, 3, true)
            .addController("Front center")
            .addCasingInfoRange("Clean Stainless Steel Machine Casing", 18, 21, false)
            .addOtherStructurePart("2 Rings of 8 Coils", "Each side of the controller")
            .addInfo("Gets 10% EU/t reduction per coil tier, up to a maximum of 50%")
            .addEnergyHatch("Any casing", 1, 2, 3)
            .addMaintenanceHatch("Any casing", 1, 2, 3)
            .addInputHatch("For cracking fluid (Steam/Hydrogen/etc.) ONLY, Any middle ring casing", 1)
            .addInputHatch("Any left/right side casing", 2, 3)
            .addOutputHatch("Any right/left side casing", 2, 3)
            .addStructureInfo("Input/Output Hatches must be on opposite sides!")
            .addStructureHint("GT5U.cracker.io_side")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sCrackingRecipes;
    }

    @Override
    protected ProcessingLogic<IVoidable, IHasWorldObjectAndCoords> getProcessingLogic() {
        if (super.getProcessingLogic() == null) {
            processingLogic = new ProcessingLogic<>() {

                @Override
                protected GT_OverclockCalculator createOverclockCalculator(GT_Recipe recipe, GT_ParallelHelper helper) {
                    return super.createOverclockCalculator(recipe, helper)
                        .setEUtDiscount(Math.max((0.1F * (heatLevel.getTier() + 1.0F)), 0.5F));
                }
            }.setController(this)
                .setTileEntity(getBaseMetaTileEntity())
                .setRecipeMap(getRecipeMap());
        }
        return super.getProcessingLogic();
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_OilCracker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public HeatingCoilLevel getCoilLevel() {
        return heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        heatLevel = aCoilLevel;
    }

    private boolean addMiddleInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mMiddleInputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addLeftHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tHatch) {
            if (mInputOnSide == 1) return false;
            mInputOnSide = 0;
            mOutputOnSide = 1;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output tHatch) {
            if (mOutputOnSide == 1) return false;
            mInputOnSide = 1;
            mOutputOnSide = 0;
            tHatch.updateTexture(aBaseCasingIndex);
            return mOutputHatches.add(tHatch);
        }
        return false;
    }

    private boolean addRightHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tHatch) {
            if (mInputOnSide == 0) return false;
            mInputOnSide = 1;
            mOutputOnSide = 0;
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tHatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output tHatch) {
            if (mOutputOnSide == 0) return false;
            mInputOnSide = 0;
            mOutputOnSide = 1;
            tHatch.updateTexture(aBaseCasingIndex);
            return mOutputHatches.add(tHatch);
        }
        return false;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        setCoilLevel(HeatingCoilLevel.None);
        mCasingAmount = 0;
        mMiddleInputHatches.clear();
        mInputOnSide = -1;
        mOutputOnSide = -1;
        replaceDeprecatedCoils(aBaseMetaTileEntity);
        return checkPiece(STRUCTURE_PIECE_MAIN, 2, 1, 0) && mInputOnSide != -1
            && mOutputOnSide != -1
            && mCasingAmount >= 18
            && mMaintenanceHatches.size() == 1
            && !mMiddleInputHatches.isEmpty();
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
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OilCracker(this.mName);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ;
        final int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        final int tY = aBaseMetaTileEntity.getYCoord();
        final int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos += (xDir != 0 ? 1 : 2))
            for (int yPos = tY - 1; yPos <= tY + 1; yPos++)
                for (int zPos = tZ - 1; zPos <= tZ + 1; zPos += (xDir != 0 ? 2 : 1)) {
                    if ((yPos == tY) && (xPos == tX || zPos == tZ)) continue;
                    final byte tUsedMeta = aBaseMetaTileEntity.getMetaID(xPos, yPos, zPos);
                    if (tUsedMeta < 12) continue;
                    if (tUsedMeta > 14) continue;
                    if (aBaseMetaTileEntity.getBlock(xPos, yPos, zPos) != GregTech_API.sBlockCasings1) continue;

                    aBaseMetaTileEntity.getWorld()
                        .setBlock(xPos, yPos, zPos, GregTech_API.sBlockCasings5, tUsedMeta - 12, 3);
                }
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        final ArrayList<FluidStack> rList = new ArrayList<>();
        for (final GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                if (isValidMetaTileEntity(tHatch)) {
                    for (final FluidStack tFluid : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                        if (tFluid != null && !GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tFluid)) {
                            rList.add(tFluid);
                        }
                    }
                }
            } else {
                if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                    if (!GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tHatch.getFillableStack()))
                        rList.add(tHatch.getFillableStack());
                }
            }
        }
        for (final GT_MetaTileEntity_Hatch_Input tHatch : mMiddleInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                if (isValidMetaTileEntity(tHatch)) {
                    for (final FluidStack tFluid : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                        if (tFluid != null && GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tFluid)) {
                            rList.add(tFluid);
                        }
                    }
                }
            } else {
                if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                    final FluidStack tStack = tHatch.getFillableStack();
                    if (GT_Recipe.GT_Recipe_Map.sCrackingRecipes.isValidCatalystFluid(tStack)) {
                        rList.add(tStack);
                    }
                }
            }
        }
        return rList;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
