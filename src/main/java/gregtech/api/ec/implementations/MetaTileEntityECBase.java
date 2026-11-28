package gregtech.api.ec.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentContainer;
import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.IECMachine;
import gregtech.api.ec.component.IActivitySoundLoopComponent;
import gregtech.api.ec.component.IClientSoundComponent;
import gregtech.api.ec.component.IOnActiveStateChangeComponent;
import gregtech.api.ec.component.IOnClientPostTickComponent;
import gregtech.api.ec.component.IOnFirstTickComponent;
import gregtech.api.ec.component.IOnMachineRemovalComponent;
import gregtech.api.ec.component.IOnMachineUnloadComponent;
import gregtech.api.ec.component.IOnScrewdriverClickComponent;
import gregtech.api.ec.component.IOnServerPostTickComponent;
import gregtech.api.ec.component.IOnServerTickFailComponent;
import gregtech.api.ec.component.IOnSolderingToolClickComponent;
import gregtech.api.ec.component.IOnWireCutterClickComponent;
import gregtech.api.ec.component.IOnWorkStateChangeComponent;
import gregtech.api.ec.component.IOnWrenchClickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public abstract class MetaTileEntityECBase extends MetaTileEntity implements IECMachine {

    public MetaTileEntityECBase(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        super(aID, aBasicName, aRegionalName, aInvSlotCount);
    }

    public MetaTileEntityECBase(String aName, int aInvSlotCount) {
        super(aName, aInvSlotCount);
    }

    private final ComponentContainer container = new ComponentContainer(this, this::onComponentsReady);

    protected final void ensureComponentsInitialized() {
        if (!container.isInitialized()) initComponents();
    }

    protected void initComponents() {
        container.initialize(this::registerComponents);
    }

    protected abstract void registerComponents();

    private boolean needsClientTick = false;

    protected IOnServerPostTickComponent[] serverPostTicks = IOnServerPostTickComponent.KEY.emptyArray;
    protected IOnClientPostTickComponent[] clientPostTicks = IOnClientPostTickComponent.KEY.emptyArray;
    protected IOnActiveStateChangeComponent[] activeStateChanges = IOnActiveStateChangeComponent.KEY.emptyArray;

    protected void onComponentsReady() {
        serverPostTicks = getComponents(IOnServerPostTickComponent.KEY);
        clientPostTicks = getComponents(IOnClientPostTickComponent.KEY);
        activeStateChanges = getComponents(IOnActiveStateChangeComponent.KEY);
        if (container.isReady()) {
            boolean shouldClientTick = clientPostTicks.length > 0;
            if (needsClientTick != shouldClientTick) {
                needsClientTick = shouldClientTick;
                var igte = getBaseMetaTileEntity();
                if (igte != null && igte.isClientSide()) {
                    // update cached mNeedsClientTick
                    igte.setMetaTileEntity(this);
                    if (needsClientTick) {
                        igte.enableTicking();
                    }
                }
            }
        }
    }

    @Override
    public final IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        var instance = createNewMetaEntity(aTileEntity);
        instance.initComponents();
        return instance;
    }

    protected abstract @NotNull MetaTileEntityECBase createNewMetaEntity(IGregTechTileEntity aTileEntity);

    @SuppressWarnings("unchecked")
    @Override
    public <C extends IComponent> @Nullable C tryGetComponent(@NotNull ComponentKey.UniqueKey<C> key) {
        ensureComponentsInitialized();
        return container.tryGetComponent(key);
    }

    @Override
    public <C extends IComponent> boolean hasComponent(@NotNull ComponentKey<C> key) {
        ensureComponentsInitialized();
        return container.hasComponent(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends IComponent> @NotNull C[] getComponents(@NotNull ComponentKey.ListKey<C> key) {
        ensureComponentsInitialized();
        return container.getComponents(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends IComponent> IECMachine addComponent(@NotNull C component) {
        ensureComponentsInitialized();
        container.addComponent(component);
        return this;
    }

    @Override
    public <C extends IComponent> boolean removeComponent(@NotNull C component) {
        if (!container.isInitialized()) return false;
        return container.removeComponent(component);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends IComponent> boolean removeComponent(@NotNull ComponentKey.ListKey<C> key) {
        if (!container.isInitialized()) return false;
        return container.removeComponent(key);
    }

    @Override
    public final boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        for (var comp : getComponents(IOnWrenchClickComponent.KEY)) {
            if (comp.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool)) return true;
        }
        return onUnhandledWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    protected boolean onUnhandledWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        for (var comp : getComponents(IOnScrewdriverClickComponent.KEY)) {
            if (comp.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool)) return;
        }
        onUnhandledScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    protected void onUnhandledScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY,
        float aZ, ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public final boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        for (var comp : getComponents(IOnWireCutterClickComponent.KEY)) {
            if (comp.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool)) return true;
        }
        return onUnhandledWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    protected boolean onUnhandledWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer aPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        return super.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public final boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer aPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        for (var comp : getComponents(IOnSolderingToolClickComponent.KEY)) {
            if (comp.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool)) return true;
        }
        return onUnhandledSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    protected boolean onUnhandledSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer aPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        return super.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean needsClientTick() {
        return needsClientTick;
    }

    @Override
    public int getProgresstime() {
        var comp = tryGetComponent(IRecipeLogicComponent.KEY);
        return comp != null ? comp.getProgressTime() : 0;
    }

    @Override
    public int maxProgresstime() {
        var comp = tryGetComponent(IRecipeLogicComponent.KEY);
        return comp != null ? comp.getMaxProgressTime() : 0;
    }

    @Override
    public int increaseProgress(int aProgress) {
        return aProgress;
    }

    @Override
    public void onDisableWorking() {
        for (var component : getComponents(IOnWorkStateChangeComponent.KEY)) {
            component.onDisableWorking();
        }
    }

    @Override
    public void onEnableWorking() {
        for (var component : getComponents(IOnWorkStateChangeComponent.KEY)) {
            component.onEnableWorking();
        }
    }

    @Override
    public final void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        for (var component : getComponents(IClientSoundComponent.KEY)) {
            if (component.onDoSound(aIndex, aX, aY, aZ)) return;
        }
        onUnhandledDoSound(aIndex, aX, aY, aZ);
    }

    protected void onUnhandledDoSound(byte aIndex, double aX, double aY, double aZ) {

    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        for (var component : getComponents(IClientSoundComponent.KEY)) {
            component.onStartSoundLoop(aIndex, aX, aY, aZ);
        }
    }

    private boolean lastActive = false;

    private void updateActiveState(@NotNull IGregTechTileEntity igte, boolean active) {
        if (lastActive == active) return;
        lastActive = active;
        if (active) {
            for (int i = 0; i < activeStateChanges.length; i++) {
                activeStateChanges[i].onMachineActivated(igte);
            }
        } else {
            for (int i = 0; i < activeStateChanges.length; i++) {
                activeStateChanges[i].onMachineDeactivated(igte);
            }
        }
    }

    @Override
    public void onSetActive(boolean active) {
        super.onSetActive(active);
        var igte = getBaseMetaTileEntity();
        if (igte != null && igte.isServerSide()) {
            updateActiveState(igte, active);
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        for (var comp : getComponents(IOnFirstTickComponent.KEY)) {
            comp.onFirstTick(aBaseMetaTileEntity);
        }
    }

    @Override
    public void onClientSoundStateChanged() {
        var activitySoundLoop = tryGetComponent(IActivitySoundLoopComponent.KEY);
        if (activitySoundLoop != null) activitySoundLoop.startActivitySound();
    }

    @Override
    public void onTickFail(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onTickFail(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.disableWorking();
            for (var component : getComponents(IOnServerTickFailComponent.KEY)) {
                component.onServerTickFail(aBaseMetaTileEntity, aTick);
            }
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (var component : getComponents(IOnMachineRemovalComponent.KEY)) {
            component.onMachineRemoval();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        for (var component : getComponents(IOnMachineUnloadComponent.KEY)) {
            component.onMachineUnload();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for (var component : getComponents(ISavableComponent.KEY)) {
            component.saveNBTData(aNBT);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (var component : getComponents(ISavableComponent.KEY)) {
            component.loadNBTData(aNBT);
        }
        var igte = getBaseMetaTileEntity();
        if (igte != null) {
            lastActive = igte.isActive();
        }
    }

    @Override
    public int getRandomNumber(int aRange) {
        return getBaseMetaTileEntity().getRandomNumber(aRange);
    }

    @Override
    public void disableWorking() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.disableWorking();
        }
    }

    @Override
    public void enableWorking() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.enableWorking();
        }
    }

    @Override
    public boolean isActive() {
        var igte = getBaseMetaTileEntity();
        if (igte == null) return false;
        return igte.isActive();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            for (int i = 0; i < serverPostTicks.length; i++) {
                serverPostTicks[i].onServerPostTick(aBaseMetaTileEntity, aTick);
            }
        } else if (clientPostTicks.length == 0) {
            aBaseMetaTileEntity.tryDisableTicking();
        } else {
            for (int i = 0; i < clientPostTicks.length; i++) {
                clientPostTicks[i].onClientPostTick(aBaseMetaTileEntity, aTick);
            }
        }
    }

}
