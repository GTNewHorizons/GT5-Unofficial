package gtPlusPlus.core.material;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.ItemStack;

public class Material {

	final String unlocalizedName;
	final String localizedName;

	private MaterialStack[] materialInput = new MaterialStack[4];

	final short[] RGBA;

	final boolean usesBlastFurnace;

	final int meltingPointK;
	final int boilingPointK;
	final int meltingPointC;
	final int boilingPointC;
	final long vProtons;
	final long vNeutrons;
	final long vMass;
	public final int vTier;
	public final int vVoltageMultiplier;

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint == 0){
			boilingPoint = meltingPoint*4;
		}
		this.boilingPointC = boilingPoint;
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = getMass();

		if (getMeltingPoint_K() >= 0 && getMeltingPoint_K() <= 750){
			this.vTier = 1;
		}
		else if(getMeltingPoint_K() >= 751 && getMeltingPoint_K() <= 1250){
			this.vTier = 2;
		}
		else if(getMeltingPoint_K() >= 1251 && getMeltingPoint_K() <= 1750){
			this.vTier = 3;
		}
		else if(getMeltingPoint_K() >= 1751 && getMeltingPoint_K() <= 2250){
			this.vTier = 4;
		}
		else if(getMeltingPoint_K() >= 2251 && getMeltingPoint_K() <= 2750){
			this.vTier = 5;
		}
		else if(getMeltingPoint_K() >= 2751 && getMeltingPoint_K() <= 3250){
			this.vTier = 6;
		}
		else if(getMeltingPoint_K() >= 3251 && getMeltingPoint_K() <= 3750){
			this.vTier = 7;
		}
		else if(getMeltingPoint_K() >= 3751 && getMeltingPoint_K() <= 4250){
			this.vTier = 8;
		}
		else if(getMeltingPoint_K() >= 4251 && getMeltingPoint_K() <= 4750){
			this.vTier = 9;
		}
		else if(getMeltingPoint_K() >= 4751 && getMeltingPoint_K() <= 9999){
			this.vTier = 10;
		}
		else {
			this.vTier = 0;
		}

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPoint_K() >= 2800 ? 64 : 16;

		if (inputs == null){
			this.materialInput = null;			
		}
		else {
			if (inputs.length != 0){
				for (int i=0; i < inputs.length; i++){
					if (inputs[i] != null){
						this.materialInput[i] = inputs[i];
					}
				}
			}
		}
		Utils.LOG_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_INFO("Protons: "+vProtons);
		Utils.LOG_INFO("Neutrons: "+vNeutrons);
		Utils.LOG_INFO("Mass: "+vMass+"/units");
		Utils.LOG_INFO("Melting Point: "+meltingPointC+"C.");
		Utils.LOG_INFO("Boiling Point: "+boilingPointC+"C.");
	}

	public String getLocalizedName(){
		return localizedName;
	}

	public String getUnlocalizedName(){
		return unlocalizedName;
	}

	public short[] getRGBA(){
		return RGBA;
	}
	
	public int getRgbAsHex(){
		return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
	}

	public long getProtons() {
		return vProtons;
	}

	public long getNeutrons() {
		return vNeutrons;
	}

	public long getMass() {
		return vProtons + vNeutrons;
	}

	public int getMeltingPoint_C() {
		return meltingPointC;
	}

	public int getBoilingPoint_C() {
		return boilingPointC;
	}

	public int getMeltingPoint_K() {
		return meltingPointK;
	}

	public int getBoilingPoint_K() {
		return boilingPointK;
	}

	public boolean requiresBlastFurnace(){
		return usesBlastFurnace;
	}

	public ItemStack getDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, stacksize);
	}

	public ItemStack getSmallDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+unlocalizedName, stacksize);
	}

	public ItemStack getTinyDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+unlocalizedName, stacksize);
	}

	public ItemStack[] getValidInputStacks(){
		return UtilsItems.validItemsForOreDict(unlocalizedName);
	}

	public ItemStack getIngot(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("ingot"+unlocalizedName, stacksize);
	}

	public ItemStack getPlate(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("plate"+unlocalizedName, stacksize);
	}

	public ItemStack getPlateDouble(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+unlocalizedName, stacksize);
	}
	
	public ItemStack getGear(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("gear"+unlocalizedName, stacksize);
	}
	
	public ItemStack getRod(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("stick"+unlocalizedName, stacksize);
	}

	public ItemStack[] getMaterialComposites(){
		//Utils.LOG_INFO("Something requested the materials needed for "+localizedName);
		if (materialInput != null && materialInput.length >= 1){
			ItemStack[] temp = new ItemStack[materialInput.length];
			for (int i=0;i<materialInput.length;i++){
				//Utils.LOG_INFO("i:"+i);
				ItemStack testNull = null;
				try {
					testNull = materialInput[i].getDustStack();
				} catch (Throwable r){
					Utils.LOG_INFO("Failed gathering material stack for "+localizedName+".");
					Utils.LOG_INFO("What Failed: Length:"+materialInput.length+" current:"+i);
				}
				try {
					if (testNull != null){
						//Utils.LOG_INFO("not null");
						temp[i] = materialInput[i].getDustStack();
					}
				} catch (Throwable r){
					Utils.LOG_INFO("Failed setting slot "+i+", using "+localizedName);
				}
			}		
			return temp;
		}
		return new ItemStack[]{};
	}

	public int[] getMaterialCompositeStackSizes(){
		if (materialInput != null && materialInput.length >= 1){
			int[] temp = new int[materialInput.length];
			for (int i=0;i<materialInput.length;i++){
				temp[i] = materialInput[i].getDustStack().stackSize;
			}		
			return temp;
		}
		return new int[]{};
	}

}
