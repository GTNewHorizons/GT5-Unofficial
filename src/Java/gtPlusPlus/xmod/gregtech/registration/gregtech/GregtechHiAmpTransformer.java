package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp;

public class GregtechHiAmpTransformer {

	
	public static void run(){
		
		long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
				| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
		int mID = 877;
		
		GregtechItemList.Transformer_HA_LV_ULV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.00",
				"ULV Hi-Amp Transformer", 0, "LV -> ULV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_MV_LV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.01",
				"LV Hi-Amp Transformer", 1, "MV -> LV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_HV_MV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.02",
				"MV Hi-Amp Transformer", 2, "HV -> MV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_EV_HV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.03",
				"HV Hi-Amp Transformer", 3, "EV -> HV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_IV_EV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.04",
				"EV Hi-Amp Transformer", 4, "IV -> EV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_LuV_IV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.05",
				"IV Hi-Amp Transformer", 5, "LuV -> IV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_ZPM_LuV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.06",
				"LuV Hi-Amp Transformer", 6, "ZPM -> LuV (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_UV_ZPM.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.07",
				"ZPM Hi-Amp Transformer", 7, "UV -> ZPM (Use Soft Hammer to invert)").getStackForm(1L));
		GregtechItemList.Transformer_HA_MAX_UV.set(new GregtechMetaTransformerHiAmp(mID++, "transformer.ha.tier.08",
				"UV Hi-Amp Transformer", 8, "Any Voltage -> UV (Use Soft Hammer to invert)").getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_LV_ULV.get(1L, new Object[0]), bitsd,
				new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Hull_ULV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.Tin), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Lead) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_MV_LV.get(1L, new Object[0]), bitsd,
				new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Hull_LV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.AnyCopper), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Tin) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_HV_MV.get(1L, new Object[0]), bitsd,
				new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Hull_MV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.Gold), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.AnyCopper) });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_EV_HV.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.Aluminium), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Gold), Character.valueOf('K'),
						ItemList.Casing_Coil_Cupronickel });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_IV_EV.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.Tungsten), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Aluminium), Character.valueOf('K'),
						ItemList.Casing_Coil_Kanthal });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_LuV_IV.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.VanadiumGallium), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Tungsten), Character.valueOf('K'),
						ItemList.Casing_Coil_Nichrome });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_ZPM_LuV.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('C'),
						OrePrefixes.wireGt16.get(Materials.Naquadah), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.VanadiumGallium), Character.valueOf('K'),
						ItemList.Casing_Coil_TungstenSteel });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_UV_ZPM.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_ZPM, Character.valueOf('C'),
						OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('B'),
						OrePrefixes.wireGt16.get(Materials.Naquadah), Character.valueOf('K'),
						ItemList.Casing_Coil_Naquadah });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Transformer_HA_MAX_UV.get(1L, new Object[0]), bitsd,
				new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Hull_UV, Character.valueOf('C'),
						OrePrefixes.wireGt01.get(Materials.Superconductor), Character.valueOf('B'),
						OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('K'),
						ItemList.Casing_Coil_NaquadahAlloy });
	}
	
}
