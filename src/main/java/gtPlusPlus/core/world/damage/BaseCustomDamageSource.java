package gtPlusPlus.core.world.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSourceIndirect;

public class BaseCustomDamageSource extends EntityDamageSourceIndirect  {

	public BaseCustomDamageSource(String name, Entity transmitter, Entity indirectSource) {
	    super(name, transmitter, indirectSource);
	    this.setDifficultyScaled();
	}
	
	 /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled()
    {
    	return true;
    }

    
}