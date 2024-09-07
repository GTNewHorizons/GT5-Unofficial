package gregtech.common.tileentities.machines;

import static gregtech.api.enums.GTValues.AuthorEvgenWarGold;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_LOCKER;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.Overlay_UU_Matter;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.gui.widget.ElectricSlotWidget;

public class MTEHatchChargingBus extends MTEHatchInputBus {

    // public final RecipeMap<?> mRecipeMap = null;
    //
    // private MTEMultiBlockBase machine;
    //
    // private ConnectionMulti connection;
    //
    // private List<ConnectionMulti> connectionList = new ArrayList<>();

    public MTEHatchChargingBus(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public MTEHatchChargingBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchChargingBus(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Item Charger Bus for multi-bocks", getSlots(this.mTier) + " Slots", AuthorEvgenWarGold };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = { MACHINE_CASINGS[this.mTier + 2][(i + 1)] };
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = { MACHINE_CASINGS[this.mTier + 2][(i + 1)], OVERLAYS_ENERGY_IN[this.mTier] };
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = { MACHINE_CASINGS[this.mTier + 2][(i + 1)], TextureFactory.of(OVERLAY_LOCKER) };
            rTextures[2][(i + 1)] = tmp2;
        }
        return rTextures;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getMetaTileEntity() instanceof MetaTileEntity mMetaTileEntity) {
                if (mMetaTileEntity.rechargerSlotCount() > 0 && aBaseMetaTileEntity.getStoredEU() > 0) {
                    this.onSetActive(true);

                    for (int i = mMetaTileEntity.rechargerSlotStartIndex(),
                        k = mMetaTileEntity.rechargerSlotCount() + i; i < k; i++) {
                        if (aBaseMetaTileEntity.getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
                            for (int u = 0; u < 10; u++) {
                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(
                                    GTModHandler.chargeElectricItem(
                                        mMetaTileEntity.mInventory[i],
                                        (int) Math.min(V[this.mTier] * 16, aBaseMetaTileEntity.getStoredEU()),
                                        Integer.MAX_VALUE,
                                        false,
                                        false),
                                    true);
                                if (mMetaTileEntity.mInventory[i].stackSize <= 0) {
                                    mMetaTileEntity.mInventory[i] = null;
                                }
                            }
                        }
                    }
                } else {
                    this.onSetActive(false);
                }
            }
        }

        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public long maxEUInput() {
        return mTier == 0 ? 128 : 512;
    }

    @Override
    public int rechargerSlotCount() {
        return getSlots(this.mTier);
    }

    @Override
    public long maxAmperesIn() {
        return getSlots(this.mTier);
    }

    @Override
    public long maxEUStore() {
        return maxEUInput() * 16;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(Overlay_UU_Matter) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(Overlay_UU_Matter) };
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        for (int i = 0; i < mInventory.length; i++)
            for (int j = i + 1; j < mInventory.length; j++) if (mInventory[j] != null
                && (mInventory[i] == null || GTUtility.areStacksEqual(mInventory[i], mInventory[j]))) {
                    GTUtility.moveStackFromSlotAToSlotB(
                        getBaseMetaTileEntity(),
                        getBaseMetaTileEntity(),
                        j,
                        i,
                        (byte) 64,
                        (byte) 1,
                        (byte) 64,
                        (byte) 1);
                }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        for (int i = 0; i < 16; i++) {
            builder.widget(new ElectricSlotWidget(inventoryHandler, i).setPos(52 + (i % 4) * 18, 7 + (i / 4) * 18));
        }
    }

    // @Override
    // public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
    // if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
    // fillStacksIntoFirstSlots();
    // }
    // if (aBaseMetaTileEntity.isServerSide()) {
    // if (hasConnection()) {
    // final ArrayList<ItemStack> inputs = machine.getStoredInputs();
    // final int inputsSize = inputs.size();
    // long machineTier = machine.getInputVoltageTier();
    //
    // if (machine.mProgresstime > 0) {
    // for (int i = inputsSize - 1; i >= 0; i--) {
    // ItemStack stack = inputs.get(i);
    // int stackTier = ((ic2.api.item.IElectricItem) stack.getItem()).getTier(stack);
    //
    // if (stackTier <= machineTier) {
    // if (GTModHandler.isElectricItem(stack)) {
    // GTModHandler.chargeElectricItem(
    // stack,
    // (int) machine.getMaxInputEu() / 4,
    // stackTier,
    // true,
    // false);
    // }
    // }
    // }
    // }
    // } else {
    // if (aTimer % 200 == 0) {
    // connection = null;
    // tryFindConnection();
    // }
    // }
    // }
    // super.onPostTick(aBaseMetaTileEntity, aTimer);
    // }

    // @Override
    // public boolean isValidSlot(int aIndex) {
    // return aIndex < getSlots(this.mTier);
    // }
    //
    // public static int getSlots(int aTier) {
    // return (1 + aTier) * 16;
    // }

    // private MTEMultiBlockBase tryFindCoreGTMultiBlock() {
    // Queue<ChunkCoordinates> tQueue = new LinkedList<>();
    // Set<ChunkCoordinates> visited = new HashSet<>(80);
    // tQueue.add(
    // this.getBaseMetaTileEntity()
    // .getCoords());
    // World world = this.getBaseMetaTileEntity()
    // .getWorld();
    // while (!tQueue.isEmpty()) {
    // final ChunkCoordinates aCoords = tQueue.poll();
    // final TileEntity tTileEntity;
    //
    // tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
    //
    // if (tTileEntity instanceof IGregTechTileEntity te
    // && te.getMetaTileEntity() instanceof MTEMultiBlockBase mte)
    // if (mte.mInputBusses.contains(this)) return mte;
    //
    // if (visited.size() < 5 || (tTileEntity instanceof IMachineBlockUpdateable
    // && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive())) {
    // ChunkCoordinates tCoords;
    //
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ)))
    // tQueue.add(tCoords);
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ)))
    // tQueue.add(tCoords);
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ)))
    // tQueue.add(tCoords);
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ)))
    // tQueue.add(tCoords);
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1)))
    // tQueue.add(tCoords);
    // if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1)))
    // tQueue.add(tCoords);
    // }
    // }
    // return null;
    // }

    // private void tryFindConnection() {
    // MTEMultiBlockBase machine = tryFindCoreGTMultiBlock();
    // if (machine != null && machine.isValid()) {
    // this.machine = machine;
    // connection = new ConnectionMulti(machine);
    // connectionList.add(connection);
    // }
    // }
    //
    // private boolean hasConnection() {
    // if (connection == null) return false;
    // if (connection.isValid()) return true;
    // return connection.reCheckConnection();
    // }
    //
    // @Override
    // public void onRemoval() {
    // if (hasConnection()) connection.machine = null;
    // }
}

// class ConnectionMulti {
//
// MTEMultiBlockBase machine;
// ItemStack machineItem;
// ChunkCoordinates machineCoord;
//
// World world;
//
// public ConnectionMulti(MTEMultiBlockBase machine) {
// this.machine = machine;
// this.machineItem = machine.getStackForm(1);
// machineCoord = machine.getBaseMetaTileEntity()
// .getCoords();
// }
//
// public boolean reCheckConnection() {
// if (machine == null) this.machine = getLoadedGT_BaseMachineAt(machineCoord, world, true);
// return isValid();
// }
//
// public MTEMultiBlockBase getLoadedGT_BaseMachineAt(ChunkCoordinates coords, World world, boolean isLoaded) {
// TileEntity te = GTUtil.getTileEntity(world, coords, isLoaded);
// if (te == null) return null;
// return (MTEMultiBlockBase) ((IGregTechTileEntity) te).getMetaTileEntity();
// }
//
// public boolean isValid() {
// return machine != null && machine.isValid();
// }
// }
