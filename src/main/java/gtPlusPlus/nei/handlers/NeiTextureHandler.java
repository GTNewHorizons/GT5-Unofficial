package gtPlusPlus.nei.handlers;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Based on crazypants.enderio.gui.IconEIO
 *
 * @author Original EIO Author
 *         <p>
 *         This is free and unencumbered software released into the public domain.
 *         <p>
 *         Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software, either in source
 *         code form or as a compiled binary, for any purpose, commercial or non-commercial, and by any means.
 *         <p>
 *         In jurisdictions that recognize copyright laws, the author or authors of this software dedicate any and all
 *         copyright interest in the software to the public domain. We make this dedication for the benefit of the
 *         public at large and to the detriment of our heirs and successors. We intend this dedication to be an overt
 *         act of relinquishment in perpetuity of all present and future rights to this software under copyright law.
 *         <p>
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *         TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *         THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *         OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *         SOFTWARE.
 *         <p>
 *         For more information, please refer to <http://unlicense.org/>
 *
 *         https://github.com/SleepyTrousers/EnderIO/blob/release/1.7.10/2.2/src/main/java/crazypants/render/RenderUtil.java
 *
 */
public final class NeiTextureHandler {

    public static final NeiTextureHandler RECIPE_BUTTON = new NeiTextureHandler(128, 116, 24, 24);

    private final double minU;
    private final double maxU;
    private final double minV;
    private final double maxV;

    private static final ResourceLocation TEXTURE = new ResourceLocation(
        GTPlusPlus.ID + ":textures/gui/nei/widgets.png");

    private NeiTextureHandler(int x, int y, int width, int height) {
        this.minU = (float) (0.00390625D * (double) x);
        this.maxU = (float) (0.00390625D * (double) (x + width));
        this.minV = (float) (0.00390625D * (double) y);
        this.maxV = (float) (0.00390625D * (double) (y + height));
    }

    public void renderIcon(double x, double y, double width, double height, double zLevel, boolean doDraw) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        if (doDraw) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            tessellator.startDrawingQuads();
        }

        tessellator.addVertexWithUV(x, y + height, zLevel, this.minU, this.maxV);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, this.maxU, this.maxV);
        tessellator.addVertexWithUV(x + width, y + 0.0D, zLevel, this.maxU, this.minV);
        tessellator.addVertexWithUV(x, y + 0.0D, zLevel, this.minU, this.minV);

        if (doDraw) {
            tessellator.draw();
        }
    }
}
