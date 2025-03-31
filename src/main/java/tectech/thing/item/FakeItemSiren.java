package tectech.thing.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.thing.CustomItemList;

public class FakeItemSiren extends Item {

    public static FakeItemSiren INSTANCE;

    private FakeItemSiren() {
        setHasSubtypes(false);
        setUnlocalizedName("tm.fakeItemSiren");
        setTextureName(Reference.MODID + ":fakeItemSiren");
    }

    public static void run() {
        INSTANCE = new FakeItemSiren();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.Fools_FakeItemSiren.set(INSTANCE)
            .hidden();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
