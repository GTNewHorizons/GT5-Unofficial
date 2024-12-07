package gregtech.api.interfaces;

import net.minecraft.world.World;

public interface IDimension {
    
    public String getWorldName();

    public boolean matches(World world);
}
