/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.BW_BLOCKS;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class GT_TileEntity_ElectricImplosionCompressor
        extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_ElectricImplosionCompressor> {

    public static GT_Recipe.GT_Recipe_Map eicMap;
    private static final boolean pistonEnabled = !ConfigHandler.disablePistonInEIC;
    private Boolean piston = true;
    private static final String sound = GregTech_API.sSoundList.get(5);
    private final ArrayList<ChunkCoordinates> chunkCoordinates = new ArrayList<>(5);

    public GT_TileEntity_ElectricImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ElectricImplosionCompressor(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_ElectricImplosionCompressor> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_ElectricImplosionCompressor>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {"ccc", "cec", "ccc"},
                        {"ttt", "tft", "ttt"},
                        {"ttt", "tft", "ttt"},
                        {"nnn", "nnn", "nnn"},
                        {"nNn", "NNN", "nNn"},
                        {"nnn", "nnn", "nnn"},
                        {"t~t", "tft", "ttt"},
                        {"ttt", "tft", "ttt"},
                        {"CCC", "CeC", "CCC"},
                    }))
                    .addElement(
                            'c',
                            ofChain(ofBlock(GregTech_API.sBlockCasings2, 0), ofBlock(GregTech_API.sBlockCasings3, 4)))
                    .addElement('t', ofBlock(BW_BLOCKS[2], 1))
                    .addElement('f', ofBlock(BW_BLOCKS[2], 0))
                    .addElement('n', ofBlock(GregTech_API.sBlockMetal5, 2))
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(
                                            GT_TileEntity_ElectricImplosionCompressor::addInputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_TileEntity_ElectricImplosionCompressor::addOutputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_TileEntity_ElectricImplosionCompressor::addMaintenanceToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofBlock(GregTech_API.sBlockCasings2, 0),
                                    ofBlock(GregTech_API.sBlockCasings3, 4)))
                    .addElement(
                            'e',
                            ofHatchAdder(
                                    GT_TileEntity_ElectricImplosionCompressor::addEnergyInputToMachineList,
                                    CASING_INDEX,
                                    2))
                    .addElement('N', new IStructureElement<GT_TileEntity_ElectricImplosionCompressor>() {

                        @Override
                        public boolean check(
                                GT_TileEntity_ElectricImplosionCompressor te, World world, int x, int y, int z) {
                            if (!te.piston && !world.isAirBlock(x, y, z)) return false;
                            if (te.piston
                                    && !(world.getBlock(x, y, z) == GregTech_API.sBlockMetal5
                                            && world.getBlockMetadata(x, y, z) == 2)) return false;
                            return true;
                        }

                        @Override
                        public boolean spawnHint(
                                GT_TileEntity_ElectricImplosionCompressor te,
                                World world,
                                int x,
                                int y,
                                int z,
                                ItemStack itemStack) {
                            if (te.piston) StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockMetal5, 2);
                            return true;
                        }

                        @Override
                        public boolean placeBlock(
                                GT_TileEntity_ElectricImplosionCompressor te,
                                World world,
                                int x,
                                int y,
                                int z,
                                ItemStack itemStack) {
                            if (te.piston) world.setBlock(x, y, z, GregTech_API.sBlockMetal5, 2, 3);
                            else world.setBlockToAir(x, y, z);
                            return true;
                        }
                    })
                    .build();

    @Override
    public IStructureDefinition<GT_TileEntity_ElectricImplosionCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Implosion Compressor")
                .addInfo("Explosions are fun")
                .addInfo("Controller block for the Electric Implosion Compressor")
                .addInfo("Uses electricity instead of Explosives")
                .addSeparator()
                .beginStructureBlock(3, 9, 3, false)
                .addController("Front 3rd layer center")
                .addCasingInfo("Solid Steel Machine Casing", 8)
                .addStructureInfo("Casings can be replaced with Explosion Warning Signs")
                .addOtherStructurePart("Transformer-Winding Blocks", "Outer layer 2,3,7,8")
                .addOtherStructurePart("Nickel-Zinc-Ferrite Blocks", "Inner layer 2,3,7,8")
                .addOtherStructurePart("Neutronium Blocks", "Layer 4,5,6")
                .addMaintenanceHatch("Any bottom casing", 1)
                .addInputBus("Any bottom casing", 1)
                .addInputHatch("Any bottom casing", 1)
                .addOutputBus("Any bottom casing", 1)
                .addMaintenanceHatch("Any bottom casing", 1)
                .addEnergyHatch("Bottom and top middle", 2)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        if (this.mEnergyHatches.get(0).getEUVar() <= 0
                || this.mEnergyHatches.get(1).getEUVar() <= 0) return false;

        ItemStack[] tItemInputs = getCompactedInputs();
        FluidStack[] tFluidInputs = getCompactedFluids();

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        if ((tItemInputs.length > 0) || (tFluidInputs.length > 0)) {
            GT_Recipe tRecipe = eicMap.findRecipe(getBaseMetaTileEntity(), false, V[tTier], tFluidInputs, tItemInputs);
            if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluidInputs, tItemInputs)) {
                this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
                this.mEfficiencyIncrease = 10000;
                calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
                // In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) return false;
                if (this.mEUt > 0) {
                    this.mEUt = -this.mEUt;
                }
                this.mOutputItems = tRecipe.mOutputs.clone();
                this.mOutputFluids = tRecipe.mFluidOutputs.clone();
                this.updateSlots();
                return true;
            }
        }
        return false;
    }

    private void updateChunkCoordinates() {
        chunkCoordinates.clear();

        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++) {
                if (!(Math.abs(x) == 1 && Math.abs(z) == 1)) {
                    int[] abc = new int[] {x, -2, z + 1};
                    int[] xyz = new int[] {0, 0, 0};
                    this.getExtendedFacing().getWorldOffset(abc, xyz);
                    xyz[0] += this.getBaseMetaTileEntity().getXCoord();
                    xyz[1] += this.getBaseMetaTileEntity().getYCoord();
                    xyz[2] += this.getBaseMetaTileEntity().getZCoord();
                    chunkCoordinates.add(new ChunkCoordinates(xyz[0], xyz[1], xyz[2]));
                }
            }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        updateChunkCoordinates();
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0) return true;
        GT_MetaTileEntity_Hatch_Energy h1 = this.mEnergyHatches.get(0), h2 = this.mEnergyHatches.get(1);
        if (!isValidMetaTileEntity(h1) || !isValidMetaTileEntity(h2)) return false;
        if (!h1.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU / 2, false)
                || !h2.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU / 2, false)) return false;
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (pistonEnabled && aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isActive() && aTick % 10 == 0)
            togglePiston(aBaseMetaTileEntity);
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        super.setExtendedFacing(newExtendedFacing);

        updateChunkCoordinates();
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    public void stopMachine() {
        if (pistonEnabled) this.resetPiston();
        super.stopMachine();
    }

    private void resetPiston() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!this.piston && this.mMachine) {
            chunkCoordinates.forEach(c ->
                    aBaseMetaTileEntity.getWorld().setBlock(c.posX, c.posY, c.posZ, GregTech_API.sBlockMetal5, 2, 3));
            this.piston = !this.piston;
        }
    }

    private void togglePiston(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.getWorld().isRemote) return;
        if (this.piston) {
            for (ChunkCoordinates c : chunkCoordinates) {
                if (aBaseMetaTileEntity.getBlock(c.posX, c.posY, c.posZ) != GregTech_API.sBlockMetal5
                        || aBaseMetaTileEntity.getMetaID(c.posX, c.posY, c.posZ) != 2) {
                    this.explodeMultiblock();
                    return;
                }
                aBaseMetaTileEntity.getWorld().setBlockToAir(c.posX, c.posY, c.posZ);
            }

        } else {
            chunkCoordinates.forEach(c ->
                    aBaseMetaTileEntity.getWorld().setBlock(c.posX, c.posY, c.posZ, GregTech_API.sBlockMetal5, 2, 3));
            if (!getBaseMetaTileEntity().hasMufflerUpgrade())
                GT_Utility.sendSoundToPlayers(
                        aBaseMetaTileEntity.getWorld(),
                        sound,
                        1f,
                        1f,
                        chunkCoordinates.get(0).posX,
                        chunkCoordinates.get(0).posY,
                        chunkCoordinates.get(0).posZ);
        }
        this.piston = !this.piston;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("piston", this.piston);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("piston")) this.piston = aNBT.getBoolean("piston");
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 6, 0)) return false;
        return this.mMaintenanceHatches.size() == 1 && this.mEnergyHatches.size() == 2;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_ElectricImplosionCompressor(this.mName);
    }

    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 6, 0);
    }
}
