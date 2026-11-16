package gregtech.api.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;

/**
 * A block that an entity climbs as a ladder when it touches a side of the block.
 */
public interface IBlockClimbableFromSide {

    /**
     * Tells if an entity can climb this block.
     *
     * @param world  the world the block is in
     * @param x      the x coordinate of the block
     * @param y      the y coordinate of the block
     * @param z      the z coordinate of the block
     * @param entity the entity that touches the block
     * @return true if the entity can climb the block
     */
    @SuppressWarnings("unused") // Convenience for classes implementing IBlockClimbableFromSide
    default boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
        return true;
    }
}
