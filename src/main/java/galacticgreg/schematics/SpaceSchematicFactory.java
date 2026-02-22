package galacticgreg.schematics;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;

import galacticgreg.api.Enums;
import galacticgreg.api.Enums.TargetBlockPosition;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.StructureInformation;

/**
 * Class for XML Structure files. You only should edit/use this file/class if you want to add/fix stuff with
 * GalacticGreg itself, and never if you're a mod developer and want to add support for GGreg to your mod. However, feel
 * free to copy this code to your own mod to implement structures. If you have questions, find me on github and ask
 */
public class SpaceSchematicFactory {

    public static SpaceSchematic createSchematic(String pName) {
        SpaceSchematic tSchem = new SpaceSchematic();
        tSchem._mStructureName = pName;
        tSchem._mRarity = 100;
        tSchem._mStructureEnabled = false;

        return tSchem;
    }

    public static StructureInformation createStructureInfo(int pX, int pY, int pZ, Block pBlock, int pMeta) {
        return new StructureInformation(
            Vec3.createVectorHelper(pX, pY, pZ),
            TargetBlockPosition.Invalid,
            new SpecialBlockComb(pBlock, pMeta, Enums.AllowedBlockPosition.AsteroidCoreAndShell));
    }
}
