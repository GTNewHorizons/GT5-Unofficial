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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.BW_BLOCKS;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_EICPistonVisualizer;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.net.EICPacket;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_ElectricImplosionCompressor
        extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_TileEntity_ElectricImplosionCompressor>
        implements ISurvivalConstructable {

    private static final boolean pistonEnabled = !ConfigHandler.disablePistonInEIC;
    private Boolean piston = true;
    private static final SoundResource sound = SoundResource.RANDOM_EXPLODE;
    private final ArrayList<ChunkCoordinates> chunkCoordinates = new ArrayList<>(5);
    private int mBlockTier = 0;
    private int mCasing;

    public GT_TileEntity_ElectricImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ElectricImplosionCompressor(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_ElectricImplosionCompressor> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_TileEntity_ElectricImplosionCompressor>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                            new String[][] { { "ccc", "cec", "ccc" }, { "ttt", "tft", "ttt" }, { "ttt", "tft", "ttt" },
                                    { "nnn", "nnn", "nnn" }, { "nNn", "NNN", "nNn" }, { "nnn", "nnn", "nnn" },
                                    { "t~t", "tft", "ttt" }, { "ttt", "tft", "ttt" }, { "CCC", "CeC", "CCC" }, }))
            .addElement('c', ofChain(ofBlock(GregTech_API.sBlockCasings2, 0), ofBlock(GregTech_API.sBlockCasings3, 4)))
            .addElement('t', ofBlock(BW_BLOCKS[2], 1)).addElement('f', ofBlock(BW_BLOCKS[2], 0))
            .addElement(
                    'n',
                    StructureUtility.ofBlocksTiered(
                            tieredBlockConverter(),
                            getAllBlockTiers(),
                            0,
                            GT_TileEntity_ElectricImplosionCompressor::setBlockTier,
                            GT_TileEntity_ElectricImplosionCompressor::getBlockTier))
            .addElement(
                    'C',
                    buildHatchAdder(GT_TileEntity_ElectricImplosionCompressor.class)
                            .atLeast(InputBus, OutputBus, Maintenance, InputHatch, OutputHatch)
                            .casingIndex(CASING_INDEX).dot(1).buildAndChain(
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings2, 0)),
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings3, 4))))
            .addElement(
                    'e',
                    buildHatchAdder(GT_TileEntity_ElectricImplosionCompressor.class).atLeast(Energy.or(ExoticEnergy))
                            .casingIndex(CASING_INDEX).dot(2).buildAndChain(
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings2, 0)),
                                    onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings3, 4))))
            .addElement('N', new IStructureElement<>() {

                // Much of this based on StructureUtility.ofBlocksTiered
                private final List<Pair<Block, Integer>> tiers = getAllBlockTiers();

                @Override
                public boolean check(GT_TileEntity_ElectricImplosionCompressor te, World world, int x, int y, int z) {
                    if (!te.piston && !world.isAirBlock(x, y, z)) return false;
                    if (te.piston) {
                        Block candidate = world.getBlock(x, y, z);
                        int candidateMeta = world.getBlockMetadata(x, y, z);
                        return getTierOfBlock(candidate, candidateMeta) != -1;
                    }
                    return true;
                }

                private Pair<Block, Integer> getTier(ItemStack trigger) {
                    return this.tiers.get(Math.min(Math.max(trigger.stackSize, 1), this.tiers.size()) - 1);
                }

                @Override
                public boolean spawnHint(GT_TileEntity_ElectricImplosionCompressor te, World world, int x, int y, int z,
                        ItemStack itemStack) {
                    Pair<Block, Integer> tier = this.getTier(itemStack);
                    if (te.piston) StructureLibAPI.hintParticle(world, x, y, z, tier.getKey(), tier.getValue());
                    return true;
                }

                @Override
                public boolean placeBlock(GT_TileEntity_ElectricImplosionCompressor te, World world, int x, int y,
                        int z, ItemStack itemStack) {
                    Pair<Block, Integer> tier = this.getTier(itemStack);
                    if (te.piston) world.setBlock(x, y, z, tier.getKey(), tier.getValue(), 3);
                    else world.setBlockToAir(x, y, z);
                    return true;
                }

                @Override
                public BlocksToPlace getBlocksToPlace(GT_TileEntity_ElectricImplosionCompressor t, World world, int x,
                        int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
                    return BlocksToPlace.createEmpty();
                }

                @Override
                public PlaceResult survivalPlaceBlock(GT_TileEntity_ElectricImplosionCompressor t, World world, int x,
                        int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
                    return isAir().survivalPlaceBlock(t, world, x, y, z, trigger, env);
                }
            }).build();

    public static List<Pair<Block, Integer>> getAllBlockTiers() {
        return new ArrayList<>() {

            private static final long serialVersionUID = 8171991663102417651L;

            {
                this.add(Pair.of(GregTech_API.sBlockMetal5, 2));
                this.add(Pair.of(LudicrousBlocks.resource_block, 1));
                this.add(Pair.of(GregTech_API.sBlockMetal9, 4));
                this.add(Pair.of(GregTech_API.sBlockMetal9, 3));
                this.add(Pair.of(GregTech_API.sBlockMetal9, 8));
            }

        };
    }

    public static ITierConverter<Integer> tieredBlockConverter() {
        return GT_TileEntity_ElectricImplosionCompressor::getTierOfBlock;
    }

    private static int getTierOfBlock(Block block, int meta) {
        if (block == null) {
            return -1;
        }
        if (block == GregTech_API.sBlockMetal5 && meta == 2) {
            return 1; // Neutronium
        }
        if (block == LudicrousBlocks.resource_block && meta == 1) {
            return 2; // Infinity
        }
        if (block == GregTech_API.sBlockMetal9) {
            return switch (meta) {
                case 4 -> 3; // Transcendent Metal
                case 3 -> 4; // SpaceTime
                case 8 -> 5; // Universium
                default -> -1;
            };
        }
        return -1;
    }

    private void setBlockTier(int tier) {
        this.mBlockTier = tier;
    }

    private int getBlockTier() {
        return this.mBlockTier;
    }

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
        tt.addMachineType("Implosion Compressor").addInfo("Explosions are fun")
                .addInfo("Controller block for the Electric Implosion Compressor")
                .addInfo("Uses electricity instead of Explosives").addInfo("Can parallel up to 4^(Tier - 1)")
                .addInfo("Tier is determined by containment block")
                .addInfo("Valid blocks: Neutronium, Infinity, Transcendent Metal, Spacetime, Universium")
                .addInfo("Minimum allowed energy hatch tier is one below recipe tier")
                .addInfo("Supports " + TT + " energy hatches").addSeparator().beginStructureBlock(3, 9, 3, false)
                .addController("Front 3rd layer center").addCasingInfoMin("Solid Steel Machine Casing", 8, false)
                .addStructureInfo("Casings can be replaced with Explosion Warning Signs")
                .addOtherStructurePart("Transformer-Winding Blocks", "Outer layer 2,3,7,8")
                .addOtherStructurePart("Nickel-Zinc-Ferrite Blocks", "Inner layer 2,3,7,8")
                .addOtherStructurePart("Containment Blocks", "Layer 4,5,6").addMaintenanceHatch("Any bottom casing", 1)
                .addInputBus("Any bottom casing", 1).addInputHatch("Any bottom casing", 1)
                .addOutputBus("Any bottom casing", 1).addEnergyHatch("Bottom middle and/or top middle", 2)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.electricImplosionCompressorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                // For overclocking we'll allow all power to be used
                return super.createOverclockCalculator(recipe)
                        .setEUt(GT_TileEntity_ElectricImplosionCompressor.this.getMaxInputEu()).setAmperage(1);
            }
        }.setMaxParallelSupplier(() -> (int) Math.pow(4, Math.max(this.mBlockTier - 1, 0)));
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        long amperage = this.getMaxInputAmps();
        long voltage = this.getAverageInputVoltage();
        // We allow one OC, if there is enough amperage, no matter which type of hatch is used
        logic.setAvailableVoltage(amperage >= 4 ? voltage * 4 : voltage);
        logic.setAvailableAmperage(amperage >= 4 ? amperage / 4 : amperage);
    }

    private void updateChunkCoordinates() {
        this.chunkCoordinates.clear();

        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            if (Math.abs(x) != 1 || Math.abs(z) != 1) {
                int[] abc = { x, -2, z + 1 };
                int[] xyz = { 0, 0, 0 };
                this.getExtendedFacing().getWorldOffset(abc, xyz);
                xyz[0] += this.getBaseMetaTileEntity().getXCoord();
                xyz[1] += this.getBaseMetaTileEntity().getYCoord();
                xyz[2] += this.getBaseMetaTileEntity().getZCoord();
                this.chunkCoordinates.add(new ChunkCoordinates(xyz[0], xyz[1], xyz[2]));
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.updateChunkCoordinates();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (pistonEnabled && aBaseMetaTileEntity.isActive() && aTick % 20 == 0) {
            if (aBaseMetaTileEntity.isClientSide()) this.animatePiston(aBaseMetaTileEntity);
            else if (aBaseMetaTileEntity.hasMufflerUpgrade()) MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                    aBaseMetaTileEntity.getWorld(),
                    new EICPacket(
                            new Coords(
                                    aBaseMetaTileEntity.getXCoord(),
                                    aBaseMetaTileEntity.getYCoord(),
                                    aBaseMetaTileEntity.getZCoord()),
                            true),
                    aBaseMetaTileEntity.getXCoord(),
                    aBaseMetaTileEntity.getZCoord());
        }
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        super.setExtendedFacing(newExtendedFacing); // Will call stopMachine

        this.updateChunkCoordinates();
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public void stopMachine() {
        this.resetPiston(this.mBlockTier);
        super.stopMachine();
    }

    private void resetPiston(int tier) {
        if (!pistonEnabled) return;
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!this.piston) {
            List<Pair<Block, Integer>> tiers = getAllBlockTiers();
            Pair<Block, Integer> tieredBlock = tiers.get(Math.min(tier, tiers.size()) - 1);
            this.chunkCoordinates.forEach(c -> {
                // Don't replace real blocks in case user has placed something (e.g. tier upgrade)
                if (aBaseMetaTileEntity.getWorld().isAirBlock(c.posX, c.posY, c.posZ)) {
                    aBaseMetaTileEntity.getWorld()
                            .setBlock(c.posX, c.posY, c.posZ, tieredBlock.getKey(), tieredBlock.getValue(), 3);
                }
            });
            this.piston = !this.piston;
        }
    }

    private void activatePiston() {
        if (!pistonEnabled) return;
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (this.piston) {
            this.chunkCoordinates.forEach(c -> aBaseMetaTileEntity.getWorld().setBlockToAir(c.posX, c.posY, c.posZ));
            this.piston = !this.piston;
        }
    }

    private void animatePiston(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!aBaseMetaTileEntity.getWorld().isRemote) return;

        if (!this.getBaseMetaTileEntity().hasMufflerUpgrade()) GT_Utility.doSoundAtClient(
                sound,
                10,
                1f,
                1f,
                this.chunkCoordinates.get(0).posX,
                this.chunkCoordinates.get(0).posY,
                this.chunkCoordinates.get(0).posZ);
        this.spawnVisualPistonBlocks(
                aBaseMetaTileEntity.getWorld(),
                this.chunkCoordinates.get(2).posX,
                this.chunkCoordinates.get(2).posY,
                this.chunkCoordinates.get(2).posZ,
                10);
    }

    @SideOnly(Side.CLIENT)
    private void spawnVisualPistonBlocks(World world, int x, int y, int z, int age) {
        BW_EICPistonVisualizer pistonVisualizer = new BW_EICPistonVisualizer(world, x, y, z, age);
        Minecraft.getMinecraft().effectRenderer.addEffect(pistonVisualizer);
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
        int pistonTier = this.mBlockTier;
        this.mCasing = 0;
        int mMaxHatchTier = 0;
        this.setBlockTier(0);
        boolean isOK = this.checkPiece(STRUCTURE_PIECE_MAIN, 1, 6, 0);

        List<GT_MetaTileEntity_Hatch> energyHatches = this.getExoticAndNormalEnergyHatchList();
        for (GT_MetaTileEntity_Hatch hatch : energyHatches) {
            mMaxHatchTier = Math.max(mMaxHatchTier, hatch.mTier);
        }

        isOK = isOK && this.mMaintenanceHatches.size() == 1 && energyHatches.size() >= 1;
        if (isOK) {
            this.activatePiston();
            return true;
        }
        this.resetPiston(pistonTier);
        return false;
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

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW).extFacing().glow()
                            .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW).extFacing().glow()
                            .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 6, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
            float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
