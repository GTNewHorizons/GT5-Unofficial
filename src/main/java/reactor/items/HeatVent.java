package reactor.items;

import items.MetaItem_CraftingComponent;
import items.MetaItem_ReactorComponent;
import kekztech.GTRecipe;
import kekztech.Items;
import kekztech.Util;
import net.minecraftforge.fluids.FluidRegistry;

public class HeatVent {
	
	public static final String TYPE = "HeatVent";
	public static final String HEAT = "HEAT";
	public static final String RESOURCE_NAME = "RESOURCE_NAME";
	public static final String HEAT_CAPACITY = "HEAT_CAPACITY";
	public static final String COMPONENT_VENT_RATE = "COMPONENT_VENT_RATE";
	public static final String HULL_DRAW_RATE = "HULL_DRAW_RATE";
	public static final String SELF_COOL_RATE = "SELF_COOL_RATE";
	
	
	public static String[] RESOURCE_NAMES = {
			"T1HeatVent", "T2HeatVent", "T3HeatVent", "T4HeatVent",
			"T1ComponentHeatVent", "T2ComponentHeatVent", "T3ComponentHeatVent", "T4ComponentHeatVent",
			"T1OverclockedHeatVent", "T2OverclockedHeatVent", "T3OverclockedHeatVent", "T4OverclockedHeatVent"
	};
	
	public static int[] HEAT_CAPACITIES = {
			1000, 4000, 8000, 32000,
			1000, 4000, 8000, 32000,
			1000, 4000, 8000, 32000
	};
	
	public static int[] COMPONENT_VENT_RATES = {
			0, 0, 0, 0,
			6, 12, 48, 96,
			0, 0, 0, 0
	};
	
	public static int[] HULL_DRAW_RATES = {
			0, 0, 0, 0,
			0, 0, 0, 0,
			18, 36, 144, 288
	};
	
	public static int[] SELF_COOL_RATES = {
			6, 12, 48, 96,
			0, 0, 0, 0,
			10, 20, 80, 160
	};
	
	public static final GTRecipe[] RECIPE = {
			// Heat Vents
			new GTRecipe().setDuration(200).setEUPerTick(120)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.CopperHeatPipe.getMetaID(), 2))
					// MV Motor
					.addInputItem(Util.getStackofAmountFromOreDict("rotorSteel", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDoubleSteel", 2))
					.addInputItem(Util.getStackofAmountFromOreDict("screwSteel", 8))
					.addInputItem(Util.getStackofAmountFromOreDict("circuitGood", 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.copper", 144))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1HeatVent.getMetaID())),
			new GTRecipe().setDuration(400).setEUPerTick(480)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.SilverHeatPipe.getMetaID(), 2))
					// HV Motor
					.addInputItem(Util.getStackofAmountFromOreDict("rotorAluminium", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDoubleAluminium", 2))
					.addInputItem(Util.getStackofAmountFromOreDict("screwAluminium", 8))
					.addInputItem(Util.getStackofAmountFromOreDict("circuitAdvanced", 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.silver", 144))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T2HeatVent.getMetaID())),
			new GTRecipe().setDuration(800).setEUPerTick(7680)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.BoronArsenideHeatPipe.getMetaID(), 2))
					// EV Motor
					.addInputItem(Util.getStackofAmountFromOreDict("rotorTungstenSteel", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDoubleTungstenSteel", 2))
					.addInputItem(Util.getStackofAmountFromOreDict("screwTungsten", 8))
					.addInputItem(Util.getStackofAmountFromOreDict("circuitData", 1))
					.addInputFluid(FluidRegistry.getFluidStack("molten.gallium", 288))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T3HeatVent.getMetaID())),
			new GTRecipe().setDuration(400).setEUPerTick(30720)
					.addInputItem(MetaItem_CraftingComponent.getInstance().getStackOfAmountFromDamage(Items.DiamondHeatPipe.getMetaID(), 2))
					// LuV Motor
					.addInputItem(Util.getStackofAmountFromOreDict("rotorHSSS", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("frameGTHSSS", 1))
					.addInputItem(Util.getStackofAmountFromOreDict("plateDoubleIridium", 6))
					.addInputItem(Util.getStackofAmountFromOreDict("screwOsmiridium", 24))
					.addInputItem(Util.getStackofAmountFromOreDict("circuitMaster", 1))
					.addInputFluid(FluidRegistry.getFluidStack("lubricant", 1000))
					.addInputFluid(FluidRegistry.getFluidStack("molten.gallium", 1152))
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T4HeatVent.getMetaID())),
			// Component Heat Vents
			new GTRecipe().setDuration(200).setEUPerTick(120)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1ComponentHeatVent.getMetaID())),
			new GTRecipe().setDuration(400).setEUPerTick(480)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T2ComponentHeatVent.getMetaID())),
			new GTRecipe().setDuration(800).setEUPerTick(7680)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T3ComponentHeatVent.getMetaID())),
			new GTRecipe().setDuration(1600).setEUPerTick(30720)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T4ComponentHeatVent.getMetaID())),
			// OC Heat Vents
			new GTRecipe().setDuration(200).setEUPerTick(120)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T1OverclockedHeatVent.getMetaID())),
			new GTRecipe().setDuration(400).setEUPerTick(480)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T2OverclockedHeatVent.getMetaID())),
			new GTRecipe().setDuration(800).setEUPerTick(7680)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T3OverclockedHeatVent.getMetaID())),
			new GTRecipe().setDuration(1600).setEUPerTick(30720)
					.addOutputItem(MetaItem_ReactorComponent.getInstance().getStackFromDamage(Items.T4OverclockedHeatVent.getMetaID())),
	};
	
}
