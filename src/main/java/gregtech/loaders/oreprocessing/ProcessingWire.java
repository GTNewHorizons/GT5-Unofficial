package gregtech.loaders.oreprocessing;

import appeng.api.config.TunnelType;
import appeng.core.Api;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ProcessingWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private Materials[] dielectrics = {Materials.PolyvinylChloride, Materials.Polydimethylsiloxane};
    private Materials[] rubbers = {Materials.Rubber, Materials.StyreneButadieneRubber, Materials.Silicone};
    private Materials[] syntheticRubbers = {Materials.StyreneButadieneRubber, Materials.Silicone};

    public ProcessingWire() {
        OrePrefixes.wireGt01.add(this);
        OrePrefixes.wireGt02.add(this);
        OrePrefixes.wireGt04.add(this);
        OrePrefixes.wireGt08.add(this);
        OrePrefixes.wireGt12.add(this);
        OrePrefixes.wireGt16.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        int cableWidth;
        OrePrefixes correspondingCable;
        switch (aPrefix) {
            case wireGt01:
                cableWidth = 1;
                correspondingCable = OrePrefixes.cableGt01;
                if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L), 100, 8);
                    GT_Values.RA.addWiremillRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L), 200, 8);
                    //GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), GT_Utility.copy(new Object[]{GT_Utility.copyAmount(2L, aStack), GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 8L)}), 100, 4);
                    //GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L), GT_Utility.copy(new Object[]{aStack, GT_OreDictUnificator.get(OrePrefixes.wireFine, aMaterial, 4L)}), 50, 4);
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial) && !aMaterial.contains(SubTag.NO_WORKING))
                    //GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"Xx", Character.valueOf('X'), OrePrefixes.plate.get(aMaterial)});
                GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L), 150, 8);
                GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(4L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L), 200, 8);
                GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(8L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt08, aMaterial, 1L), 300, 8);
                GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(12L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 12L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt12, aMaterial, 1L), 400, 8);
                GT_Values.RA.addAssemblerRecipe(GT_Utility.copyAmount(16L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 16L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt16, aMaterial, 1L), 500, 8);
                break;
            case wireGt02:
                cableWidth = 2;
                correspondingCable = OrePrefixes.cableGt02;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 2L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial)});
                break;
            case wireGt04:
                cableWidth = 4;
                correspondingCable = OrePrefixes.cableGt04;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 4L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial)});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt02.get(aMaterial), OrePrefixes.wireGt02.get(aMaterial)});
                break;
            case wireGt08:
                cableWidth = 8;
                correspondingCable = OrePrefixes.cableGt08;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 8L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial),OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial), OrePrefixes.wireGt01.get(aMaterial)});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt04.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial)});
                break;
            case wireGt12:
                cableWidth = 12;
                correspondingCable = OrePrefixes.cableGt12;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 12L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt08.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial)});
                break;
            case wireGt16:
                cableWidth = 16;
                correspondingCable = OrePrefixes.cableGt16;
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 16L), new Object[]{aOreDictName});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt08.get(aMaterial), OrePrefixes.wireGt08.get(aMaterial)});
                GT_ModHandler.addShapelessCraftingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), new Object[]{OrePrefixes.wireGt12.get(aMaterial), OrePrefixes.wireGt04.get(aMaterial)});

                if (GT_Mod.gregtechproxy.mAE2Integration) {
                    Api.INSTANCE.registries().p2pTunnel().addNewAttunement(aStack, TunnelType.IC2_POWER);
                }
                break;
            default:
                GT_Log.err.println("OrePrefix " + aPrefix.name() + " cannot be registered as a cable for Material " + aMaterial.mName);
                return;
        }

        int costMultiplier = cableWidth / 4 + 1;


        switch (aMaterial.mName){
            case "RedAlloy":case "Cobalt": case "Lead": case "Tin": case "Zinc":case "SolderingAlloy":
                ArrayList<Object> craftingListRubber = new ArrayList<Object>();
                craftingListRubber.add(aOreDictName);
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(OrePrefixes.plate.get(Materials.Rubber));
                }
                GT_ModHandler.addShapelessCraftingRecipe(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), craftingListRubber.toArray());
                GT_Values.RA.addBoxingRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Rubber), costMultiplier),  GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt01, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L), GT_OreDictUnificator.get(OrePrefixes.wireGt02, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt02, aMaterial, 1L), 200, 16);
                GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 4L), GT_OreDictUnificator.get(OrePrefixes.wireGt04, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.cableGt04, aMaterial, 1L), 300, 30);
            case "Iron": case "Nickel": case "Cupronickel": case "Copper": case "AnnealedCopper":
            case "Kanthal": case "Gold": case "Electrum": case "Silver": case "Blue Alloy":
            case "Nichrome": case "Steel": case "BlackSteel": case "Titanium": case "Aluminium": case "BlueAlloy" :
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Rubber.getMolten(144 * costMultiplier),
                        GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier),
                        GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Silicone.getMolten(72 * costMultiplier),
                        GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {
                        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, dielectric.getDustSmall(costMultiplier)},
                                syntheticRubber.getMolten(costMultiplier * 36), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    }
                }
            case "RedstoneAlloy":
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier),
                        GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Silicone.getMolten(72 * costMultiplier),
                        GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                for (Materials dielectric : dielectrics) {
                    for (Materials syntheticRubber : syntheticRubbers) {
                        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, dielectric.getDustSmall(costMultiplier)},
                                syntheticRubber.getMolten(costMultiplier * 36), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    }
                }
                break;
                default:
                if (GT_Mod.gregtechproxy.mEasierIVPlusCables) {
                    GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Rubber.getMolten(144 * costMultiplier),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.StyreneButadieneRubber.getMolten(108 * costMultiplier),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    GT_Values.RA.addAssemblerRecipe(aStack, GT_Utility.getIntegratedCircuit(24), Materials.Silicone.getMolten(72 * costMultiplier),
                            GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    for (Materials dielectric : dielectrics) {
                        for (Materials syntheticRubber : syntheticRubbers) {
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier)},
                                    syntheticRubber.getMolten(costMultiplier * 144), GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L), 400, 8);
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, dielectric.getDustSmall(costMultiplier)},
                                    syntheticRubber.getMolten(costMultiplier * 36), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                        }
                    }
                } else {
                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier), GT_Utility.getIntegratedCircuit(24)},
                            Materials.Silicone.getMolten(costMultiplier * 72), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier), GT_Utility.getIntegratedCircuit(24)},
                            Materials.Silicone.getMolten(costMultiplier * 72), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                    for (Materials dielectric : dielectrics) {
                        for (Materials syntheticRubber : syntheticRubbers) {
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier), GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier * 4)},
                                    syntheticRubber.getMolten(costMultiplier * 144), GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L), 400, 8);
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_Utility.copyAmount(4, aStack), dielectric.getDust(costMultiplier), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier * 4)},
                                    syntheticRubber.getMolten(costMultiplier * 144), GT_OreDictUnificator.get(correspondingCable, aMaterial, 4L), 400, 8);
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, dielectric.getDustSmall(costMultiplier), GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, costMultiplier)},
                                    syntheticRubber.getMolten(costMultiplier * 36), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{aStack, dielectric.getDustSmall(costMultiplier), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyphenyleneSulfide, costMultiplier)},
                                    syntheticRubber.getMolten(costMultiplier * 36), GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), 100, 8);
                        }
                    }
                }
                break;
        }
        GT_Values.RA.addUnboxingRecipe(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), GT_Utility.copyAmount(1L, new Object[]{aStack}), null, 100, 8);
        if (GT_Mod.gregtechproxy.mAE2Integration) {
            Api.INSTANCE.registries().p2pTunnel().addNewAttunement(aStack, TunnelType.IC2_POWER);
            Api.INSTANCE.registries().p2pTunnel().addNewAttunement(GT_OreDictUnificator.get(correspondingCable, aMaterial, 1L), TunnelType.IC2_POWER);
        }
    }
}
