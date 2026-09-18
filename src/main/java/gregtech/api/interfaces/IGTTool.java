package gregtech.api.interfaces;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import gregtech.api.enums.Materials;

/**
 * The common surface of every GregTech tool item, regardless of how that item stores its material and stats.
 * <p/>
 * Two implementations exist:
 * <ul>
 * <li>{@link gregtech.api.items.MetaGeneratedTool}, where the metadata is a tool type and the material lives in NBT,
 * and</li>
 * <li>the standalone single-purpose tool items in {@code gregtech.common.items.tools}, where the item itself
 * identifies the tool type and the metadata is the material's {@link Materials#mMetaItemSubID}.</li>
 * </ul>
 * Code that only needs to ask "is this a GT tool, and what are its stats" should dispatch on this interface rather
 * than on {@code MetaGeneratedTool}, so that both families are handled.
 */
public interface IGTTool {

    /**
     * @return the generic stats of this tool, or null if the stack is not a valid tool.
     */
    IToolStats getToolStats(ItemStack stack);

    /**
     * @return the primary (head) material of this tool, or {@link Materials#_NULL} if it has none.
     */
    Materials getToolMaterial(ItemStack stack);

    /**
     * @return the material of this tool's handle, which is what a tool with a visible handle renders the lower half
     *         of its icon from. Tools without a handle never ask for it.
     */
    Materials getToolHandleMaterial(ItemStack stack);

    /**
     * @return the damage this tool has accumulated, in the internal unit where 100 is one durability point. Always 0
     *         for tools that do not wear out, such as the electric wrenches.
     */
    long getStoredDamage(ItemStack stack);

    /**
     * @return the damage at which this tool breaks, in the same unit as {@link #getStoredDamage(ItemStack)}. 0 means
     *         the tool does not wear out, in which case {@link #getStoredDamage(ItemStack)} is meaningless.
     */
    long getMaxStoredDamage(ItemStack stack);

    /**
     * Applies the cost of one action to this tool: durability, energy, or both, depending on the tool.
     *
     * @param amount the cost, in the unit where 100 is one durability point (and one EU for electric tools).
     * @return whether the tool could pay the cost, i.e. whether the action may proceed.
     */
    boolean doDamage(ItemStack stack, long amount);

    /**
     * Spends one action's worth of this tool and reports whether it could be paid for: a durability point for a tool
     * that wears out, or its energy cost per action for one that runs on EU.
     */
    boolean spendOneUse(ItemStack stack);

    /**
     * @return the energy stored on this tool, or 0 if it is not electric.
     */
    long getStoredCharge(ItemStack stack);

    /**
     * @return the energy this tool can hold, or 0 if it is not electric.
     */
    long getMaxStoredCharge(ItemStack stack);

    /**
     * @return the currently selected mode of this tool, counting from 0. Tools with a single mode always return 0.
     */
    byte getMode(ItemStack stack);

    /**
     * @return whether the mode could be stored on the stack.
     */
    boolean setMode(ItemStack stack, byte mode);

    /**
     * @return how many modes this tool has. 1 means the tool cannot switch modes.
     */
    byte getMaxMode(ItemStack stack);

    /**
     * @return the strength with which this tool breaks the given block, overriding Forge's default. Used by the
     *         wrench's precise mode, which caps the strength below the instant-break threshold.
     */
    float getBlockStrength(ItemStack stack, Block block, EntityPlayer player, World world, int x, int y, int z,
        float defaultBlockStrength);

    /**
     * Called by the Block Break Speed Event within the GTProxy.
     */
    float onBlockBreakSpeedEvent(float defaultSpeed, ItemStack stack, EntityPlayer player, Block block, int x, int y,
        int z, int metaData, PlayerEvent.BreakSpeed event);

    /**
     * Called by the Block Harvesting Event within the GTProxy.
     */
    void onHarvestBlockEvent(ArrayList<ItemStack> drops, ItemStack stack, EntityPlayer player, Block block, int x,
        int y, int z, int metaData, int fortune, boolean silkTouch, BlockEvent.HarvestDropsEvent event);
}
