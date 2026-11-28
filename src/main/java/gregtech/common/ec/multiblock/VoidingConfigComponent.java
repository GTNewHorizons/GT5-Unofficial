package gregtech.common.ec.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IProcessingLogicSetupComponent;
import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.enums.VoidingMode;
import gregtech.api.logic.ProcessingLogic;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.tileentities.machines.RecipeCheckReason;

public class VoidingConfigComponent extends BaseMultiblockComponent implements IVoidingConfigComponent,
    IProcessingLogicSetupComponent, ISavableComponent, IGuiProviderComponent, IGuiSyncableComponent {

    private VoidingMode voidingMode = VoidingMode.VOID_NONE;
    private Set<VoidingMode> allowedVoidingModes = VoidingMode.ALL_OPTIONS;

    protected static final String VOID_EXCESS_NBT_KEY = "voidExcess";
    protected static final String VOIDING_MODE_NBT_KEY = "voidingMode";

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return allowedVoidingModes;
    }

    public VoidingConfigComponent setAllowedVoidingModes(@NotNull Set<VoidingMode> allowedVoidingModes) {
        this.allowedVoidingModes = allowedVoidingModes;
        if (!allowedVoidingModes.contains(voidingMode)) {
            voidingMode = allowedVoidingModes.iterator()
                .next();
        }
        return this;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return voidingMode;
    }

    @Override
    public void setVoidingMode(@NotNull VoidingMode mode) {
        if (!allowedVoidingModes.contains(mode)) return;
        voidingMode = mode;
        var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
        if (recipeLogic != null) {
            recipeLogic.scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
        }
    }

    @Override
    public void setupProcessingLogic(ProcessingLogic logic) {
        logic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
    }

    @Override
    public @NotNull String getSaveKey() {
        return "voiding";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var nbt = new NBTTagCompound();
        nbt.setString(VOIDING_MODE_NBT_KEY, voidingMode.name);
        return nbt;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound nbt) {
        VoidingMode mode = null;
        if (nbt.hasKey(VOIDING_MODE_NBT_KEY, Constants.NBT.TAG_STRING)) {
            mode = VoidingMode.fromName(nbt.getString(VOIDING_MODE_NBT_KEY));
        } else if (nbt.hasKey(VOID_EXCESS_NBT_KEY)) {
            // backward compatibility
            mode = nbt.getBoolean(VOID_EXCESS_NBT_KEY) ? VoidingMode.VOID_ALL : VoidingMode.VOID_NONE;
        }
        if (mode != null && getAllowedVoidingModes().contains(mode)) {
            voidingMode = mode;
        }
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        loadLegacyNBTData(nbt);
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        EnumSyncValue<VoidingMode, ?> voidExcessSyncer = new EnumSyncValue<>(
            VoidingMode.class,
            this::getVoidingMode,
            this::setVoidingMode).allowC2S();
        syncManager.syncValue("voidExcess", voidExcessSyncer);
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.leftGap()
            .replace(LeftGapSection.VOIDING, (panel, syncManager) -> {
                @SuppressWarnings("unchecked")
                EnumSyncValue<VoidingMode, ?> voidExcessSyncer = syncManager
                    .findSyncHandler("voidExcess", EnumSyncValue.class);
                return new ButtonWidget<>().onMousePressed(mouseButton -> {
                    Set<VoidingMode> allowed = getAllowedVoidingModes();
                    VoidingMode voidingMode = VoidingMode.VOID_NONE;
                    switch (mouseButton) {
                        case 0 -> voidingMode = getVoidingMode().nextInCollection(allowed);
                        case 1 -> voidingMode = getVoidingMode().previousInCollection(allowed);
                    }
                    voidExcessSyncer.setValue(voidingMode);
                    return true;
                })
                    .overlay(new DynamicDrawable(() -> getVoidingMode().buttonOverlay))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .tooltipBuilder(
                        (t) -> t
                            .addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.voiding_mode")))
                            .addLine(
                                IKey.dynamic(() -> StatCollector.translateToLocal(getVoidingMode().getTransKey()))));
            });
    }
}
