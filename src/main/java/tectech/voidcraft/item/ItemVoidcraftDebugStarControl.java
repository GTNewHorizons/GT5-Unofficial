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
 * Debug items: right-clicking one on the UnstableSolarSystem machine applies one of the star-control effects —
 * reveal random ripple points, set the star's size to the injector's cap, set the remaining lifespan, or force the
 * star's expiry now (no resource cost — the effects ride the debug item → effect registry, the items themselves
 * are inert).
 *
 * <p>
 * A plain stackable item with no block of its own.
 */
public final class ItemVoidcraftDebugStarControl extends Item {

    /** The ripple points one click of the ripple scanner reveals (random, unrevealed ones). */
    public static final int RIPPLES_PER_CLICK = 4;

    /** The remaining lifespan (in seconds) one click of the lifespan setter sets. */
    public static final long LIFESPAN_SECONDS = 60L;

    /** The dedicated 16×16 item icons. */
    public static final String ICON_RIPPLE_SCAN = "tectech:iconsets/VC_ITEM_DEBUG_RIPPLE_SCAN";
    public static final String ICON_STAR_SIZE = "tectech:iconsets/VC_ITEM_DEBUG_STAR_SIZE";
    public static final String ICON_LIFESPAN = "tectech:iconsets/VC_ITEM_DEBUG_LIFESPAN";
    public static final String ICON_FORCE_EXPIRY = "tectech:iconsets/VC_ITEM_DEBUG_FORCE_EXPIRY";

    /** The ripple scanner's debug item. */
    public static ItemVoidcraftDebugStarControl RIPPLE_SCAN;
    /** The star-size setter's debug item (the injector's 1.5× cap). */
    public static ItemVoidcraftDebugStarControl STAR_SIZE_MAX;
    /** The lifespan setter's debug item. */
    public static ItemVoidcraftDebugStarControl LIFESPAN;
    /** The expiry trigger's debug item. */
    public static ItemVoidcraftDebugStarControl FORCE_EXPIRY;

    private final String iconName;
    private final String hintLang;

    private IIcon icon;

    private ItemVoidcraftDebugStarControl(String unlocalizedName, String iconName, String hintLang) {
        this.iconName = iconName;
        this.hintLang = hintLang;
        setMaxDamage(0);
        setMaxStackSize(16);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal(hintLang));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(iconName);
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
        RIPPLE_SCAN = new ItemVoidcraftDebugStarControl(
            "tt.voidcraft_debug_ripple_scan",
            ICON_RIPPLE_SCAN,
            "tt.voidcraft_debug_ripple_scan.hint");
        STAR_SIZE_MAX = new ItemVoidcraftDebugStarControl(
            "tt.voidcraft_debug_star_size",
            ICON_STAR_SIZE,
            "tt.voidcraft_debug_star_size.hint");
        LIFESPAN = new ItemVoidcraftDebugStarControl(
            "tt.voidcraft_debug_lifespan",
            ICON_LIFESPAN,
            "tt.voidcraft_debug_lifespan.hint");
        FORCE_EXPIRY = new ItemVoidcraftDebugStarControl(
            "tt.voidcraft_debug_force_expiry",
            ICON_FORCE_EXPIRY,
            "tt.voidcraft_debug_force_expiry.hint");
        GameRegistry.registerItem(RIPPLE_SCAN, RIPPLE_SCAN.getUnlocalizedName());
        GameRegistry.registerItem(STAR_SIZE_MAX, STAR_SIZE_MAX.getUnlocalizedName());
        GameRegistry.registerItem(LIFESPAN, LIFESPAN.getUnlocalizedName());
        GameRegistry.registerItem(FORCE_EXPIRY, FORCE_EXPIRY.getUnlocalizedName());
    }
}
