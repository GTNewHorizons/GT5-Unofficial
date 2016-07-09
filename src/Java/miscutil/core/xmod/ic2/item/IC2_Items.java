package miscutil.core.xmod.ic2.item;

import ic2.core.init.InternalName;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class IC2_Items {
	
	  public static ItemStack woodrotor;
	  public static ItemStack ironrotor;
	  public static ItemStack steelrotor;
	  public static ItemStack carbonrotor;
	  public static ItemStack woodrotorblade;
	  public static ItemStack ironrotorblade;
	  public static ItemStack steelrotorblade;
	  public static ItemStack carbonrotorblade;

	public static void register(){
		//woodrotor = new ItemStack(new ItemWindRotor(InternalName.itemwoodrotor, 5, 10800, 0.25F, 10, 60, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorWoodmodel.png")));
	    //ironrotor = new ItemStack(new ItemWindRotor(InternalName.itemironrotor, 7, 86400, 0.5F, 14, 75, new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorIronmodel.png")));
	    steelrotor = new ItemStack(new RotorBase(InternalName.itemsteelrotor, 13, 172800, 1.5F, 15, 320, new ResourceLocation(CORE.MODID, "textures/items/rotorIridiumModel.png")).setCreativeTab(AddToCreativeTab.tabMachines).setUnlocalizedName("Kinetic Gearbox Rotor (Iridium)"));
	    carbonrotor = new ItemStack(new RotorCustom("itemIridiumRotor", 13, 320000, 1.5F, 15, 320, new ResourceLocation(CORE.MODID, "textures/items/rotorIridiumModel.png")));
	}
	
}
