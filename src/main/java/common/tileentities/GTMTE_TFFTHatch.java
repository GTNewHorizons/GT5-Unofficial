package common.tileentities;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IExternalStorageHandler;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEFluidStack;
import appeng.util.item.FluidList;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Textures;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;

@Optional.Interface(iface = "appeng.api.storage.IMEMonitor", modid = "appliedenergistics2", striprefs = true)
public class GTMTE_TFFTHatch extends GT_MetaTileEntity_Hatch implements IMEMonitor<IAEFluidStack> {

    @Optional.Interface(
            iface = "appeng.api.storage.IExternalStorageHandler",
            modid = "appliedenergistics2",
            striprefs = true)
    private static class AE2TFFTHatchHandler implements IExternalStorageHandler {

        @Override
        @Optional.Method(modid = "appliedenergistics2")
        public boolean canHandle(TileEntity te, ForgeDirection d, StorageChannel channel, BaseActionSource mySrc) {
            return channel == StorageChannel.FLUIDS && te instanceof BaseMetaTileEntity
                    && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof GTMTE_TFFTHatch;
        }

        @Override
        @Optional.Method(modid = "appliedenergistics2")
        public IMEInventory getInventory(TileEntity te, ForgeDirection d, StorageChannel channel,
                BaseActionSource src) {
            if (channel == StorageChannel.FLUIDS) {
                return ((GTMTE_TFFTHatch) (((BaseMetaTileEntity) te).getMetaTileEntity()));
            }
            return null;
        }
    }

    private static final Textures.BlockIcons.CustomIcon TEXTURE_TFFT_HATCH = new Textures.BlockIcons.CustomIcon(
            "iconsets/TFFT_HATCH");

    private HashMap<IMEMonitorHandlerReceiver<IAEFluidStack>, Object> listeners = new HashMap<>();
    private GTMTE_TFFT controller;

    public GTMTE_TFFTHatch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 3, 0, "All-in-one access for the T.F.F.T");
    }

    public GTMTE_TFFTHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        return super.getTexture(aBaseMetaTileEntity, aSide, aFacing, aColorIndex, aActive, aRedstone);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
                TextureFactory.builder().addIcon(TEXTURE_TFFT_HATCH).extFacing().build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
                TextureFactory.builder().addIcon(TEXTURE_TFFT_HATCH).extFacing().build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTMTE_TFFTHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return (controller != null) ? controller.pull(resource, doFill) : 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (controller != null) {
            final FluidTankGT sFluid = controller.getSelectedFluid();
            if (controller.getFluidSelector() == -1 || (sFluid != null && sFluid.contains(resource))) {
                return controller.push(resource, doDrain);
            }
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (controller != null) {
            final FluidTankGT sFluid = controller.getSelectedFluid();
            if (controller.getFluidSelector() == -1) return controller.push(maxDrain, doDrain);
            if (sFluid != null) return controller.push(sFluid.get(maxDrain), doDrain);
        }
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return (controller != null) ? controller.getTankInfo() : null;
    }

    public void bind(GTMTE_TFFT controller) {
        this.controller = controller;
    }

    public void unbind() {
        this.controller = null;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public static void registerAEIntegration() {
        AEApi.instance().registries().externalStorage().addExternalStorageInterface(new AE2TFFTHatchHandler());
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IItemList<IAEFluidStack> getAvailableItems(IItemList out) {
        if (controller != null) {
            for (int i = 0; i < GTMTE_TFFT.MAX_DISTINCT_FLUIDS; i++) {
                if (!controller.STORE[i].isEmpty()) {
                    IAEFluidStack s = AEFluidStack.create(controller.STORE[i].get());
                    s.setStackSize(controller.STORE[i].amount());
                    out.add(s);
                }
            }
        }
        return out;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IItemList<IAEFluidStack> getStorageList() {
        IItemList<IAEFluidStack> fluidList = new FluidList();
        if (controller != null) {
            for (int i = 0; i < GTMTE_TFFT.MAX_DISTINCT_FLUIDS; i++) {
                if (!controller.STORE[i].isEmpty()) {
                    IAEFluidStack s = AEFluidStack.create(controller.STORE[i].get());
                    s.setStackSize(controller.STORE[i].amount());
                    fluidList.add(s);
                }
            }
        }
        return fluidList;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void addListener(IMEMonitorHandlerReceiver<IAEFluidStack> l, Object verificationToken) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.put(l, verificationToken);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void removeListener(IMEMonitorHandlerReceiver<IAEFluidStack> l) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.remove(l);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean isPrioritized(IAEFluidStack input) {
        if (controller == null || input == null) return false;
        return controller.contains(input.getFluidStack()) || controller.fluidCount() < GTMTE_TFFT.MAX_DISTINCT_FLUIDS;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean canAccept(IAEFluidStack input) {
        if (controller == null || input == null) return false;
        return controller.contains(input.getFluidStack()) || controller.fluidCount() < GTMTE_TFFT.MAX_DISTINCT_FLUIDS;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public int getPriority() {
        return 0;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public int getSlot() {
        return 0;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean validForPass(int i) {
        return true;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IAEFluidStack injectItems(IAEFluidStack input, Actionable mode, BaseActionSource src) {
        final FluidStack inputStack = input.getFluidStack();
        if (inputStack == null) return null;
        if (controller == null || getBaseMetaTileEntity() == null) return input;
        if (mode != Actionable.SIMULATE) getBaseMetaTileEntity().markDirty();
        long amount = controller.pull(input.getFluidStack(), input.getStackSize(), mode != Actionable.SIMULATE);
        if (amount == 0) return input;
        if (amount == input.getStackSize()) return null;
        IAEFluidStack result = AEFluidStack.create(input.getFluidStack());
        result.setStackSize(input.getStackSize() - amount);
        return result;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public IAEFluidStack extractItems(IAEFluidStack request, Actionable mode, BaseActionSource src) {
        if (controller == null || getBaseMetaTileEntity() == null) return null;
        if (mode != Actionable.SIMULATE) getBaseMetaTileEntity().markDirty();
        long amount = controller.push(request.getFluidStack(), request.getStackSize(), mode != Actionable.SIMULATE);
        if (amount == 0) return null;
        if (amount == request.getStackSize()) return request.copy();
        IAEFluidStack result = AEFluidStack.create(request.getFluidStack());
        result.setStackSize(amount);
        return result;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public StorageChannel getChannel() {
        return StorageChannel.FLUIDS;
    }
}
