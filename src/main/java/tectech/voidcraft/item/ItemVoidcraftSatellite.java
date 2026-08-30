package tectech.voidcraft.item;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.TecTech;

/**
 * A Power Satellite — the infrastructure payload of the Satellite Rail Launcher (the Dyson Swarm pass).
 *
 * <p>
 * A plain stackable item with no block of its own: it rides ship cargo (the hold's infrastructure axis) and is
 * consumed ONE PER LAUNCH by a Satellite Rail Launcher on a station anchored to the star, joining the star's
 * satellite swarm (the Dyson Swarm shell the star renders while satellites are in orbit).
 */
public final class ItemVoidcraftSatellite extends Item {

    /** The dedicated 16×16 item icon (also the block-atlas name the inventory model uses). */
    public static final String ICON_NAME = "tectech:iconsets/VC_ITEM_POWER_SATELLITE";

    public static ItemVoidcraftSatellite INSTANCE;

    private static IIcon icon;

    private ItemVoidcraftSatellite() {
        setMaxDamage(0);
        setMaxStackSize(64);
        setUnlocalizedName("tt.voidcraft_satellite");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /** One satellite stack (for the gateway pull + tests). */
    public static ItemStack stack() {
        return new ItemStack(INSTANCE, 1);
    }

    /** @return a 64-stack (the creative tab + recipe convenience) */
    public static ItemStack stack(int count) {
        return new ItemStack(INSTANCE, count);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal("tt.voidcraft_satellite.hint"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(ICON_NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack aStack, int pass) {
        return icon != null ? icon : super.getIcon(aStack, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icon != null ? icon : super.getIconFromDamage(damage);
    }

    public static void run() {
        INSTANCE = new ItemVoidcraftSatellite();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
