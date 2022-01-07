package gtPlusPlus.core.material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class NONMATERIAL {
	
	//Soul Sand
	public static final Material SOULSAND = MaterialUtils.generateMaterialFromGtENUM(Materials.SoulSand);

	//Redstone
	public static final Material REDSTONE = MaterialUtils.generateMaterialFromGtENUM(Materials.Redstone);
	
	//Glowstone Dust
	public static final Material GLOWSTONE = MaterialUtils.generateMaterialFromGtENUM(Materials.Glowstone);

	//Enderpearl
	public static final Material ENDERPEARL = MaterialUtils.generateMaterialFromGtENUM(Materials.EnderPearl);

	//Raw Flesh
	public static final Material MEAT = MaterialUtils.generateMaterialFromGtENUM(Materials.MeatRaw);
	
	//Clay
	public static final Material CLAY = MaterialUtils.generateMaterialFromGtENUM(Materials.Clay);

	//Wrought Iron
	public static final Material WROUGHT_IRON = MaterialUtils.generateMaterialFromGtENUM(Materials.WroughtIron);

	//PTFE
	public static final Material PTFE = MaterialUtils.generateMaterialFromGtENUM(MaterialUtils.getMaterial("Polytetrafluoroethylene", "Plastic"));

	//Plastic
	public static final Material PLASTIC = MaterialUtils.generateMaterialFromGtENUM(MaterialUtils.getMaterial("Plastic", "Rubber"));
	
	
	static {
		MEAT.setTextureSet(TextureSet.SET_ROUGH);
		CLAY.setTextureSet(TextureSet.SET_ROUGH);
	}
	
	
	
}
