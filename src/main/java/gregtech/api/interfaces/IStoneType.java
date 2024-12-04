package gregtech.api.interfaces;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.block.Block;

public interface IStoneType {
    
    /** Checks if this stone type contains this specific block. */
    public boolean contains(Block block, int meta);

    public OrePrefixes getPrefix();

    public Materials getMaterial();

    public ITexture getTexture();
}
