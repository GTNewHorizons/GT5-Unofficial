package gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock;

import static gregtech.api.modernmaterials.ModernMaterialUtilities.tooltipGenerator;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.effects.IMaterialEffect;

public class BaseMaterialItemBlock extends ItemBlock {

    public BaseMaterialItemBlock(@NotNull final Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_ORES); // todo add new tabs.
    }

    @Override
    public int getMetadata(final int metadata) {
        return metadata;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull final ItemStack itemStack, final EntityPlayer player, List<String> tooltipList,
        boolean F3_H) {
        tooltipList.addAll(tooltipGenerator(itemStack, player, tooltipList, F3_H));
    }

    @Override
    public String getItemStackDisplayName(@NotNull final ItemStack itemStack) {

        ModernMaterial material = getMaterial(itemStack);
        if (material == null) return "";

        // todo custom name overrides, for e.g. names that change over time.

        BaseMaterialBlock block = (BaseMaterialBlock) Block.getBlockFromItem(itemStack.getItem());
        BlocksEnum blockEnum = block.getBlockEnum();

        return blockEnum.getLocalisedName(material);
    }

    public BlocksEnum getPart() {
        return ((BaseMaterialBlock) field_150939_a).getBlockEnum();
    }

    public static ModernMaterial getMaterial(@NotNull final ItemStack itemStack) {
        return ModernMaterial.getMaterialFromID(itemStack.getItemDamage());
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        final ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        for (IMaterialEffect effect : material.getEffects()) {
            effect.apply(itemStack, world, entity, slotIndex, isCurrentItem);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata) {
        // field_150939_a is the underlying block of this ItemBlock.
        return this.field_150939_a.getIcon(0, metadata);
    }

}
