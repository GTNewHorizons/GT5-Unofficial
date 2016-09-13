package gtPlusPlus.core.material;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.ItemStack;

public class Material {

	final String unlocalizedName;
	final String localizedName;

	final MaterialStack[] materialInput = null;

	final short[] RGBA;

	final boolean usesBlastFurnace;

	final int meltingPointK;
	final int boilingPointK;
	final int meltingPointC;
	final int boilingPointC;
	final long vProtons;
	final long vNeutrons;
	final long vMass;

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
		this.usesBlastFurnace = blastFurnace;

		for (int i=0; i < inputs.length; i++){
			if (inputs[i] != null){
				materialInput[i] = inputs[i];
			}
		}

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
    
    public boolean requiresBlastFurnace(){
    	return usesBlastFurnace;
    }

	public ItemStack getDust(){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, 1);
	}

	public ItemStack[] getValidInputStacks(){
		return UtilsItems.validItemsForOreDict(unlocalizedName);
	}
	
	public ItemStack[] getMaterialComposites(){
		ItemStack[] temp = {};
		for (int i=0;i<materialInput.length;i++){
			temp[i] = materialInput[i].getDustStack();
		}		
		return temp;
	}
	
	public int[] getMaterialCompositeStackSizes(){
		int[] temp = {};
		for (int i=0;i<materialInput.length;i++){
			temp[i] = materialInput[i].getDustStack().stackSize;
		}		
		return temp;
	}
	
}
