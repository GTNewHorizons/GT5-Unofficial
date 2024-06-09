package gtPlusPlus.core.item.wearable.hazmat;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.ic2.CustomInternalName;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorHazmat;

public class ItemArmorHazmatEx extends ItemArmorHazmat {

    public static void init() {
        GregtechItemList.Armour_Hazmat_Advanced_Helmet
            .set(new ItemStack(new ItemArmorHazmatEx(CustomInternalName.aHazmatHelmetEx, 0)));
        GregtechItemList.Armour_Hazmat_Advanced_Chest
            .set(new ItemStack(new ItemArmorHazmatEx(CustomInternalName.aHazmatChestEx, 1)));
        GregtechItemList.Armour_Hazmat_Advanced_Legs
            .set(new ItemStack(new ItemArmorHazmatEx(CustomInternalName.aHazmatLegsEx, 2)));
        GregtechItemList.Armour_Hazmat_Advanced_Boots
            .set(new ItemStack(new ItemArmorHazmatEx(CustomInternalName.aHazmatBootsEx, 3)));
    }

    private ItemArmorHazmatEx(InternalName internalName, int type) {
        super(internalName, type);
        this.setMaxDamage(256);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        int suffix = this.armorType == 2 ? 2 : 1;
        return IC2.textureDomain + ":textures/armor/" + "hazmatEx_" + suffix + ".png";
    }
}
