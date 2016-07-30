package miscutil.core.lib;

import static miscutil.core.lib.CORE.noItem;
import static miscutil.core.util.item.UtilsItems.getItemStackOfAmountFromOreDict;
import net.minecraft.item.ItemStack;

public enum MaterialInfo {
		
	ENERGYCRYSTAL(getStack("dustInfusedFire", 8), getStack("dustInfusedEarth", 8), getStack("dustInfusedEntropy", 8), getStack("dustInfusedOrder", 8), "dustEnergyCrystal", 1, noItem, 0),
	BLOODSTEEL(null, null, null, null, noItem, 0, noItem, 0),
	STABALLOY(getStack("dustTitanium", 1), getStack("dustUranium", 8), null, null, "dustStaballoy", 1, noItem, 0),
	TANTALLOY60(getStack("dustTungsten", 1), getStack("dustTantalum", 8), getStack("dustTinyTitanium", 5), null,	"dustTantalloy60", 1, noItem, 0),
	TANTALLOY61(getStack("dustTungsten", 1), getStack("dustSmallTitanium", 3),	getStack("dustSmallYttrium", 2), getStack("dustTantalum", 9),	"dustTantalloy61", 1, noItem, 0),
	QUANTUM(null, null,	null, null, noItem, 0, noItem, 0),
	TUMBAGA(getStack("dustGold", 6), getStack("dustCopper", 3),	null, null,	"dustTumbaga", 1, noItem, 0),
	POTIN(getStack("dustBronze", 3), getStack("dustTin", 2), getStack("dustLead", 4), null, "dustPotin", 1, noItem, 0),	
	BEDROCKIUM(null, null, null, null, noItem, 0, noItem, 0),
	INCONEL625(getStack("dustNickel", 5), getStack("dustChrome", 2), getStack("dustWroughtIron", 1), getStack("dustMolybdenum", 1), "dustInconel625", 4, "dustTinyDarkAsh", 1),
	INCONEL690(getStack("dustNickel", 5), getStack("dustChrome", 2), getStack("dustNiobium", 1), getStack("dustMolybdenum", 1), "dustInconel690", 2, "dustTinyDarkAsh", 1),
	INCONEL792(getStack("dustNickel", 5), getStack("dustChrome", 1), getStack("dustAluminium", 2), getStack("dustNiobium", 1), "dustInconel792", 2, "dustTinyDarkAsh", 1),
	TUNGSTENCARBIDE(getStack("dustTungsten", 16), getStack("dustCarbon", 16), null, null, "dustTungstenCarbide", 4, noItem, 0),
	SILICONCARBIDE(getStack("dustSilicon", 16), getStack("dustCarbon", 16), null, null, "dustSiliconCarbide", 4, noItem, 0),
	ZERON100(getStack("dustChrome", 5), getStack("dustSmallNickel", 6), getStack("dustSmallMolybdenum", 3), getStack("dustSteel", 14), "dustZeron100", 5, noItem, 0),
	MARAGING250(getStack("dustSteel", 4), getStack("dustNickel", 2), getStack("dustCobalt", 1), getStack("dustTinyTitanium", 1), "dustMaragingSteel250", 6, noItem, 0),
	MARAGING300(getStack("dustSteel", 5), getStack("dustNickel", 2), getStack("dustCobalt", 2), getStack("dustSmallTitanium", 1), "dustMaragingSteel300", 5, noItem, 0),
	MARAGING350(getStack("dustSteel", 6), getStack("dustNickel", 3), getStack("dustCobalt", 3), getStack("dustTitanium", 1), "dustMaragingSteel350", 4, noItem, 0),
	STELLITE(getStack("dustCobalt", 4), getStack("dustChrome", 4), getStack("dustManganese", 2), getStack("dustTitanium", 1), "dustStellite", 2, noItem, 0),
	TALONITE(getStack("dustCobalt", 4), getStack("dustChrome", 4), getStack("dustPhosphorus", 1), getStack("dustMolybdenum", 1), "dustTalonite", 2, noItem, 0);

	private ItemStack input1;
	private ItemStack input2;
	private ItemStack input3;
	private ItemStack input4;
	private String out1;
	private String out2;
	private int outAmount1;
	private int outAmount2;
	
	private MaterialInfo (
			ItemStack inputMaterial_1, ItemStack inputMaterial_2,
			ItemStack inputMaterial_3, ItemStack inputMaterial_4,
			String output_A,int amount1, String output_B, int amount2)
	{
		this.input1 = inputMaterial_1;
		this.input2 = inputMaterial_2;
		this.input3 = inputMaterial_3;
		this.input4 = inputMaterial_4;
		this.out1 = output_A;
		this.out2 = output_B;
		this.outAmount1 = amount1;
		this.outAmount2 = amount2;
	}

	public ItemStack[] getInputs() {
		return new ItemStack[]{input1, input2, input3, input4};
	}
	
	public ItemStack[] getOutputs() {
		return new ItemStack[]{getStack(out1, outAmount1), getStack(out2, outAmount2)};
	}
	
	public static ItemStack getStack(String i, int r){
		return getItemStackOfAmountFromOreDict(i,r);
	}


}
