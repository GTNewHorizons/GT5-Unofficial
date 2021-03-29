package gregtech.common.tileentities.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.AEApi;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;

public class GT_MetaTileEntity_Hatch_OutputBus_ME extends GT_MetaTileEntity_Hatch_OutputBus {
    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;

    public GT_MetaTileEntity_Hatch_OutputBus_ME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1, new String[]{
            "Item Output for Multiblocks", "Stores directly into ME"}, 0);
    }

    public GT_MetaTileEntity_Hatch_OutputBus_ME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, 0, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OutputBus_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ME_HATCH)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ME_HATCH)};
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy();
    }

    @Optional.Method(modid = "appliedenergistics2")
    public int store(final ItemStack stack) {
        if (stack == null)
            return 0;
        try {
            AENetworkProxy proxy = getProxy();
            if (proxy == null)
                return stack.stackSize;
            IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
            final IEnergySource src = proxy.getEnergy();
            IAEItemStack toStore = AEApi.instance().storage().createItemStack(stack);
            IAEItemStack rest = Platform.poweredInsert( src, sg, toStore, getRequest());
            if (rest != null)
                return (int)rest.getStackSize();
            return 0;
        }
        catch( final GridAccessException ignored )
        {
        }
        return stack.stackSize;
    }

    @Optional.Method(modid = "appliedenergistics2")
    private BaseActionSource getRequest() {
        if (requestSource == null)
            requestSource = new MachineSource((IActionHost)getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing((byte)forgeDirection.ordinal()) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy((IGridProxyable)getBaseMetaTileEntity(), "proxy", ItemList.Hatch_Output_Bus_ME.get(1), true);
                gridProxy.onReady();
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            }
        }
        return this.gridProxy;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {
    }
}
