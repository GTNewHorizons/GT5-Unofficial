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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis.mega;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;

public class GT_TileEntity_MegaVacuumFreezer extends GT_TileEntity_MegaMultiBlockBase<GT_TileEntity_MegaVacuumFreezer>
    implements ISurvivalConstructable {

    public GT_TileEntity_MegaVacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_MegaVacuumFreezer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TileEntity_MegaVacuumFreezer(this.mName);
    }

    private int mCasingFrostProof = 0;
    private int mTier = 1;

    public static class SubspaceCoolingFluid {

        public Materials material = null;
        public int perfectOverclocks = 0;
        public long amount = 0;
    }

    private boolean subspaceCoolingActive = false;

    private static final int CASING_INDEX = 17;
    private static final int CASING_INDEX_T2 = ((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings8)
        .getTextureIndex(14);
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_T2 = "main_t2";

    private static final String[][] structure = transpose(
        new String[][] {
            { "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccc~ccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc" } });
    private static final String[][] structure_tier2 = transpose(
        new String[][] {
            { "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccc~ccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "c             c",
                "c             c", "c             c", "c             c", "c             c", "ccccccccccccccc" },
            { "ddddddddddddddd", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "d             d",
                "d             d", "d             d", "d             d", "d             d", "ddddddddddddddd" },
            { "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc",
                "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc", "ccccccccccccccc" } });

    private static final IStructureDefinition<GT_TileEntity_MegaVacuumFreezer> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_TileEntity_MegaVacuumFreezer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(STRUCTURE_PIECE_MAIN_T2, structure_tier2)
        .addElement(
            'c',
            buildHatchAdder(GT_TileEntity_MegaVacuumFreezer.class)
                .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(x -> x.mCasingFrostProof++, ofBlock(GregTech_API.sBlockCasings2, 1))))
        .addElement(
            'd',
            buildHatchAdder(GT_TileEntity_MegaVacuumFreezer.class).casingIndex(CASING_INDEX_T2)
                .dot(2)
                // Tier 2 uses infinity cooled casing
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings8, 14)))
        .build();

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Vacuum Freezer")
            .addInfo("Controller Block for the Mega Vacuum Freezer")
            .addInfo("Cools hot ingots and cells")
            // TODO: Add info about subspace cooling
            .addSeparator()
            .beginStructureBlock(15, 15, 15, true)
            .addController("Front center")
            .addCasingInfoMin("Frost Proof Machine Casing", 800, false)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addOutputHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public IStructureDefinition<GT_TileEntity_MegaVacuumFreezer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        if (aStack.stackSize == 1) {
            this.buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 7, 7, 0);
        } else {
            this.buildPiece(STRUCTURE_PIECE_MAIN_T2, aStack, aHintsOnly, 7, 7, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        if (stackSize.stackSize == 1) {
            return this
                .survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 7, 0, realBudget, source, actor, false, true);
        } else {
            return this.survivialBuildPiece(
                STRUCTURE_PIECE_MAIN_T2,
                stackSize,
                7,
                7,
                0,
                realBudget,
                source,
                actor,
                false,
                true);
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            this.batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
        if (aNBT.hasKey("subspaceActive")) {
            this.subspaceCoolingActive = aNBT.getBoolean("subspaceActive");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("subspaceActive", subspaceCoolingActive);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallel(ConfigHandler.megaMachinesMax);
    }

    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasingFrostProof = 0;
        this.mTier = 1;
        // If check for T1 fails, also do a check for T2 structure
        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 7, 7, 0)) {
            // Reset mCasing in between checks, so they don't count again
            this.mCasingFrostProof = 0;
            if (!this.checkPiece(STRUCTURE_PIECE_MAIN_T2, 7, 7, 0)) {
                return false;
            }
            // Structure is Tier 2
            this.mTier = 2;
        }
        return this.mMaintenanceHatches.size() == 1 && this.mCasingFrostProof >= 800;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { casingTexturePages[0][17] };
        }
        return rTexture;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Tier: " + mTier);
        return info.toArray(new String[] {});
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
