// package com.github.bartimaeusnek.crossmod.ae2;
//
// import appeng.api.AEApi;
// import appeng.api.config.FuzzyMode;
// import appeng.api.config.IncludeExclude;
// import appeng.api.implementations.items.IStorageCell;
// import appeng.api.storage.*;
// import appeng.api.storage.data.IAEItemStack;
// import appeng.core.localization.GuiText;
// import appeng.items.contents.CellUpgrades;
// import appeng.util.Platform;
// import com.github.bartimaeusnek.bartworks.common.items.SimpleSubItemClass;
// import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
// import cpw.mods.fml.common.registry.GameRegistry;
// import cpw.mods.fml.relauncher.Side;
// import cpw.mods.fml.relauncher.SideOnly;
// import net.minecraft.entity.player.EntityPlayer;
// import net.minecraft.inventory.IInventory;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
//
// import java.util.List;
//
// public class ItemSingleItemStorageCell extends SimpleSubItemClass implements IStorageCell {
//
//    public ItemSingleItemStorageCell(String tex) {
//        super(tex);
//        Item thizz = this;
//        GameRegistry.registerItem(this, BartWorksCrossmod.MOD_ID+this.iconString);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List lines, boolean p_77624_4_) {
//        IMEInventoryHandler<?> inventory = AEApi.instance().registries().cell().getCellInventory(stack,
// (ISaveProvider)null, StorageChannel.ITEMS);
//        if (inventory instanceof ICellInventoryHandler) {
//            ICellInventoryHandler handler = (ICellInventoryHandler)inventory;
//            ICellInventory cellInventory = handler.getCellInv();
//            if (cellInventory != null) {
//                lines.add(cellInventory.getUsedBytes() + " " + GuiText.Of.getLocal() + ' ' +
// cellInventory.getTotalBytes() + ' ' + GuiText.BytesUsed.getLocal());
//                lines.add(cellInventory.getStoredItemTypes() + " " + GuiText.Of.getLocal() + ' ' +
// cellInventory.getTotalItemTypes() + ' ' + GuiText.Types.getLocal());
//                if (handler.isPreformatted()) {
//                    String list = (handler.getIncludeExcludeMode() == IncludeExclude.WHITELIST ? GuiText.Included :
// GuiText.Excluded).getLocal();
//                    if (handler.isFuzzy()) {
//                        lines.add(GuiText.Partitioned.getLocal() + " - " + list + ' ' + GuiText.Fuzzy.getLocal());
//                    } else {
//                        lines.add(GuiText.Partitioned.getLocal() + " - " + list + ' ' + GuiText.Precise.getLocal());
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getBytes(ItemStack itemStack) {
//        return getTotalTypes(itemStack)*getBytesPerType(itemStack);
//    }
//
//    @Override
//    public int BytePerType(ItemStack itemStack) {
//        return 1;
//    }
//
//    @Override
//    public int getBytesPerType(ItemStack itemStack) {
//        return 1;
//    }
//
//    @Override
//    public int getTotalTypes(ItemStack itemStack) {
//        return 4096;
//    }
//
//    @Override
//    public boolean isBlackListed(ItemStack itemStack, IAEItemStack iaeItemStack) {
//        return iaeItemStack == null || iaeItemStack.getItem().getItemStackLimit() != 1;
//    }
//
//    @Override
//    public boolean storableInStorageCell() {
//        return true;
//    }
//
//    @Override
//    public boolean isStorageCell(ItemStack itemStack) {
//        return true;
//    }
//
//    @Override
//    public double getIdleDrain() {
//        return 4D;
//    }
//
//    @Override
//    public boolean isEditable(ItemStack itemStack) {
//        return true;
//    }
//
//    @Override
//    public IInventory getUpgradesInventory(ItemStack itemStack) {
//        return new CellUpgrades(itemStack, 2);
//    }
//
//    @Override
//    public IInventory getConfigInventory(ItemStack itemStack) {
//        return new CellUpgrades(itemStack,2);
//    }
//    @Override
//    public FuzzyMode getFuzzyMode(ItemStack is) {
//        String fz = Platform.openNbtData(is).getString("FuzzyMode");
//
//        try {
//            return FuzzyMode.valueOf(fz);
//        } catch (Throwable var4) {
//            return FuzzyMode.IGNORE_ALL;
//        }
//    }
//    @Override
//    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {
//        Platform.openNbtData(is).setString("FuzzyMode", fzMode.name());
//    }
// }
