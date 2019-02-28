package gtPlusPlus.xmod.tinkers.material;

import static gtPlusPlus.core.util.math.MathUtils.safeCast_LongToInt;

import java.util.HashMap;

import cpw.mods.fml.common.event.FMLInterModComms;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.Smeltery;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.smeltery.TinkerSmeltery;

public class BaseTinkersMaterial {

	private static HashMap<String, Integer> aInternalMaterialIdMap = new HashMap<String, Integer>();
	private static int aNextFreeID = 440;
	private final String mUnlocalName;
	public final String mLocalName;
	private final int aID;
	
	public BaseTinkersMaterial(Material aMaterial) {
		mLocalName = Utils.sanitizeString(aMaterial.getLocalizedName());
		mUnlocalName = "material.gtpp."+Utils.sanitizeString(mLocalName);
		aID = aNextFreeID++;
		aInternalMaterialIdMap.put(mUnlocalName, aID);
		
		
		int id = aID;
		if (id > 0) {

			ToolMaterial mat = new ToolMaterial(mLocalName, mUnlocalName, 4, 100, 700, 2,
					0.6F, 4, 0.0F, EnumChatFormatting.WHITE.toString(), 16777215);

			TConstructRegistry.addtoolMaterial(id, mat);
			TConstructRegistry.addDefaultToolPartMaterial(id);
			TConstructRegistry.addBowMaterial(id, calcBowDrawSpeed(aMaterial), 1.0F);
			TConstructRegistry.addArrowMaterial(id, calcProjectileMass(aMaterial), calcProjectileFragility(aMaterial));

			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("Id", id);
			tag.setString("Name", mLocalName);
			tag.setString("localizationString", mUnlocalName);
			tag.setInteger("Durability", calcDurability(aMaterial)); // 97
			tag.setInteger("MiningSpeed", calcMiningSpeed(aMaterial)); // 150
			tag.setInteger("HarvestLevel", calcHarvestLevel(aMaterial)); // 1
			tag.setInteger("Attack", calcAttack(aMaterial)); // 0
			tag.setFloat("HandleModifier", calcHandleModifier(aMaterial)); // 1.0f
			tag.setInteger("Reinforced", calcReinforced(aMaterial)); // 0
			tag.setFloat("Bow_ProjectileSpeed", calcBowProjectileSpeed(aMaterial)); // 3.0f
			tag.setInteger("Bow_DrawSpeed", calcBowDrawSpeed(aMaterial)); // 18
			tag.setFloat("Projectile_Mass", calcProjectileMass(aMaterial)); // 0.69f
			tag.setFloat("Projectile_Fragility", calcProjectileFragility(aMaterial)); // 0.2f
			tag.setString("Style", calcStyle(aMaterial));
			tag.setInteger("Color", calcColour(aMaterial));
			FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
			

			generateRecipes(aMaterial);			
			

			ItemStack itemstack = aMaterial.getIngot(1);
			tag = new NBTTagCompound();
			tag.setInteger("MaterialId", id);
			NBTTagCompound item = new NBTTagCompound();
			itemstack.writeToNBT(item);
			tag.setTag("Item", item);
			tag.setInteger("Value", 2); // What is value for?
			FMLInterModComms.sendMessage("TConstruct", "addPartBuilderMaterial", tag);

			tag = new NBTTagCompound();
			tag.setInteger("MaterialId", id);
			tag.setInteger("Value", 2); // What is value for?
			item = new NBTTagCompound();
			itemstack.writeToNBT(item);
			tag.setTag("Item", item);

			FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);


		}
		
		
		
		
		
		
		
	}
	
	public String getUnlocalName() {
		return mUnlocalName;
	}
	
	
	private static int calcDurability(Material aMaterial) {
		return safeCast_LongToInt(aMaterial.vDurability);
	}
	
	private static int calcMiningSpeed(Material aMaterial) {
		return (aMaterial.vHarvestLevel * 2)+aMaterial.vTier;
	}
	
	private static int calcHarvestLevel(Material aMaterial) {
		return aMaterial.vHarvestLevel;
	}
	
	private static int calcAttack(Material aMaterial) {
		return aMaterial.vHarvestLevel+aMaterial.vTier+aMaterial.vRadiationLevel;
	}
	
	private static float calcHandleModifier(Material aMaterial) {
		return 1f;
	}
	
	private static int calcReinforced(Material aMaterial) {
		return aMaterial.getMeltingPointC()/3600;
	}
	
	private static int calcBowProjectileSpeed(Material aMaterial) {
		return aMaterial.vHarvestLevel+2;
	}
	
	private static int calcBowDrawSpeed(Material aMaterial) {
		return aMaterial.vHarvestLevel+8;
	}

	private static float calcProjectileMass(Material aMaterial) {
		return (aMaterial.getMeltingPointC()/1800)*0.1f;
	}
	
	private static float calcProjectileFragility(Material aMaterial) {
		return 0f;
	}
	
	private static String calcStyle(Material aMaterial) {
		String aReturn = "" + EnumChatFormatting.WHITE;
		int aTemp = aMaterial.getMeltingPointC();
		if (aTemp < 3600) {
			aReturn = "" + EnumChatFormatting.WHITE;
		}
		else if (aTemp >= 3600) {
			aReturn = "" + EnumChatFormatting.YELLOW;
		}
		else if (aTemp >= (3600*2)) {
			aReturn = "" + EnumChatFormatting.GREEN;
		}
		else if (aTemp >= (3600*3)) {
			aReturn = "" + EnumChatFormatting.RED;
		}
		else if (aTemp >= (3600*4)) {
			aReturn = "" + EnumChatFormatting.DARK_RED;
		}
		else {
			aReturn = "" + EnumChatFormatting.GOLD;			
		}
		return aReturn;
	}
	
	private static int calcColour(Material aMaterial) {
		return aMaterial.getRgbAsHex();
	}
	
	private boolean generateRecipes(Material aMaterial) {
		
		//Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 0), ExtraUtils.decorative1, 5, 850,	aMaterial.getFluid(72));

		
		
		FluidType.registerFluidType(mLocalName, aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(0).getFluid(), true);
		
		Smeltery.addMelting(aMaterial.getBlock(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144*9));
		Smeltery.addMelting(aMaterial.getIngot(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144));
		
		if (aMaterial.getMeltingPointC() <= 3600) {
			ItemStack ingotcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
			TConstructRegistry.getBasinCasting().addCastingRecipe(aMaterial.getBlock(1),
					aMaterial.getFluid(144*9), (ItemStack) null, true, 100);
			TConstructRegistry.getTableCasting().addCastingRecipe(aMaterial.getIngot(1),
					aMaterial.getFluid(144), ingotcast, false, 50);
		}
		

		return true;
	}
	
	
	
	
	
	
	
	
	
}
