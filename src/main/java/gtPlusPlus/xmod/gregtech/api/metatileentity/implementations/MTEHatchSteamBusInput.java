package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusGui;
import gtPlusPlus.core.util.Utils;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchSteamBusInput extends MTEHatchInputBus {

    public RecipeMap<?> mRecipeMap = null;

    public MTEHatchSteamBusInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots() + 1);
    }

    public MTEHatchSteamBusInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots() + 1, aDescription, aTextures);
    }

    public static int getSlots() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return Utils.splitLocalizedWithAlkalus("gt.blockmachines.input_bus_steam.desc");
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSteamBusInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int getCircuitSlot() {
        return getSlots();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusGui(this) {

            // steam input buses don't follow into the common formula that others do, so its changed here,
            @Override
            protected int getDimension() {
                return 2;
            }
        }.build(data, syncManager, uiSettings);
    }
}
