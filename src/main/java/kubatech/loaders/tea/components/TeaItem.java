package kubatech.loaders.tea.components;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public abstract class TeaItem extends Item {

    private IIcon mainIcon;
    private IIcon fluidKeyIcon;
    private IIcon emptyIcon;

    public TeaItem() {
        setHasSubtypes(true);
    }

    abstract protected String componentName();

    @Override
    public void registerIcons(IIconRegister register) {
        mainIcon = register.registerIcon("kubatech:tea/" + componentName());
        fluidKeyIcon = register.registerIcon("kubatech:tea/" + componentName() + "_fluid_key");
        emptyIcon = register.registerIcon("kubatech:tea/" + componentName() + "_empty");
        itemIcon = mainIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "kubatech.tea." + componentName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = Tea.metaToName.getOrDefault(stack.getItemDamage(), null);
        if (name == null) return StatCollector.translateToLocal(this.getUnlocalizedName(stack) + ".empty");
        return StatCollector
            .translateToLocalFormatted(this.getUnlocalizedName(stack), Tea.teas.get(name).fluid.getLocalizedName());
    }

    public ItemStack getItem(int meta) {
        return new ItemStack(this, 1, meta);
    }

    public IIcon getFluidKeyIcon(ItemStack stack) {
        return fluidKeyIcon;
    }

    public IIcon getEmptyIcon(ItemStack stack) {
        return emptyIcon;
    }

    public FluidStack getFluidStack(ItemStack stack, int amount) {
        String t = Tea.metaToName.getOrDefault(stack.getItemDamage(), null);
        if (t == null) return null;
        return new FluidStack(Tea.teas.get(t).fluid, amount);
    }
}
