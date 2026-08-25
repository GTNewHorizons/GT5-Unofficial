package gregtech.common.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

@SideOnly(Side.CLIENT)
public final class DigitalStorageItemRenderer implements IItemRenderer {

    private final IItemRenderer delegate;

    public DigitalStorageItemRenderer(IItemRenderer delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return isDigitalTank(item) ? type == ItemRenderType.INVENTORY : delegate.handleRenderType(item, type);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return isDigitalTank(item) ? helper == ItemRendererHelper.INVENTORY_BLOCK
            : delegate.shouldUseRenderHelper(type, item, helper);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        IMetaTileEntity mte = ItemMachines.getMetaTileEntity(item);
        if (mte instanceof MTEDigitalTankBase tank) {
            DigitalStorageRenderer.renderTankItem(item, tank);
        } else {
            delegate.renderItem(type, item, data);
        }
    }

    private static boolean isDigitalTank(ItemStack item) {
        return ItemMachines.getMetaTileEntity(item) instanceof MTEDigitalTankBase;
    }
}
