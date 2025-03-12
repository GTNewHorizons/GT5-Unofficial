package gregtech.common.render;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.client.model.Model;
import gregtech.common.tileentities.render.TileEntityStargateRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class StargateRenderer extends TileEntitySpecialRenderer {

    private boolean initialized = false;
    private Model gate = null;

    public StargateRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStargateRenderer.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_,
        float p_147500_8_) {
        if (!initialized) {
            final ResourceLocation loc = new ResourceLocation("gregtech", "models/stargate/ring_sg1.gltf");
            gate = new Model(loc);
            initialized = true;
        }
    }
}
