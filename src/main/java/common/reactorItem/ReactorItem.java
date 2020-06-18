package common.reactorItem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ReactorItem extends AbstractReactorItem {

    private static final ReactorItem INSTANCE = new ReactorItem();

    private final IIcon[] icons = new IIcon[50];

    private ReactorItem() {
        super();
    }

    public static ReactorItem registerItem() {
        INSTANCE.setHasSubtypes(true);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setMaxStackSize(1);
        final String unloc = "kekztech_reactor_item";
        INSTANCE.setUnlocalizedName(unloc);
        GameRegistry.registerItem(INSTANCE, unloc);
        return INSTANCE;
    }

    @Override
    public void registerIcons(IIconRegister reg) {

    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta];
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
