package gtPlusPlus.xmod.ic2.item;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.LoadedMods;
import net.minecraft.item.ItemStack;

public class IC2_Items {

	public static ItemStack rotor_Material_1; //Energetic Alloy
	public static ItemStack rotor_Material_2; //TungstenSteel
	public static ItemStack rotor_Material_3; //Vibrant Alloy
	public static ItemStack rotor_Material_4; //Iridium

	public static ItemStack rotor_Blade_Material_1;
	public static ItemStack rotor_Blade_Material_2;
	public static ItemStack rotor_Blade_Material_3;
	public static ItemStack rotor_Blade_Material_4;

	public static ItemStack shaft_Material_1; //Energetic Alloy
	public static ItemStack shaft_Material_2; //TungstenSteel
	public static ItemStack shaft_Material_3; //Vibrant Alloy
	public static ItemStack shaft_Material_4; //Iridium

	public static ItemStack blockRTG;
	public static ItemStack blockKineticGenerator;

	private static final String[] mData1 = new String[] {"itemEnergeticRotorBlade", "itemMagnaliumRotorBlade"};
	private static final String[] mData2 = new String[] {"itemEnergeticShaft", "itemMagnaliumShaft"};
	private static final String[] mData3 = new String[] {"itemVibrantRotorBlade", "itemUltimetRotorBlade"};
	private static final String[] mData4 = new String[] {"itemVibrantShaft", "itemUltimetShaft"};
	

	public static void register(){

		int aIndexEIO = (LoadedMods.EnderIO ? 0 : 1);		
		
		// Rotor Blades
		rotor_Blade_Material_1 = new ItemStack (new CoreItem(mData1[aIndexEIO], AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		rotor_Blade_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		rotor_Blade_Material_3 = new ItemStack (new CoreItem(mData3[aIndexEIO], AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		rotor_Blade_Material_4 = new ItemStack (new CoreItem("itemIridiumRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		
		// Rotor Shafts
		shaft_Material_1 = new ItemStack (new CoreItem(mData2[aIndexEIO], AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		shaft_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		shaft_Material_3 = new ItemStack (new CoreItem(mData4[aIndexEIO], AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
		shaft_Material_4 = new ItemStack (new CoreItem("itemIridiumShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
				
		// Rotors
		rotor_Material_1 = new ItemStack (new CustomKineticRotor(0));
		rotor_Material_2 = new ItemStack (new CustomKineticRotor(1));
		rotor_Material_3 = new ItemStack (new CustomKineticRotor(2));
		rotor_Material_4 = new ItemStack (new CustomKineticRotor(3));

	}
}
