package reactor.items;

import items.MetaItem_CraftingComponent;
import items.MetaItem_ReactorComponent;
import kekztech.GTRecipe;
import kekztech.Items;
import kekztech.Util;
import net.minecraftforge.fluids.FluidRegistry;

public class HeatExchanger {
	
	public static String TYPE = "HeatExchanger";
	
	public static String[] RESOURCE_NAME = {
			"T1HeatExchanger", "T2HeatExchanger", "T3HeatExchanger", "T4HeatExchanger"
	};
	
	public static int[] HEAT_CAPACITY = {
			2000, 8000, 32000, 128000
	};
	
	public static int[] COMPONENT_EXCHANGE_RATE = {
			12, 24, 96, 384
	};
	
	public static int[] HULL_EXCHANGE_RATE = {
			4, 8, 32, 128
	};
	
	public static GTRecipe[] RECIPE = {
			new GTRecipe().setDuration(200).setEUPerTick(120)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.CopperHeatPipe.getMetaID(), 4))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDenseCopper", 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.copper", 576))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1HeatExchanger.getMetaID())),
			new GTRecipe().setDuration(400).setEUPerTick(480)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.SilverHeatPipe.getMetaID(), 4))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDenseSilver", 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.silver", 576))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T2HeatExchanger.getMetaID())),
			new GTRecipe().setDuration(800).setEUPerTick(7680)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.BoronArsenideHeatPipe.getMetaID(), 4))
					.addInputItem(Util.getStackofAmountFromOreDict("pipeTinyCopper", 4))
					.addInputItem(Util.getStackofAmountFromOreDict("wireFineSilver", 16))
					//.addInputItem(new ItemStack(ItemList.Pump_EV.getItem(), 1))
					.addInputFluid(FluidRegistry.getFluidStack("ic2coolant", 720))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T3HeatExchanger.getMetaID())),
			new GTRecipe().setDuration(1600).setEUPerTick(30720)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.DiamondHeatPipe.getMetaID(), 4))
					.addInputItem(Util.getStackofAmountFromOreDict("frameGTHSSE", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("plateHSSE", 6))
					.addInputItem(Util.getStackofAmountFromOreDict("screwOsmiridium", 24))
					.addInputItem(Util.getStackofAmountFromOreDict("pipeTinyEnderium", 4))
					//.addInputItem(new ItemStack(ItemList.Pump_LuV.getItem(), 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.gallium", 1152))
					.addInputFluid(FluidRegistry.getFluidStack("helium", 10000))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T4HeatExchanger.getMetaID()))
	};
	
}
