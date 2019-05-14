package gtPlusPlus.core.item.bauble;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import baubles.api.BaubleType;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class FireProtectionBauble extends BaseBauble {

	public static HashMap<UUID, Boolean> mDataMap = new HashMap<UUID, Boolean>();
	
	public static HashSet<UUID> mPlayerMap = new HashSet<UUID>();

	private static Field isImmuneToFire;
	
	static {
		isImmuneToFire = ReflectionUtils.getField(Entity.class, "isImmuneToFire");
	}
	
	public static boolean fireImmune(Entity aEntity) {
		return aEntity.isImmuneToFire();
	}
	
	public static boolean setEntityImmuneToFire(Entity aEntity, boolean aImmune) {
		return ReflectionUtils.setField(aEntity, isImmuneToFire, aImmune);
	}
	
	
	public FireProtectionBauble() {		
		super(BaubleType.RING, "GTPP.bauble.fireprotection.0" + ".name", 0);
		String aUnlocalName =  "GTPP.bauble.fireprotection.0" + ".name";
		this.setUnlocalizedName(aUnlocalName);
		this.setTextureName(CORE.MODID + ":" + getTextureNameForBauble());
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(AddToCreativeTab.tabMachines);				
		if (GameRegistry.findItem(CORE.MODID, aUnlocalName) == null) {
			GameRegistry.registerItem(this, aUnlocalName);			
		}		
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
			final boolean p_77663_5_) {		
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
		
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return (EnumChatFormatting.DARK_RED + super.getItemStackDisplayName(p_77653_1_) + EnumChatFormatting.GRAY);
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		String aString1 = StatCollector.translateToLocal("GTPP.battpack.tooltip.1");
		String aString2 = StatCollector.translateToLocal("GTPP.battpack.tooltip.2");
		String aString3 = StatCollector.translateToLocal("GTPP.battpack.tooltip.3");
		String aString4 = StatCollector.translateToLocal("GTPP.battpack.tooltip.4");
		
		String aEU = StatCollector.translateToLocal("GTPP.info.eu");	
		String aEUT = aEU+"/t";

		list.add(EnumChatFormatting.GREEN + aString1 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString4 + EnumChatFormatting.GRAY);
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
	public void onEquipped(final ItemStack arg0, final EntityLivingBase aPlayer) {
		if (!aPlayer.worldObj.isRemote) {
			if (aPlayer instanceof EntityPlayer) {				
				EntityPlayer bPlayer = (EntityPlayer) aPlayer;
				mPlayerMap.add(bPlayer.getUniqueID());
			}
		}		
	}

	@Override
	public void onUnequipped(final ItemStack arg0, final EntityLivingBase aPlayer) {
		if (!aPlayer.worldObj.isRemote) {
			if (aPlayer instanceof EntityPlayer) {
				EntityPlayer bPlayer = (EntityPlayer) aPlayer;
				if (bPlayer.isPotionActive(Potion.fireResistance)) {
					bPlayer.removePotionEffect(Potion.fireResistance.id);
				}
				mPlayerMap.remove(bPlayer.getUniqueID());
				setEntityImmuneToFire(bPlayer, false);				
			}
		}
	}

	@Override
	public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase aPlayer) {
		if (!aPlayer.worldObj.isRemote) {
			if (aPlayer instanceof EntityPlayer) {				
				EntityPlayer bPlayer = (EntityPlayer) aPlayer;				
				if (!fireImmune(bPlayer)) {
					setEntityImmuneToFire(bPlayer, true);
				}				
				if (!bPlayer.isPotionActive(Potion.fireResistance)) {
					bPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 4));
				}
			}
		}		
	}
	
	public String getTextureNameForBauble() {
		return "chargepack/"+1;
	}

}
