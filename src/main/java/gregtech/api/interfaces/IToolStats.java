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

import org.jetbrains.annotations.Range;

import gregtech.api.items.MetaGeneratedTool;

/**
 * The stats for GT tools. Not including any material modifiers.
 * <p/>
 * And this is supposed to not have any ItemStack parameters as these are generic stats.
 */
public interface IToolStats {

    /**
     * Called when a player crafts this tool
     */
    void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Called when this gets added to a Tool Item
     */
    void onStatsAddedToTool(MetaGeneratedTool aItem, int aID);

    /**
     * @implNote if you are only modifying drops, override
     *           {@link #convertBlockDrops(List, ItemStack, EntityPlayer, Block, int, int, int, int, int, boolean, BlockEvent.HarvestDropsEvent)}
     * @param player   the player
     * @param x        Block pos
     * @param y        Block pos
     * @param z        Block pos
     * @param block    the block
     * @param metadata block metadata
     * @param tile     TileEntity of the block if exist
     * @param event    event, cancel it to prevent the block from being broken
     */
    default void onBreakBlock(@Nonnull EntityPlayer player, int x, int y, int z, @Nonnull Block block, int metadata,
        @Nullable TileEntity tile, @Nonnull BlockEvent.BreakEvent event) {}

    /**
     * @return damage the tool receives when breaking a block. 100 is one damage point (or 100 EU).
     */
    int getToolDamagePerBlockBreak();

    /**
     * @return damage the tool receives when converting the drops of a block. 100 is one damage point (or 100 EU).
     */
    int getToolDamagePerDropConversion();

    /**
     * @return damage the tool receives when being used as a container item. 100 is one use, however, it is usually 8
     *         times
     *         more than normal.
     */
    int getToolDamagePerContainerCraft();

    /**
     * @return damage the tool receives when being used as a weapon; 200 is the normal value, 100 for actual weapons.
     */
    int getToolDamagePerEntityAttack();

    /**
     * @return Basic quality of the tool, 0 is normal. If increased, it will increase the general quality of all tools
     *         of this type. Decreasing is also possible.
     */
    int getBaseQuality();

    /**
     * @return The damage bonus for this type of tool against mobs. 1.0F is a normal punch.
     */
    float getBaseDamage();

    /**
     * @return This gets the hurt resistance time for entities getting hit. (always does 1 as a minimum)
     */
    int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity);

    /**
     * @return This is a multiplier for the tool speed. 1.0F = no special speed.
     */
    float getSpeedMultiplier();

    /**
     * @return This is a multiplier for the tool speed. 1.0F = no special durability.
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
     * @return If this tool can be used for blocking damage like a sword.
     */
    boolean canBlock();

    /**
     * @return If this tool can be used as an RC crowbar.
     */
    boolean isCrowbar();

    /**
     * @return If this tool can be used as an FR grafter.
     */
    boolean isGrafter();

    boolean isChainsaw();

    boolean isSaw();

    /**
     * @return If this tool can be used as a BC wrench.
     */
    boolean isWrench();

    /**
     * @return if this tool can be used as a PR screwdriver
     */
    default boolean isScrewdriver() {
        return false;
    }

    /**
     * @return If this Tool can be used as a weapon i.e., if that is the main purpose.
     */
    boolean isWeapon();

    /**
     * @return If this tool is a ranged weapon. Return false at isWeapon unless you have a blade attached to your
     *         bow, gun, or something
     */
    boolean isRangedWeapon();

    /**
     * @return If this tool can be used as a weapon i.e., if that is the main purpose.
     */
    boolean isMiningTool();

    /**
     * {@link Block#getHarvestTool(int)} can return the following values, for example, "axe", "pickaxe", "sword",
     * "shovel", "hoe", "grafter", "saw", "wrench", "crowbar", "file", "hammer", "plow", "plunger", "scoop",
     * "screwdriver", "sense", "scythe", "softmallet", "cutter", and "plasmatorch".
     *
     * @return If this is a minable Block. Tool quality checks (like diamond tier or something) are separate from this
     *         check.
     */
    boolean isMinableBlock(Block aBlock, int aMetaData);

    /**
     * This lets you modify the drop list when this type of tool has been used.
     *
     * @return the number of modified items used to determine the extra durability cost
     */
    int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY,
        int aZ, int aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent);

    /**
     * @return Returns a broken version of the item.
     */
    ItemStack getBrokenItem(ItemStack aStack);

    /**
     * @return the damage actually done to the mob.
     */
    float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * @return the damage actually done to the mob.
     */
    float getMagicDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

    IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack);

    short[] getRGBa(boolean aIsToolHead, ItemStack aStack);

    float getMiningSpeed(Block aBlock, int aMetaData, float aDefault, EntityPlayer aPlayer, World worldObj, int aX,
        int aY, int aZ);

    /**
     * Get the overridden block strength for this tool.
     *
     * @param block                the block to break
     * @param player               the player breaking the block
     * @param world                the world the block is in
     * @param x                    the x coordinate of the block
     * @param y                    the y coordinate of the block
     * @param z                    the z coordinate of the block
     * @param defaultBlockStrength the default block strength (the default return value)
     * @return the new block strength
     */
    default float getBlockStrength(ItemStack tool, Block block, EntityPlayer player, World world, int x, int y, int z,
        float defaultBlockStrength) {
        return defaultBlockStrength;
    }

    default String getToolTypeName() {
        return null;
    }

    /**
     * @return the number of the modes this tool has.
     */
    @Range(from = 1, to = Byte.MAX_VALUE)
    default byte getMaxMode() {
        return 1;
    }
}
