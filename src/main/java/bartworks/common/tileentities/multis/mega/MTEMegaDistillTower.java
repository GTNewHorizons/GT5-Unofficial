/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.multis.mega;

import static bartworks.util.BWTooltipReference.MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementCheckOnly;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.ConfigHandler;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchOutputME;

public class MTEMegaDistillTower extends MegaMultiBlockBase<MTEMegaDistillTower> implements ISurvivalConstructable {

    protected static final int CASING_INDEX = 49;
    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "top";
    private static final IStructureDefinition<MTEMegaDistillTower> STRUCTURE_DEFINITION;

    static {
        IHatchElement<MTEMegaDistillTower> layeredOutputHatch = OutputHatch
            .withCount(MTEMegaDistillTower::getCurrentLayerOutputHatchCount)
            .withAdder(MTEMegaDistillTower::addLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<MTEMegaDistillTower>builder()
            .addShape(
                STRUCTURE_PIECE_BASE,
                transpose(
                    new String[][] { { "bbbbbbb~bbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbb" }, }))
            .addShape(
                STRUCTURE_PIECE_LAYER,
                transpose(
                    new String[][] {
                        { "lllllllllllllll", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl",
                            "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl",
                            "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl", "lcccccccccccccl",
                            "lcccccccccccccl", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" }, }))
            .addShape(
                STRUCTURE_PIECE_TOP_HINT,
                transpose(
                    new String[][] {
                        { "lllllllllllllll", "lllllllllllllll", "lllllllllllllll", "lllllllllllllll", "lllllllllllllll",
                            "lllllllllllllll", "lllllllllllllll", "lllllllllllllll", "lllllllllllllll",
                            "lllllllllllllll", "lllllllllllllll", "lllllllllllllll", "lllllllllllllll",
                            "lllllllllllllll", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" },
                        { "lllllllllllllll", "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "l=============l", "l=============l", "l=============l",
                            "l=============l", "lllllllllllllll" }, }))
            .addElement('=', StructureElementAirNoHint.getInstance())
            .addElement(
                'b',
                buildHatchAdder(MTEMegaDistillTower.class)
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .buildAndChain(
                        onElementPass(MTEMegaDistillTower::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
            .addElement(
                'l',
                buildHatchAdder(MTEMegaDistillTower.class)
                    .atLeast(layeredOutputHatch, Maintenance, Energy.or(ExoticEnergy))
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .buildAndChain(
                        onElementPass(MTEMegaDistillTower::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
            .addElement('c', (IStructureElementCheckOnly<MTEMegaDistillTower>) (t, world, x, y, z) -> {
                if (world.isAirBlock(x, y, z)) {
                    if (t.mTopState < 1) {
                        t.mTopState = 0;
                        return true;
                    }
                    // definitely top - cannot be air
                    return false;
                }
                // from here on we must be looking at a top layer, since it's not air
                if (t.mTopState == 0)
                    // must be air but failed, so no
                    return false;
                t.mTopState = 1;
                // hatch adder
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof IGregTechTileEntity entity && t.addLayerOutputHatch(entity, CASING_INDEX)) {
                    t.onTopLayerFound(false);
                    return true;
                }
                // block adder
                if (world.getBlock(x, y, z) == GregTechAPI.sBlockCasings4 && world.getBlockMetadata(x, y, z) == 1) {
                    t.onTopLayerFound(true);
                    return true;
                }
                return false;
            })
            .build();
    }

    protected final List<List<MTEHatchOutput>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    // -1 => maybe top, maybe not, 0 => definitely not top, 1 => definitely top
    private int mTopState = -1;

    public MTEMegaDistillTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTEMegaDistillTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaDistillTower(this.mName);
    }

    protected void onCasingFound() {
        this.mCasing++;
    }

    protected int getCurrentLayerOutputHatchCount() {
        return this.mOutputHatchesByLayer.size() < this.mHeight || this.mHeight <= 0 ? 0
            : this.mOutputHatchesByLayer.get(this.mHeight - 1)
                .size();
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead() || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput))
            return false;
        while (this.mOutputHatchesByLayer.size() < this.mHeight) this.mOutputHatchesByLayer.add(new ArrayList<>());
        MTEHatchOutput tHatch = (MTEHatchOutput) aTileEntity.getMetaTileEntity();
        tHatch.updateTexture(aBaseCasingIndex);
        return this.mOutputHatchesByLayer.get(this.mHeight - 1)
            .add(tHatch);
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        this.mTopLayerFound = true;
        if (aIsCasing) this.onCasingFound();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.distillationTowerRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Distillery")
            .addInfo("Controller block for the Distillation Tower")
            .addInfo("Fluids are only put out at the correct height")
            .addInfo("The correct height equals the slot number in the NEI recipe")
            .addSeparator()
            .beginVariableStructureBlock(15, 15, 16, 56, 15, 15, true)
            .addController("Front bottom")
            .addOtherStructurePart("Clean Stainless Steel Machine Casing", "15 x h - 5 (minimum)")
            .addEnergyHatch("Any casing")
            .addMaintenanceHatch("Any casing")
            .addInputHatch("Any bottom layer casing")
            .addOutputBus("Any bottom layer casing")
            .addOutputHatch("2-11x Output Hatches (One per Output Layer except bottom layer)")
            .addStructureInfo("An \"Output Layer\" consists of 5 layers!")
            .addStructureHint("The interior of this Mega Multiblock's hologram is empty, it should be all air.")
            .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS);
        return tt;
    }

    @Override
    public IStructureDefinition<MTEMegaDistillTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // reset
        this.mOutputHatchesByLayer.forEach(List::clear);
        this.mHeight = 1;
        this.mTopLayerFound = false;
        this.mTopState = -1;

        // check base
        if (!this.checkPiece(STRUCTURE_PIECE_BASE, 7, 0, 0)) return false;

        // check each layer
        while (this.mHeight < 12) {
            if (!checkPiece(STRUCTURE_PIECE_LAYER, 7, mHeight * 5, 0)) {
                return false;
            }
            if (this.mOutputHatchesByLayer.size() < this.mHeight || this.mOutputHatchesByLayer.get(this.mHeight - 1)
                .isEmpty())
                // layer without output hatch
                return false;
            if (mTopLayerFound) {
                break;
            }
            this.mTopState = -1;
            // not top
            this.mHeight++;
        }

        // validate final invariants...
        return this.mCasing >= 75 * this.mHeight + 10 && this.mHeight >= 2
            && this.mTopLayerFound
            && this.mMaintenanceHatches.size() == 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 7, 0, 0);
        int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            this.buildPiece(STRUCTURE_PIECE_LAYER, stackSize, hintsOnly, 7, 5 * i, 0);
        }
        this.buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 7, 5 * (tTotalHeight - 1), 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        this.mHeight = 0;
        int built = this
            .survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 7, 0, 0, realBudget, source, actor, false, true);
        if (built >= 0) return built;
        int tTotalHeight = Math.min(12, stackSize.stackSize + 2); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            this.mHeight = i;
            built = this.survivialBuildPiece(
                STRUCTURE_PIECE_LAYER,
                stackSize,
                7,
                5 * this.mHeight,
                0,
                realBudget,
                source,
                actor,
                false,
                true);
            if (built >= 0) return built;
        }
        this.mHeight = tTotalHeight - 1;
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_TOP_HINT,
            stackSize,
            7,
            5 * this.mHeight,
            0,
            realBudget,
            source,
            actor,
            false,
            true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            this.batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallel(ConfigHandler.megaMachinesMax);
    }

    @Override
    public boolean canDumpFluidToME() {

        // All fluids can be dumped to ME only if each layer contains a ME Output Hatch.
        for (List<MTEHatchOutput> tLayerOutputHatches : this.mOutputHatchesByLayer) {

            boolean foundMEHatch = false;

            for (IFluidStore tHatch : tLayerOutputHatches) {
                if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                    if (tMEHatch.canAcceptFluid()) {
                        foundMEHatch = true;
                        break;
                    }
                }
            }

            // Exit if we didn't find a valid hatch on this layer.
            if (!foundMEHatch) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length && i < this.mOutputHatchesByLayer.size(); i++) {
            FluidStack tStack = mOutputFluids2[i].copy();
            if (!dumpFluid(this.mOutputHatchesByLayer.get(i), tStack, true))
                dumpFluid(this.mOutputHatchesByLayer.get(i), tStack, false);
        }
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return this.getFluidOutputSlotsByLayer(toOutput, this.mOutputHatchesByLayer);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
