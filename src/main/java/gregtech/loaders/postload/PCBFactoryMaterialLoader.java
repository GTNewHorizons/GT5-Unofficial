package gregtech.loaders.postload;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.util.PCBFactoryManager;

public class PCBFactoryMaterialLoader {

    public static void load() {
        // add Plastics
        PCBFactoryManager.addPlasticTier(Materials.Plastic, 1);
        PCBFactoryManager.addPlasticTier(Materials.PolyvinylChloride, 2);
        PCBFactoryManager.addPlasticTier(Materials.Polytetrafluoroethylene, 3);
        PCBFactoryManager.addPlasticTier(Materials.Epoxid, 4);
        PCBFactoryManager.addPlasticTier(Materials.EpoxidFiberReinforced, 5);
        PCBFactoryManager.addPlasticTier(Materials.Polybenzimidazole, 6);
        PCBFactoryManager.addPlasticTier(MaterialsKevlar.Kevlar, 7);
    }
}
