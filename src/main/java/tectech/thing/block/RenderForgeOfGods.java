package tectech.thing.block;

import static tectech.Reference.MODID;
import static tectech.rendering.EOH.EOHRenderingUtils.renderStarLayer;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_0;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_1;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_2;

import java.awt.Color;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class RenderForgeOfGods extends TileEntitySpecialRenderer {

    public static IModelCustom starModel;

    public RenderForgeOfGods() {
        starModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Star.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEntityForgeOfGods)) return;

        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);

            // Innermost layer should be opaque
            enableOpaqueColorInversion();
            renderStarLayer(0, STAR_LAYER_0, new Color(1.0f, 0.4f, 0.05f, 1.0f), 1.0f, 25);
            disableOpaqueColorInversion();

            enablePseudoTransparentColorInversion();
            renderStarLayer(1, STAR_LAYER_1, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.4f, 25);
            renderStarLayer(2, STAR_LAYER_2, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.2f, 25);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    public static void enablePseudoTransparentColorInversion() {
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_INVERTED);
    }

    public static void enableOpaqueColorInversion() {
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_COPY_INVERTED);
    }

    public static void disableOpaqueColorInversion() {
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
    }
}
