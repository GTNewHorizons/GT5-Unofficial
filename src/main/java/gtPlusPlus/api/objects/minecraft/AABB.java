package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Generates an AABB around an entity.
 * @author Alkalus
 *
 */
public class AABB {
	
	private final AxisAlignedBB mAabb;
	private final World mWorld;
	
	/**
	 * Creates a AxisAlignedBB based around an Entity.
	 * @param aEntity - The Entity to work with.
	 * @param x - Maximum X from origin.
	 * @param y - Maximum Y from origin.
	 * @param z - Maximum Z from origin.
	 */
	public AABB(Entity aEntity, int x, int y, int z) {	
		if (aEntity == null) {
			mAabb = null;
			mWorld = null;
		}
		else {
			mWorld = aEntity.worldObj;
			BlockPos aEntityLocation = EntityUtils.findBlockPosUnderEntity(aEntity);		
			int xMin, xMax, yMin, yMax, zMin, zMax;
			xMin = aEntityLocation.xPos;
			yMin = aEntityLocation.yPos;
			zMin = aEntityLocation.zPos;		
			xMax = aEntityLocation.xPos + x;
			yMax = aEntityLocation.yPos + y;
			zMax = aEntityLocation.zPos + z;
			mAabb = AxisAlignedBB.getBoundingBox(xMin, yMin, zMin, xMax, yMax, zMax);
		}	

	}
	
	/**
	 * Used to get the AxisAlignedBB from this class.
	 * @return
	 */
	public AxisAlignedBB get() {
		return mAabb;
	}
	
	/**
	 * Used to determine if this object is valid or not.
	 * @return
	 */
	public boolean valid() {
		return mAabb != null && mWorld != null;
	}
	
	public World world() {
		return mWorld;
	}
	
}
