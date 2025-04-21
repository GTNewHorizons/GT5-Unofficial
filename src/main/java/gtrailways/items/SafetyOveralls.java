package gtrailways.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import mods.railcraft.api.core.items.ISafetyPants;

public class SafetyOveralls extends ItemArmor implements IHazardProtector, ISafetyPants {

    public SafetyOveralls() {
        super(EnumHelper.addArmorMaterial("SAFETY_OVERALLS", 6, new int[] { 1, 3, 2, 1}, 15), 0, 2);
        this.setMaxDamage(256);
    }

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }

    @Override
    public boolean blocksElectricTrackDamage(ItemStack pants) {
        return protectsAgainst(pants, Hazard.ELECTRICAL);
    }

    public void onShock(ItemStack pants, EntityPlayer player) {};

    public boolean lowersLocomotiveDamage(ItemStack pants) {
        return true;
    };

    public void onHitLocomotive(ItemStack pants, EntityPlayer player) {};
}
