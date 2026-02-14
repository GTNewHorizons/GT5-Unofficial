package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.items.GTGenericItem;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import gregtech.common.fluid.GTFluid;
import gtPlusPlus.api.objects.minecraft.FluidGT6;
import gtPlusPlus.core.material.Material;

public class ItemFluidDisplay extends GTGenericItem {

    private static final Map<Fluid, String> sFluidTooltips = new HashMap<>();

    public ItemFluidDisplay() {
        super("GregTech_FluidDisplay", "Fluid Display", null);
        ItemList.Display_Fluid.set(this);
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (FluidRegistry.getFluid(aStack.getItemDamage()) != null) {
            addTooltipForFluid(FluidRegistry.getFluid(aStack.getItemDamage()), aList);
        }

        if (GTValues.D1 || Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
            Fluid tFluid = FluidRegistry.getFluid(aStack.getItemDamage());
            if (tFluid != null) {
                aList.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.fluid.registry", tFluid.getName()));
            }
        }

        NBTTagCompound aNBT = aStack.getTagCompound();

        if (aNBT != null) {
            if (Client.tooltip.showFluidAmount) {
                long tToolTipAmount = aNBT.getLong("mFluidDisplayAmount");
                if (tToolTipAmount > 0L) {
                    aList.add(
                        EnumChatFormatting.BLUE
                            + StatCollector
                                .translateToLocalFormatted("GT5U.tooltip.fluid.amount", formatFluid(tToolTipAmount))
                            + EnumChatFormatting.GRAY);
                }
            }
            if (Client.tooltip.showFluidTemperature) {
                aList.add(
                    EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(
                        "GT5U.tooltip.fluid.temperature",
                        formatNumber(aNBT.getLong("mFluidDisplayHeat"))) + EnumChatFormatting.GRAY);
            }
            if (Client.tooltip.showFluidState) {
                aList.add(
                    EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted(
                        "GT5U.tooltip.fluid.stat",
                        aNBT.getBoolean("mFluidState") ? StatCollector.translateToLocal("GT5U.tooltip.fluid.stat.gas")
                            : StatCollector.translateToLocal("GT5U.tooltip.fluid.stat.liquid"))
                        + EnumChatFormatting.GRAY);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {}

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        return Stream.of(FluidRegistry.getFluid(aMetaData), FluidRegistry.WATER)
            .filter(Objects::nonNull)
            .map(Fluid::getStillIcon)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack aStack, int aRenderPass) {
        Fluid tFluid = FluidRegistry.getFluid(aStack.getItemDamage());
        return tFluid == null ? 16777215 : tFluid.getColor();
    }

    @Override
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        if (aStack != null) {
            return GTUtility.getFluidName(FluidRegistry.getFluid(aStack.getItemDamage()), false);
        }
        return "";
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        if (aStack != null) {
            return GTUtility.getFluidName(FluidRegistry.getFluid(aStack.getItemDamage()), true);
        }
        return "";
    }

    @SideOnly(Side.CLIENT)
    public static void addTooltipForFluid(Fluid fluid, List<String> list) {
        final Werkstoff w = WerkstoffLoader.fluids.inverse()
            .get(fluid);
        if (w != null) {
            w.addTooltips(list);
            return;
        }
        if (fluid instanceof FluidGT6 gtppFluid) {
            final Material material = gtppFluid.getMaterial();
            if (material != null) material.addTooltips(list);
            return;
        }
        if (fluid instanceof GTFluid gtFluid) {
            Materials material = Materials.FLUID_MAP.get(gtFluid);
            if (material != null) material.addTooltips(list);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        if (GTValues.D1) {
            int i = 0;
            for (int j = FluidRegistry.getMaxID(); i < j; i++) {
                ItemStack tStack = GTUtility.getFluidDisplayStack(FluidRegistry.getFluid(i));
                if (tStack != null) {
                    aList.add(tStack);
                }
            }
        }
    }

    public static boolean isCell(ItemStack tItemStack) {
        for (int tOreDict : OreDictionary.getOreIDs(tItemStack)) {
            String tOreDictName = OreDictionary.getOreName(tOreDict);
            if (tOreDictName.startsWith("cell")) return true;
        }
        return false;
    }

    public static Materials getMaterialFromCell(ItemStack tItemStack) {
        for (int tOreDict : OreDictionary.getOreIDs(tItemStack)) {
            String tOreDictName = OreDictionary.getOreName(tOreDict);
            if (tOreDictName.startsWith("cell")) {
                return Materials.getRealMaterial(
                    tOreDictName.replace("cell", "")
                        .replace("Molten", "")
                        .replace("Plasma", ""));
            }
        }
        return Materials._NULL;
    }
}
