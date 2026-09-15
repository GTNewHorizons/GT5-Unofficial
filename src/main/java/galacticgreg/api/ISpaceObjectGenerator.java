package galacticgreg.api;

import java.util.List;

import net.minecraft.util.Vec3;

import galacticgreg.api.Enums.SpaceObjectType;

public interface ISpaceObjectGenerator {

    Vec3 getCenterPoint();

    /**
     * Set the center-point of the object to generate, by providing X, Y and Z directly
     *
     * @param pX
     * @param pY
     * @param pZ
     */
    void setCenterPoint(int pX, int pY, int pZ);

    /**
     * Set the center-point of the object to generate, by providing a Vec3 instance
     *
     * @param pCenter
     */
    void setCenterPoint(Vec3 pCenter);

    List<StructureInformation> getStructure();

    /**
     * Calculate the structure Called after randomize()
     */
    void calculate();

    /**
     * Randomize the structure. Called before calculate()
     *
     * @param pSizeMin The minimum size for the structure. It is up to you how you handle this value. it's what the user
     *                 sets in his config file
     * @param pSizeMax The maximum size for the structure. It is up to you how you handle this value. it's what the user
     *                 sets in his config file
     */
    void randomize(int pSizeMin, int pSizeMax);

    /**
     * Define the type of the generator. OreAsteroid will be used to spawn ores at given coordinates, where
     * NonOreSchematic will use the Blocks provided in the structural information to generate your structure
     *
     * @return
     */
    SpaceObjectType getType();

    /**
     * This function is called every time the generator shall be reset in order to generate a blank, new structure
     */
    void reset();
}
