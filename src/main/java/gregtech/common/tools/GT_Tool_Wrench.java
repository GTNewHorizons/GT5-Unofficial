package gregtech.common.tools;

import static gregtech.api.items.GT_MetaGenerated_Tool.getPrimaryMaterial;

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
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.common.items.behaviors.Behaviour_Switch_Mode;
import gregtech.common.items.behaviors.Behaviour_Wrench;
import ic2.api.tile.IWrenchable;

public class GT_Tool_Wrench extends GT_Tool {

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
    public int getToolDamagePerDropConversion() {
        return 100;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 800;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 200;
    }

    @Override
    public int getBaseQuality() {
        return 0;
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
    public float getSpeedMultiplier() {
        return 1.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.IC2_TOOLS_WRENCH.toString();
    }

    @Override
    public String getEntityHitSound() {
        return null;
    }

    @Override
    public String getMiningSound() {
        return SoundResource.IC2_TOOLS_WRENCH.toString();
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean isCrowbar() {
        return false;
    }

    @Override
    public boolean isWrench() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block block, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(block, aMetaData, "wrench")
            || GT_ToolHarvestHelper.isAppropriateMaterial(block, Material.piston)
            || block instanceof AEBaseTileBlock
            || GT_ToolHarvestHelper.isSpecialBlock(block, Blocks.crafting_table, Blocks.bookshelf)
            || Behaviour_Wrench.isVanillaRotatable(block)
            || GT_ToolHarvestHelper.isIC2Wrenchable(block);
    }

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.WRENCH : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new Behaviour_Switch_Mode());
        aItem.addItemBehavior(aID, new Behaviour_Wrench(100));
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
     * {@link #convertBlockDrops(List, ItemStack, EntityPlayer, Block, int, int, int, byte, int, boolean, BlockEvent.HarvestDropsEvent)},
     * this is filled during
     * {@link #onBreakBlock(EntityPlayer, int, int, int, Block, byte, TileEntity, BlockEvent.BreakEvent)}
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
    public void onBreakBlock(@Nonnull EntityPlayer player, int x, int y, int z, @Nonnull Block block, byte metadata,
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
        int z, byte metaData, int fortune, boolean silkTouch, BlockEvent.HarvestDropsEvent event) {
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
        return 2;
    }

    @Override
    public String getToolTypeName() {
        return "wrench";
    }
}
