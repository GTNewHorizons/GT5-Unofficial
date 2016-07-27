package miscutil.core.item.init;

import miscutil.core.item.ModItems;
import miscutil.core.item.base.dusts.BaseItemDust;
import miscutil.core.util.Utils;

public class ItemsDusts {

	public static void load(){
		run();
	}

	private static void run(){

		//Staballoy Dusts
		ModItems.itemDustStaballoy = new BaseItemDust("itemDustStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Dust");
		ModItems.itemDustTinyStaballoy = new BaseItemDust("itemDustTinyStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Tiny");
		ModItems.itemDustSmallStaballoy = new BaseItemDust("itemDustSmallStaballoy", "Staballoy", Utils.rgbtoHexValue(68, 75, 66), "Small");

		//Tantalloy60 Dusts
		ModItems.itemDustTantalloy60 = new BaseItemDust("itemDustTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166), "Dust");
		ModItems.itemDustTinyTantalloy60 = new BaseItemDust("itemDustTinyTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166), "Tiny");
		ModItems.itemDustSmallTantalloy60 = new BaseItemDust("itemDustSmallTantalloy60", "Tantalloy-60", Utils.rgbtoHexValue(68, 75, 166), "Small");

		//Tantalloy60 Dusts
		ModItems.itemDustTantalloy61 = new BaseItemDust("itemDustTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196), "Dust");
		ModItems.itemDustTinyTantalloy61 = new BaseItemDust("itemDustTinyTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196), "Tiny");
		ModItems.itemDustSmallTantalloy61 = new BaseItemDust("itemDustSmallTantalloy61", "Tantalloy-61", Utils.rgbtoHexValue(122, 135, 196), "Small");

		//BloodSteel Dusts
		ModItems.itemDustBloodSteel = new BaseItemDust("itemDustBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Dust");
		ModItems.itemDustTinyBloodSteel = new BaseItemDust("itemDustTinyBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Tiny");
		ModItems.itemDustSmallBloodSteel = new BaseItemDust("itemDustSmallBloodSteel", "BloodSteel", Utils.rgbtoHexValue(142, 28, 0), "Small");

	}

}
