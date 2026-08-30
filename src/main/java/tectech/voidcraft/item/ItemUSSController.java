package tectech.voidcraft.item;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.CustomItemList;
import tectech.voidcraft.uss.USSStarType;

/**
 * USS Controller (the EoH rework).
 *
 * <p>
 * The ignition source for the Unstable Solar System multiblock. Insert one into the system's controller slot
 * (sneak-right-click on the casing) to ignite the star. The controller is <em>consumed</em> when the star burns
 * out — one controller = one star life.
 *
 * <p>
 * Sixteen variants, one per star class (meta 0–15, the {@link USSStarType} ordinal — the ignition order) — the
 * <strong>item decides the star type</strong> (not derived from the spacetime tier; the tier still scales the
 * star's rendering and the miner ore band).
 *
 * @see tectech.voidcraft.uss.MTEUnstableSolarSystem
 */
public class ItemUSSController extends Item {

    public static ItemUSSController INSTANCE;

    /** Number of star-class variants (meta values 0–15). */
    public static final int VARIANT_COUNT = USSStarType.values().length;

    private IIcon[] icons;

    private ItemUSSController() {
        setHasSubtypes(true);
        setMaxStackSize(1);
        // Item name keys: item.tt.ussController.name (meta 0), item.tt.ussController.1.name (meta 1) …
        // item.tt.ussController.15.name (meta 15) — 1.7.10 appends the meta to the unlocalized name (see
        // ItemVoidcraftCovers.getUnlocalizedName for the same pattern).
        setUnlocalizedName("tt.ussController");
        setTextureName(Reference.MODID + ":itemUssController");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /**
     * @param meta 0–15
     * @return the star class selected by that controller variant (null for unknown metas).
     */
    public static USSStarType starTypeOf(int meta) {
        USSStarType[] types = USSStarType.values();
        if (meta < 0 || meta >= types.length) {
            return null;
        }
        return types[meta];
    }

    /**
     * @param stack a controller stack (may be null or any other item)
     * @return the star class that stack would ignite, or null when it is not a recognized controller (the machine
     *         then leaves the star cold — the slot keeps the unrecognized item).
     */
    public static USSStarType starTypeOf(ItemStack stack) {
        if (stack == null || stack.getItem() != INSTANCE) {
            return null;
        }
        return starTypeOf(stack.getItemDamage());
    }

    /**
     * @param starType the star class to ignite with
     * @return a single controller of that class (null for unknown types).
     */
    public static ItemStack stack(USSStarType starType) {
        if (starType == null) {
            return null;
        }
        return new ItemStack(INSTANCE, 1, starType.ordinal());
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tt.ussController.desc1"));
        aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tt.ussController.desc2"));
        aList.add(EnumChatFormatting.AQUA + translateToLocal("item.tt.ussController.desc3"));
        USSStarType starType = starTypeOf(aStack.getItemDamage());
        if (starType != null) {
            aList.add(
                EnumChatFormatting.YELLOW + translateToLocal("item.tt.ussController.star")
                    + " "
                    + translateToLocal(starType.getLangKey()));
        }
    }

    public static void run() {
        INSTANCE = new ItemUSSController();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.UssController.set(INSTANCE);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + aStack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[VARIANT_COUNT];
        icons[0] = iconRegister.registerIcon(getIconString());
        for (int meta = 1; meta < VARIANT_COUNT; meta++) {
            icons[meta] = iconRegister.registerIcon(getIconString() + meta);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        if (icons != null && damage >= 0 && damage < icons.length && icons[damage] != null) {
            return icons[damage];
        }
        return super.getIconFromDamage(damage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (USSStarType starType : USSStarType.values()) {
            aList.add(stack(starType));
        }
    }
}
