package gregtech.common.tools;

import static gregtech.api.items.MetaGeneratedTool.getPrimaryMaterial;
import static gregtech.api.items.MetaGeneratedTool.getToolMode;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import appeng.api.parts.IPartHost;
import appeng.block.AEBaseTileBlock;
import appeng.parts.PartPlacement;
import appeng.util.Platform;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;
import gregtech.common.items.behaviors.BehaviourSwitchMode;
import gregtech.common.items.behaviors.BehaviourWrench;
import ic2.api.tile.IWrenchable;

public class ToolWrench extends GTTool {

    // according to https://minecraft.wiki/w/Instant_mining,
    // the instant break won't be active if it is less than 1.0F
    public static final float INSTANT_BREAK_THRESHOLD = 0.99F;

    public static final List<String> mEffectiveList = Arrays
        .asList(EntityIronGolem.class.getName(), "EntityTowerGuardian");

    @Override
    public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack,
        EntityPlayer aPlayer) {
        String tName = aEntity.getClass()
            .getName();
        tName = tName.substring(tName.lastIndexOf('.') + 1);
        return (mEffectiveList.contains(tName)) || (tName.contains("Golem")) ? aOriginalDamage * 2.0F : aOriginalDamage;
    }

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public float getBaseDamage() {
        return 3.0F;
    }

    @Override
    public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {
        return aOriginalHurtResistance * 2;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.GTCEU_OP_WRENCH.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.GTCEU_OP_WRENCH.toString();
    }

    @Override
    public boolean isWrench() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block block, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(block, aMetaData, "wrench")
            || GTToolHarvestHelper.isAppropriateMaterial(block, Material.piston)
            || block instanceof AEBaseTileBlock
            || GTToolHarvestHelper.isSpecialBlock(block, Blocks.crafting_table, Blocks.bookshelf)
            || BehaviourWrench.isVanillaRotatable(block)
            || GTToolHarvestHelper.isIC2Wrenchable(block);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_wrench]
            : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourSwitchMode());
        aItem.addItemBehavior(aID, new BehaviourWrench(100));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.GREEN + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " threw a Monkey Wrench into the Plans of "
                + EnumChatFormatting.RED
                + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }

    /**
     * <p>
     * holding drop from {@link IWrenchable#getWrenchDrop(EntityPlayer)}
     * </p>
     * Since no tile available during
     * {@link #convertBlockDrops(List, ItemStack, EntityPlayer, Block, int, int, int, int, int, boolean, BlockEvent.HarvestDropsEvent)},
     * this is filled during
     * {@link #onBreakBlock(EntityPlayer, int, int, int, Block, int, TileEntity, BlockEvent.BreakEvent)}
     */
    private ItemStack wrenchableDrop = null;
    /**
     * <p>
     * drop rate from {@link IWrenchable#getWrenchDropRate()}
     * </p>
     * see {@link #wrenchableDrop}
     */
    private float wrenchableDropRate = 0.0f;

    /**
     * <p>
     * prevent recursion from
     * {@link AEBaseTileBlock#onBlockActivated(World, int, int, int, EntityPlayer, int, float, float, float)}
     * </p>
     */
    private boolean LastEventFromThis = false;

    @Override
    public float getBlockStrength(ItemStack tool, Block block, EntityPlayer player, World world, int x, int y, int z,
        float defaultBlockStrength) {
        if (getToolMode(tool) == 2) { // Precise Mode
            return Math.min(INSTANT_BREAK_THRESHOLD, defaultBlockStrength);
        }
        return super.getBlockStrength(tool, block, player, world, x, y, z, defaultBlockStrength);
    }

    @Override
    public void onBreakBlock(@Nonnull EntityPlayer player, int x, int y, int z, @Nonnull Block block, int metadata,
        TileEntity tile, @Nonnull BlockEvent.BreakEvent event) {
        if (tile instanceof IWrenchable wrenchable) {
            if (!wrenchable.wrenchCanRemove(player)) {
                event.setCanceled(true);
                return;
            }
            wrenchableDrop = wrenchable.getWrenchDrop(player);
            wrenchableDropRate = wrenchable.getWrenchDropRate();
        }
        if (block instanceof AEBaseTileBlock aeBaseTileBlock) {
            if (LastEventFromThis) {
                return;
            }
            final boolean sneak = player.isSneaking();
            try {
                LastEventFromThis = true;
                player.setSneaking(true);
                final MovingObjectPosition movObjPosition = Platform.rayTrace(player, true, false);
                if (movObjPosition == null) {
                    event.setCanceled(true);
                    return;
                }

                final int sideHit = movObjPosition.sideHit;
                if (tile instanceof IPartHost) {
                    if (sneak && PartPlacement.place(
                        player.getHeldItem(),
                        x,
                        y,
                        z,
                        sideHit,
                        player,
                        player.worldObj,
                        PartPlacement.PlaceType.INTERACT_FIRST_PASS,
                        0)) {
                        event.setCanceled(true);
                    }
                    return;
                }
                if (aeBaseTileBlock.onBlockActivated(event.world, x, y, z, player, sideHit, x, y, z)) {
                    event.setCanceled(true);
                }
            } finally {
                LastEventFromThis = false;
                player.setSneaking(sneak);
            }
        }
    }

    @Override
    public int convertBlockDrops(List<ItemStack> drops, ItemStack Stack, EntityPlayer player, Block block, int x, int y,
        int z, int metaData, int fortune, boolean silkTouch, BlockEvent.HarvestDropsEvent event) {
        ItemStack drop = null;
        int modified = 0;
        if (wrenchableDrop != null) {
            drop = wrenchableDrop;
            wrenchableDrop = null;
            modified = wrenchableDropRate == 1.0f ? 3 : 10;
            wrenchableDropRate = 0.0f;
        } else if (block == Blocks.bookshelf || block == Blocks.ender_chest) {
            drop = new ItemStack(block);
            modified = 1;
        }

        if (drop != null) {
            event.dropChance = 1.0f;
            drops.clear();
            drops.add(drop);
        }
        return modified;
    }

    @Override
    public byte getMaxMode() {
        return 3;
    }

    @Override
    public String getToolTypeName() {
        return "wrench";
    }
}
