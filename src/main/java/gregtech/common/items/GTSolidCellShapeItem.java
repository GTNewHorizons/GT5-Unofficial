package gregtech.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.materials.CellShapes;

/// The `cellSolid` MaterialLib shape, a cell for a material with no fluid. It is a plain item shape since a
/// [com.ruling_0.materiallib.api.ShapeFluidInContainer] requires a fluid. It draws like [CellShapes#cell]: an
/// untinted container base under the tinted `cell` fill.
public class GTSolidCellShapeItem extends ShapeItem {

    private final String baseIconPath;
    private IIcon baseIcon;

    /// As [ShapeItem#ShapeItem], with `baseIconPath` naming the untinted container texture drawn under the fill.
    public GTSolidCellShapeItem(String modid, String name, String baseIconPath, String... oreDicts) {
        super(modid, name, oreDicts);
        this.baseIconPath = baseIconPath;
    }

    /// The empty cell this item leaves behind when consumed.
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return CellShapes.emptyCell.getStack(1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    protected String iconName() {
        return CellShapes.cell.getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        baseIcon = register.registerIcon(baseIconPath);
    }

    /// One pass for the container base, plus one per fill layer.
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int meta) {
        return super.getRenderPasses(meta) + 1;
    }

    /// The untinted container base for pass 0, and the fill's layer at `pass - 1` for every later pass.
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
        if (pass == 0) return baseIcon;
        return super.getIconFromDamageForRenderPass(damage, pass - 1);
    }

    /// The container base, not the fill.
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return baseIcon;
    }

    /// White for the container base, and each fill layer's own color after it.
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass == 0) return 0xFFFFFFFF;
        return super.getColorFromItemStack(stack, renderPass - 1);
    }
}
