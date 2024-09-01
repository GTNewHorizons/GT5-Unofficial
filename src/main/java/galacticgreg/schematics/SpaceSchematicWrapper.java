package galacticgreg.schematics;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;

import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.GalacticGreg;
import galacticgreg.api.BlockMetaComb;
import galacticgreg.api.Enums.SpaceObjectType;
import galacticgreg.api.Enums.TargetBlockPosition;
import galacticgreg.api.ISpaceObjectGenerator;
import galacticgreg.api.StructureInformation;
import galacticgreg.schematics.SpaceSchematic.BaseStructureInfo;

/**
 * Class for XML Structure files. You only should edit/use this file/class if you want to add/fix stuff with
 * GalacticGreg itself, and never if you're a mod developer and want to add support for GGreg to your mod. However, feel
 * free to copy this code to your own mod to implement structures. If you have questions, find me on github and ask
 */
public class SpaceSchematicWrapper implements ISpaceObjectGenerator {

    private SpaceSchematic _mSchematic;
    private Vec3 _mCenter = Vec3.createVectorHelper(0, 0, 0);
    private List<StructureInformation> _mFinalizedStructure;

    public SpaceSchematicWrapper(SpaceSchematic pSchematic) {
        _mSchematic = pSchematic;
    }

    public boolean isCalculated() {
        return _mFinalizedStructure != null && _mFinalizedStructure.size() > 0;
    }

    /**
     * Recalculate the Structures position, center it around _mCenter
     */
    private void RecalculatePosition() {
        _mFinalizedStructure = new ArrayList<>();

        for (BaseStructureInfo bsi : _mSchematic.coordInfo()) {
            try {
                String tModID = bsi.blockName.split(":")[0];
                String tBlockName = bsi.blockName.split(":")[1];

                Block tBlock = GameRegistry.findBlock(tModID, tBlockName);
                if (tBlock != null) {
                    BlockMetaComb bmc = new BlockMetaComb(tBlock, bsi.blockMeta);
                    Vec3 tCenteredPos = _mCenter.addVector(bsi.posX, bsi.posY, bsi.posZ);
                    StructureInformation tnewSI = new StructureInformation(
                        tCenteredPos,
                        TargetBlockPosition.StructureBlock,
                        bmc);
                    _mFinalizedStructure.add(tnewSI);
                } else GalacticGreg.Logger
                    .warn("Block %s:%s could not be found. Schematic will be incomplete!", tModID, tBlockName);
            } catch (Exception e) {
                e.printStackTrace();
                GalacticGreg.Logger.error("Error while recalculating blocks position");
            }
        }
    }

    @Override
    public Vec3 getCenterPoint() {
        return _mCenter;
    }

    @Override
    public void setCenterPoint(int pX, int pY, int pZ) {
        _mCenter = Vec3.createVectorHelper(pX, pY, pZ);
    }

    @Override
    public void setCenterPoint(Vec3 pCenter) {
        _mCenter = pCenter;
    }

    @Override
    public List<StructureInformation> getStructure() {
        return _mFinalizedStructure;
    }

    @Override
    public void calculate() {
        RecalculatePosition();
    }

    @Override
    public void randomize(int pSizeMin, int pSizeMax) {}

    @Override
    public SpaceObjectType getType() {
        return SpaceObjectType.NonOreSchematic;
    }

    @Override
    public void reset() {

    }

}
