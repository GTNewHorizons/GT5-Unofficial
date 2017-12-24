package gtPlusPlus.xmod.gregtech.api.items.tools;

import java.util.*;
import java.util.Map.Entry;

import gregtech.api.GregTech_API;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.*;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_ToolStats;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

/**
 * This is an example on how you can create a Tool ItemStack, in this case a Bismuth Wrench:
 * GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01").getToolWithStats(16, 1, Materials.Bismuth, Materials.Bismuth, null);
 */
public abstract class GT_MetaGenTool extends GT_MetaGenerated_Tool {
	/**
	 * All instances of this Item Class are listed here.
	 * This gets used to register the Renderer to all Items of this Type, if useStandardMetaItemRenderer() returns true.
	 * <p/>
	 * You can also use the unlocalized Name gotten from getUnlocalizedName() as Key if you want to get a specific Item.
	 */
	public static final HashMap<String, GT_MetaGenTool> sInstances = new HashMap<>();

	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */

	public final HashMap<Short, IToolStats> mToolStats = new HashMap<>();

	/**
	 * Creates the Item using these Parameters.
	 *
	 * @param aUnlocalized The Unlocalized Name of this Item.
	 */
	public GT_MetaGenTool(final String aUnlocalized) {
		super(aUnlocalized);
		GT_ModHandler.registerBoxableItemToToolBox(this);
		this.setCreativeTab(GregTech_API.TAB_GREGTECH);
		this.setMaxStackSize(1);
		sInstances.put(this.getUnlocalizedName(), this);
	}

	/* ---------- FOR ADDING CUSTOM ITEMS INTO THE REMAINING 766 RANGE ---------- */

	public static final Materials getPrimaryMaterialEx(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if (aNBT != null) {
				return Materials.getRealMaterial(aNBT.getString("PrimaryMaterial"));
			}
		}
		return Materials._NULL;
	}

	public static final Materials getSecondaryMaterialEx(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if (aNBT != null) {
				return Materials.getRealMaterial(aNBT.getString("SecondaryMaterial"));
			}
		}
		return Materials._NULL;
	}

	/**
	 * This adds a Custom Item to the ending Range.
	 *
	 * @param aID                     The Id of the assigned Tool Class [0 - 32765] (only even Numbers allowed! Uneven ID's are empty electric Items)
	 * @param aEnglish                The Default Localized Name of the created Item
	 * @param aToolTip                The Default ToolTip of the created Item, you can also insert null for having no ToolTip
	 * @param aToolStats              The Food Value of this Item. Can be null as well.
	 * @param aOreDictNamesAndAspects The OreDict Names you want to give the Item. Also used to assign Thaumcraft Aspects.
	 * @return An ItemStack containing the newly created Item, but without specific Stats.
	 */
	public final ItemStack addToolEx(final int aID, final String aEnglish, String aToolTip, final IToolStats aToolStats, final Object... aOreDictNamesAndAspects) {
		if (aToolTip == null) {
			aToolTip = "";
		}
		if ((aID >= 0) && (aID < 32766) && ((aID % 2) == 0)) {
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + aID + ".name", aEnglish);
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + aID + ".tooltip", aToolTip);
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + (aID + 1) + ".name", aEnglish + " (Empty)");
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + (aID + 1) + ".tooltip", "You need to recharge it");
			this.mToolStats.put((short) aID, aToolStats);
			this.mToolStats.put((short) (aID + 1), aToolStats);
			aToolStats.onStatsAddedToTool(this, aID);
			final ItemStack rStack = new ItemStack(this, 1, aID);
			final List<TC_AspectStack> tAspects = new ArrayList<>();
			for (final Object tOreDictNameOrAspect : aOreDictNamesAndAspects) {
				if (tOreDictNameOrAspect instanceof TC_AspectStack) {
					((TC_AspectStack) tOreDictNameOrAspect).addToAspectList(tAspects);
				} else {
					GT_OreDictUnificator.registerOre(tOreDictNameOrAspect, rStack);
				}
			}
			if (GregTech_API.sThaumcraftCompat != null) {
				GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
			}
			return rStack;
		}
		return null;
	}

	/**
	 * This Function gets an ItemStack Version of this Tool
	 *
	 * @param aToolID            the ID of the Tool Class
	 * @param aAmount            Amount of Items (well normally you only need 1)
	 * @param aPrimaryMaterial   Primary Material of this Tool
	 * @param aSecondaryMaterial Secondary (Rod/Handle) Material of this Tool
	 * @param aElectricArray     The Electric Stats of this Tool (or null if not electric)
	 */
	public final ItemStack getToolWithStatsEx(final int aToolID, final int aAmount, final Materials aPrimaryMaterial, final Materials aSecondaryMaterial, final long[] aElectricArray) {
		final ItemStack rStack = new ItemStack(this, aAmount, aToolID);
		final IToolStats tToolStats = this.getToolStats(rStack);
		if (tToolStats != null) {
			final NBTTagCompound tMainNBT = new NBTTagCompound(), tToolNBT = new NBTTagCompound();
			if (aPrimaryMaterial != null) {
				tToolNBT.setString("PrimaryMaterial", aPrimaryMaterial.toString());
				tToolNBT.setLong("MaxDamage", 100L * (long) (aPrimaryMaterial.mDurability * tToolStats.getMaxDurabilityMultiplier()));
			}
			if (aSecondaryMaterial != null) {
				tToolNBT.setString("SecondaryMaterial", aSecondaryMaterial.toString());
			}

			if (aElectricArray != null) {
				tToolNBT.setBoolean("Electric", true);
				tToolNBT.setLong("MaxCharge", aElectricArray[0]);
				tToolNBT.setLong("Voltage", aElectricArray[1]);
				tToolNBT.setLong("Tier", aElectricArray[2]);
				tToolNBT.setLong("SpecialData", aElectricArray[3]);
			}

			tMainNBT.setTag("GT.ToolStats", tToolNBT);
			rStack.setTagCompound(tMainNBT);
		}
		this.isItemStackUsable(rStack);
		return rStack;
	}

	/**
	 * Called by the Block Harvesting Event within the GT_Proxy
	 */
	@Override
	public void onHarvestBlockEvent(final ArrayList<ItemStack> aDrops, final ItemStack aStack, final EntityPlayer aPlayer, final Block aBlock, final int aX, final int aY, final int aZ, final byte aMetaData, final int aFortune, final boolean aSilkTouch, final BlockEvent.HarvestDropsEvent aEvent) {
		final IToolStats tStats = this.getToolStats(aStack);
		if (this.isItemStackUsable(aStack) && (this.getDigSpeed(aStack, aBlock, aMetaData) > 0.0F)) {
			this.doDamage(aStack, tStats.convertBlockDrops(aDrops, aStack, aPlayer, aBlock, aX, aY, aZ, aMetaData, aFortune, aSilkTouch, aEvent) * tStats.getToolDamagePerDropConversion());
		}
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack aStack, final EntityPlayer aPlayer, final Entity aEntity) {
		final IToolStats tStats = this.getToolStats(aStack);
		if ((tStats == null) || !this.isItemStackUsable(aStack)) {
			return true;
		}
		GT_Utility.doSoundAtClient(tStats.getEntityHitSound(), 1, 1.0F);
		if (super.onLeftClickEntity(aStack, aPlayer, aEntity)) {
			return true;
		}
		if (aEntity.canAttackWithItem() && !aEntity.hitByEntity(aPlayer)) {
			final float tMagicDamage = tStats.getMagicDamageAgainstEntity(aEntity instanceof EntityLivingBase ? EnchantmentHelper.getEnchantmentModifierLiving(aPlayer, (EntityLivingBase) aEntity) : 0.0F, aEntity, aStack, aPlayer);
			float tDamage = tStats.getNormalDamageAgainstEntity((float) aPlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() + this.getToolCombatDamage(aStack), aEntity, aStack, aPlayer);
			if ((tDamage + tMagicDamage) > 0.0F) {
				final boolean tCriticalHit = (aPlayer.fallDistance > 0.0F) && !aPlayer.onGround && !aPlayer.isOnLadder() && !aPlayer.isInWater() && !aPlayer.isPotionActive(Potion.blindness) && (aPlayer.ridingEntity == null) && (aEntity instanceof EntityLivingBase);
				if (tCriticalHit && (tDamage > 0.0F)) {
					tDamage *= 1.5F;
				}
				tDamage += tMagicDamage;
				if (aEntity.attackEntityFrom(tStats.getDamageSource(aPlayer, aEntity), tDamage)) {
					if (aEntity instanceof EntityLivingBase) {
						aEntity.setFire(EnchantmentHelper.getFireAspectModifier(aPlayer) * 4);
					}
					final int tKnockcack = (aPlayer.isSprinting() ? 1 : 0) + (aEntity instanceof EntityLivingBase ? EnchantmentHelper.getKnockbackModifier(aPlayer, (EntityLivingBase) aEntity) : 0);
					if (tKnockcack > 0) {
						aEntity.addVelocity(-MathHelper.sin((aPlayer.rotationYaw * (float) Math.PI) / 180.0F) * tKnockcack * 0.5F, 0.1D, MathHelper.cos((aPlayer.rotationYaw * (float) Math.PI) / 180.0F) * tKnockcack * 0.5F);
						aPlayer.motionX *= 0.6D;
						aPlayer.motionZ *= 0.6D;
						aPlayer.setSprinting(false);
					}
					if (tCriticalHit) {
						aPlayer.onCriticalHit(aEntity);
					}
					if (tMagicDamage > 0.0F) {
						aPlayer.onEnchantmentCritical(aEntity);
					}
					if (tDamage >= 18.0F) {
						aPlayer.triggerAchievement(AchievementList.overkill);
					}
					aPlayer.setLastAttacker(aEntity);
					if (aEntity instanceof EntityLivingBase) {
						EnchantmentHelper.func_151384_a((EntityLivingBase) aEntity, aPlayer);
					}
					EnchantmentHelper.func_151385_b(aPlayer, aEntity);
					if (aEntity instanceof EntityLivingBase) {
						aPlayer.addStat(StatList.damageDealtStat, Math.round(tDamage * 10.0F));
					}
					aEntity.hurtResistantTime = Math.max(1, tStats.getHurtResistanceTime(aEntity.hurtResistantTime, aEntity));
					aPlayer.addExhaustion(0.3F);
					this.doDamage(aStack, tStats.getToolDamagePerEntityAttack());
				}
			}
		}
		if (aStack.stackSize <= 0) {
			aPlayer.destroyCurrentEquippedItem();
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
		final IToolStats tStats = this.getToolStats(aStack);
		if ((tStats != null) && tStats.canBlock()) {
			aPlayer.setItemInUse(aStack, 72000);
		}
		return super.onItemRightClick(aStack, aWorld, aPlayer);
	}


	@Override
	public Long[] getFluidContainerStats(final ItemStack aStack) {
		return null;
	}

	@Override
	public Long[] getElectricStats(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if ((aNBT != null) && aNBT.getBoolean("Electric")) {
				return new Long[]{aNBT.getLong("MaxCharge"), aNBT.getLong("Voltage"), aNBT.getLong("Tier"), aNBT.getLong("SpecialData")};
			}
		}
		return null;
	}

	@Override
	public float getToolCombatDamage(final ItemStack aStack) {
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats == null) {
			return 0;
		}
		return tStats.getBaseDamage() + getPrimaryMaterial(aStack).mToolQuality;
	}

	@Override
	public float getDigSpeed(final ItemStack aStack, final Block aBlock, final int aMetaData) {
		if (!this.isItemStackUsable(aStack)) {
			return 0.0F;
		}
		final IToolStats tStats = this.getToolStats(aStack);
		if ((tStats == null) || (Math.max(0, this.getHarvestLevel(aStack, "")) < aBlock.getHarvestLevel(aMetaData))) {
			return 0.0F;
		}
		return tStats.isMinableBlock(aBlock, (byte) aMetaData) ? Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed) : 0.0F;
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack aStack, final World aWorld, final Block aBlock, final int aX, final int aY, final int aZ, final EntityLivingBase aPlayer) {
		if (!this.isItemStackUsable(aStack)) {
			return false;
		}
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats == null) {
			return false;
		}
		GT_Utility.doSoundAtClient(tStats.getMiningSound(), 1, 1.0F);
		this.doDamage(aStack, (int) Math.max(1, aBlock.getBlockHardness(aWorld, aX, aY, aZ) * tStats.getToolDamagePerBlockBreak()));
		return this.getDigSpeed(aStack, aBlock, aWorld.getBlockMetadata(aX, aY, aZ)) > 0.0F;
	}

	private ItemStack getContainerItem(ItemStack aStack, final boolean playSound) {
		if (!this.isItemStackUsable(aStack)) {
			return null;
		}
		aStack = GT_Utility.copyAmount(1, aStack);
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats == null) {
			return null;
		}
		this.doDamage(aStack, tStats.getToolDamagePerContainerCraft());
		aStack = aStack.stackSize > 0 ? aStack : null;
		if (playSound) {
			//String sound = (aStack == null) ? tStats.getBreakingSound() : tStats.getCraftingSound();
			//GT_Utility.doSoundAtClient(sound, 1, 1.0F);
		}
		return aStack;
	}

	@Override
	public Interface_ToolStats getToolStats(final ItemStack aStack) {
		this.isItemStackUsable(aStack);
		return this.getToolStatsInternal(aStack);
	}

	private Interface_ToolStats getToolStatsInternal(final ItemStack aStack) {
		return (Interface_ToolStats) (aStack == null ? null : this.mToolStats.get((short) aStack.getItemDamage()));
	}

	@Override
	public float getSaplingModifier(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer, final int aX, final int aY, final int aZ) {
		final IToolStats tStats = this.getToolStats(aStack);
		return (tStats != null) && tStats.isGrafter() ? Math.min(100.0F, (1 + this.getHarvestLevel(aStack, "")) * 20.0F) : 0.0F;
	}

	@Override
	public boolean canWhack(final EntityPlayer aPlayer, final ItemStack aStack, final int aX, final int aY, final int aZ) {
		if (!this.isItemStackUsable(aStack)) {
			return false;
		}
		final IToolStats tStats = this.getToolStats(aStack);
		return (tStats != null) && tStats.isCrowbar();
	}

	@Override
	public void onWhack(final EntityPlayer aPlayer, final ItemStack aStack, final int aX, final int aY, final int aZ) {
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats != null) {
			this.doDamage(aStack, tStats.getToolDamagePerEntityAttack());
		}
	}

	@Override
	public boolean canWrench(final EntityPlayer player, final int x, final int y, final int z) {
		System.out.println("canWrench");
		if(player==null) {
			return false;
		}
		if(player.getCurrentEquippedItem()==null) {
			return false;
		}
		if (!this.isItemStackUsable(player.getCurrentEquippedItem())) {
			return false;
		}
		final Interface_ToolStats tStats = this.getToolStats(player.getCurrentEquippedItem());
		return (tStats != null) && tStats.isWrench();
	}

	@Override
	public void wrenchUsed(final EntityPlayer player, final int x, final int y, final int z) {
		if(player==null) {
			return;
		}
		if(player.getCurrentEquippedItem()==null) {
			return;
		}
		final IToolStats tStats = this.getToolStats(player.getCurrentEquippedItem());
		if (tStats != null) {
			this.doDamage(player.getCurrentEquippedItem(), tStats.getToolDamagePerEntityAttack());
		}
	}

	@Override
	public boolean canUse(final ItemStack stack, final EntityPlayer player, final int x, final int y, final int z){
		return this.canWrench(player, x, y, z);
	}

	@Override
	public void used(final ItemStack stack, final EntityPlayer player, final int x, final int y, final int z){
		this.wrenchUsed(player, x, y, z);
	}

	@Override
	public boolean shouldHideFacades(final ItemStack stack, final EntityPlayer player) {
		if(player==null) {
			return false;
		}
		if(player.getCurrentEquippedItem()==null) {
			return false;
		}
		if (!this.isItemStackUsable(player.getCurrentEquippedItem())) {
			return false;
		}
		final Interface_ToolStats tStats = this.getToolStats(player.getCurrentEquippedItem());
		return tStats.isWrench();
	}

	@Override
	public boolean canLink(final EntityPlayer aPlayer, final ItemStack aStack, final EntityMinecart cart) {
		if (!this.isItemStackUsable(aStack)) {
			return false;
		}
		final IToolStats tStats = this.getToolStats(aStack);
		return (tStats != null) && tStats.isCrowbar();
	}

	@Override
	public void onLink(final EntityPlayer aPlayer, final ItemStack aStack, final EntityMinecart cart) {
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats != null) {
			this.doDamage(aStack, tStats.getToolDamagePerEntityAttack());
		}
	}

	@Override
	public boolean canBoost(final EntityPlayer aPlayer, final ItemStack aStack, final EntityMinecart cart) {
		if (!this.isItemStackUsable(aStack)) {
			return false;
		}
		final IToolStats tStats = this.getToolStats(aStack);
		return (tStats != null) && tStats.isCrowbar();
	}

	@Override
	public void onBoost(final EntityPlayer aPlayer, final ItemStack aStack, final EntityMinecart cart) {
		final IToolStats tStats = this.getToolStats(aStack);
		if (tStats != null) {
			this.doDamage(aStack, tStats.getToolDamagePerEntityAttack());
		}
	}

	@Override
	public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
		final IToolStats tStats = this.getToolStats(aStack);
		if ((tStats != null) && (aPlayer != null)) {
			tStats.onToolCrafted(aStack, aPlayer);
		}
		super.onCreated(aStack, aWorld, aPlayer);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean isItemStackUsable(final ItemStack aStack) {
		final IToolStats tStats = this.getToolStatsInternal(aStack);
		if (((aStack.getItemDamage() % 2) == 1) || (tStats == null)) {
			final NBTTagCompound aNBT = aStack.getTagCompound();
			if (aNBT != null) {
				aNBT.removeTag("ench");
			}
			return false;
		}
		final Materials aMaterial = getPrimaryMaterial(aStack);
		final HashMap<Integer, Integer> tMap = new HashMap<>(), tResult = new HashMap<>();
		if (aMaterial.mEnchantmentTools != null) {
			tMap.put(aMaterial.mEnchantmentTools.effectId, (int) aMaterial.mEnchantmentToolsLevel);
			if (aMaterial.mEnchantmentTools == Enchantment.fortune) {
				tMap.put(Enchantment.looting.effectId, (int) aMaterial.mEnchantmentToolsLevel);
			}
			if (aMaterial.mEnchantmentTools == Enchantment.knockback) {
				tMap.put(Enchantment.power.effectId, (int) aMaterial.mEnchantmentToolsLevel);
			}
			if (aMaterial.mEnchantmentTools == Enchantment.fireAspect) {
				tMap.put(Enchantment.flame.effectId, (int) aMaterial.mEnchantmentToolsLevel);
			}
		}
		final Enchantment[] tEnchants = tStats.getEnchantments(aStack);
		final int[] tLevels = tStats.getEnchantmentLevels(aStack);
		for (int i = 0; i < tEnchants.length; i++) {
			if (tLevels[i] > 0) {
				final Integer tLevel = tMap.get(tEnchants[i].effectId);
				tMap.put(tEnchants[i].effectId, tLevel == null ? tLevels[i] : tLevel == tLevels[i] ? tLevel + 1 : Math.max(tLevel, tLevels[i]));
			}
		}
		for (final Entry<Integer, Integer> tEntry : tMap.entrySet()) {
			if ((tEntry.getKey() == 33) || ((tEntry.getKey() == 20) && (tEntry.getValue() > 2)) || (tEntry.getKey() == Enchantment_Radioactivity.INSTANCE.effectId)) {
				tResult.put(tEntry.getKey(), tEntry.getValue());
			} else {
				switch (Enchantment.enchantmentsList[tEntry.getKey()].type) {
				case weapon:
					if (tStats.isWeapon()) {
						tResult.put(tEntry.getKey(), tEntry.getValue());
					}
					break;
				case all:
					tResult.put(tEntry.getKey(), tEntry.getValue());
					break;
				case armor:
				case armor_feet:
				case armor_head:
				case armor_legs:
				case armor_torso:
					break;
				case bow:
					if (tStats.isRangedWeapon()) {
						tResult.put(tEntry.getKey(), tEntry.getValue());
					}
					break;
				case breakable:
					break;
				case fishing_rod:
					break;
				case digger:
					if (tStats.isMiningTool()) {
						tResult.put(tEntry.getKey(), tEntry.getValue());
					}
					break;
				}
			}
		}
		EnchantmentHelper.setEnchantments(tResult, aStack);
		return true;
	}

	@Override
	public short getChargedMetaData(final ItemStack aStack) {
		return (short) (aStack.getItemDamage() - (aStack.getItemDamage() % 2));
	}

	@Override
	public short getEmptyMetaData(final ItemStack aStack) {
		final NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT.removeTag("ench");
		}
		return (short) ((aStack.getItemDamage() + 1) - (aStack.getItemDamage() % 2));
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(final ItemStack aStack, final ItemStack aBook) {
		return false;
	}

	@Override
	public boolean getIsRepairable(final ItemStack aStack, final ItemStack aMaterial) {
		return false;
	}
}