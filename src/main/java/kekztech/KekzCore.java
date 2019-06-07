package kekztech;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import reactor.items.CoolantCell;
import reactor.items.FuelRod;
import reactor.items.HeatExchanger;
import reactor.items.HeatPipe;
import reactor.items.HeatVent;
import reactor.items.NeutronReflector;

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
		ErrorItem.getInstance().registerItem();
		MetaItem_ReactorComponent.getInstance().registerItem();
		MetaItem_CraftingComponent.getInstance().registerItem();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event	) {
		
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
