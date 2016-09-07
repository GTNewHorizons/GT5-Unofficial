package gtPlusPlus.core.lib;

import static gtPlusPlus.core.lib.CORE.noItem;
import static gtPlusPlus.core.util.item.UtilsItems.getItemStackOfAmountFromOreDict;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.util.materials.MaterialUtils;

import java.util.List;

import net.minecraft.item.ItemStack;

public enum MaterialInfo {
		
	ENERGYCRYSTAL(GTplusplus.randomDust_A, 8, GTplusplus.randomDust_B, 8, GTplusplus.randomDust_C, 8, GTplusplus.randomDust_D, 8, "dustEnergyCrystal", 1, noItem, 0),
	BLOODSTEEL(noItem, 0, noItem, 0, noItem, 0, noItem, 0, noItem, 0, noItem, 0),
	STABALLOY("dustTitanium", 1, "dustUranium", 8, noItem, 0, noItem, 0, "dustStaballoy", 1, noItem, 0),
	TANTALLOY60("dustTungsten", 1, "dustTantalum", 8, "dustTinyTitanium", 5, noItem, 0,	"dustTantalloy60", 1, noItem, 0),
	TANTALLOY61("dustTungsten", 1, "dustSmallTitanium", 3,	"dustSmallYttrium", 2, "dustTantalum", 9,	"dustTantalloy61", 1, noItem, 0),
	QUANTUM(noItem, 0, noItem, 0,	noItem, 0, noItem, 0, noItem, 0, noItem, 0),
	TUMBAGA("dustGold", 6, "dustCopper", 3,	noItem, 0, noItem, 0, "dustTumbaga", 2, noItem, 0),
	POTIN("dustBronze", 3, "dustTin", 2, "dustLead", 4, noItem, 0, "dustPotin", 3, noItem, 0),	
	BEDROCKIUM(noItem, 0, noItem, 0, noItem, 0, noItem, 0, noItem, 0, noItem, 0),
	INCONEL625("dustNickel", 5, "dustChrome", 2, "dustWroughtIron", 1, "dustMolybdenum", 1, "dustInconel625", 4, "dustTinyDarkAsh", 1),
	INCONEL690("dustNickel", 5, "dustChrome", 2, "dustNiobium", 1, "dustMolybdenum", 1, "dustInconel690", 2, "dustTinyDarkAsh", 1),
	INCONEL792("dustNickel", 5, "dustChrome", 1, "dustAluminium", 2, "dustNiobium", 1, "dustInconel792", 2, "dustTinyDarkAsh", 1),
	TUNGSTENCARBIDE("dustTungsten", 16, "dustCarbon", 16, noItem, 0, noItem, 0, "dustTungstenCarbide", 4, noItem, 0),
	SILICONCARBIDE("dustSilicon", 16, "dustCarbon", 16, noItem, 0, noItem, 0, "dustSiliconCarbide", 4, noItem, 0),
	ZERON100("dustChrome", 5, "dustSmallNickel", 6, "dustSmallMolybdenum", 3, "dustSteel", 14, "dustZeron100", 5, noItem, 0),
	MARAGING250("dustSteel", 4, "dustNickel", 2, "dustCobalt", 1, "dustTinyTitanium", 1, "dustMaragingSteel250", 6, noItem, 0),
	MARAGING300("dustSteel", 5, "dustNickel", 2, "dustCobalt", 2, "dustSmallTitanium", 1, "dustMaragingSteel300", 5, noItem, 0),
	MARAGING350("dustSteel", 6, "dustNickel", 3, "dustCobalt", 3, "dustTitanium", 1, "dustMaragingSteel350", 4, noItem, 0),
	STELLITE("dustCobalt", 4, "dustChrome", 4, "dustManganese", 2, "dustTitanium", 1, "dustStellite", 2, noItem, 0),
	TALONITE("dustCobalt", 4, "dustChrome", 4, "dustPhosphorus", 1, "dustMolybdenum", 1, "dustTalonite", 2, noItem, 0),
	HASTELLOY_W("dustSmallCobalt", 1, "dustSmallChrome", 4, "dustMolybdenum", 2, "dustNickel", 6, "dustHastelloyW", 2, noItem, 0),
	HASTELLOY_X("dustTinyCobalt", 6, "dustChrome", 2, "dustMolybdenum", 1, "dustNickel", 5, "dustHastelloyX", 2, noItem, 0),
	HASTELLOY_C276("dustSmallCobalt", 1, "dustSmallChrome", 14, "dustSmallMolybdenum", 14, "dustNickel", 5, "dustHastelloyC276", 2, noItem, 0),
	INCOLOY020("dustIron", 4, "dustChrome", 2, "dustTinyCarbon", 2, "dustSmallCopper", 4, "dustIncoloy020", 1, noItem, 0),
	INCOLOYDS("dustIron", 4, "dustChrome", 2, "dustTinyTitanium", 2, "dustSmallManganese", 1, "dustIncoloyDS", 1, noItem, 0),
	INCOLOYMA956("dustIron", 6, "dustChrome", 2, "dustSmallAluminium", 5, "dustTinyYttrium", 1, "dustIncoloyMA956", 1, noItem, 0),
	TANTALUMCARBIDE("dustTantalum", 4, "dustCarbon", 2, noItem, 0, noItem, 0, "dustTantalumCarbide", 1, noItem, 0),
	ZIRCONIUM(noItem, 0, noItem, 0, noItem, 0, noItem, 0, "dustZirconium", 1, noItem, 0),
	ZIRCONIUMCARBIDE("dustZirconium", 2, "dustCarbon", 2, noItem, 0, noItem, 0, "dustZirconiumCarbide", 1, noItem, 0),
	NIOMBIUMCARBIDE("dustNiobium", 2, "dustCarbon", 2, noItem, 0, noItem, 0, "dustNiobiumCarbide", 1, noItem, 0), 
	HASTELLOY_N("dustIron", 1, "dustSmallChrome", 7, "dustSmallMolybdenum", 12, "dustNickel", 4, "dustHastelloyN", 1, noItem, 0);
	
	
	
	private String input1;
	private String input2;
	private String input3;
	private String input4;
	private int inputAmount1;
	private int inputAmount2;
	private int inputAmount3;
	private int inputAmount4;
	private String out1;
	private String out2;
	private int outAmount1;
	private int outAmount2;
	public static List<String> nonLoadedInputs;
	public final Materials materialGT;
	
	private MaterialInfo (
			String inputMaterial_1, int amountIn1,
			String inputMaterial_2, int amountIn2,
			String inputMaterial_3, int amountIn3,
			String inputMaterial_4, int amountIn4,
			String output_A,int amount1, String output_B, int amount2)
	{
		this.input1 = inputMaterial_1;
		this.input2 = inputMaterial_2;
		this.input3 = inputMaterial_3;
		this.input4 = inputMaterial_4;
		this.inputAmount1 = amountIn1;
		this.inputAmount2 = amountIn2;
		this.inputAmount3 = amountIn3;
		this.inputAmount4 = amountIn4;
		this.out1 = output_A;
		this.out2 = output_B;
		this.outAmount1 = amount1;
		this.outAmount2 = amount2;
		
		
		
		this.materialGT = MaterialUtils.addGtMaterial(
				getMaterialName().toLowerCase(), TextureSet.SET_DULL, 2f, 512, 2,
				1 | 2 | 16 | 32 | 64 | 128,
				193, 211, 217, 0, getMaterialName(), 0, 0, 3015, 2150, true,
				false, 1, 2, 1, Dyes.dyeWhite, 2, null, null);		
	}

	public ItemStack[] getInputs() {
		return new ItemStack[]{
				getStack(input1, inputAmount1),
				getStack(input2, inputAmount2), 
				getStack(input3, inputAmount3),
				getStack(input4, inputAmount4)
				};
	}
	
	public ItemStack[] getOutputs() {
		return new ItemStack[]{
				getStack(out1, outAmount1), 
				getStack(out2, outAmount2)
				};
	}
	
	public String[] getInputItemsAsList(){
		String[] inputArray = {
				input1,
				input2,
				input3,
				input4
		};
		return inputArray;
	}
	
	public int[] getInputStackSizesAsList(){
		int[] inputArray = {
				inputAmount1,
				inputAmount2,
				inputAmount3,
				inputAmount4
		};
		return inputArray;
	}
	
	@SuppressWarnings("static-method")
	public ItemStack getStack(String i, int r){
		if (i == ""){
			return null;
		}
		ItemStack temp = getItemStackOfAmountFromOreDict(i,r);
		if (temp.getDisplayName().toLowerCase().contains("tell alkalus")){
			//temp = null;
		}
		return temp;
	}
	
	public String getMaterialName(){
		String x = this.name();
		return x;
	}


}
