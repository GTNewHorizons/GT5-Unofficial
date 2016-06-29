package miscutil.core.intermod.psychedelicraft.fluids;

import ivorius.ivtoolkit.gui.IntegerRange;
import ivorius.psychedelicraft.blocks.TileEntityMashTub;
import ivorius.psychedelicraft.config.PSConfig;
import ivorius.psychedelicraft.fluids.FluidAlcohol;
import ivorius.psychedelicraft.items.PSItems;
import miscutil.core.lib.CORE;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class PS_Fluids {

	public static FluidAlcohol alcJD;
	
	public static void registerFluids(){
		alcJD = new FluidAlcohol("psc_JD", 2, 0.45D, 1.9D, 0.15D, PSConfig.alcInfoPotato);
	    alcJD.addName(CORE.MODID + ":" + "drinkMash", new IntegerRange(0, -1), new IntegerRange(0, 0));
	    alcJD.addName(CORE.MODID + ":" + "drinkAgedWhisky", new IntegerRange(0, 0), new IntegerRange(1, -1));
	    alcJD.addName(CORE.MODID + ":" + "drinkWhisky", new IntegerRange(0, -1), new IntegerRange(1, -1));
	    alcJD.setColor(-1426150904);
	    alcJD.setStillIconName(CORE.MODID + ":" + "mash_still");
	    alcJD.setFlowingIconName(CORE.MODID + ":" + "mash_flow");
	    alcJD.addIcon(new IntegerRange(-1, -1), new IntegerRange(0, 3), new IntegerRange(2, -1), CORE.MODID + ":" + "clear_still", CORE.MODID + ":" + "clear_flow");
	    alcJD.addIcon(new IntegerRange(-1, -1), new IntegerRange(4, 13), new IntegerRange(0, -1), CORE.MODID + ":" + "rum_semi_mature_still", CORE.MODID + ":" + "rum_semi_mature_flow");
	    alcJD.addIcon(new IntegerRange(-1, -1), new IntegerRange(14, -1), new IntegerRange(0, -1), CORE.MODID + ":" + "rum_mature_still", CORE.MODID + ":" + "rum_mature_flow");
	    FluidRegistry.registerFluid(alcJD);
	}
	
	public static void registerAlcohols(){
		addMashTubRecipe2(new FluidStack(alcJD, TileEntityMashTub.MASH_TUB_CAPACITY), new Object[] { "foodPotato", "foodPotato", "foodPotato", "foodPotato", "foodPotato", "foodBanana", "foodBanana", "foodBanana" });
	}
	 
	private static void addMashTubRecipe2(FluidStack fluid, Object... ingredients)
	  {
	    ItemStack mashTubStack = new ItemStack(PSItems.itemMashTub);
	    PSItems.itemMashTub.fill(mashTubStack, fluid, true);
	    
	    Object[] ing = new Object[ingredients.length + 1];
	    System.arraycopy(ingredients, 0, ing, 1, ingredients.length);
	    ing[0] = new ItemStack(PSItems.itemMashTub);
	    
	    addShapelessRecipe2(mashTubStack, ing);
	  }
	
	private static void addShapelessRecipe2(ItemStack output, Object... params)
	  {
	    GameRegistry.addRecipe(new ShapelessOreRecipe(output, params));
	  }
	
}
