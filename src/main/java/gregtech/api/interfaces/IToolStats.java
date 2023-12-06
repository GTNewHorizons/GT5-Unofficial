package gregtech.api.interfaces;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.items.GT_MetaGenerated_Tool;

/**
 * The Stats for GT Tools. Not including any Material Modifiers.
 * <p/>
 * And this is supposed to not have any ItemStack Parameters as these are generic Stats.
 */
public interface IToolStats {

    /**
     * Called when aPlayer crafts this Tool
     */
    void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Called when this gets added to a Tool Item
     */
    void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID);

    /**
     * @implNote if you are only modifying drops, override
     *           {@link #convertBlockDrops(List, ItemStack, EntityPlayer, Block, int, int, int, byte, int, boolean, BlockEvent.HarvestDropsEvent)}
     * @param player   The player
     * @param x        Block pos
     * @param y        Block pos
     * @param z        Block pos
     * @param block    the block
     * @param metadata block metadata
     * @param tile     TileEntity of the block if exist
     * @param event    the event, cancel it to prevent the block from being broken
     */
    default void onBreakBlock(@Nonnull EntityPlayer player, int x, int y, int z, @Nonnull Block block, byte metadata,
        @Nullable TileEntity tile, @Nonnull BlockEvent.BreakEvent event) {}

    /**
     * @return Damage the Tool receives when breaking a Block. 100 is one Damage Point (or 100 EU).
     */
    int getToolDamagePerBlockBreak();

    /**
     * @return Damage the Tool receives when converting the drops of a Block. 100 is one Damage Point (or 100 EU).
     */
    int getToolDamagePerDropConversion();

    /**
     * @return Damage the Tool receives when being used as Container Item. 100 is one use, however it is usually 8 times
     *         more than normal.
     */
    int getToolDamagePerContainerCraft();

    /**
     * @return Damage the Tool receives when being used as Weapon, 200 is the normal Value, 100 for actual Weapons.
     */
    int getToolDamagePerEntityAttack();

    /**
     * @return Basic Quality of the Tool, 0 is normal. If increased, it will increase the general quality of all Tools
     *         of this Type. Decreasing is also possible.
     */
    int getBaseQuality();

    /**
     * @return The Damage Bonus for this Type of Tool against Mobs. 1.0F is normal punch.
     */
    float getBaseDamage();

    /**
     * @return This gets the Hurt Resistance time for Entities getting hit. (always does 1 as minimum)
     */
    int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity);

    /**
     * @return This is a multiplier for the Tool Speed. 1.0F = no special Speed.
     */
    float getSpeedMultiplier();

    /**
     * @return This is a multiplier for the Tool Speed. 1.0F = no special Durability.
     */
    float getMaxDurabilityMultiplier();

    DamageSource getDamageSource(EntityLivingBase aPlayer, Entity aEntity);

    String getMiningSound();

    String getCraftingSound();

    String getEntityHitSound();

    String getBreakingSound();

    Enchantment[] getEnchantments(ItemStack aStack);

    int[] getEnchantmentLevels(ItemStack aStack);

    /**
     * @return If this Tool can be used for blocking Damage like a Sword.
     */
    boolean canBlock();

    /**
     * @return If this Tool can be used as an RC Crowbar.
     */
    boolean isCrowbar();

    /**
     * @return If this Tool can be used as an FR Grafter.
     */
    boolean isGrafter();

    boolean isChainsaw();

    /**
     * @return If this Tool can be used as an BC Wrench.
     */
    boolean isWrench();

    /**
     * @return if this Tool can be used as an PR screwdriver
     */
    default boolean isScrewdriver() {
        return false;
    }

    /**
     * @return If this Tool can be used as Weapon i.e. if that is the main purpose.
     */
    boolean isWeapon();

    /**
     * @return If this Tool is a Ranged Weapon. Return false at isWeapon unless you have a Blade attached to your
     *         Bow/Gun or something
     */
    boolean isRangedWeapon();

    /**
     * @return If this Tool can be used as Weapon i.e. if that is the main purpose.
     */
    boolean isMiningTool();

    /**
     * {@link Block#getHarvestTool(int)} can return the following Values for example. "axe", "pickaxe", "sword",
     * "shovel", "hoe", "grafter", "saw", "wrench", "crowbar", "file", "hammer", "plow", "plunger", "scoop",
     * "screwdriver", "sense", "scythe", "softhammer", "cutter", "plasmatorch"
     *
     * @return If this is a minable Block. Tool Quality checks (like Diamond Tier or something) are separate from this
     *         check.
     */
    boolean isMinableBlock(Block aBlock, byte aMetaData);

    /**
     * This lets you modify the Drop List, when this type of Tool has been used.
     *
     * @return the Amount of modified Items, used to determine the extra durability cost
     */
    int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY,
        int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent);

    /**
     * @return Returns a broken Version of the Item.
     */
    ItemStack getBrokenItem(ItemStack aStack);

    /**
     * @return the Damage actually done to the Mob.
     */
    float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * @return the Damage actually done to the Mob.
     */
    float getMagicDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

    IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack);

    short[] getRGBa(boolean aIsToolHead, ItemStack aStack);

    float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World worldObj, int aX,
        int aY, int aZ);
}
