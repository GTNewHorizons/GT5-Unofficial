package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_HATCH_PATTERN_PROVIDER;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.items.misc.ItemEncodedPattern;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchPatternProviderGui;

public class MTEHatchPatternProvider extends MTEHatchInputBus {

    public MTEHatchPatternProvider(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, getSlots(tier), null);
        this.disableSort = true;
    }

    public MTEHatchPatternProvider(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, getSlots(tier), description, textures);
        this.disableSort = true;
    }

    @Override
    public String[] getDescription() {
        return GTSplit
            .splitLocalizedFormatted("GT5U.gui.tooltip.hatch.crafting_pattern_provider", this.mInventory.length);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return getTexturesInactive(aBaseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_HATCH_PATTERN_PROVIDER) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchPatternProvider(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
        return isValidPattern(itemStack) && super.isValidSlot(index);
    }

    public boolean isValidPattern(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!(itemStack.getItem() instanceof ItemEncodedPattern patItem)) return false;

        final ICraftingPatternDetails pattern = patItem.getPatternForItem(
            itemStack,
            this.getBaseMetaTileEntity()
                .getWorld());
        if (pattern == null) return false;

        return pattern.isCraftable();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        // Empty, disable sorting behavior
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchPatternProviderGui(this).build(data, syncManager, uiSettings);
    }

    public static int getSlots(int tier) {
        // should be a multiple of 9 to show properly
        return switch (tier) {
            case 5 -> 36; // IV (36)
            case 6 -> 45; // LuV (49->45)
            case 7 -> 63; // ZPM (64->63)
            case 8 -> 81; // UV (81)
            case 9 -> 99; // UHV (100->99)
            default -> 9; // should be unreachable
        };
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
}
