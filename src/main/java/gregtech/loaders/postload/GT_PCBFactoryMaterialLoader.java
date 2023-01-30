package gregtech.loaders.postload;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.util.GT_PCBFactoryManager;

public class GT_PCBFactoryMaterialLoader {

    public static void load() {
        // add Plastics
        GT_PCBFactoryManager.addPlasticTier(Materials.Plastic, 1);
        GT_PCBFactoryManager.addPlasticTier(Materials.PolyvinylChloride, 2);
        GT_PCBFactoryManager.addPlasticTier(Materials.Polytetrafluoroethylene, 3);
        GT_PCBFactoryManager.addPlasticTier(Materials.Epoxid, 4);
        GT_PCBFactoryManager.addPlasticTier(Materials.EpoxidFiberReinforced, 5);
        GT_PCBFactoryManager.addPlasticTier(Materials.Polybenzimidazole, 6);
        GT_PCBFactoryManager.addPlasticTier(MaterialsKevlar.Kevlar, 7);
    }
}
