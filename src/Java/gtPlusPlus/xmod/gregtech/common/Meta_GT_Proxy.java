package gtPlusPlus.xmod.gregtech.common;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;

import gregtech.api.GregTech_API;

import gtPlusPlus.api.objects.Logger;
import net.minecraftforge.oredict.OreDictionary;

public class Meta_GT_Proxy {

	public static List<Runnable> GT_BlockIconload = new ArrayList<>();
	public static List<Runnable> GT_ItemIconload = new ArrayList<>();

	@SideOnly(Side.CLIENT)
	public static IIconRegister sBlockIcons, sItemIcons;

	public Meta_GT_Proxy() {
		Logger.INFO("GT_PROXY - initialized.");
		for (final String tOreName : OreDictionary.getOreNames()) {

		}
	}

	public static boolean areWeUsingGregtech5uExperimental(){
		final int version = GregTech_API.VERSION;
		if ((version == 508) || (version == 507)){
			return false;
		}
		else if (version == 509){
			return true;
		}
		else {
			return false;
		}
	}

}
