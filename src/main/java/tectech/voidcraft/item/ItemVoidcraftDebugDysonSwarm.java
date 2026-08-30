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
 * Debug item: right-clicking it on the UnstableSolarSystem machine injects a fixed fraction of the star's
 * satellite capacity into the Dyson Swarm (no resource cost — the effect rides the debug item → effect
 * registry, the item itself is inert).
 *
 * <p>
 * A plain stackable item with no block of its own.
 */
public final class ItemVoidcraftDebugDysonSwarm extends Item {

    /** The fraction of the star's satellite capacity one click injects. */
    public static final double INJECT_FRACTION = 0.10;

    /** The dedicated 16×16 item icon. */
    public static final String ICON_NAME = "tectech:iconsets/VC_ITEM_DEBUG_DYSON_SWARM";

    public static ItemVoidcraftDebugDysonSwarm INSTANCE;

    private static IIcon icon;

    private ItemVoidcraftDebugDysonSwarm() {
        setMaxDamage(0);
        setMaxStackSize(16);
        setUnlocalizedName("tt.voidcraft_debug_dyson_swarm");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /**
     * @return the satellites one click injects for the given capacity — round(capacity ·
     *         {@link #INJECT_FRACTION}), at least 1 for any non-zero capacity; 0 for a non-positive capacity
     */
    public static long injectAmountFor(long capacity) {
        if (capacity <= 0L) {
            return 0L;
        }
        return Math.max(1L, Math.round(capacity * INJECT_FRACTION));
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal("tt.voidcraft_debug_dyson_swarm.hint"));
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
        INSTANCE = new ItemVoidcraftDebugDysonSwarm();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
