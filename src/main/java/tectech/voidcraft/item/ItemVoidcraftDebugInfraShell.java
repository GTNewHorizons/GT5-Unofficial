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
import tectech.voidcraft.uss.USSInfraBuild;

/**
 * Debug items: right-clicking one on the UnstableSolarSystem machine injects a fixed fraction of an
 * infrastructure shell's capacity into that shell (no resource cost — the effect rides the debug item → effect
 * registry, the items themselves are inert).
 *
 * <p>
 * One instance per shell: the Stellar Injector and the Stellar Gravitational Lens build on the star; the
 * Continuum Stabilizer builds on the first ripple whose shell is not fully built (the click reveals that ripple).
 *
 * <p>
 * A plain stackable item with no block of its own.
 */
public final class ItemVoidcraftDebugInfraShell extends Item {

    /** The fraction of the shell's capacity one click injects. */
    public static final double INJECT_FRACTION = 0.10;

    /** The dedicated 16×16 item icons. */
    public static final String ICON_INJECTOR = "tectech:iconsets/VC_ITEM_DEBUG_INJECTOR_SHELL";
    public static final String ICON_STABILIZER = "tectech:iconsets/VC_ITEM_DEBUG_STABILIZER_SHELL";
    public static final String ICON_LENS = "tectech:iconsets/VC_ITEM_DEBUG_LENS_SHELL";

    /** The Stellar Injector shell's debug item (the star's shell). */
    public static ItemVoidcraftDebugInfraShell INJECTOR;
    /** The Continuum Stabilizer shell's debug item (a ripple's shell). */
    public static ItemVoidcraftDebugInfraShell STABILIZER;
    /** The Stellar Gravitational Lens shell's debug item (the star's shell). */
    public static ItemVoidcraftDebugInfraShell LENS;

    private final int infraType;
    private final String iconName;
    private final String hintLang;

    private IIcon icon;

    private ItemVoidcraftDebugInfraShell(int infraType, String unlocalizedName, String iconName, String hintLang) {
        this.infraType = infraType;
        this.iconName = iconName;
        this.hintLang = hintLang;
        setMaxDamage(0);
        setMaxStackSize(16);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /** @return the infrastructure type this debug item injects into ({@link USSInfraBuild}) */
    public int infraType() {
        return infraType;
    }

    /**
     * @param capacity the shell's capacity
     * @return the units one click injects for that capacity — round(capacity · {@link #INJECT_FRACTION}), at
     *         least 1 for any non-zero capacity; 0 for a non-positive capacity
     */
    public static long injectAmountFor(long capacity) {
        if (capacity <= 0L) {
            return 0L;
        }
        return Math.max(1L, Math.round(capacity * INJECT_FRACTION));
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
        INJECTOR = new ItemVoidcraftDebugInfraShell(
            USSInfraBuild.INJECTOR,
            "tt.voidcraft_debug_injector_shell",
            ICON_INJECTOR,
            "tt.voidcraft_debug_injector_shell.hint");
        STABILIZER = new ItemVoidcraftDebugInfraShell(
            USSInfraBuild.STABILIZER,
            "tt.voidcraft_debug_stabilizer_shell",
            ICON_STABILIZER,
            "tt.voidcraft_debug_stabilizer_shell.hint");
        LENS = new ItemVoidcraftDebugInfraShell(
            USSInfraBuild.LENS,
            "tt.voidcraft_debug_lens_shell",
            ICON_LENS,
            "tt.voidcraft_debug_lens_shell.hint");
        GameRegistry.registerItem(INJECTOR, INJECTOR.getUnlocalizedName());
        GameRegistry.registerItem(STABILIZER, STABILIZER.getUnlocalizedName());
        GameRegistry.registerItem(LENS, LENS.getUnlocalizedName());
    }
}
