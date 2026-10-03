package gregtech.common.render;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.api.API;
import codechicken.nei.api.IFluidRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;

/**
 * Renders GT fluids in NEI with {@link FluidDisplayStackRenderer}.
 */
@SideOnly(Side.CLIENT)
public class NEIFluidRenderer implements IFluidRenderer {

    private static final FluidDisplayStackRenderer RENDERER = new FluidDisplayStackRenderer();

    private final ItemStack displayStack;

    private NEIFluidRenderer(Fluid fluid) {
        this.displayStack = GTUtility.getFluidDisplayStack(new FluidStack(fluid, 0), false, true);
    }

    public static void register() {
        for (CondensateType type : CondensateType.VALUES) {
            register(type.getEntangledFluid());
        }

        for (Map.Entry<Fluid, Materials> entry : Materials.FLUID_MAP.entrySet()) {
            if (entry.getValue().renderer != null) {
                register(entry.getKey());
            }
        }
    }

    private static void register(Fluid fluid) {
        API.registerFluidRenderer(fluid, new NEIFluidRenderer(fluid));
    }

    @Override
    public void renderFluid(Fluid fluid, int x, int y) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        RENDERER.renderItem(ItemRenderType.INVENTORY, displayStack);
        GL11.glPopMatrix();
    }
}
