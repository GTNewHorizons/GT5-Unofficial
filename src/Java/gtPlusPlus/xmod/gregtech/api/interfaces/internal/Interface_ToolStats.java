package gtPlusPlus.xmod.gregtech.api.interfaces.internal;

import java.util.List;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaTool;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.world.BlockEvent;

/**
 * The Stats for GT Tools. Not including any Material Modifiers.
 * <p/>
 * And this is supposed to not have any ItemStack Parameters as these are generic Stats.
 */
public interface Interface_ToolStats extends IToolStats{
	/**
	 * Called when aPlayer crafts this Tool
	 */
	@Override
	public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer);

	/**
	 * Called when this gets added to a Tool Item
	 */
	public void onStatsAddedToTool(Gregtech_MetaTool gregtech_MetaTool, int aID);

	/**
	 * @return Damage the Tool receives when breaking a Block. 100 is one Damage Point (or 100 EU).
	 */
	@Override
	public int getToolDamagePerBlockBreak();

	/**
	 * @return Damage the Tool receives when converting the drops of a Block. 100 is one Damage Point (or 100 EU).
	 */
	@Override
	public int getToolDamagePerDropConversion();

	/**
	 * @return Damage the Tool receives when being used as Container Item. 100 is one use, however it is usually 8 times more than normal.
	 */
	@Override
	public int getToolDamagePerContainerCraft();

	/**
	 * @return Damage the Tool receives when being used as Weapon, 200 is the normal Value, 100 for actual Weapons.
	 */
	@Override
	public int getToolDamagePerEntityAttack();

	/**
	 * @return Basic Quality of the Tool, 0 is normal. If increased, it will increase the general quality of all Tools of this Type. Decreasing is also possible.
	 */
	@Override
	public int getBaseQuality();

	/**
	 * @return The Damage Bonus for this Type of Tool against Mobs. 1.0F is normal punch.
	 */
	@Override
	public float getBaseDamage();

	/**
	 * @return This gets the Hurt Resistance time for Entities getting hit. (always does 1 as minimum)
	 */
	@Override
	public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity);

	/**
	 * @return This is a multiplier for the Tool Speed. 1.0F = no special Speed.
	 */
	@Override
	public float getSpeedMultiplier();

	/**
	 * @return This is a multiplier for the Tool Speed. 1.0F = no special Durability.
	 */
	@Override
	public float getMaxDurabilityMultiplier();

	@Override
	public DamageSource getDamageSource(EntityLivingBase aPlayer, Entity aEntity);

	@Override
	public String getMiningSound();

	@Override
	public String getCraftingSound();

	@Override
	public String getEntityHitSound();

	@Override
	public String getBreakingSound();

	@Override
	public Enchantment[] getEnchantments(ItemStack aStack);

	@Override
	public int[] getEnchantmentLevels(ItemStack aStack);

	/**
	 * @return If this Tool can be used for blocking Damage like a Sword.
	 */
	@Override
	public boolean canBlock();

	/**
	 * @return If this Tool can be used as an RC Crowbar.
	 */
	@Override
	public boolean isCrowbar();

	/**
	 * @return If this Tool can be used as an BC Wrench.
	 */
	@Override
	public boolean isWrench();

	/**
	 * @return If this Tool can be used as Weapon i.e. if that is the main purpose.
	 */
	@Override
	public boolean isWeapon();

	/**
	 * @return If this Tool is a Ranged Weapon. Return false at isWeapon unless you have a Blade attached to your Bow/Gun or something
	 */
	@Override
	public boolean isRangedWeapon();

	/**
	 * @return If this Tool can be used as Weapon i.e. if that is the main purpose.
	 */
	@Override
	public boolean isMiningTool();

	/**
	 * aBlock.getHarvestTool(aMetaData) can return the following Values for example.
	 * "axe", "pickaxe", "sword", "shovel", "hoe", "grafter", "saw", "wrench", "crowbar", "file", "hammer", "plow", "plunger", "scoop", "screwdriver", "sense", "scythe", "softhammer", "cutter", "plasmatorch"
	 *
	 * @return If this is a minable Block. Tool Quality checks (like Diamond Tier or something) are separate from this check.
	 */
	@Override
	public boolean isMinableBlock(Block aBlock, byte aMetaData);

	/**
	 * This lets you modify the Drop List, when this type of Tool has been used.
	 *
	 * @return the Amount of modified Items.
	 */
	@Override
	public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent);

	/**
	 * @return Returns a broken Version of the Item.
	 */
	@Override
	public ItemStack getBrokenItem(ItemStack aStack);

	/**
	 * @return the Damage actually done to the Mob.
	 */
	@Override
	public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

	/**
	 * @return the Damage actually done to the Mob.
	 */
	@Override
	public float getMagicDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer);

	@Override
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack);

	@Override
	public short[] getRGBa(boolean aIsToolHead, ItemStack aStack);

	/**
	 * Called when this gets added to a Tool Item
	 */
	@Override
	public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID);
}