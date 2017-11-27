package gtPlusPlus.xmod.gregtech.api.items;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;

import java.util.*;
import java.util.Map.Entry;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_ToolStats;
import mods.railcraft.api.core.items.IToolCrowbar;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

/**
 * This is an example on how you can create a Tool ItemStack, in this case a Bismuth Wrench:
 * GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01").getToolWithStats(16, 1, Materials.Bismuth, Materials.Bismuth, null);
 */
@Optional.InterfaceList({@Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "Forestry"),
	@Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "Railcraft"),
	@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft"),
	@Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO")})
public abstract class Gregtech_MetaTool extends GT_MetaGenerated_Tool implements IDamagableItem, IToolCrowbar, IToolWrench {
	/**
	 * All instances of this Item Class are listed here.
	 * This gets used to register the Renderer to all Items of this Type, if useStandardMetaItemRenderer() returns true.
	 * <p/>
	 * You can also use the unlocalized Name gotten from getUnlocalizedName() as Key if you want to get a specific Item.
	 */
	public static final HashMap<String, Gregtech_MetaTool> sInstances = new HashMap<>();

	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */

	public final HashMap<Short, IToolStats> mToolStats = new HashMap<>();

	/**
	 * Creates the Item using these Parameters.
	 *
	 * @param aUnlocalized The Unlocalized Name of this Item.
	 */
	public Gregtech_MetaTool(final String aUnlocalized) {
		super(aUnlocalized);
		GT_ModHandler.registerBoxableItemToToolBox(this);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setMaxStackSize(1);
		sInstances.put(this.getUnlocalizedName(), this);
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


	public void addAdditionalToolTips(final List aList, final ItemStack aStack) {
		final long tMaxDamage = getToolMaxDamage(aStack);
		final Materials tMaterial = getPrimaryMaterial(aStack);
		final IToolStats tStats = this.getToolStats(aStack);
		final int tOffset = this.getElectricStats(aStack) != null ? 2 : 1;
		if (tStats != null) {
			final String name = aStack.getUnlocalizedName();
			if (name.equals("gt.metatool.01.170") || name.equals("gt.metatool.01.172") || name.equals("gt.metatool.01.174") || name.equals("gt.metatool.01.176")) {
				aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
				aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + this.getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
				aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Turbine Efficency: " + EnumChatFormatting.BLUE + (50.0F + (10.0F * this.getToolCombatDamage(aStack))) + EnumChatFormatting.GRAY);
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Steam flow: " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 1000) + EnumChatFormatting.GRAY + "L/sec");
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Gas flow(EU burnvalue per tick): " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 25) + EnumChatFormatting.GRAY + "EU/t");
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Plasma flow(Plasma energyvalue per tick): " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 1000) + EnumChatFormatting.GRAY + "EU/t");

			} else {
				aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
				aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + this.getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
				aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Attack Damage: " + EnumChatFormatting.BLUE + this.getToolCombatDamage(aStack) + EnumChatFormatting.GRAY);
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Mining Speed: " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed) + EnumChatFormatting.GRAY);
			}
		}
	}

	public void addAdditionalToolTips(final List aList, final ItemStack aStack, final EntityPlayer aPlayer) {
		final long tMaxDamage = getToolMaxDamage(aStack);
		final Materials tMaterial = getPrimaryMaterial(aStack);
		final IToolStats tStats = this.getToolStats(aStack);
		final int tOffset = this.getElectricStats(aStack) != null ? 2 : 1;
		if (tStats != null) {
			final String name = aStack.getUnlocalizedName();
			if (name.equals("gt.metatool.01.170") || name.equals("gt.metatool.01.172") || name.equals("gt.metatool.01.174") || name.equals("gt.metatool.01.176")) {
				aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
				aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + this.getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
				aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Turbine Efficency: " + EnumChatFormatting.BLUE + (50.0F + (10.0F * this.getToolCombatDamage(aStack))) + EnumChatFormatting.GRAY);
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Steam flow: " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 1000) + EnumChatFormatting.GRAY + "L/sec");
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Gas flow(EU burnvalue per tick): " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 50) + EnumChatFormatting.GRAY + "EU/t");
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Optimal Plasma flow(Plasma energyvalue per tick): " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed * 1000) + EnumChatFormatting.GRAY + "EU/t");

			} else {
				aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
				aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + this.getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
				aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Attack Damage: " + EnumChatFormatting.BLUE + this.getToolCombatDamage(aStack) + EnumChatFormatting.GRAY);
				aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Mining Speed: " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed) + EnumChatFormatting.GRAY);
				NBTTagCompound aNBT = aStack.getTagCompound();
				if (aNBT != null) {
					aNBT = aNBT.getCompoundTag("GT.ToolStats");
					if ((aNBT != null) && aNBT.hasKey("Heat")){
						int tHeat = aNBT.getInteger("Heat");
						final long tWorldTime = aPlayer.getEntityWorld().getWorldTime();
						if(aNBT.hasKey("HeatTime")){
							final long tHeatTime = aNBT.getLong("HeatTime");
							if(tWorldTime>(tHeatTime+10)){
								tHeat = (int) (tHeat - ((tWorldTime-tHeatTime)/10));
								if((tHeat<300)&&(tHeat>-10000)) {
									tHeat=300;
								}
							}
							aNBT.setLong("HeatTime", tWorldTime);
							if(tHeat>-10000) {
								aNBT.setInteger("Heat", tHeat);
							}
						}

						aList.add(tOffset + 3, EnumChatFormatting.RED + "Heat: " + aNBT.getInteger("Heat")+" K" + EnumChatFormatting.GRAY);
					}
				}
			}
		}
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
		//System.out.println("canWrench");
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