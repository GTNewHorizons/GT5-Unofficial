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

package bartworks.common.tileentities.multis;

import static bartworks.common.loaders.ItemRegistry.BW_BLOCKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.client.renderer.EICPistonVisualizer;
import bartworks.common.configs.Configuration;
import bartworks.common.net.PacketEIC;
import bartworks.util.Coords;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.misc.GTStructureChannels;

public class MTEElectricImplosionCompressor extends MTEExtendedPowerMultiBlockBase<MTEElectricImplosionCompressor>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private static final boolean pistonEnabled = !Configuration.multiblocks.disablePistonInEIC;
    private boolean piston = true;
    private static final SoundResource sound = SoundResource.RANDOM_EXPLODE;
    private final ArrayList<ChunkCoordinates> chunkCoordinates = new ArrayList<>(5);
    private int mBlockTier = 0;
    private int mCasing;
    private boolean isSuccessful = false;

    public MTEElectricImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEElectricImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEElectricImplosionCompressor(this.mName);
    }

    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SUCCESSFUL = "main_successful";
    private static final String[][] shape = new String[][] { { "CCC", "CBC", "CCC" }, { "DDD", "DGD", "DDD" },
        { "DDD", "DGD", "DDD" }, { "EEE", "EEE", "EEE" }, { "EFE", "FFF", "EFE" }, { "EEE", "EEE", "EEE" },
        { "D~D", "DGD", "DDD" }, { "DDD", "DGD", "DDD" }, { "CCC", "CBC", "CCC" } };

    public static ImmutableList<Pair<Block, Integer>> getTierBlockList() {
        ImmutableList.Builder<Pair<Block, Integer>> builder = ImmutableList.builder();

        builder.add(Pair.of(GregTechAPI.sBlockMetal5, 2));

        if (Mods.Avaritia.isModLoaded()) {
            builder.add(Pair.of(LudicrousBlocks.resource_block, 1));
        }

        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 4));
        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 3));
        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 8));

        return builder.build();
    }

    @Nullable
    public static Integer getTierBlock(Block block, int meta) {
        if (block == null) return null;
        if (block == GregTechAPI.sBlockMetal5 && meta == 2) return 1;

        if (Mods.Avaritia.isModLoaded()) {
            if (block == LudicrousBlocks.resource_block && meta == 1) return 2;
            if (block == GregTechAPI.sBlockMetal9 && meta == 4) return 3;
            if (block == GregTechAPI.sBlockMetal9 && meta == 3) return 4;
            if (block == GregTechAPI.sBlockMetal9 && meta == 8) return 5;
        } else {
            if (block == GregTechAPI.sBlockMetal9 && meta == 4) return 2;
            if (block == GregTechAPI.sBlockMetal9 && meta == 3) return 3;
            if (block == GregTechAPI.sBlockMetal9 && meta == 8) return 4;
        }
        return null;
    }

    @Override
    public IStructureDefinition<MTEElectricImplosionCompressor> getStructureDefinition() {
        return StructureDefinition.<MTEElectricImplosionCompressor>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addShape(
                STRUCTURE_PIECE_MAIN_SUCCESSFUL,
                Arrays.stream(transpose(shape))
                    .map(
                        sa -> Arrays.stream(sa)
                            .map(s -> s.replaceAll("F", "H"))
                            .toArray(String[]::new))
                    .toArray(String[][]::new))
            .addElement(
                'B',
                buildHatchAdder(MTEElectricImplosionCompressor.class).atLeast(Energy.or(ExoticEnergy))
                    .casingIndex(CASING_INDEX)
                    .hint(2)
                    .buildAndChain(
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTechAPI.sBlockCasings2, 0)),
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTechAPI.sBlockCasings3, 4))))
            .addElement(
                'C',
                buildHatchAdder(MTEElectricImplosionCompressor.class)
                    .atLeast(InputBus, OutputBus, Maintenance, InputHatch, OutputHatch)
                    .casingIndex(CASING_INDEX)
                    .hint(1)
                    .buildAndChain(
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTechAPI.sBlockCasings2, 0)),
                        onElementPass(x -> ++x.mCasing, ofBlock(GregTechAPI.sBlockCasings3, 4))))
            .addElement('D', ofBlock(BW_BLOCKS[2], 1))
            .addElement(
                'E',
                GTStructureChannels.EIC_PISTON.use(
                    StructureUtility.ofBlocksTiered(
                        MTEElectricImplosionCompressor::getTierBlock,
                        getTierBlockList(),
                        -1,
                        (t, m) -> t.mBlockTier = m,
                        t -> t.mBlockTier)))
            .addElement(
                'F',
                GTStructureChannels.EIC_PISTON.use(
                    StructureUtility.ofBlocksTiered(
                        MTEElectricImplosionCompressor::getTierBlock,
                        getTierBlockList(),
                        -1,
                        (t, m) -> t.mBlockTier = m,
                        t -> t.mBlockTier)))
            .addElement('G', ofBlock(BW_BLOCKS[2], 0))
            .addElement('H', isAir())
            .build();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Implosion Compressor, EIC")
            .addInfo("Explosions are fun!")
            .addInfo("Uses electricity instead of Explosives")
            .addInfo(
                EnumChatFormatting.GOLD + "Parallels"
                    + EnumChatFormatting.GRAY
                    + " are determined by "
                    + EnumChatFormatting.WHITE
                    + "Containment Block"
                    + EnumChatFormatting.GRAY
                    + " Tier")
            .addInfo(createParallelText(EnumChatFormatting.WHITE, "Neutronium", 1))
            .addInfo(createParallelText(EnumChatFormatting.RED, "Infinity", 4))
            .addInfo(createParallelText(EnumChatFormatting.DARK_GRAY, "Transcendent Metal", 16))
            .addInfo(createParallelText(EnumChatFormatting.LIGHT_PURPLE, "Spacetime", 64))
            .addInfo(createParallelText(EnumChatFormatting.DARK_AQUA, "Universium", 256))
            .addMaxTierSkips(1)
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 9, 3, false)
            .addController("Front 3rd layer center")
            .addCasingInfoMin("Solid Steel Machine Casing", 8, false)
            .addStructureInfo("Casings can be replaced with Explosion Hazard Signs")
            .addOtherStructurePart("Transformer-Winding Blocks", "Outer layer 2,3,7,8")
            .addOtherStructurePart("Nickel-Zinc-Ferrite Blocks", "Inner layer 2,3,7,8")
            .addOtherStructurePart("Containment Blocks", "Layer 4,5,6")
            .addMaintenanceHatch("Any Solid Steel Machine casing", 1)
            .addInputBus("Any Solid Steel Machine casing", 1)
            .addInputHatch("Any Solid Steel Machine casing", 1)
            .addOutputBus("Any Solid Steel Machine casing", 1)
            .addEnergyHatch("Bottom middle and/or top middle", 2)
            .addSubChannelUsage(GTStructureChannels.EIC_PISTON)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.electricImplosionCompressorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) GTUtility.powInt(4, Math.max(this.mBlockTier - 1, 0));
    }

    private void updateChunkCoordinates() {
        this.chunkCoordinates.clear();

        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            if (Math.abs(x) != 1 || Math.abs(z) != 1) {
                int[] abc = { x, -2, z + 1 };
                int[] xyz = { 0, 0, 0 };
                this.getExtendedFacing()
                    .getWorldOffset(abc, xyz);
                xyz[0] += this.getBaseMetaTileEntity()
                    .getXCoord();
                xyz[1] += this.getBaseMetaTileEntity()
                    .getYCoord();
                xyz[2] += this.getBaseMetaTileEntity()
                    .getZCoord();
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
            else if (aBaseMetaTileEntity.isMuffled()) GTValues.NW.sendPacketToAllPlayersInRange(
                aBaseMetaTileEntity.getWorld(),
                new PacketEIC(
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
    public void stopMachine(@NotNull ShutDownReason reason) {
        this.resetPiston(this.mBlockTier);
        super.stopMachine(reason);
    }

    private void resetPiston(int tier) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!this.piston) {
            List<Pair<Block, Integer>> tiers = getTierBlockList();
            final int index = Math.max(0, Math.min(tier, tiers.size()) - 1);
            Pair<Block, Integer> tieredBlock = tiers.get(index);
            this.chunkCoordinates.forEach(c -> {
                // Don't replace real blocks in case user has placed something (e.g. tier upgrade)
                if (aBaseMetaTileEntity.getWorld()
                    .isAirBlock(c.posX, c.posY, c.posZ)) {
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
            this.chunkCoordinates.forEach(
                c -> aBaseMetaTileEntity.getWorld()
                    .setBlockToAir(c.posX, c.posY, c.posZ));
            this.piston = !this.piston;
            this.isSuccessful = true;
        }
    }

    private void animatePiston(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!aBaseMetaTileEntity.getWorld().isRemote) return;

        if (!this.getBaseMetaTileEntity()
            .isMuffled())
            GTUtility.doSoundAtClient(
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
            this.chunkCoordinates.get(2).posZ);
    }

    @SideOnly(Side.CLIENT)
    private void spawnVisualPistonBlocks(World world, int x, int y, int z) {
        EICPistonVisualizer pistonVisualizer = new EICPistonVisualizer(world, x, y, z, 10);
        Minecraft.getMinecraft().effectRenderer.addEffect(pistonVisualizer);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("piston", this.piston);
        aNBT.setBoolean("isSuccessful", this.isSuccessful);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("piston")) this.piston = aNBT.getBoolean("piston");
        if (aNBT.hasKey("isSuccessful")) this.isSuccessful = aNBT.getBoolean("isSuccessful");
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        int pistonTier = this.mBlockTier;
        int mMaxHatchTier = 0;
        boolean isOK;
        this.mCasing = 0;
        this.mBlockTier = -1;

        if (this.isSuccessful) {
            isOK = this.checkPiece(STRUCTURE_PIECE_MAIN_SUCCESSFUL, 1, 6, 0);
        } else {
            isOK = this.checkPiece(STRUCTURE_PIECE_MAIN, 1, 6, 0);
        }

        List<MTEHatch> energyHatches = this.getExoticAndNormalEnergyHatchList();
        for (MTEHatch hatch : energyHatches) {
            mMaxHatchTier = Math.max(mMaxHatchTier, hatch.mTier);
        }

        isOK = isOK && this.mMaintenanceHatches.size() == 1 && !energyHatches.isEmpty();
        if (isOK) {
            this.activatePiston();
            return true;
        }

        this.isSuccessful = false;
        this.resetPiston(pistonTier);
        return false;
    }

    @Override
    public void onBlockDestroyed() {
        this.resetPiston(this.mBlockTier);
        super.onBlockDestroyed();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.isSuccessful = false;
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        this.isSuccessful = false;
        return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 6, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public void onPreviewStructureComplete(@NotNull ItemStack trigger) {
        resetPiston(this.mBlockTier);
    }

    private String createParallelText(EnumChatFormatting blockColor, String block, int parallels) {
        return String.format(
            "%s%s%s : %s%d%s %s",
            blockColor,
            block,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.GOLD,
            parallels,
            EnumChatFormatting.GRAY,
            parallels == 1 ? "Parallel" : "Parallels");
    }
}
