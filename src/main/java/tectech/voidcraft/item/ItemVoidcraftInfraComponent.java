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
 * An infrastructure COMPONENT — the builder payload of the constructor-built infrastructure (the Stellar
 * Injector / Spacetime Continuum Stabilizer / Stellar Gravitational Lens).
 *
 * <p>
 * A plain stackable item with no block of its own (the Power Satellite pattern): it rides ship cargo (the hold's
 * infrastructure axis — the constructor delivers it to the build site, the finished base's hold receives it at
 * spawn) and is consumed ONE PER BUILD INTERVAL by the matching infrastructure-builder component on a base
 * standing at the structure's target, joining the target's shell until its triangle capacity is reached.
 */
public final class ItemVoidcraftInfraComponent extends Item {

    /** The dedicated 16×16 item icon (also the block-atlas name the inventory model uses). */
    public static final String ICON_INJECTOR = "tectech:iconsets/VC_ITEM_INFRA_INJECTOR";
    public static final String ICON_STABILIZER = "tectech:iconsets/VC_ITEM_INFRA_STABILIZER";
    public static final String ICON_LENS = "tectech:iconsets/VC_ITEM_INFRA_LENS";

    /** The Stellar Injector's component item. */
    public static ItemVoidcraftInfraComponent INJECTOR;
    /** The Continuum Stabilizer's component item. */
    public static ItemVoidcraftInfraComponent STABILIZER;
    /** The Stellar Gravitational Lens's component item. */
    public static ItemVoidcraftInfraComponent LENS;

    private final int infraType;
    private final String iconName;
    private final String hintLang;

    private IIcon icon;

    private ItemVoidcraftInfraComponent(int infraType, String unlocalizedName, String iconName, String hintLang) {
        this.infraType = infraType;
        this.iconName = iconName;
        this.hintLang = hintLang;
        setMaxDamage(0);
        setMaxStackSize(64);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /** @return the infrastructure type this item is the component of ({@link USSInfraBuild}) */
    public int infraType() {
        return infraType;
    }

    /**
     * One component stack of the given infrastructure type (for the gateway pull + tests) — null for an unknown
     * type.
     */
    public static ItemStack stack(int infraType) {
        switch (infraType) {
            case USSInfraBuild.INJECTOR:
                return new ItemStack(INJECTOR, 1);
            case USSInfraBuild.STABILIZER:
                return new ItemStack(STABILIZER, 1);
            case USSInfraBuild.LENS:
                return new ItemStack(LENS, 1);
            default:
                return null;
        }
    }

    /**
     * The infrastructure type of the given component stack — -1 when the stack is null or not an infrastructure
     * component.
     */
    public static int typeOf(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemVoidcraftInfraComponent)) {
            return -1;
        }
        return ((ItemVoidcraftInfraComponent) stack.getItem()).infraType;
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
        INJECTOR = new ItemVoidcraftInfraComponent(
            USSInfraBuild.INJECTOR,
            "tt.voidcraft_injector_component",
            ICON_INJECTOR,
            "tt.voidcraft_injector_component.hint");
        STABILIZER = new ItemVoidcraftInfraComponent(
            USSInfraBuild.STABILIZER,
            "tt.voidcraft_stabilizer_component",
            ICON_STABILIZER,
            "tt.voidcraft_stabilizer_component.hint");
        LENS = new ItemVoidcraftInfraComponent(
            USSInfraBuild.LENS,
            "tt.voidcraft_lens_component",
            ICON_LENS,
            "tt.voidcraft_lens_component.hint");
        GameRegistry.registerItem(INJECTOR, INJECTOR.getUnlocalizedName());
        GameRegistry.registerItem(STABILIZER, STABILIZER.getUnlocalizedName());
        GameRegistry.registerItem(LENS, LENS.getUnlocalizedName());
    }
}
