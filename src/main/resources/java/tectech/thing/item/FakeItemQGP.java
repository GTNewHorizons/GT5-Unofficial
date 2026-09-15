package tectech.thing.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.thing.CustomItemList;

public class FakeItemQGP extends Item {

    public static FakeItemQGP INSTANCE;

    private FakeItemQGP() {
        setHasSubtypes(false);
        setUnlocalizedName("tm.fakeItemQGP");
        setTextureName(Reference.MODID + ":fakeItemQGP");
    }

    public static void run() {
        INSTANCE = new FakeItemQGP();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.Godforge_FakeItemQGP.set(INSTANCE)
            .hidden();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
