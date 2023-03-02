package com.minecraft7771.gtnhintergalactic.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.minecraft7771.gtnhintergalactic.GTNHIntergalactic;
import gregtech.api.enums.Materials;

/**
 * Drones used for the Space Mining module of the Space Elevator
 *
 * @author minecraft7771
 */
public class ItemMiningDrones extends Item {

    private static final String[] names = new String[] { "MiningDroneLV", "MiningDroneMV", "MiningDroneHV",
            "MiningDroneEV", "MiningDroneIV", "MiningDroneLuV", "MiningDroneZPM", "MiningDroneUV", "MiningDroneUHV",
            "MiningDroneUEV", };
    private static final IIcon[] icons = new IIcon[names.length];

    public ItemMiningDrones() {
        setCreativeTab(GTNHIntergalactic.tab);
        setHasSubtypes(true);
        setUnlocalizedName("MiningDrone");
    }

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
        UEV
    }

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
        UEV(Materials.Neutronium);

        private Materials mat;

        DroneMaterials(Materials aMat) {
            mat = aMat;
        }

        public Materials getMaterial() {
            return mat;
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta % icons.length];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List variants) {
        for (int i = 0; i < names.length; i++) {
            variants.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + names[stack.getItemDamage() % names.length];
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":drones/" + names[i]);
        }
    }
}
