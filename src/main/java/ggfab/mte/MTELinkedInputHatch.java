package ggfab.mte;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTELinkedInputHatchGui;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;

@IMetaTileEntity.SkipGenerateDescription
public class MTELinkedInputHatch extends MTEHatchMultiInput implements IRecipeProcessingAwareHatch {

    public static final int SIZE_TANKS = 4;
    private static final int TANK_CAPACITY = 128_000;
    private SharedFluidInventory realInventory;
    private String channel;
    private boolean privateChannel;
    private State state;
    private WorldSave save;

    public MTELinkedInputHatch(int id, String name, String nameRegional, int tier) {
        super(id, SIZE_TANKS, name, nameRegional, tier);
    }

    public MTELinkedInputHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, SIZE_TANKS, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELinkedInputHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTELinkedInputHatchGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public int getCapacity() {
        return TANK_CAPACITY;
    }

    @Override
    public void startRecipeProcessing() {
        if (realInventory == null) return;
        if (realInventory.used) {
            state = State.Blocked;
        } else {
            realInventory.used = true;
            state = State.Activated;
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        if (state == State.Activated) {
            assert realInventory != null;
            realInventory.used = false;
        }
        state = State.Default;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void updateSlots() {
        if (channel == null || realInventory == null) return;
        compactFluids();
        markDirty();
        getWorldSave().markDirty();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (realInventory != null) {
            if (--realInventory.ref <= 0) getWorldSave().remove(getRealChannel());
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (channel != null) aNBT.setString("channel", channel);
        aNBT.setBoolean("private", privateChannel);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        String savedChannel = aNBT.hasKey("channel", Constants.NBT.TAG_STRING) ? aNBT.getString("channel") : null;
        channel = savedChannel == null || savedChannel.isEmpty() ? null : savedChannel;
        privateChannel = aNBT.getBoolean("private");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (channel != null) {
            realInventory = getWorldSave().get(getRealChannel());
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mFluid = getFluid();
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && realInventory != null) {
            compactFluids();
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        if (aPlayer.isSneaking()) {
            if (this.realInventory == null) {
                aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.no_channel"));
                return;
            }
            realInventory.disableSort = !realInventory.disableSort;
            GTUtility.sendChatComp(
                aPlayer,
                new ChatComponentTranslation("GT5U.hatch.disableSort." + realInventory.disableSort));
        } else {
            this.disableFilter = !this.disableFilter;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + this.disableFilter);
        }
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    public boolean isPrivate() {
        return privateChannel;
    }

    public void setPrivate(boolean aPrivate) {
        if (aPrivate == privateChannel) return;
        if (getBaseMetaTileEntity().isClientSide()) {
            privateChannel = aPrivate;
            return;
        }
        if (this.channel == null) {
            privateChannel = aPrivate;
            return;
        }
        getWorldSave().markDirty();
        if (--this.realInventory.ref <= 0) {
            getWorldSave().remove(getRealChannel());
        }
        privateChannel = aPrivate;
        realInventory = getWorldSave().get(getRealChannel());
        realInventory.ref++;
        getWorldSave().markDirty();
    }

    private String getRealChannel() {
        if (channel == null) return null;
        if (privateChannel) return getBaseMetaTileEntity().getOwnerUuid() + channel;
        return new UUID(0, 0) + channel;
    }

    public String getChannel() {
        return channel == null ? "" : channel;
    }

    public void setChannel(String aChannel) {
        if (aChannel.isEmpty()) aChannel = null;
        if (getBaseMetaTileEntity().isClientSide()) {
            channel = aChannel;
            return;
        }
        if (Objects.equals(this.channel, aChannel)) return;
        if (this.channel != null) {
            if (--this.realInventory.ref <= 0) {
                getWorldSave().remove(getRealChannel());
            }
        }
        if (aChannel == null) {
            this.channel = null;
            this.realInventory = null;
        } else {
            this.channel = aChannel;
            this.realInventory = getWorldSave().get(getRealChannel());
            realInventory.ref++;
        }
        getWorldSave().markDirty();
    }

    private WorldSave getWorldSave() {
        if (save == null) {
            WorldSave save = (WorldSave) getBaseMetaTileEntity().getWorld()
                .loadItemData(WorldSave.class, "LinkedInputHatches");
            if (save == null) {
                save = new WorldSave("LinkedInputHatches");
                getBaseMetaTileEntity().getWorld()
                    .setItemData(save.mapName, save);
            }
            this.save = save;
        }
        return save;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (state == State.Blocked || realInventory == null || aFluid == null || !isFluidInputAllowed(aFluid)) return 0;
        int filled = 0;
        int tAmount = aFluid.amount;
        for (int i = 0; i < SIZE_TANKS; i++) {
            FluidStack tankFluid = realInventory.fluids[i];
            if (tankFluid != null && tankFluid.isFluidEqual(aFluid)) {
                int canFill = getCapacity() - tankFluid.amount;
                int toFill = Math.min(canFill, tAmount);
                if (toFill > 0) {
                    if (doFill) {
                        tankFluid.amount += toFill;
                        getWorldSave().markDirty();
                    }
                    filled += toFill;
                    tAmount -= toFill;
                    if (tAmount <= 0) return filled;
                }
            }
        }
        for (int i = 0; i < SIZE_TANKS; i++) {
            if (realInventory.fluids[i] == null) {
                int toFill = Math.min(getCapacity(), tAmount);
                if (doFill) {
                    realInventory.fluids[i] = new FluidStack(aFluid, toFill);
                    getWorldSave().markDirty();
                }
                filled += toFill;
                tAmount -= toFill;
                if (tAmount <= 0) return filled;
            }
        }
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (state == State.Blocked || realInventory == null) return null;
        for (FluidStack storedFluid : realInventory.fluids) {
            if (storedFluid == null) continue;
            FluidStack drained = storedFluid.copy();
            drained.amount = Math.min(maxDrain, drained.amount);
            if (doDrain) {
                storedFluid.amount -= drained.amount;
                if (storedFluid.amount <= 0) {
                    realInventory.fluids[getFluidSlot(storedFluid)] = null;
                }
                getWorldSave().markDirty();
            }
            return drained;
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {
        return drain(from, aFluid, aFluid == null ? 0 : aFluid.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, int amount, boolean doDrain) {
        if (state == State.Blocked || realInventory == null || aFluid == null) return null;
        for (FluidStack storedFluid : realInventory.fluids) {
            if (storedFluid == null || !aFluid.isFluidEqual(storedFluid)) continue;
            FluidStack drained = storedFluid.copy();
            drained.amount = Math.min(amount, drained.amount);
            if (doDrain) {
                storedFluid.amount -= drained.amount;
                if (storedFluid.amount <= 0) {
                    realInventory.fluids[getFluidSlot(storedFluid)] = null;
                }
                getWorldSave().markDirty();
            }
            return drained;
        }
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (realInventory == null) return new FluidTankInfo[0];
        FluidTankInfo[] infos = new FluidTankInfo[SIZE_TANKS];
        for (int i = 0; i < SIZE_TANKS; i++) {
            infos[i] = new FluidTankInfo(realInventory.fluids[i], getCapacity());
        }
        return infos;
    }

    @Override
    public FluidStack[] getStoredFluid() {
        if (realInventory == null || state == State.Blocked) return new FluidStack[0];
        return realInventory.fluids;
    }

    @Override
    public FluidStackTank[] getFluidTanks() {
        FluidStackTank[] tanks = new FluidStackTank[SIZE_TANKS];
        for (int i = 0; i < SIZE_TANKS; i++) {
            final int index = i;
            tanks[i] = new FluidStackTank(() -> realInventory == null ? null : realInventory.fluids[index], f -> {
                if (realInventory != null) {
                    realInventory.fluids[index] = f;
                    getWorldSave().markDirty();
                }
            }, getCapacity()) {

                @Override
                public int fill(FluidStack resource, boolean doFill) {
                    int filled = super.fill(resource, doFill);
                    if (doFill && filled > 0) {
                        markDirty();
                        getWorldSave().markDirty();
                    }
                    return filled;
                }

                @Override
                public FluidStack drain(int maxDrain, boolean doDrain) {
                    FluidStack drained = super.drain(maxDrain, doDrain);
                    if (doDrain && drained != null) {
                        markDirty();
                        getWorldSave().markDirty();
                    }
                    return drained;
                }
            };
        }
        return tanks;
    }

    @Override
    public FluidStack getFluid() {
        if (realInventory == null || state == State.Blocked) return null;
        for (FluidStack storedFluid : realInventory.fluids) {
            if (storedFluid != null && storedFluid.amount > 0) return storedFluid;
        }
        return null;
    }

    @Override
    public int getFluidAmount() {
        FluidStack fluid = getFluid();
        return fluid == null ? 0 : fluid.amount;
    }

    @Override
    public int getFluidSlot(FluidStack fluid) {
        if (realInventory == null || fluid == null) return -1;
        for (int i = 0; i < SIZE_TANKS; i++) {
            if (realInventory.fluids[i] == fluid) return i;
        }
        return -1;
    }

    private void compactFluids() {
        if (realInventory == null) return;
        int nextSlot = 0;
        for (int i = 0; i < SIZE_TANKS; i++) {
            FluidStack fluid = realInventory.fluids[i];
            if (fluid == null || fluid.amount <= 0) {
                realInventory.fluids[i] = null;
                continue;
            }
            if (!realInventory.disableSort && i != nextSlot) {
                realInventory.fluids[nextSlot] = fluid;
                realInventory.fluids[i] = null;
            }
            nextSlot++;
        }
    }

    private enum State {
        Activated,
        Blocked,
        Default,
    }

    private static class SharedFluidInventory {

        private final FluidStack[] fluids;
        public boolean disableSort;
        private boolean used;
        private int ref;

        public SharedFluidInventory() {
            this.fluids = new FluidStack[SIZE_TANKS];
        }

        public SharedFluidInventory(NBTTagCompound tag) {
            this.fluids = new FluidStack[SIZE_TANKS];
            for (int i = 0; i < SIZE_TANKS; i++) {
                String key = "" + i;
                if (!tag.hasKey(key, Constants.NBT.TAG_COMPOUND)) continue;
                fluids[i] = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(key));
            }
            ref = tag.getInteger("ref");
            disableSort = tag.getBoolean("ds");
        }

        public NBTTagCompound save() {
            NBTTagCompound tag = new NBTTagCompound();
            for (int i = 0; i < SIZE_TANKS; i++) {
                FluidStack fluid = fluids[i];
                if (fluid == null) continue;
                tag.setTag("" + i, fluid.writeToNBT(new NBTTagCompound()));
            }
            tag.setInteger("ref", ref);
            tag.setBoolean("ds", disableSort);
            return tag;
        }
    }

    public static class WorldSave extends WorldSavedData {

        private final Map<String, SharedFluidInventory> data = new HashMap<>();

        public WorldSave(String name) {
            super(name);
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            data.clear();
            @SuppressWarnings("unchecked")
            Set<Map.Entry<String, NBTBase>> set = tag.tagMap.entrySet();
            for (Map.Entry<String, NBTBase> e : set) {
                data.put(e.getKey(), new SharedFluidInventory((NBTTagCompound) e.getValue()));
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            for (Map.Entry<String, SharedFluidInventory> e : data.entrySet()) {
                if (e.getValue().ref > 0) tag.setTag(
                    e.getKey(),
                    e.getValue()
                        .save());
            }
        }

        public SharedFluidInventory get(Object channel) {
            return data.computeIfAbsent(channel.toString(), k -> new SharedFluidInventory());
        }

        public void remove(Object channel) {
            data.remove(channel.toString());
            markDirty();
        }
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormatted("gt.blockmachines.hatch_input_linked.desc", getCapacity());
    }
}
