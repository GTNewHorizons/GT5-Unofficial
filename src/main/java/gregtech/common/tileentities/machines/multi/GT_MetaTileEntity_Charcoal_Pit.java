package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TooltipMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.GT_Pollution;

public class GT_MetaTileEntity_Charcoal_Pit extends GT_MetaTileEntity_TooltipMultiBlockBase
    implements ISecondaryDescribable {

    private boolean running = false;

    public GT_MetaTileEntity_Charcoal_Pit(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Charcoal_Pit(String aName) {
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

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
            GT_Pollution.addPollution(getBaseMetaTileEntity(), mMaxProgresstime * getPollutionPerTick(null));
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
                            GregTech_API.sBlockReinforced,
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
            || (aX == 0 && aY == -1 && aZ == 0 && blockYPos == GregTech_API.sBlockMachines))) {
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
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionCharcoalPitPerSecond;
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
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Charcoal_Pit(mName);
    }

    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Charcoal Pile Igniter")
            .addInfo("Controller for the Charcoal Pit")
            .addInfo("Converts Logs into Brittle Charcoal blocks")
            .addInfo("Will automatically start when valid")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginVariableStructureBlock(3, 13, 3, 7, 3, 13, false)
            .addStructureInfo("Can be up to 13x7x13 in size, including the dirt; shape doesn't matter")
            .addOtherStructurePart("Controller", "Top layer, directly touching a wood log")
            .addOtherStructurePart("Dirt/Grass", "Top and middle layers, covering wood logs")
            .addOtherStructurePart("Bricks", "Bottom layer, under all wood logs")
            .addOtherStructurePart("Wood Logs", "Up to 5 layers, inside the previously mentioned blocks")
            .addStructureInfo("No air between logs allowed.")
            .addStructureInfo(
                "All logs must be within 6 x/z of the controller, so it must be dead-center for a full 11x11 square of wood.")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == ForgeDirection.UP) {
            if (aActive) return new ITexture[] { casingTexturePages[0][10],
                TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][10], TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_GLOW)
                    .glow()
                    .build(), };
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
