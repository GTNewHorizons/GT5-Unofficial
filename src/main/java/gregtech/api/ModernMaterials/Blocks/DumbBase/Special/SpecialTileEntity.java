package gregtech.api.ModernMaterials.Blocks.DumbBase.Special;

import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import java.util.HashMap;

public class SpecialTileEntity extends BaseTileEntity implements IGetTESR {

    public HashMap<Integer, TileEntitySpecialRenderer> masterTESRMap = new HashMap<>();

    @Override
    public TileEntitySpecialRenderer getTESR(int ID) {
        return masterTESRMap.get(ID);
    }

}
