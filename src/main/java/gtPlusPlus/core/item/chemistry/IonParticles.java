package gtPlusPlus.core.item.chemistry;

import java.util.HashMap;
import java.util.List;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class IonParticles extends BaseItemParticle {

	public static HashMap<String, Integer> NameToMetaMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> MetaToNameMap = new HashMap<Integer, String>();
	
	
	public IonParticles() {
		super("Ion", ELEMENT.NAMES.length, EnumRarity.rare);
	}
	
	public static IIcon[] overlays = new IIcon[ELEMENT.NAMES.length];
	public static IIcon baseTexture;
	
	static {
		//Generate Ions
		int key = 0;
		for (String s : ELEMENT.NAMES) {
			//Map names to Meta
			NameToMetaMap.put(Utils.sanitizeString(s.toLowerCase()), key);
			MetaToNameMap.put(key, Utils.sanitizeString(s.toLowerCase()));
			Materials m = Materials.get(s);
			int aColour = 0;
			if (m == null) {
				aColour = Utils.rgbtoHexValue(128, 128, 128);
			}
			else {
				aColour = Utils.rgbtoHexValue(m.mRGBa[0], m.mRGBa[1], m.mRGBa[2]);
			}
			aColourMap.put(key++, aColour);
		}
		
	}

	@Override
	public String[] getAffixes() {
		return new String[] {"", ""};
	}

	@Override
	public String getUnlocalizedName() {
		return "";
	}
	
	@Override
    public String getUnlocalizedName(final ItemStack itemStack) {
        return "item.particle.ion" + "." + ELEMENT.NAMES[itemStack.getItemDamage()];
    }
	
	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Charge", 0);
		tagMain.setTag("Ion", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}
	
	public static final long getChargeState(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("Ion");
			if (aNBT != null) {
				return aNBT.getLong("Charge");
			}
		}
		else {
		createNBT(aStack);
		}
		return 0L;
	}

	public static final boolean setChargeState(final ItemStack aStack, final long aCharge) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("Ion");
			if (aNBT != null) {
				aNBT.setLong("Charge", aCharge);
				return true;
			}
		}
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.getTagCompound() == null){
			createNBT(stack);
        }
		double chargeState = getChargeState(stack);	
        return  chargeState;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {		
		long aCharge = getChargeState(stack);
		String aState = EnumChatFormatting.YELLOW+"Unknown"+EnumChatFormatting.GRAY;		
		//State not set
		if (aCharge == 0) {
			list.add(EnumChatFormatting.GRAY+"A "+MetaToNameMap.get(stack.getItemDamage())+" Ion with an "+aState+" charge state");			
		}
		else {
			if (aCharge > 0) {
				aState = EnumChatFormatting.GREEN+"Positive"+EnumChatFormatting.GRAY;
			}
			else {
				aState = EnumChatFormatting.RED+"Negative"+EnumChatFormatting.GRAY;
			}

			list.add(EnumChatFormatting.GRAY+"A "+MetaToNameMap.get(stack.getItemDamage())+" Ion with a "+aState+" charge state of "+aCharge+"");	
		}		
		super.addInformation(stack, player, list, bool);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {		
		for (int i = 0; i < IonParticles.overlays.length; i++) {
			IonParticles.overlays[i] = reg.registerIcon(CORE.MODID + ":" + "ion/"+i);			
		}
		IonParticles.baseTexture = reg.registerIcon(CORE.MODID + ":" + "ion/IonBase");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return IonParticles.overlays[meta];
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int aMeta, int aPass) {
		if (aPass == 0) {
			return IonParticles.baseTexture;
		}
		else {
			return IonParticles.overlays[aMeta];
		}
	}

	@Override
	public int getRenderPasses(int metadata) {
		return 2;
	}

}
