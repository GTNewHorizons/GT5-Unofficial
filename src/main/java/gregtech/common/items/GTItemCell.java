package gregtech.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.color.RGBColor;
import com.gtnewhorizon.gtnhlib.itemrendering.IItemTexture;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemRenderUtils;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemTexture;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemWithTextures;
import com.gtnewhorizon.gtnhlib.itemrendering.TexturedItemRenderer;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTUtility;

/// A simple fluid cell that maps to a single fluid. The fluid's own texture is masked into the cell window the same way
/// [gregtech.common.render.items.GeneratedMaterialRenderer] renders generated fluid cells, then a pre-cropped overlay
/// icon is drawn over the fluid.
public class GTItemCell extends Item implements ItemWithTextures {

    private static final IIconContainer CELL_ICONS = TextureSet.SET_FLUID.mTextures[MaterialIconRegistry.IconType.CELL
        .ordinal()];

    private final String name, overlayIconName;
    private final Fluid fluid;

    private IIcon overlayIcon;

    public GTItemCell(String name, String overlayIconName, Fluid fluid) {
        this.name = name;
        this.overlayIconName = overlayIconName;
        this.fluid = fluid;

        setUnlocalizedName("gt.cell." + name);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);

        GameRegistry.registerItem(this, "gt.cell." + name);
        FluidContainerRegistry.registerFluidContainer(this.fluid, new ItemStack(this, 1), ItemList.Cell_Empty.get(1));

        if (GTUtility.isClient()) {
            TexturedItemRenderer.register(this);
        }
    }

    public Fluid getFluid() {
        return fluid;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        overlayIcon = register.registerIcon("gregtech:cell/" + overlayIconName);
    }

    @Override
    public IItemTexture[] getTextures(ItemStack stack) {
        return new IItemTexture[] { this::renderFluid, new ItemTexture(overlayIcon, RGBColor.WHITE),
            new ItemTexture(CELL_ICONS.getOverlayIcon(), RGBColor.WHITE) };
    }

    @SideOnly(Side.CLIENT)
    private void renderFluid(ItemRenderType type, ItemStack stack) {
        IIcon fluidIcon = fluid.getIcon();
        if (fluidIcon == null) return;

        GL11.glColorMask(false, false, false, false);
        ItemRenderUtils.renderItem(type, CELL_ICONS.getIcon());
        GL11.glColorMask(true, true, true, true);

        int color = CondensateType.getRenderColor(fluid);
        GL11.glColor3ub((byte) (color >> 16), (byte) (color >> 8), (byte) color);

        TextureManager textureManager = Minecraft.getMinecraft()
            .getTextureManager();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        ItemRenderUtils.renderItem(type, fluidIcon);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        textureManager.bindTexture(TextureMap.locationItemsTexture);
    }
}
