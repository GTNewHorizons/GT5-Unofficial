package gregtech.loaders.postload;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.PCBFactoryManager;

public class PCBFactoryMaterialLoader {

    public static void load() {
        // add Plastics
        PCBFactoryManager.addPlasticTier(Materials2Materials.Plastic, 1);
        PCBFactoryManager.addPlasticTier(Materials2Materials.PolyvinylChloride, 2);
        PCBFactoryManager.addPlasticTier(Materials2Materials.Polytetrafluoroethylene, 3);
        PCBFactoryManager.addPlasticTier(Materials2Materials.Epoxid, 4);
        PCBFactoryManager.addPlasticTier(Materials2Materials.EpoxidFiberReinforced, 5);
        PCBFactoryManager.addPlasticTier(Materials2Materials.Polybenzimidazole, 6);
        PCBFactoryManager.addPlasticTier(Materials2Materials.Kevlar, 7);
        PCBFactoryManager.addPlasticTier(Materials2Materials.RadoxPoly, 8);
    }
}
