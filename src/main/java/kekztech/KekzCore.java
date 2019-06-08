package kekztech;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import fuelcell.Block_GDCUnit;
import fuelcell.Block_YSZUnit;
import fuelcell.GTMTE_SOFuelCellMK1;
import fuelcell.GTMTE_SOFuelCellMK2;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import reactor.GTMTE_ModularNuclearReactor;
import reactor.items.HeatExchanger;
import reactor.items.HeatPipe;

@Mod(modid = KekzCore.MODID, name = KekzCore.NAME, version = KekzCore.VERSION, 
		dependencies = "required-after:IC2; "
			+ "required-after:gregtech"
		)
public class KekzCore {
	
	public static final String NAME = "KekzTech";
	public static final String MODID = "kekztech";
	public static final String VERSION = "0.1a";
		
	@Mod.Instance("kekztech")
	public static KekzCore instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Items
		ErrorItem.getInstance().registerItem();
		MetaItem_ReactorComponent.getInstance().registerItem();
		MetaItem_CraftingComponent.getInstance().registerItem();
		// Blocks
		Block_YSZUnit.getInstance().registerBlock();
		Block_GDCUnit.getInstance().registerBlock();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event	) {
		final GTMTE_SOFuelCellMK1 sofc1 = new GTMTE_SOFuelCellMK1(5000, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
		final GTMTE_SOFuelCellMK2 sofc2 = new GTMTE_SOFuelCellMK2(5001, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
		final GTMTE_ModularNuclearReactor mdr = new GTMTE_ModularNuclearReactor(5002, "multimachine.nuclearreactor", "Nuclear Reactor");
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for(GTRecipe recipe : HeatPipe.RECIPE) {
			GT_Values.RA.addLatheRecipe(recipe.getInputItem(), recipe.getOutputItem(), null, recipe.getDuration(), recipe.getEuPerTick());
		}
		
		for(GTRecipe recipe : HeatExchanger.RECIPE) {
			GT_Values.RA.addAssemblerRecipe(
					recipe.getInputItems(), recipe.getInputFluid(), recipe.getOutputItem(), recipe.getDuration(), recipe.getEuPerTick());
		}
		
		// Heat Vents
		final ItemStack[] t1HeatVent = {
				MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.CopperHeatPipe.getMetaID(), 2),
				ItemList.Electric_Motor_MV.get(1L, (Object[]) null),
				Util.getStackofAmountFromOreDict("rotorSteel", 1),
				Util.getStackofAmountFromOreDict("plateDoubleSteel", 2),
				Util.getStackofAmountFromOreDict("screwSteel", 8),
				Util.getStackofAmountFromOreDict("circuitGood", 1)
		};
		GT_Values.RA.addAssemblerRecipe(t1HeatVent, 
				FluidRegistry.getFluidStack("molten.copper", 144),
				MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1HeatVent.getMetaID()),
				200, 120
				);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
