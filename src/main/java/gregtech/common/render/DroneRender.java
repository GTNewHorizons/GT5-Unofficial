package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DroneRender {

    private static final ResourceLocation[] DroneTextures = new ResourceLocation[] { createRl("drone1.png"),
        createRl("drone2.png"), createRl("drone3.png"), createRl("drone4.png") };
    private static final IModelCustom Drone = AdvancedModelLoader.loadModel(createRl("drone.obj"));
    private static final IModelCustom DroneBlade = AdvancedModelLoader.loadModel(createRl("drone_blade.obj"));

    private static ResourceLocation createRl(String name) {
        return new ResourceLocation(GregTech.ID, "textures/model/drone/" + name);
    }

    public static void renderDrone(double x, double y, double z, float timeSinceLastTick, int level) {
        if (level <= 0 || level > DroneTextures.length) return;
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager()
            .bindTexture(DroneTextures[level - 1]);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        float time = mc.theWorld.getTotalWorldTime() + timeSinceLastTick;
        GL11.glTranslated(0, Math.sin(time / 10.0) * 0.15, 0);

        Drone.renderAll();

        final double bladeOffset = 0.845;
        float rotation = time * 80f % 360f;
        renderBlade(rotation, -bladeOffset, -bladeOffset);
        renderBlade(-rotation, -bladeOffset, bladeOffset);
        renderBlade(-rotation, bladeOffset, -bladeOffset);
        renderBlade(rotation, bladeOffset, bladeOffset);
        GL11.glPopMatrix();
    }

    private static void renderBlade(float rotation, double offsetX, double offsetZ) {
        GL11.glPushMatrix();
        GL11.glTranslated(offsetX, 1.265, offsetZ);
        GL11.glRotated(rotation, 0, 1, 0);
        DroneBlade.renderAll();
        GL11.glPopMatrix();
    }
}
