package tectech.voidcraft.item;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.TecTech;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftCoverComponent;

/**
 * The Voidcraft cover parts (8 subtypes, one per {@link VoidcraftCoverComponent}).
 *
 * <p>
 * Use by sneaking + right-click on any face of a Voidcraft hull block (standard GT cover interaction, remove with a
 * crowbar). Each cover adds its stats to the ship and — for the thruster nozzle — thrust, but only when mounted on
 * the ship's REAR face (the assembler side, pass 24).
 *
 * <p>
 * Icons are the dedicated per-cover art (pass 24: {@code tectech:iconsets/VC_COVER_*} — the same icons the mounted
 * cover face and the in-flight model use).
 */
public final class ItemVoidcraftCovers extends Item {

    public static ItemVoidcraftCovers INSTANCE;
    private static IIcon[] icons;

    private ItemVoidcraftCovers() {
        setHasSubtypes(true);
        setUnlocalizedName("tt.voidcraft_cover");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    /** One cover stack (for recipes and tests). */
    public static ItemStack stack(VoidcraftCoverComponent cover) {
        return new ItemStack(INSTANCE, 1, cover.getId());
    }

    /** The cover part for a damage value, or null. */
    public static VoidcraftCoverComponent byDamage(int damage) {
        if (damage < 0 || damage >= VoidcraftCoverComponent.ALL.length) {
            return null;
        }
        return VoidcraftCoverComponent.ALL[damage];
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        VoidcraftCoverComponent cover = byDamage(getDamage(aStack));
        aList.add(EnumChatFormatting.BLUE + translateToLocal("tt.voidcraft_cover.mount_hint"));
        if (cover == null) {
            return;
        }
        if (cover.getMass() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.mass",
                    NumberFormatUtil.formatNumber(cover.getMass())));
        }
        if (cover.getThrust() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.thrust",
                    NumberFormatUtil.formatNumber(cover.getThrust())));
        }
        if (cover.getCargoSlots() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.cargo",
                    NumberFormatUtil.formatNumber(cover.getCargoSlots())));
        }
        if (cover.getMiningPower() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.mining",
                    NumberFormatUtil.formatNumber(cover.getMiningPower())));
        }
        if (cover.getScanPower() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.scan",
                    NumberFormatUtil.formatNumber(cover.getScanPower())));
        }
        if (cover.getConstructionPower() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.construction",
                    NumberFormatUtil.formatNumber(cover.getConstructionPower())));
        }
        if (cover.getStarlifterPower() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.starlifter",
                    NumberFormatUtil.formatNumber(cover.getStarlifterPower())));
        }
        if (cover.getEnergyBuffer() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.buffer",
                    NumberFormatUtil.formatNumber(cover.getEnergyBuffer())));
        }
        if (cover.getEnergyDraw() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.draw",
                    NumberFormatUtil.formatNumber(cover.getEnergyDraw())));
        }
        if (cover.getIntegrity() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.voidcraft.item.stat.integrity",
                    NumberFormatUtil.formatNumber(cover.getIntegrity())));
        }
        if (cover.getTier() > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted("tt.voidcraft.item.stat.tier", cover.getTier()));
        }
        if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE) {
            aList.add(EnumChatFormatting.GOLD + translateToLocal("tt.voidcraft_cover.thrust_direction"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[VoidcraftCoverComponent.ALL.length];
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            // Pass 24: dedicated per-cover icon (same art as the mounted face / in-flight model).
            icons[cover.getId()] = iconRegister
                .registerIcon("tectech:iconsets/" + VoidcraftTextures.COVER_ICON_BASE[cover.getId()]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack aStack, int pass) {
        int damage = getDamage(aStack);
        IIcon icon = icons != null && damage >= 0 && damage < icons.length ? icons[damage] : null;
        return icon != null ? icon : super.getIcon(aStack, pass);
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
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            aList.add(stack(cover));
        }
    }

    public static void run() {
        INSTANCE = new ItemVoidcraftCovers();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
