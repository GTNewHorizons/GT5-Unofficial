package gregtech.common.render;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileEntityBlackhole;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BlackholeRenderer extends TileEntitySpecialRenderer {

    private boolean initialized = false;


    public BlackholeRenderer(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlackhole.class, this);
    }

    private void init(){
        try{

        }catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }


        initialized = true;
    }


    private void renderBlackHole(TileEntityBlackhole tile, double x, double y, double z, float timer){

    }

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEntityBlackhole blackhole)) return;
        if (!initialized){
            init();
            if (!initialized) return;
        }

        renderBlackHole(blackhole,x,y,z,tile.getWorldObj().getWorldTime()+timeSinceLastTick);

    }

}
