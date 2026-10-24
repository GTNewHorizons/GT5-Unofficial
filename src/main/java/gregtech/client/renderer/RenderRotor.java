package gregtech.client.renderer;

import static gregtech.api.enums.Mods.GregTech;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRotor {

    private static final ResourceLocation[] rotorTextures = {
        // spotless:off
        rotorTextureLocation("wood.png"),
        rotorTextureLocation("iron.png"),
        rotorTextureLocation("steel.png"),
        rotorTextureLocation("carbon.png"),
        rotorTextureLocation("energeticAlloy.png"),
        rotorTextureLocation("tungstenSteel.png"),
        rotorTextureLocation("vibrantAlloy.png"),
        rotorTextureLocation("iridium.png"),
        rotorTextureLocation("primitiveLeather.png"),
        rotorTextureLocation("primitiveWool.png"),
        rotorTextureLocation("primitivePaper.png"),
        rotorTextureLocation("primitiveMixed.png")
        // spotless:on
    };

    // model cache
    private static final HashMap<Integer, RotorModel> rotorModels = new HashMap();

    private static final int[] diameters = { 5, 7, 9, 11, 9, 11, 13, 15, 7, 7, 7, 9 };

    private static int getDiameterFromID(int rotorID) {
        if (rotorID == -1) return diameters[0];
        return diameters[rotorID];
    }

    private static ResourceLocation rotorTextureLocation(String fileName) {
        return new ResourceLocation(GregTech.ID, "textures/model/rotor/" + fileName);
    }

    public static void doRender(double x, double y, double z, double rotorRotation, ForgeDirection facing,
        int rotorID) {

        int diameter = getDiameterFromID(rotorID);
        RotorModel model = rotorModels.get(diameter);
        if (model == null) {
            model = new RotorModel(diameter);
            rotorModels.put(diameter, model);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);

        // axis rotation (align with bearing)
        float angle = switch (facing.ordinal()) {
            case 3 -> 180f;
            case 4 -> 90f;
            case 5 -> -90f;
            default -> 0f;
        };
        GL11.glRotatef(angle, 0f, 1f, 0f);

        // spin
        GL11.glRotatef((float) rotorRotation, 0f, 0f, 1f);

        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(getRotorTextures(rotorID));

        model.render(null, 0f, 0f, -0.1f, 0f, 0f, 0.0625f);

        GL11.glPopMatrix();
    }

    protected static ResourceLocation getRotorTextures(int rotorID) {
        if (rotorID == -1) return rotorTextures[0];
        return rotorTextures[rotorID];
    }
}
