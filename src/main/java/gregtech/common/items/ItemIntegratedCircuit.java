package gregtech.common.items;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.UIInfos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.items.GTGenericItem;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;

public class ItemIntegratedCircuit extends GTGenericItem implements INetworkUpdatableItem {

    private static final String aTextEmptyRow = "   ";
    private static final List<ItemStack> ALL_VARIANTS = new ArrayList<>();
    protected final IIcon[] mIconDamage = new IIcon[25];

    public ItemIntegratedCircuit() {
        super("integrated_circuit", "Programmed Circuit", "");
        setHasSubtypes(true);
        setMaxDamage(0);

        ItemList.Circuit_Integrated.set(this);

        ALL_VARIANTS.add(new ItemStack(this, 0, 0));
        for (int i = 1; i <= 24; i++) {
            ItemStack aStack = new ItemStack(this, 0, i);
            GregTechAPI.registerConfigurationCircuit(aStack, 1);
            ALL_VARIANTS.add(aStack);
        }

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 0L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { OrePrefixes.circuit.get(Materials.LV) });
        long bits = GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE;
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 1L),
            bits,
            new Object[] { "d  ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 2L),
            bits,
            new Object[] { " d ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 3L),
            bits,
            new Object[] { "  d", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 4L),
            bits,
            new Object[] { aTextEmptyRow, " Pd", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 5L),
            bits,
            new Object[] { aTextEmptyRow, " P ", "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 6L),
            bits,
            new Object[] { aTextEmptyRow, " P ", " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 7L),
            bits,
            new Object[] { aTextEmptyRow, " P ", "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 8L),
            bits,
            new Object[] { aTextEmptyRow, "dP ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });

        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 9L),
            bits,
            new Object[] { "P d", aTextEmptyRow, aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 10L),
            bits,
            new Object[] { "P  ", "  d", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 11L),
            bits,
            new Object[] { "P  ", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 12L),
            bits,
            new Object[] { "P  ", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 13L),
            bits,
            new Object[] { "  P", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 14L),
            bits,
            new Object[] { "  P", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 15L),
            bits,
            new Object[] { "  P", aTextEmptyRow, "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 16L),
            bits,
            new Object[] { "  P", "d  ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 17L),
            bits,
            new Object[] { aTextEmptyRow, aTextEmptyRow, "d P", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 18L),
            bits,
            new Object[] { aTextEmptyRow, "d  ", "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 19L),
            bits,
            new Object[] { "d  ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 20L),
            bits,
            new Object[] { " d ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 21L),
            bits,
            new Object[] { "d  ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 22L),
            bits,
            new Object[] { " d ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 23L),
            bits,
            new Object[] { "  d", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
        GTModHandler.addCraftingRecipe(
            ItemList.Circuit_Integrated.getWithDamage(1L, 24L),
            bits,
            new Object[] { aTextEmptyRow, "  d", "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L) });
    }

    private static String getModeString(int aMetaData) {
        return switch ((byte) (aMetaData >>> 8)) {
            case 0 -> "==";
            case 1 -> "<=";
            case 2 -> ">=";
            case 3 -> "<";
            case 4 -> ">";
            default -> "";
        };
    }

    private static String getConfigurationString(int aMetaData) {
        return getModeString(aMetaData) + " " + (byte) (aMetaData & 0xFF);
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        aList.add(
            GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".configuration", "Configuration: ")
                + getConfigurationString(getDamage(aStack)));
        aList.add(
            GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip.0", "Right click to reconfigure"));
        aList.add(
            GTLanguageManager.addStringLocalization(
                getUnlocalizedName() + ".tooltip.1",
                "Needs a screwdriver or circuit programming tool"));
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        aList.add(new ItemStack(this, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        for (int i = 0; i < mIconDamage.length; i++) {
            mIconDamage[i] = aIconRegister
                .registerIcon(GregTech.getResourcePath(GTConfig.troll ? "troll" : getUnlocalizedName() + "/" + i));
        }
        if (GregTechAPI.sPostloadFinished) {
            GTLog.out.println("GTMod: Starting Item Icon Load Phase");
            GT_FML_LOGGER.info("GTMod: Starting Item Icon Load Phase");
            GregTechAPI.sItemIcons = aIconRegister;
            try {
                for (Runnable tRunnable : GregTechAPI.sGTItemIconload) {
                    tRunnable.run();
                }
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
            GTLog.out.println("GTMod: Finished Item Icon Load Phase");
            GT_FML_LOGGER.info("GTMod: Finished Item Icon Load Phase");
        }
    }

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        byte circuitMode = ((byte) (aMetaData & 0xFF)); // Mask out the MSB Comparison Mode Bits. See: getModeString
        return mIconDamage[circuitMode < mIconDamage.length ? circuitMode : 0];
    }

    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        int meta = tag.hasKey("meta", Constants.NBT.TAG_BYTE) ? tag.getByte("meta") : -1;
        if (meta < 0 || meta > 24) return true;

        if (!player.capabilities.isCreativeMode) {
            Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> toolIndex = findConfiguratorInInv(player);
            if (toolIndex == null) return true;

            ItemStack[] mainInventory = player.inventory.mainInventory;
            mainInventory[toolIndex.getKey()] = toolIndex.getValue()
                .apply(mainInventory[toolIndex.getKey()], player);
        }
        stack.setItemDamage(meta);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // nothing on server side or fake player
        if (player instanceof FakePlayer || !world.isRemote) return stack;
        // check if any screwdriver
        ItemStack configuratorStack;
        if (player.capabilities.isCreativeMode) {
            configuratorStack = null;
        } else {
            Pair<Integer, ?> configurator = findConfiguratorInInv(player);
            if (configurator == null) {
                int count;
                try {
                    count = Integer
                        .parseInt(StatCollector.translateToLocal("GT5U.item.programmed_circuit.no_screwdriver.count"));
                } catch (NumberFormatException e) {
                    player.addChatComponentMessage(
                        new ChatComponentText(
                            "Error in translation GT5U.item.programmed_circuit.no_screwdriver.count: "
                                + e.getMessage()));
                    count = 1;
                }
                player.addChatComponentMessage(
                    new ChatComponentTranslation(
                        "GT5U.item.programmed_circuit.no_screwdriver." + XSTR.XSTR_INSTANCE.nextInt(count)));
                return stack;
            }
            configuratorStack = player.inventory.mainInventory[configurator.getKey()];
        }
        openSelectorGui(configuratorStack, stack.getItemDamage(), player);
        return stack;
    }

    private void openSelectorGui(ItemStack configurator, int meta, EntityPlayer player) {
        UIInfos.openClientUI(
            player,
            buildContext -> new SelectItemUIFactory(
                StatCollector.translateToLocal("GT5U.item.programmed_circuit.select.header"),
                configurator,
                ItemIntegratedCircuit::onConfigured,
                ALL_VARIANTS,
                meta,
                true).createWindow(buildContext));
    }

    private static void onConfigured(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("meta", (byte) stack.getItemDamage());
        GTValues.NW.sendToServer(new GTPacketUpdateItem(tag));
    }

    private static Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> findConfiguratorInInv(
        EntityPlayer player) {
        ItemStack[] mainInventory = player.inventory.mainInventory;
        for (int j = 0, mainInventoryLength = mainInventory.length; j < mainInventoryLength; j++) {
            ItemStack toolStack = mainInventory[j];

            if (!GTUtility.isStackValid(toolStack)) continue;

            for (Map.Entry<Predicate<ItemStack>, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> p : GregTechAPI.sCircuitProgrammerList
                .entrySet())
                if (p.getKey()
                    .test(toolStack)) return Pair.of(j, p.getValue());
        }
        return null;
    }
}
