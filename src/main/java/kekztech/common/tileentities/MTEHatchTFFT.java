package kekztech.common.tileentities;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.google.common.collect.ImmutableSet;

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
import gregtech.api.enums.Textures;
import gregtech.api.fluid.GTFluidTank;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class MTEHatchTFFT extends MTEHatch implements IMEMonitor<IAEFluidStack> {

    private static class AE2TFFTHatchHandler implements IExternalStorageHandler {

        @Override

        public boolean canHandle(TileEntity te, ForgeDirection d, StorageChannel channel, BaseActionSource mySrc) {
            return channel == StorageChannel.FLUIDS && te instanceof BaseMetaTileEntity
                && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof MTEHatchTFFT;
        }

        @Override

        public IMEInventory getInventory(TileEntity te, ForgeDirection d, StorageChannel channel,
            BaseActionSource src) {
            if (channel == StorageChannel.FLUIDS) {
                return ((MTEHatchTFFT) (((BaseMetaTileEntity) te).getMetaTileEntity()));
            }
            return null;
        }
    }

    private static final IIconContainer TEXTURE_TFFT_HATCH = Textures.BlockIcons.custom("iconsets/TFFT_HATCH");

    private HashMap<IMEMonitorHandlerReceiver<IAEFluidStack>, Object> listeners = new HashMap<>();
    private MTETankTFFT controller;

    public MTEHatchTFFT(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 3, 0, "All-in-one access for the T.F.F.T");
    }

    public MTEHatchTFFT(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return super.getTexture(aBaseMetaTileEntity, side, facing, colorIndex, aActive, aRedstone);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
            TextureFactory.builder()
                .addIcon(TEXTURE_TFFT_HATCH)
                .extFacing()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN),
            TextureFactory.builder()
                .addIcon(TEXTURE_TFFT_HATCH)
                .extFacing()
                .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchTFFT(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int accepted = (controller != null) ? controller.pull(resource, doFill) : 0;
        if (doFill && resource != null) {
            FluidStack acceptedStack = resource.copy();
            acceptedStack.amount = accepted;
            notifyListeners(true, acceptedStack);

        }
        return accepted;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (controller != null) {
            final GTFluidTank sFluid = controller.getSelectedFluid();
            if (controller.getFluidSelector() == -1 || (sFluid != null && sFluid.contains(resource))) {
                FluidStack drained = controller.push(resource, doDrain);
                if (doDrain) notifyListeners(false, drained);
                return drained;
            }
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (controller != null) {
            final GTFluidTank sFluid = controller.getSelectedFluid();
            if (controller.getFluidSelector() == -1) {
                FluidStack drained = controller.push(maxDrain, doDrain);
                if (doDrain) notifyListeners(false, drained);
                return drained;
            }
            if (sFluid != null) {

                FluidStack drained = controller.push(sFluid.get(maxDrain), doDrain);
                if (doDrain) notifyListeners(false, drained);

                return drained;
            }
        }
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return (controller != null) ? controller.getTankInfo() : null;
    }

    public void bind(MTETankTFFT controller) {
        if (controller == this.controller) {
            // try to bind the same controller, do nothing
            return;
        }
        if (this.controller != null) {
            // notify the listeners of the disappearance of fluid in old controller
            unbind();
        }
        this.controller = controller;
        for (GTFluidTank tank : controller.STORE) notifyListeners(true, tank.get());

    }

    public void unbind() {
        if (controller == null) return;
        for (GTFluidTank tank : controller.STORE) notifyListeners(false, tank.get());
        this.controller = null;

    }

    public static void registerAEIntegration() {
        AEApi.instance()
            .registries()
            .externalStorage()
            .addExternalStorageInterface(new AE2TFFTHatchHandler());
    }

    @Override

    public IItemList<IAEFluidStack> getAvailableItems(IItemList out, int iteration) {
        if (controller != null) {
            for (int i = 0; i < MTETankTFFT.MAX_DISTINCT_FLUIDS; i++) {
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

    public IItemList<IAEFluidStack> getStorageList() {
        IItemList<IAEFluidStack> fluidList = new FluidList();
        if (controller != null) {
            for (int i = 0; i < MTETankTFFT.MAX_DISTINCT_FLUIDS; i++) {
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

    public void addListener(IMEMonitorHandlerReceiver l, Object verificationToken) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.put(l, verificationToken);
    }

    @Override

    public void removeListener(IMEMonitorHandlerReceiver l) {
        if (listeners == null) listeners = new HashMap<>();
        listeners.remove(l);
    }

    @Override

    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override

    public boolean isPrioritized(IAEFluidStack input) {
        if (controller == null || input == null) return false;
        return controller.contains(input.getFluidStack()) || controller.fluidCount() < MTETankTFFT.MAX_DISTINCT_FLUIDS;
    }

    @Override
    public boolean canAccept(IAEFluidStack input) {
        if (controller == null || input == null) return false;
        return controller.contains(input.getFluidStack()) || controller.fluidCount() < MTETankTFFT.MAX_DISTINCT_FLUIDS;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true;
    }

    @Override
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
    public StorageChannel getChannel() {
        return StorageChannel.FLUIDS;
    }

    /*
     * This method is only called when fluid is injected/extracted by not an ME Fluid Storage Bus.
     * For example, from Input Hatches or Output Hatches on TFFT Tank or fluid pipes connected to this Hatch.
     */
    public void notifyListeners(boolean isIncrement, FluidStack stack) {
        if (stack == null) return;
        AEFluidStack s = AEFluidStack.create(stack);
        if (isIncrement == false) s.setStackSize(-s.getStackSize());
        listeners.forEach((l, o) -> {
            if (l.isValid(o)) l.postChange(this, ImmutableSet.of(s), null);
            else removeListener(l);
        });
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (controller != null && ((!controller.isValid()) || controller.tfftHatch != this)) {
                // controller is destroyed or controller has a different tfftHatch
                unbind();
            }
        }
    }

    @Override
    public int getCapacity() {
        if (controller != null) {
            return (int) Math.min(controller.getCapacityPerFluid(), Integer.MAX_VALUE);
        }
        return 0;
    }

}
