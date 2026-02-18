package gregtech.common.tileentities.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import appeng.items.misc.ItemEncodedPattern;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.client.GTTooltipHandler;
import gregtech.common.gui.modularui.hatch.MTEHatchPatternProviderGui;

public class MTEHatchPatternProvider extends MTEHatchInputBus {

    public MTEHatchPatternProvider(int id, String name, String nameRegional) {
        super(
            id,
            name,
            nameRegional,
            GTTooltipHandler.Tier.LV.ordinal(),
            getSlots(),
            new String[] { "Holds patterns for the Large Molecular Assembler (LMA)." });
        this.disableSort = true;
    }

    public MTEHatchPatternProvider(String name, int slots, String[] description, ITexture[][][] textures) {
        super(name, GTTooltipHandler.Tier.LV.ordinal(), slots, description, textures);
        this.disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchPatternProvider(this.mName, getSlots(), this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getSizeInventory() {
        return getSlots();
    }

    @Override
    public int getCircuitSlot() {
        return -1;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if (itemStack == null) return false;
        if(!(itemStack.getItem() instanceof ItemEncodedPattern patItem)) return false;

        final var pattern = patItem.getPatternForItem(itemStack, this.getBaseMetaTileEntity().getWorld());
        if (pattern == null) return false;

        return super.isValidSlot(index) && pattern.isCraftable();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        // Empty, disable sorting behavior
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return super.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchPatternProviderGui(this).build(data, syncManager, uiSettings);
    }

    private static int getSlots() {
        return 9 * 11;
    }
}
