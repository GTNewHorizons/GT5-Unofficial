package gregtech.common.tileentities.machines.basic;

import static gregtech.common.items.IDMetaItem01.Armor_Core_T1;
import static gregtech.common.items.IDMetaItem01.Armor_Core_T2;
import static gregtech.common.items.IDMetaItem01.Armor_Core_T3;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

public class MTEModificationTable extends MTEBasicMachine implements IAddUIWidgets {

    public MTEModificationTable(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1);
    }

    public MTEModificationTable(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName, mTier, mDescriptionArray, mTextures);
    }

    private ItemStackHandler inputHandler = new ItemStackHandler(2);

    private NBTTagCompound getTagFromItem(ItemStack item) {

        NBTTagCompound armorItemTag = new NBTTagCompound();

        int metaID = item.getItemDamage() - 32000;
        int coreID = 0;
        if (metaID == Armor_Core_T1.ID) coreID = 1;
        else if (metaID == Armor_Core_T2.ID) coreID = 2;
        else if (metaID == Armor_Core_T3.ID) coreID = 3;

        armorItemTag.setInteger("core", coreID);

        return armorItemTag;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inputHandler, 0).setAccess(true, true)
                .setPos(79, 34))
            .widget(
                new SlotWidget(inputHandler, 1).setAccess(true, true)
                    .setPos(79, 50)
                    .setBackground(() -> new IDrawable[] { new ItemDrawable(new ItemStack(Items.iron_helmet)) }))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                ItemStack coreItem = inputHandler.getStackInSlot(0);
                ItemStack armorItem = inputHandler.getStackInSlot(1);

                if (coreItem == null || armorItem == null) return;
                coreItem.stackSize -= 1;
                armorItem.setTagCompound(getTagFromItem(coreItem));

            })
                .setPos(20, 20)
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    return ret.toArray(new IDrawable[0]);
                }));
    }
}
