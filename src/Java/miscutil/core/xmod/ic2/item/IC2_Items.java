package miscutil.core.xmod.ic2.item;

import ic2.core.IC2;
import ic2.core.init.InternalName;
import miscutil.core.creative.AddToCreativeTab;
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

	public static void register(){
		rotor_Material_1 = new ItemStack(new RotorBase(InternalName.itemsteelrotor, 9, 512000, 0.9F, 12, 80, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemEnergeticRotor"));
		rotor_Material_2 = new ItemStack(new RotorBase(InternalName.itemsteelrotor, 11, 809600, 1.0F, 14, 120, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemTungstenSteelRotor"));
		rotor_Material_3 = new ItemStack(new RotorBase(InternalName.itemsteelrotor, 13, 1600000, 1.2F, 16, 160, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("ItemVibrantRotor"));
		rotor_Material_4 = new ItemStack(new RotorBase(InternalName.itemsteelrotor, 15, 3200000, 1.5F, 18, 320, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("itemIridiumRotor"));
		   }
	
}
