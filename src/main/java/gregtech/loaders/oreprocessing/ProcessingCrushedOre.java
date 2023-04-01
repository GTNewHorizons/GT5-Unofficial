package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrushedOre() {
        OrePrefixes.crushedCentrifuged.add(this);
        OrePrefixes.crushedPurified.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        switch (aPrefix) {
            case crushedCentrifuged -> {
                GT_Values.RA.addForgeHammerRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        10,
                        16);
                GT_ModHandler.addPulverisationRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.dust,
                                GT_Utility.selectItemInList(2, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                                1L),
                        10,
                        false);
            }
            case crushedPurified -> {
                GT_ModHandler.addThermalCentrifugeRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        new int[] { 10000, 1111 },
                        (int) Math.min(5000L, Math.abs(aMaterial.getMass() * 20L)),
                        GT_OreDictUnificator.get(
                                OrePrefixes.crushedCentrifuged,
                                aMaterial.mMacerateInto,
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                                1L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.dust,
                                GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                                1L));
                ItemStack tGem = GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
                if (tGem != null) {
                    switch (aMaterial.mName) {
                        case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" ->
                                GT_Values.RA.addSifterRecipe(
                                        GT_Utility.copyAmount(1L, aStack),
                                        new ItemStack[] {
                                                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                                                tGem,
                                                GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L) },
                                        new int[] { 300, 1200, 4500, 1400, 2800, 3500 },
                                        800,
                                        16);
                        default -> GT_Values.RA.addSifterRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                new ItemStack[] {
                                        GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L), tGem,
                                        GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L) },
                                new int[] { 100, 400, 1500, 2000, 4000, 5000 },
                                800,
                                16);
                    }
                }
            }
            default -> {
            }
        }
    }
}
