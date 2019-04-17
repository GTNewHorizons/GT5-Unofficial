package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.TextureSet;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GenericChem extends ItemPackage {

	/**
	 * Materials
	 */	
	
	public static final Material BAKELITE = new Material("Bakelite", MaterialState.SOLID, TextureSet.SET_DULL, new short[]{90, 140, 140}, 120, 240, 23, 24, true, null, 0);//Not a GT Inherited Material
	public static final Material NYLON = new Material("Nylon", MaterialState.SOLID, TextureSet.SET_SHINY, new short[]{45, 45, 45}, 300, 600, 44, 48, true, null, 0);//Not a GT Inherited Material
	
	//Refined PTFE
	public static final Material TEFLON = new Material(
			"Teflon",
			MaterialState.SOLID,
			TextureSet.SET_SHINY,
			new short[] { 75, 45, 75 },
			330, 640,
			-1, -1,
			false,
			null,
			0,
			new MaterialStack[] {
					new MaterialStack(NONMATERIAL.PTFE, 75),
					new MaterialStack(NONMATERIAL.PLASTIC, 15),
					new MaterialStack(ELEMENT.getInstance().NITROGEN, 5),
					new MaterialStack(ELEMENT.getInstance().SODIUM, 5)
					});// Not a GT
																										// Inherited
																										// Material

	//public static final Material HYPOGEN = new Material("Hypogen", MaterialState.SOLID, TextureSets.NUCLEAR.get(), new short[]{220, 120, 75}, 12255, 19377, 240, 251, true, "Hy⚶", 0);//Not a GT Inherited Material
	//public static final Material HYPOGEN = new Material("Hypogen", MaterialState.SOLID, TextureSets.NUCLEAR.get(), new short[]{220, 120, 75}, 12255, 19377, 240, 251, true, "Hy⚶", 0);//Not a GT Inherited Material
	//public static final Material Nylon = new Material();
	
	/**
	 * Fluids
	 */

	public Fluid Benzene;
	public Fluid NitroBenzene;
	public Fluid Aniline;
	public Fluid Polyurethane;
	public Fluid Phenol; //https://en.wikipedia.org/wiki/Phenol#Uses
	public Fluid Cyclohexane; //https://en.wikipedia.org/wiki/Cyclohexane	
	public Fluid Cyclohexanone; //https://en.wikipedia.org/wiki/Cyclohexanone
	
	public Fluid Cadaverine; //https://en.wikipedia.org/wiki/Cadaverine
	public Fluid Putrescine; //https://en.wikipedia.org/wiki/Putrescine
	

	/**
	 * Items
	 */

	// Phenol Byproducts
	public Item PhenolicResins; //https://en.wikipedia.org/wiki/Phenol_formaldehyde_resin



	@Override
	public void items() {		
		PhenolicResins = ItemUtils.generateSpecialUseDusts("phenolicresins", "Phenolic Resin", "HOC6H4CH2OH", Utils.rgbtoHexValue(80, 40, 40))[0];		
		MaterialGenerator.generate(BAKELITE, false);	
		MaterialGenerator.generate(NYLON, false);
		MaterialGenerator.generate(TEFLON, false);
	}

	@Override
	public void blocks() {}

	@Override
	public void fluids() {
		
		if (!FluidRegistry.isFluidRegistered("benzene")) {
			Benzene = FluidUtils.generateFluidNoPrefix("benzene", "Benzene", 278,	new short[] { 100, 70, 30, 100 }, true);			
		}
		else {
			Benzene = FluidRegistry.getFluid("benzene");
		}
		
		NitroBenzene = FluidUtils.generateFluidNoPrefix("nitrobenzene", "NitroBenzene", 278,	new short[] { 70, 50, 40, 100 }, true);
		
		Aniline = FluidUtils.generateFluidNoPrefix("aniline", "Aniline", 266,	new short[] { 100, 100, 30, 100 }, true);
		
		Polyurethane = FluidUtils.generateFluidNoPrefix("polyurethane", "Polyurethane", 350,	new short[] { 100, 70, 100, 100 }, true);
		
		if (!FluidRegistry.isFluidRegistered("phenol")) {
			Phenol = FluidUtils.generateFluidNoPrefix("phenol", "Phenol", 313,	new short[] { 100, 70, 30, 100 }, true);			
		}
		else {
			Phenol = FluidRegistry.getFluid("phenol");
		}		
		
		Cyclohexane = FluidUtils.generateFluidNoPrefix("cyclohexane", "Cyclohexane", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);		
		Cyclohexanone = FluidUtils.generateFluidNoPrefix("cyclohexanone", "Cyclohexanone", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		
		Cadaverine = FluidUtils.generateFluidNoPrefix("cadaverine", "Cadaverine", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		Putrescine = FluidUtils.generateFluidNoPrefix("putrescine", "Putrescine", 32 + 175,	new short[] { 100, 70, 30, 100 }, true);
		
	}	

	@Override
	public String errorMessage() {
		// TODO Auto-generated method stub
		return "Failed to generate recipes for AgroChem.";
	}

	@Override
	public boolean generateRecipes() {		

		
		return true;
	}
}
