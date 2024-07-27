package gregtech.api.multitileentity.enums;

<<<<<<< HEAD
import static gregtech.api.util.GT_StructureUtilityMuTE.createMuTEStructureCasing;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.CASING_REGISTRY_NAME;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GT_StructureUtilityMuTE;
=======
import gregtech.api.enums.GT_Values;
>>>>>>> 295704b8ca (remove old mutes)

public enum GT_MultiTileCasing {

    CokeOven(0),
    Chemical(1),
    Distillation(2),
    Macerator(18000),
    LaserEngraver(4),
    Mirror(5),
    BlackLaserEngraverCasing(6),
    LaserEngraverUpgrade1(7),
    LaserEngraverUpgrade2(8),
    LaserEngraverUpgrade3(9),
    LaserEngraverUpgrade4(10),
    NONE(GTValues.W);

    private final int meta;

    GT_MultiTileCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}
