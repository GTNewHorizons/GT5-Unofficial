package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_EV_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_HV_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_IV_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_LV_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_LuV_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_MAX_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_MV_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_UV_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Transformer_HA_ZPM_LuV;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTETransformerHiAmp;

public class GregtechHiAmpTransformer {

    public static void run() {

        long bitsd = GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
            | GTModHandler.RecipeBits.REVERSIBLE
            | GTModHandler.RecipeBits.BUFFERED;

        String mHammerName = "Mallet";

        GregtechItemList.Transformer_HA_LV_ULV.set(
            new MTETransformerHiAmp(Transformer_HA_LV_ULV.ID, "transformer.ha.tier.00", "ULV Hi-Amp Transformer", 0)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_MV_LV.set(
            new MTETransformerHiAmp(Transformer_HA_MV_LV.ID, "transformer.ha.tier.01", "LV Hi-Amp Transformer", 1)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_HV_MV.set(
            new MTETransformerHiAmp(Transformer_HA_HV_MV.ID, "transformer.ha.tier.02", "MV Hi-Amp Transformer", 2)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_EV_HV.set(
            new MTETransformerHiAmp(Transformer_HA_EV_HV.ID, "transformer.ha.tier.03", "HV Hi-Amp Transformer", 3)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_IV_EV.set(
            new MTETransformerHiAmp(Transformer_HA_IV_EV.ID, "transformer.ha.tier.04", "EV Hi-Amp Transformer", 4)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_LuV_IV.set(
            new MTETransformerHiAmp(Transformer_HA_LuV_IV.ID, "transformer.ha.tier.05", "IV Hi-Amp Transformer", 5)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_ZPM_LuV.set(
            new MTETransformerHiAmp(Transformer_HA_ZPM_LuV.ID, "transformer.ha.tier.06", "LuV Hi-Amp Transformer", 6)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_UV_ZPM.set(
            new MTETransformerHiAmp(Transformer_HA_UV_ZPM.ID, "transformer.ha.tier.07", "ZPM Hi-Amp Transformer", 7)
                .getStackForm(1L));
        GregtechItemList.Transformer_HA_MAX_UV.set(
            new MTETransformerHiAmp(Transformer_HA_MAX_UV.ID, "transformer.ha.tier.08", "UV Hi-Amp Transformer", 8)
                .getStackForm(1L));
        ItemStack mItem_1;
        ItemStack mItem_2;
        ItemStack mItem_3;

        mItem_1 = new ItemStack(GregTechAPI.sBlockCasings5, 1, 3);
        mItem_2 = new ItemStack(GregTechAPI.sBlockCasings5, 1, 4);
        mItem_3 = new ItemStack(GregTechAPI.sBlockCasings5, 1, 9);

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_LV_ULV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Transformer_LV_ULV, 'C',
                OrePrefixes.wireGt04.get(Materials.Tin), 'B', OrePrefixes.wireGt04.get(Materials.Lead) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_MV_LV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Transformer_MV_LV, 'C',
                OrePrefixes.wireGt04.get(Materials.AnyCopper), 'B', OrePrefixes.wireGt04.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_HV_MV.get(1L),
            bitsd,
            new Object[] { " BB", "CM ", " BB", 'M', ItemList.Transformer_HV_MV, 'C',
                OrePrefixes.wireGt04.get(Materials.Gold), 'B', OrePrefixes.wireGt04.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_EV_HV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_EV_HV, 'C',
                OrePrefixes.wireGt04.get(Materials.Aluminium), 'B', OrePrefixes.wireGt04.get(Materials.Gold), 'K',
                ItemList.Casing_Coil_Cupronickel });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_IV_EV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_IV_EV, 'C',
                OrePrefixes.wireGt04.get(Materials.Tungsten), 'B', OrePrefixes.wireGt04.get(Materials.Aluminium), 'K',
                ItemList.Casing_Coil_Kanthal });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_LuV_IV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_LuV_IV, 'C',
                OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'B', OrePrefixes.wireGt04.get(Materials.Tungsten),
                'K', ItemList.Casing_Coil_Nichrome });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_ZPM_LuV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_ZPM_LuV, 'C',
                OrePrefixes.wireGt04.get(Materials.Naquadah), 'B', OrePrefixes.wireGt04.get(Materials.VanadiumGallium),
                'K', mItem_1 });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_UV_ZPM.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_UV_ZPM, 'C',
                OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'B', OrePrefixes.wireGt04.get(Materials.Naquadah),
                'K', mItem_2 });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Transformer_HA_MAX_UV.get(1L),
            bitsd,
            new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Transformer_MAX_UV, 'C',
                OrePrefixes.wireGt04.get(Materials.Bedrockium), 'B', OrePrefixes.wireGt04.get(Materials.NaquadahAlloy),
                'K', mItem_3 });
    }
}
