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
 * Debug item: right-clicking it on the UnstableSolarSystem machine depletes a fixed fraction of the star's
 * PRIMARY material reserve — the stellar evolution's depletion read (no resource cost — the effect rides the
 * debug item → effect registry, the item itself is inert).
 *
 * <p>
 * A plain stackable item with no block of its own.
 */
public final class ItemVoidcraftDebugStarDepletion extends Item {

    /** The fraction of the star's primary material one click depletes. */
    public static final double DEPLETE_FRACTION = 0.10;

    /** The dedicated 16×16 item icon. */
    public static final String ICON_NAME = "tectech:iconsets/VC_ITEM_DEBUG_STAR_DEPLETION";

    public static ItemVoidcraftDebugStarDepletion INSTANCE;

    private static IIcon icon;

    private ItemVoidcraftDebugStarDepletion() {
        setMaxDamage(0);
        setMaxStackSize(16);
        setUnlocalizedName("tt.voidcraft_debug_star_depletion");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal("tt.voidcraft_debug_star_depletion.hint"));
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
        INSTANCE = new ItemVoidcraftDebugStarDepletion();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
