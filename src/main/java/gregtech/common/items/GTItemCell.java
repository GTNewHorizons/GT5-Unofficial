package gregtech.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.color.RGBColor;
import com.gtnewhorizon.gtnhlib.itemrendering.IItemTexture;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemTexture;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemWithTextures;
import com.gtnewhorizon.gtnhlib.itemrendering.TexturedItemRenderer;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTUtility;

/// A simple fluid cell that maps to a single fluid. The fluid texture must be pre-cropped, though the fluid color will
/// be applied to it.
public class GTItemCell extends Item implements ItemWithTextures {

    private final String name, iconName;
    private final Fluid fluid;

    private IIcon fluidIcon;

    public GTItemCell(String name, String iconName, Fluid fluid) {
        this.name = name;
        this.iconName = iconName;
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
        fluidIcon = register.registerIcon("gregtech:cell/" + iconName);
    }

    @Override
    public IItemTexture[] getTextures(ItemStack stack) {
        return new IItemTexture[] { new ItemTexture(
            ItemList.Cell_Empty.get(1)
                .getIconIndex(),
            RGBColor.WHITE), new ItemTexture(fluidIcon, RGBColor.fromRGB(fluid.getColor(new FluidStack(fluid, 1)))), };
    }
}
