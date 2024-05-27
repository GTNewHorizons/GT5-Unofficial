package gregtech.mixin.mixins.late.thaumcraft;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileWandPedestal;

@Mixin(TileWandPedestal.class)
public abstract class TileWandPedestalMixin extends TileThaumcraft implements ISidedInventory, IAspectContainer {

    @WrapWithCondition(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/ItemWandCasting;addVis(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/aspects/Aspect;IZ)I",
            remap = false))
    boolean gregtech$checkWandServerWorld(ItemWandCasting instance, ItemStack is, Aspect aspect, int amount,
        boolean doit) {
        return !this.worldObj.isRemote;
    }

    @WrapWithCondition(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/baubles/ItemAmuletVis;addVis(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/aspects/Aspect;IZ)I",
            remap = false))
    boolean gregtech$checkAmuletServerWorld(ItemAmuletVis instance, ItemStack is, Aspect aspect, int amount,
        boolean doit) {
        return !this.worldObj.isRemote;
    }

}
