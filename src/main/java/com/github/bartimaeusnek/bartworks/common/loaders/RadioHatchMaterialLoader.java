package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class RadioHatchMaterialLoader {

    public static void run() {

        for (Werkstoff material : Werkstoff.werkstoffHashSet) {
            if (material != null && material.getStats().isRadioactive()) {
                int level = (int) material.getStats().getProtons();
                short[] rgba = material.getRGBA();
                if (material.hasItemType(OrePrefixes.stick))
                    BWRecipes.instance.addRadHatch(material.get(OrePrefixes.stick), level, 1, rgba);
                if (material.hasItemType(OrePrefixes.stickLong))
                    BWRecipes.instance.addRadHatch(material.get(OrePrefixes.stickLong), level, 2, rgba);
            }
        }

        for (Materials material : Materials.getAll()) {
            if (material == null || material.mElement == null) continue;
            boolean validProton = material.getProtons() >= 83
                    && !material.equals(Materials.Tritanium) // No Tritanium
                    && !material.equals(Materials.Naquadah); // Naquadah needs spacial value
            if (validProton) {
                int level = (int) material.getProtons();
                short[] rgba = material.getRGBA();
                if (GT_OreDictUnificator.get(OrePrefixes.stick, material, 1) != null)
                    BWRecipes.instance.addRadHatch(
                            GT_OreDictUnificator.get(OrePrefixes.stick, material, 1), level, 1, rgba);
                if (GT_OreDictUnificator.get(OrePrefixes.stickLong, material, 1) != null)
                    BWRecipes.instance.addRadHatch(
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, material, 1), level, 2, rgba);
            }
        }

        Materials[] spacialMaterial =
                new Materials[] {Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria};
        int[] spacialValue = new int[] {130, 140, 150};

        for (int i = 0; i < spacialMaterial.length; i++) {
            if (GT_OreDictUnificator.get(OrePrefixes.stick, spacialMaterial[i], 1) != null)
                BWRecipes.instance.addRadHatch(
                        GT_OreDictUnificator.get(OrePrefixes.stick, spacialMaterial[i], 1),
                        spacialValue[i],
                        1,
                        spacialMaterial[i].mRGBa);
            if (GT_OreDictUnificator.get(OrePrefixes.stickLong, spacialMaterial[i], 1) != null)
                BWRecipes.instance.addRadHatch(
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, spacialMaterial[i], 1),
                        spacialValue[i],
                        2,
                        spacialMaterial[i].mRGBa);
        }

        BWRecipes.instance.addRadHatch(
                ItemList.ThoriumCell_1.get(1), (int) Materials.Thorium.getProtons(), 3, Materials.Thorium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.ThoriumCell_2.get(1), (int) Materials.Thorium.getProtons(), 6, Materials.Thorium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.ThoriumCell_4.get(1), (int) Materials.Thorium.getProtons(), 12, Materials.Thorium.mRGBa);

        BWRecipes.instance.addRadHatch(ItemList.NaquadahCell_1.get(1), 140, 3, Materials.NaquadahEnriched.mRGBa);
        BWRecipes.instance.addRadHatch(ItemList.NaquadahCell_2.get(1), 140, 6, Materials.NaquadahEnriched.mRGBa);
        BWRecipes.instance.addRadHatch(ItemList.NaquadahCell_4.get(1), 140, 12, Materials.NaquadahEnriched.mRGBa);

        BWRecipes.instance.addRadHatch(
                ItemList.Moxcell_1.get(1), (int) Materials.Plutonium.getProtons(), 3, Materials.Plutonium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Moxcell_2.get(1), (int) Materials.Plutonium.getProtons(), 6, Materials.Plutonium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Moxcell_4.get(1), (int) Materials.Plutonium.getProtons(), 12, Materials.Plutonium.mRGBa);

        BWRecipes.instance.addRadHatch(
                ItemList.Uraniumcell_1.get(1), (int) Materials.Uranium.getProtons(), 3, Materials.Uranium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Uraniumcell_2.get(1), (int) Materials.Uranium.getProtons(), 6, Materials.Uranium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Uraniumcell_4.get(1), (int) Materials.Uranium.getProtons(), 12, Materials.Uranium.mRGBa);

        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons(),
                3,
                WerkstoffLoader.Tiberium.getRGBA());
        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.TiberiumCell_2.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons(),
                6,
                WerkstoffLoader.Tiberium.getRGBA());
        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.TiberiumCell_4.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons(),
                12,
                WerkstoffLoader.Tiberium.getRGBA());

        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.TheCoreCell.get(1), 140, 96, Materials.NaquadahEnriched.mRGBa);

        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Thorium_1.get(1),
                (int) Materials.Thorium.getProtons() / 10,
                3,
                Materials.Thorium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Thorium_2.get(1),
                (int) Materials.Thorium.getProtons() / 10,
                6,
                Materials.Thorium.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Thorium_4.get(1),
                (int) Materials.Thorium.getProtons() / 10,
                12,
                Materials.Thorium.mRGBa);

        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Naquadah_1.get(1), 140 / 10, 3, Materials.NaquadahEnriched.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Naquadah_2.get(1), 140 / 10, 6, Materials.NaquadahEnriched.mRGBa);
        BWRecipes.instance.addRadHatch(
                ItemList.Depleted_Naquadah_4.get(1), 140 / 10, 12, Materials.NaquadahEnriched.mRGBa);

        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorMOXSimpledepleted", 1),
                (int) Materials.Plutonium.getProtons() / 10,
                3,
                Materials.Plutonium.mRGBa);
        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorMOXDualdepleted", 1),
                (int) Materials.Plutonium.getProtons() / 10,
                6,
                Materials.Plutonium.mRGBa);
        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorMOXQuaddepleted", 1),
                (int) Materials.Plutonium.getProtons() / 10,
                12,
                Materials.Plutonium.mRGBa);

        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorUraniumSimpledepleted", 1),
                (int) Materials.Uranium.getProtons() / 10,
                3,
                Materials.Uranium.mRGBa);
        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorUraniumDualdepleted", 1),
                (int) Materials.Uranium.getProtons() / 10,
                6,
                Materials.Uranium.mRGBa);
        BWRecipes.instance.addRadHatch(
                GT_ModHandler.getModItem("IC2", "reactorUraniumQuaddepleted", 1),
                (int) Materials.Uranium.getProtons() / 10,
                12,
                Materials.Uranium.mRGBa);

        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.Depleted_Tiberium_1.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons() / 10,
                3,
                WerkstoffLoader.Tiberium.getRGBA());
        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.Depleted_Tiberium_2.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons() / 10,
                6,
                WerkstoffLoader.Tiberium.getRGBA());
        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.Depleted_Tiberium_4.get(1),
                (int) WerkstoffLoader.Tiberium.getBridgeMaterial().getProtons() / 10,
                12,
                WerkstoffLoader.Tiberium.getRGBA());

        BWRecipes.instance.addRadHatch(
                BW_NonMeta_MaterialItems.Depleted_TheCoreCell.get(1), 130 / 10, 96, Materials.Naquadah.mRGBa);

        if (WerkstoffLoader.gtnhGT) {
            BWRecipes.instance.addRadHatch(ItemList.MNqCell_1.get(1), 150, 3, Materials.Naquadria.mRGBa);
            BWRecipes.instance.addRadHatch(ItemList.MNqCell_2.get(1), 150, 6, Materials.Naquadria.mRGBa);
            BWRecipes.instance.addRadHatch(ItemList.MNqCell_4.get(1), 150, 12, Materials.Naquadria.mRGBa);

            BWRecipes.instance.addRadHatch(ItemList.Depleted_MNq_1.get(1), 150 / 10, 3, Materials.Naquadria.mRGBa);
            BWRecipes.instance.addRadHatch(ItemList.Depleted_MNq_2.get(1), 150 / 10, 6, Materials.Naquadria.mRGBa);
            BWRecipes.instance.addRadHatch(ItemList.Depleted_MNq_4.get(1), 150 / 10, 12, Materials.Naquadria.mRGBa);
        }
    }
}
