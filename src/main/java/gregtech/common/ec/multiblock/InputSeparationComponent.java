package gregtech.common.ec.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IOnScrewdriverClickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IInputSeparationComponent;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockECBaseGui;
import gregtech.common.tileentities.machines.RecipeCheckReason;

public class InputSeparationComponent extends BaseMultiblockComponent implements IInputSeparationComponent,
    ISavableComponent, IOnScrewdriverClickComponent, IGuiSyncableComponent, IGuiProviderComponent {

    protected static final String INPUT_SEPARATION_NBT_KEY = "inputSeparation";
    private boolean inputSeparation = true;
    private boolean allowScrewdriverToggle = false;

    @Override
    public boolean isInputSeparationEnabled() {
        return inputSeparation;
    }

    @Override
    public InputSeparationComponent setInputSeparation(boolean enabled) {
        inputSeparation = enabled;
        var recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
        if (recipeLogic != null) {
            recipeLogic.scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
        }
        return this;
    }

    public InputSeparationComponent setAllowScrewdriverToggle(boolean allow) {
        allowScrewdriverToggle = allow;
        return this;
    }

    @Override
    public @NotNull String getSaveKey() {
        return "inSep";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean(INPUT_SEPARATION_NBT_KEY, inputSeparation);
        return nbt;
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound nbt) {
        if (nbt.hasKey(INPUT_SEPARATION_NBT_KEY)) inputSeparation = nbt.getBoolean(INPUT_SEPARATION_NBT_KEY);
        else if (nbt.hasKey("isBussesSeparate")) inputSeparation = nbt.getBoolean("isBussesSeparate");
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        inputSeparation = nbt.getBoolean(INPUT_SEPARATION_NBT_KEY);
    }

    @Override
    public boolean onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!allowScrewdriverToggle) return false;
        setInputSeparation(!isInputSeparationEnabled());
        GTUtility.sendChatTrans(
            aPlayer,
            inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
        return true;
    }

    @Override
    public int getOnScrewdriverClickPriority() {
        return OnScrewdriverClickPriority.INPUT_SEPARATION.priority;
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        BooleanSyncValue inputSeparationSyncer = new BooleanSyncValue(
            this::isInputSeparationEnabled,
            this::setInputSeparation).allowC2S();
        syncManager.syncValue("inputSeparation", inputSeparationSyncer);
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.leftGap()
            .replace(
                LeftGapSection.INPUT_SEPARATION,
                (panel, syncManager) -> MTEMultiBlockECBaseGui.createActiveFeatureToggleButton(
                    syncManager.findSyncHandler("inputSeparation", BooleanSyncValue.class),
                    GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF,
                    GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON,
                    "GT5U.gui.button.input_separation",
                    this::isInputSeparationEnabled));
    }
}
