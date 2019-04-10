package gtPlusPlus.core.item.bauble;

import java.util.List;

import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.AABB;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import ic2.api.item.ElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class MonsterKillerBaseBauble extends ElectricBaseBauble {

	private final Class[] mTargets;

	public MonsterKillerBaseBauble(Class[] aMonsterBaseClassArray, String aMonsterTypeName, int aPowerTier) {
		super(BaubleType.AMULET, aPowerTier, GT_Values.V[aPowerTier] * 20 * 300,
				"GTPP.MonsterKiller." + aMonsterTypeName + ".name");
		mTargets = aMonsterBaseClassArray;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		ItemStack charged;
		if (this.getEmptyItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 0.0D, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}
		if (this.getChargedItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}

	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
			final boolean p_77663_5_) {
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(final ItemStack itemStack) {
		double aItemCharge = ElectricItem.manager.getCharge(itemStack);
		return aItemCharge > 0;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return (EnumChatFormatting.WHITE + super.getItemStackDisplayName(p_77653_1_) + EnumChatFormatting.GRAY);
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		String aString1 = StatCollector.translateToLocal("GTPP.monsterkiller.tooltip.1");
		String aString2 = StatCollector.translateToLocal("GTPP.monsterkiller.tooltip.2");
		String aString3 = StatCollector.translateToLocal("GTPP.monsterkiller.tooltip.3");
		String aString4 = StatCollector.translateToLocal("GTPP.monsterkiller.tooltip.4");
		String aEU = StatCollector.translateToLocal("GTPP.info.eu");
		String aEUT = aEU + "/t";

		list.add(EnumChatFormatting.GREEN + aString1 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString2 + " " + (int) getTransferLimit(stack) + aEUT + aString3
				+ EnumChatFormatting.GRAY);
		list.add("");
		list.add("" + EnumChatFormatting.GREEN + aString4 + " " + EnumChatFormatting.GRAY);
		for (Class cz : mTargets) {
			list.add("- " + EnumChatFormatting.DARK_GREEN + cz.getSimpleName() + EnumChatFormatting.GRAY);
		}

		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override
	public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override // TODO
	public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase aPlayerEntity) {
		if (aPlayerEntity == null) {
			return;
		}
		EntityPlayer aPlayer = (EntityPlayer) aPlayerEntity;
		if (!aPlayer.worldObj.isRemote) {
			if (this.getCharge(aBaubleStack) >= getTransferLimit(aBaubleStack)) {
				AABB aPlayerBox = new AABB(aPlayerEntity, 5, 5, 5);
				if (this.mTargets != null && this.mTargets.length > 0) {
					for (Class clazz : mTargets) {
						AutoMap<?> aEnts = EntityUtils.getEntitiesWithinBoundingBox(clazz, aPlayerBox);
						if (aEnts.isEmpty()) {
							continue;
						} else {
							EntityLiving aClosest = null;
							double aEntityDistance = Short.MIN_VALUE;
							for (Object ent : aEnts) {
								if (!EntityLiving.class.isInstance(ent)) {
									continue;
								} else {
									double aCurEntDis = EntityUtils.getDistance(aPlayerEntity, (Entity) ent);
									if (aEntityDistance == Short.MIN_VALUE || aCurEntDis < aEntityDistance) {
										aEntityDistance = aCurEntDis;
										aClosest = (EntityLiving) ent;
									}
								}
							}
							if (aClosest != null) {
								float aEntHealth = aClosest.getHealth();
								float aEntMaxHealth = aClosest.getMaxHealth();
								double aEntHealthPerc = MathUtils.findPercentage(aEntHealth, aEntMaxHealth);
								long aEuUsage = (long) (aEntHealthPerc * 50 * aEntMaxHealth);
								if (this.discharge(aBaubleStack, aEuUsage, mTier, true, false, false) > 0) {
									aClosest.setDead();
									break;
								}
							} else {
								continue;
							}
						}
					}
				}
			} else {
				return;
			}
		}
	}

	@Override
	public String getTextureNameForBauble() {
		return CORE.MODID + ":" + "baubles/itemAmulet";
	}

}
