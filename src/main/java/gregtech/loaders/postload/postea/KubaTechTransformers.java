package gregtech.loaders.postload.postea;

import static gregtech.loaders.postload.postea.Transformers.remapItem;

import kubatech.loaders.ArcFurnaceElectrode;

final class KubaTechTransformers {

    private KubaTechTransformers() {}

    static void register() {
        registerArcFurnaceElectrodes();
    }

    /// `Damage` of a saved electrode is its [ArcFurnaceElectrode] id. The used-durability NBT carries over.
    private static void registerArcFurnaceElectrodes() {
        remapItem("kubatech:arc_furnace_electrode", damage -> {
            ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(damage);
            return electrode == null ? null : electrode.getElectrodeItem(1);
        });
    }
}
