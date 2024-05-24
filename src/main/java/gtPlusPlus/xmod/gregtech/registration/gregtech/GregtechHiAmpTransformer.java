package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp;

public class GregtechHiAmpTransformer {

    public static void run() {

        long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;
        int mID = 877;

        String mHammerName = "Mallet";

        GregtechItemList.Transformer_HA_LV_ULV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.00",
                "ULV Hi-Amp Transformer",
                0,
                "LV -> ULV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_MV_LV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.01",
                "LV Hi-Amp Transformer",
                1,
                "MV -> LV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_HV_MV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.02",
                "MV Hi-Amp Transformer",
                2,
                "HV -> MV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_EV_HV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.03",
                "HV Hi-Amp Transformer",
                3,
                "EV -> HV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_IV_EV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.04",
                "EV Hi-Amp Transformer",
                4,
                "IV -> EV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_LuV_IV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.05",
                "IV Hi-Amp Transformer",
                5,
                "LuV -> IV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_ZPM_LuV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.06",
                "LuV Hi-Amp Transformer",
                6,
                "ZPM -> LuV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_UV_ZPM.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.07",
                "ZPM Hi-Amp Transformer",
                7,
                "UV -> ZPM (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        GregtechItemList.Transformer_HA_MAX_UV.set(
            new GregtechMetaTransformerHiAmp(
                mID++,
                "transformer.ha.tier.08",
                "UV Hi-Amp Transformer",
                8,
                "UHV -> UV (Use Soft " + mHammerName + " to invert)").getStackForm(1L));
        ItemStack mItem_1;
        ItemStack mItem_2;
        ItemStack mItem_3;

        mItem_1 = ItemUtils.simpleMetaStack(
            ItemUtils.getSimpleStack(GregTech_API.sBlockCasings5)
                .getItem(),
            3,
            1);
        mItem_2 = ItemUtils.simpleMetaStack(
            ItemUtils.getSimpleStack(GregTech_API.sBlockCasings5)
                .getItem(),
            4,
            1);
        mItem_3 = ItemUtils.simpleMetaStack(
            ItemUtils.getSimpleStack(GregTech_API.sBlockCasings5)
                .getItem(),
            9,
            1);

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_LV_ULV.get(1L, new Object[0]),
            bitsd,
            new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_LV_ULV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Tin), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_MV_LV.get(1L, new Object[0]),
            bitsd,
            new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_MV_LV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.AnyCopper), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_HV_MV.get(1L, new Object[0]),
            bitsd,
            new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_HV_MV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Gold), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_EV_HV.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_EV_HV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Aluminium), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Gold), Character.valueOf('K'), ItemList.Casing_Coil_Cupronickel });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_IV_EV.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_IV_EV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Tungsten), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Aluminium), Character.valueOf('K'), ItemList.Casing_Coil_Kanthal });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_LuV_IV.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_LuV_IV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.VanadiumGallium), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Tungsten), Character.valueOf('K'), ItemList.Casing_Coil_Nichrome });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_ZPM_LuV.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_ZPM_LuV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Naquadah), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.VanadiumGallium), Character.valueOf('K'), mItem_1 });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_UV_ZPM.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_UV_ZPM,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.Naquadah), Character.valueOf('K'), mItem_2 });
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_MAX_UV.get(1L, new Object[0]),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_MAX_UV,
                Character.valueOf('C'), OrePrefixes.wireGt04.get(Materials.Bedrockium), Character.valueOf('B'),
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('K'), mItem_3 });
    }
}
