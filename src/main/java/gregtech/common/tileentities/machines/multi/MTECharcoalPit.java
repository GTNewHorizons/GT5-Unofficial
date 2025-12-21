package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CHARCOAL_PIT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CHARCOAL_PIT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CHARCOAL_PIT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETooltipMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.pollution.Pollution;

public class MTECharcoalPit extends MTETooltipMultiBlockBase implements ISecondaryDescribable {

    private boolean running = false;

    private static final Block EtFuturumDirtPath = GameRegistry.findBlock("etfuturum", "grass_path");

    public MTECharcoalPit(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECharcoalPit(String aName) {
        super(aName);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return (facing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // No GUI, do not capture right-click so it does not interfere when placing logs
        return false;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        if (!checkRecursiveBlocks()) {
            mEfficiency = 0;
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
            running = false;
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (mEfficiency == 0) {
            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = Math.max(1, mMaxProgresstime);

            // adds all the pollution at once when the recipe starts
            Pollution.addPollution(getBaseMetaTileEntity(), mMaxProgresstime * getPollutionPerTick(null));
            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else {
            mEfficiency = 0;
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    private boolean checkRecursiveBlocks() {
        ArrayList<ChunkPosition> tList1 = new ArrayList<>();
        ArrayList<ChunkPosition> tList2 = new ArrayList<>();

        Block tBlock = getBaseMetaTileEntity().getBlockOffset(0, -1, 0);
        if (isWoodLog(tBlock, getBaseMetaTileEntity().getMetaIDOffset(0, -1, 0))) {
            tList2.add(new ChunkPosition(0, -1, 0));
        } else return false;
        while (!tList2.isEmpty()) {
            ChunkPosition tPos = tList2.get(0);
            tList2.remove(0);
            if (!checkAllBlockSides(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ, tList1, tList2)) {
                return false;
            }
        }
        if (running) {
            for (ChunkPosition tPos : tList1) {
                if (isWoodLog(
                    getBaseMetaTileEntity().getBlockOffset(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ),
                    getBaseMetaTileEntity().getMetaIDOffset(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ)))
                    getBaseMetaTileEntity().getWorld()
                        .setBlock(
                            getBaseMetaTileEntity().getXCoord() + tPos.chunkPosX,
                            getBaseMetaTileEntity().getYCoord() + tPos.chunkPosY,
                            getBaseMetaTileEntity().getZCoord() + tPos.chunkPosZ,
                            GregTechAPI.sBlockReinforced,
                            4,
                            3);
            }
            running = false;
            return false;
        } else {
            mMaxProgresstime = (int) Math.sqrt(tList1.size() * 240000);
        }
        running = true;
        return true;
    }

    private boolean isWoodLog(Block log, int meta) {
        if (log instanceof BlockLog) return true;
        for (int id : OreDictionary.getOreIDs(new ItemStack(log, 1, meta))) {
            if (OreDictionary.getOreName(id)
                .equals("logWood")) return true;
        }
        String tTool = log.getHarvestTool(meta);
        return OrePrefixes.log.contains(new ItemStack(log, 1, meta)) && ("axe".equals(tTool))
            && (log.getMaterial() == Material.wood);
    }

    private boolean checkAllBlockSides(int aX, int aY, int aZ, ArrayList<? super ChunkPosition> aList1,
        ArrayList<? super ChunkPosition> aList2) {
        boolean expandToChunkXPos = false;
        boolean expandToChunkXNeg = false;
        boolean expandToChunkYPos = false;
        boolean expandToChunkYNeg = false;
        boolean expandToChunkZPos = false;
        boolean expandToChunkZNeg = false;

        Block blockXPos = getBaseMetaTileEntity().getBlockOffset(aX + 1, aY, aZ);
        if (aX + 1 < 6 && (isWoodLog(blockXPos, getBaseMetaTileEntity().getMetaIDOffset(aX + 1, aY, aZ)))) {
            if (!aList1.contains(new ChunkPosition(aX + 1, aY, aZ))
                && (!aList2.contains(new ChunkPosition(aX + 1, aY, aZ)))) expandToChunkXPos = true;
        } else if (!(blockXPos == Blocks.dirt || blockXPos == Blocks.grass)) {
            return false;
        }

        Block blockXNeg = getBaseMetaTileEntity().getBlockOffset(aX - 1, aY, aZ);
        if (aX - 1 > -6 && (isWoodLog(blockXNeg, getBaseMetaTileEntity().getMetaIDOffset(aX - 1, aY, aZ)))) {
            if (!aList1.contains(new ChunkPosition(aX - 1, aY, aZ))
                && (!aList2.contains(new ChunkPosition(aX - 1, aY, aZ)))) expandToChunkXNeg = true;
        } else if (!(blockXNeg == Blocks.dirt || blockXNeg == Blocks.grass)) {
            return false;
        }

        Block blockYPos = getBaseMetaTileEntity().getBlockOffset(aX, aY + 1, aZ);
        if (aY + 1 < 1 && (isWoodLog(blockYPos, getBaseMetaTileEntity().getMetaIDOffset(aX, aY + 1, aZ)))) {
            if (!aList1.contains(new ChunkPosition(aX, aY + 1, aZ))
                && (!aList2.contains(new ChunkPosition(aX, aY + 1, aZ)))) expandToChunkYPos = true;
        } else if (!(blockYPos == Blocks.dirt || blockYPos == Blocks.grass
            || blockYPos == Blocks.farmland
            || (EtFuturumDirtPath != null && blockYPos == EtFuturumDirtPath)
            || (aX == 0 && aY == -1 && aZ == 0 && blockYPos == GregTechAPI.sBlockMachines))) {
                return false;
            }

        Block blockYNeg = getBaseMetaTileEntity().getBlockOffset(aX, aY - 1, aZ);
        if (aY - 1 > -6 && (isWoodLog(blockYNeg, getBaseMetaTileEntity().getMetaIDOffset(aX, aY - 1, aZ)))) {
            if (!aList1.contains(new ChunkPosition(aX, aY - 1, aZ))
                && (!aList2.contains(new ChunkPosition(aX, aY - 1, aZ)))) expandToChunkYNeg = true;
        } else if (blockYNeg != Blocks.brick_block) {
            return false;
        }

        Block blockZPos = getBaseMetaTileEntity().getBlockOffset(aX, aY, aZ + 1);
        if (aZ + 1 < 6 && (isWoodLog(blockZPos, getBaseMetaTileEntity().getMetaIDOffset(aX, aY, aZ + 1)))) {
            if (!aList1.contains(new ChunkPosition(aX, aY, aZ + 1))
                && (!aList2.contains(new ChunkPosition(aX, aY, aZ + 1)))) expandToChunkZPos = true;
        } else if (!(blockZPos == Blocks.dirt || blockZPos == Blocks.grass)) {
            return false;
        }

        Block blockZNeg = getBaseMetaTileEntity().getBlockOffset(aX, aY, aZ - 1);
        if (aZ - 1 > -6 && (isWoodLog(blockZNeg, getBaseMetaTileEntity().getMetaIDOffset(aX, aY, aZ - 1)))) {
            if (!aList1.contains(new ChunkPosition(aX, aY, aZ - 1))
                && (!aList2.contains(new ChunkPosition(aX, aY, aZ - 1)))) expandToChunkZNeg = true;
        } else if (!(blockZNeg == Blocks.dirt || blockZNeg == Blocks.grass)) {
            return false;
        }
        aList1.add(new ChunkPosition(aX, aY, aZ));
        if (expandToChunkXPos) aList2.add(new ChunkPosition(aX + 1, aY, aZ));
        if (expandToChunkXNeg) aList2.add(new ChunkPosition(aX - 1, aY, aZ));
        if (expandToChunkYPos) aList2.add(new ChunkPosition(aX, aY + 1, aZ));
        if (expandToChunkYNeg) aList2.add(new ChunkPosition(aX, aY - 1, aZ));
        if (expandToChunkZPos) aList2.add(new ChunkPosition(aX, aY, aZ + 1));
        if (expandToChunkZNeg) aList2.add(new ChunkPosition(aX, aY, aZ - 1));
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionCharcoalPitPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECharcoalPit(mName);
    }

    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Charcoal Pile Igniter, CPI")
            .addInfo("gt.charcoal_pit.tips.1")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginVariableStructureBlock(3, 13, 3, 7, 3, 13, false)
            .addStructureInfo("gt.charcoal_pit.info.1")
            .addController("gt.charcoal_pit.info.2")
            .addStructurePart("gt.charcoal_pit.info.3", "gt.charcoal_pit.info.4")
            .addStructurePart("tile.brick.name", "gt.charcoal_pit.info.5")
            .addStructurePart("gt.charcoal_pit.info.6", "gt.charcoal_pit.info.7")
            .addStructureInfo("gt.charcoal_pit.info.8")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == ForgeDirection.UP) {
            if (aActive) return new ITexture[] { casingTexturePages[0][10],
                TextureFactory.of(OVERLAY_CHARCOAL_PIT_ACTIVE), TextureFactory.builder()
                    .addIcon(OVERLAY_CHARCOAL_PIT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][10], TextureFactory.of(OVERLAY_CHARCOAL_PIT) };
        }
        return new ITexture[] { casingTexturePages[0][10] };
    }

    @Override
    public boolean polluteEnvironment(int aPollutionLevel) {
        // Do nothing and don't choke on pollution. This is fine because we add
        // all the pollution at once when the recipe starts
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {

            new WorldSpawnedEventBuilder.ParticleEventBuilder().setMotion(0D, 0.3D, 0D)
                .setIdentifier(ParticleFX.LARGE_SMOKE)
                .setPosition(
                    aBaseMetaTileEntity.getOffsetX(ForgeDirection.UP, 1) + XSTR_INSTANCE.nextFloat(),
                    aBaseMetaTileEntity.getOffsetY(ForgeDirection.UP, 1),
                    aBaseMetaTileEntity.getOffsetZ(ForgeDirection.UP, 1) + XSTR_INSTANCE.nextFloat())
                .setWorld(getBaseMetaTileEntity().getWorld())
                .run();
        }
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
