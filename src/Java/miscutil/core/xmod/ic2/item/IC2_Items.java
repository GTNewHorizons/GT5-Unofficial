package miscutil.core.xmod.ic2.item;

import ic2.core.IC2;
import ic2.core.init.InternalName;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.item.base.CoreItem;
import miscutil.core.lib.LoadedMods;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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

	public static void register(){

		if(LoadedMods.EnderIO){
			//Tier 1
			rotor_Blade_Material_1 = new ItemStack (new CoreItem("itemEnergeticRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_1 = new ItemStack (new CoreItem("itemEnergeticShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_1 = new ItemStack (new RotorBase(InternalName.itemwoodrotor, 9, 512000, 0.9F, 12, 80, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorEnergeticModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemEnergeticRotor"));
			//Tier 2
			rotor_Blade_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_2 = new ItemStack (new RotorBase(InternalName.itemironrotor, 11, 809600, 1.0F, 14, 120, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorTungstenSteelModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemTungstenSteelRotor"));
			//Tier 3
			rotor_Blade_Material_3 = new ItemStack (new CoreItem("itemVibrantRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_3 = new ItemStack (new CoreItem("itemVibrantShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_3 = new ItemStack (new RotorBase(InternalName.itemsteelrotor, 13, 1600000, 1.2F, 16, 160, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorVibrantModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemVibrantRotor"));
			//Tier 4
			rotor_Blade_Material_4 = new ItemStack (new CoreItem("itemIridiumRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_4 = new ItemStack (new CoreItem("itemIridiumShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_4 = new ItemStack (new RotorIridium(InternalName.itemwcarbonrotor, 15, 3200000, 1.5F, 18, 320, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemIridiumRotor"));

		}
		else {
			//Tier 1 - Magnalium
			rotor_Blade_Material_1 = new ItemStack (new CoreItem("itemMagnaliumRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_1 = new ItemStack (new CoreItem("itemMagnaliumShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_1 = new ItemStack (new RotorBase(InternalName.itemwoodrotor, 9, 512000, 0.9F, 12, 80, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorMagnaliumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemMagnaliumRotor"));
			//Tier 2
			rotor_Blade_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_2 = new ItemStack (new CoreItem("itemTungstenSteelShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_2 = new ItemStack (new RotorBase(InternalName.itemironrotor, 11, 809600, 1.0F, 14, 120, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorTungstenSteelModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemTungstenSteelRotor"));
			//Tier 3 - Ultimet
			rotor_Blade_Material_3 = new ItemStack (new CoreItem("itemUltimetRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_3 = new ItemStack (new CoreItem("itemUltimetShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_3 = new ItemStack (new RotorBase(InternalName.itemsteelrotor, 13, 1600000, 1.2F, 16, 160, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorUltimetModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemUltimetRotor"));
			//Tier 4
			rotor_Blade_Material_4 = new ItemStack (new CoreItem("itemIridiumRotorBlade", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			shaft_Material_4 = new ItemStack (new CoreItem("itemIridiumShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
			rotor_Material_4 = new ItemStack (new RotorIridium(InternalName.itemwcarbonrotor, 15, 3200000, 1.5F, 18, 320, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemIridiumRotor"));
		}

	}
}
