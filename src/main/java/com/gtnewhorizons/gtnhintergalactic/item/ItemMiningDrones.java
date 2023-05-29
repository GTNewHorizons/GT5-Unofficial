package com.gtnewhorizons.gtnhintergalactic.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.gtnewhorizons.gtnhintergalactic.GTNHIntergalactic;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;

/**
 * Drones used for the Space Mining module of the Space Elevator
 *
 * @author minecraft7771
 */
public class ItemMiningDrones extends Item {

    /** Names of the mining drones */
    private static final String[] NAMES = new String[] { "MiningDroneLV", "MiningDroneMV", "MiningDroneHV",
            "MiningDroneEV", "MiningDroneIV", "MiningDroneLuV", "MiningDroneZPM", "MiningDroneUV", "MiningDroneUHV",
            "MiningDroneUEV", "MiningDroneUIV", "MiningDroneUMV", "MiningDroneUXV", "MiningDroneMAX" };
    /** Icons of the mining drones */
    private static final IIcon[] ICONS = new IIcon[NAMES.length];

    /**
     * Create a new item for the mining drones
     */
    public ItemMiningDrones() {
        setCreativeTab(GTNHIntergalactic.tab);
        setHasSubtypes(true);
        setUnlocalizedName("MiningDrone");
    }

    /**
     * Enumeration for the available drone tiers
     */
    public enum DroneTiers {
        LV,
        MV,
        HV,
        EV,
        IV,
        LuV,
        ZPM,
        UV,
        UHV,
        UEV,
        UIV,
        UMV,
        UXV,
        MAX
    }

    /**
     * Materials for the supplied drills and tool rods which each drone tier needs
     */
    public enum DroneMaterials {

        LV(Materials.Steel),
        MV(Materials.Steel),
        HV(Materials.Titanium),
        EV(Materials.Titanium),
        IV(Materials.TungstenSteel),
        LuV(Materials.TungstenSteel),
        ZPM(Materials.Naquadah),
        UV(Materials.Naquadah),
        UHV(Materials.NaquadahAlloy),
        UEV(Materials.Neutronium),
        UIV(Materials.CosmicNeutronium),
        UMV(Materials.Infinity),
        UXV(MaterialsUEVplus.TranscendentMetal);

        /** Material for this drone tier */
        private final Materials mat;

        /**
         * Register a new drone tier material
         *
         * @param aMat needed material
         */
        DroneMaterials(Materials aMat) {
            mat = aMat;
        }

        /**
         * @return Material which this drone tier needs
         */
        public Materials getMaterial() {
            return mat;
        }
    }

    /**
     * Get the icon for this item
     *
     * @param meta Meta value of the item
     * @return Icon for this item
     */
    @Override
    public IIcon getIconFromDamage(int meta) {
        return ICONS[meta % ICONS.length];
    }

    /**
     * Get all possible variants of this item
     *
     * @param item     Item of which the variants should be gotten
     * @param tab      Creative tab of this item
     * @param variants List of variants to which will be added
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List variants) {
        for (int i = 0; i < NAMES.length; i++) {
            variants.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Get the unlocalized name of this item
     *
     * @param stack Item stack, which contains this item
     * @return Unlocalized name
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + NAMES[stack.getItemDamage() % NAMES.length];
    }

    /**
     * Register the icons for this item
     *
     * @param iconRegister Register to which the icons will be added
     */
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < NAMES.length; i++) {
            ICONS[i] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":drones/" + NAMES[i]);
        }
    }
}
