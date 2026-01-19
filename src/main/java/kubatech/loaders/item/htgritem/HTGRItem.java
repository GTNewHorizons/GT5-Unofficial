package kubatech.loaders.item.htgritem;

import static kubatech.kubatech.KT;

import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.MinecraftForgeClient;

import org.apache.commons.lang3.tuple.Triple;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import kubatech.api.utils.ModUtils;
import kubatech.client.renderer.HTGRItemRenderer;

public abstract class HTGRItem extends Item {

    public static final Triple<Double, Double, Double> DEFAULT_FUEL_PROPERTIES = Triple.of(1.0, 1.0, 1.0);

    private static int nextFreeItemID = 0;
    private static final HashMap<Materials, Integer> knownMaterials = new HashMap<>();
    private static final HashMap<Integer, Materials> metaToMaterialMap = new HashMap<>();
    private static final HashMap<Materials, Triple<Double, Double, Double>> fuelProperties = new HashMap<>();

    public static TRISOMixture TRISO_MIX;
    public static IncompleteBISOFuel INCOMPLETE_BISO;
    public static IncompleteTRISOFuel INCOMPLETE_TRISO;
    public static TRISOFuel TRISO;
    public static BurnedTRISOFuel BURNED_TRISO;

    public static void initItems() {
        TRISO_MIX = new TRISOMixture();
        INCOMPLETE_BISO = new IncompleteBISOFuel();
        INCOMPLETE_TRISO = new IncompleteTRISOFuel();
        TRISO = new TRISOFuel();
        BURNED_TRISO = new BurnedTRISOFuel();

        GameRegistry.registerItem(TRISO_MIX, "htgr_item_triso_mixture");
        GameRegistry.registerItem(INCOMPLETE_BISO, "htgr_item_incomplete_biso_fuel");
        GameRegistry.registerItem(INCOMPLETE_TRISO, "htgr_item_incomplete_triso_fuel");
        GameRegistry.registerItem(TRISO, "htgr_item_triso_fuel");
        GameRegistry.registerItem(BURNED_TRISO, "htgr_item_burned_triso_fuel");
        if (ModUtils.isClientSided) {
            HTGRItemRenderer renderer = new HTGRItemRenderer();
            MinecraftForgeClient.registerItemRenderer(TRISO_MIX, renderer);
            MinecraftForgeClient.registerItemRenderer(INCOMPLETE_BISO, renderer);
            MinecraftForgeClient.registerItemRenderer(INCOMPLETE_TRISO, renderer);
            MinecraftForgeClient.registerItemRenderer(TRISO, renderer);
            MinecraftForgeClient.registerItemRenderer(BURNED_TRISO, renderer);
        }
    }

    public HTGRItem() {
        super();
        this.setMaxDamage(0);
        setHasSubtypes(true);
        this.setCreativeTab(KT);
        this.setUnlocalizedName("htgr_item");
    }

    protected abstract String getNameKey();

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Materials material = getItemMaterial(stack);
        return StatCollector.translateToLocalFormatted(
            getNameKey(),
            (material == null ? "NULL" : (material.getLocalizedNameForItem("%material"))));
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        for (int i = 0; i < nextFreeItemID; i++) {
            p_150895_3_.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getNameKey();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add(StatCollector.translateToLocal("kubatech.tooltip.htgr_material"));
        Materials material = getItemMaterial(stack);
        if (material != null) {
            tooltipList.add(" - " + material.getLocalizedNameForItem("%material"));
            Triple<Double, Double, Double> properties = fuelProperties.getOrDefault(material, DEFAULT_FUEL_PROPERTIES);
            tooltipList.add(
                StatCollector.translateToLocalFormatted(
                    "kubatech.infodata.htgr.fuel_properties",
                    properties.getLeft(),
                    properties.getMiddle(),
                    properties.getRight()));
        }
    }

    private static ItemStack getItemWithMaterial(Materials material, HTGRItem item) {
        ItemStack stack = new ItemStack(item, 1, knownMaterials.get(material));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("material", material.mName);
        stack.setTagCompound(tag);
        return stack;
    }

    public static Materials getItemMaterial(ItemStack stack) {
        int i = stack.getItemDamage();
        return metaToMaterialMap.getOrDefault(i, null);
    }

    public static void addKnownMaterial(Materials material) {
        if (!knownMaterials.containsKey(material)) {
            knownMaterials.put(material, nextFreeItemID);
            metaToMaterialMap.put(nextFreeItemID++, material);
        }
    }

    public static void setFuelProperties(Materials material, Triple<Double, Double, Double> properties) {
        fuelProperties.put(material, properties);
    }

    public static Triple<Double, Double, Double> getFuelProperties(Materials material) {
        return fuelProperties.getOrDefault(material, DEFAULT_FUEL_PROPERTIES);
    }

    public static ItemStack createTRISOMixture(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, TRISO_MIX);
        return stack;
    }

    public static ItemStack createIncompleteBISOFuel(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, INCOMPLETE_BISO);
        return stack;
    }

    public static ItemStack createIncompleteTRISOFuel(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, INCOMPLETE_TRISO);
        return stack;
    }

    public static ItemStack createTRISOFuel(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, TRISO);
        return stack;
    }

    public static ItemStack createBurnedTRISOFuel(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, BURNED_TRISO);
        return stack;
    }
}
