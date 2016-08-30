package miscutil.core.item.base.plates;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemPlate extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;
	private int mTier;

	public BaseItemPlate(String unlocalizedName, String materialName, int colour, int tier, int sRadioactivity) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemPlate");
		this.colour = colour;
		this.mTier = tier;
		this.materialName = materialName;
		this.sRadiation = sRadioactivity;
		GameRegistry.registerItem(this, unlocalizedName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemP", "p"), UtilsItems.getSimpleStack(this));
		addBendingRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Plate");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A flat plate of " + materialName + ".");		
		}
		if (sRadiation > 0){
			list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.GREEN+"Radioactive! "+EnumChatFormatting.GOLD+" Avoid direct handling without hazmat protection.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}
	
	protected final int sRadiation;
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
			if (!world.isRemote){
				if (this.sRadiation > 0 && (entityHolding instanceof EntityLivingBase)) {
			         EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
			         if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
			             IC2Potion.radiation.applyTo(entityLiving, sRadiation * 20, sRadiation * 10);
			         }
			     }
			}
		}

	private void addBendingRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Plates");
		String tempIngot = unlocalName.replace("itemPlate", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addBenderRecipe(tempOutputStack,
					UtilsItems.getSimpleStack(this),
					14*mTier*20,
					64*mTier);	
		}				
	}

}
