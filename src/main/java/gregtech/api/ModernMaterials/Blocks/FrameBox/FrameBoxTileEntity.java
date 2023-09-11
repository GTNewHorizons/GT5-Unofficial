package gregtech.api.ModernMaterials.Blocks.FrameBox;

import gregtech.api.ModernMaterials.Blocks.DumbBase.IGetTESR;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import java.util.HashMap;

public class FrameBoxTileEntity extends DumbTileEntity implements IGetTESR {

    public static HashMap<Integer, TileEntitySpecialRenderer> masterTESRMap = new HashMap<>();

    @Override
    public TileEntitySpecialRenderer getTESR(int ID) {
        return masterTESRMap.get(ID);
    }
}
