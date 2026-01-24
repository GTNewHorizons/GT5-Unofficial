package gtPlusPlus.xmod.gregtech.loaders;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTModHandler.RecipeBits;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;

public class ProcessingAngleGrinder implements Runnable {

    public ProcessingAngleGrinder() {}

    public void materialsLoops() {
        final Materials[] i = Materials.values();
        final int size = i.length;
        Logger.MATERIALS("Materials to attempt tool gen. with: " + size);
        int used = 0;
        Materials aMaterial = null;
        for (Materials materials : i) {
            aMaterial = materials;
            if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)
                && (aMaterial != Materials.Rubber)
                && (aMaterial != Materials._NULL)) {
                if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY))
                    && (!aMaterial.contains(SubTag.NO_SMASHING))
                    && (!aMaterial.contains(SubTag.TRANSPARENT))
                    && (!aMaterial.contains(SubTag.FLAMMABLE))
                    && (!aMaterial.contains(SubTag.MAGICAL))
                    && (!aMaterial.contains(SubTag.NO_SMELTING))) {
                    Logger.MATERIALS("Generating Angle Grinder from " + MaterialUtils.getMaterialName(aMaterial));
                    // Input 1

                    final ItemStack plate = GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L);
                    final ItemStack longrod = GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L);

                    if ((null != plate && longrod != null)) {
                        addRecipe(aMaterial, 1600000L, 3, ItemList.Battery_RE_HV_Lithium.get(1));
                        addRecipe(aMaterial, 1200000L, 3, ItemList.Battery_RE_HV_Cadmium.get(1));
                        addRecipe(aMaterial, 800000L, 3, ItemList.Battery_RE_HV_Sodium.get(1));
                        used++;
                    } else {
                        Logger.MATERIALS(
                            "Unable to generate Angle Grinder from " + MaterialUtils.getMaterialName(aMaterial)
                                + ", Plate or Long Rod may be invalid. Invalid | Plate? "
                                + (plate == null)
                                + " | Rod? "
                                + (longrod == null)
                                + " |");
                    }
                    // GTModHandler.addCraftingRecipe(,
                    // GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS |
                    // GTModHandler.RecipeBits.BUFFERED, new Object[]{"P H", "PIP", " I ",
                    // Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial),
                    // Character.valueOf('P'), OrePrefixes.plate.get(aMaterial),
                    // Character.valueOf('H'), OrePrefixes.toolHeadHammer.get(aMaterial)});
                } else {
                    Logger
                        .MATERIALS("Unable to generate Angle Grinder from " + MaterialUtils.getMaterialName(aMaterial));
                }
            } else {
                Logger.MATERIALS("Unable to generate Angle Grinder from " + MaterialUtils.getMaterialName(aMaterial));
            }
        }

        Logger.INFO("Materials used for tool gen: " + used);
    }

    @Override
    public void run() {
        Logger.INFO("Generating Angle Grinders for all valid GT Materials.");
        this.materialsLoops();
    }

    public boolean addRecipe(Materials aMaterial, long aBatteryStorage, int aVoltageTier, ItemStack aBattery) {

        ItemStack aOutputStack = MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(
            MetaGeneratedGregtechTools.ANGLE_GRINDER,
            1,
            aMaterial,
            Materials.Titanium,
            new long[] { aBatteryStorage, GTValues.V[aVoltageTier], 3L, -1L });

        long aDura = MetaGeneratedGregtechTools.getToolMaxDamage(aOutputStack);
        if (aDura <= 32000) {
            Logger.MATERIALS(
                "Unable to generate Angle Grinder from " + MaterialUtils.getMaterialName(aMaterial)
                    + ", Durability: "
                    + aDura);
            return false;
        }

        ItemStack motor;
        if (aVoltageTier == 1) {
            motor = ItemList.Electric_Motor_LV.get(1);
        } else if (aVoltageTier == 2) {
            motor = ItemList.Electric_Motor_MV.get(1);
        } else {
            motor = ItemList.Electric_Motor_HV.get(1);
        }

        return GTModHandler.addCraftingRecipe(
            aOutputStack,
            RecipeBits.DISMANTLEABLE | RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M', motor, 'S',
                OrePrefixes.screw.get(Materials.Titanium), 'L', OrePrefixes.stickLong.get(aMaterial), 'P',
                OrePrefixes.plate.get(aMaterial), 'G', MaterialsElements.STANDALONE.BLACK_METAL.getGear(1), 'B',
                aBattery });
    }
}
