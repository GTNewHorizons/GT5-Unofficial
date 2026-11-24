package gregtech.common.ec.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.interfaces.tileentity.IGregTechDeviceInformation.encode;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IOnMachineStopComponent;
import gregtech.api.ec.component.IOnRecipeFinishComponent;
import gregtech.api.ec.component.IOnRecipeIdleComponent;
import gregtech.api.ec.component.IOnRecipeStartComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IRecipeEfficiencyLogicComponent;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;

public class RecipeEfficiencyLogicComponent extends BaseMultiblockComponent implements IRecipeEfficiencyLogicComponent,
    IOnRecipeStartComponent, IOnRecipeFinishComponent, IOnRecipeIdleComponent, ISavableComponent,
    IOnMachineStopComponent, IScannerInfoComponent, IWailaDataComponent, IGuiProviderComponent, IGuiSyncableComponent {

    private boolean instantMaxEfficiencyOnStart = true;
    private @NotNull ToIntFunction<IRecipeLogicComponent> increaseCalculator = _ -> 10000;

    public RecipeEfficiencyLogicComponent setRampUpEfficiencyPerRecipe(int efficiencyIncreate) {
        this.instantMaxEfficiencyOnStart = false;
        this.increaseCalculator = _ -> efficiencyIncreate;
        return this;
    }

    public RecipeEfficiencyLogicComponent setRampUpEfficiencyPerRecipe(
        @NotNull ToIntFunction<IRecipeLogicComponent> increaseCalculator) {
        this.instantMaxEfficiencyOnStart = false;
        this.increaseCalculator = increaseCalculator;
        return this;
    }

    @Override
    public void onRecipeFinish(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        mEfficiency = Math.clamp(mEfficiency + mEfficiencyIncrease, 0, getEffectiveMaxEfficiency());
        mEfficiencyIncrease = 0;
    }

    @Override
    public int getRecipeFinishPriority() {
        return RecipeFinishPriority.EFFICIENCY.priority;
    }

    @Override
    public void onRecipeIdle(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        if (mEfficiency > 0) {
            mEfficiency = Math.max(0, mEfficiency - mEfficiencyDecrease);
        }
    }

    @Override
    public int getRecipeIdlePriority() {
        return RecipeIdlePriority.EFFICIENCY.priority;
    }

    @Override
    public void onRecipeStart(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        if (instantMaxEfficiencyOnStart) {
            mEfficiency = getEffectiveMaxEfficiency();
            mEfficiencyIncrease = getBaseMaxEfficiency();
        } else {
            mEfficiencyIncrease = increaseCalculator.applyAsInt(recipeLogicComponent);
        }
    }

    @Override
    public int getRecipeStartPriority() {
        return RecipeStartPriority.EFFICIENCY.priority;
    }

    @Override
    public void onMachineStop(@NotNull ShutDownReason reason) {
        mEfficiency = 0;
        mEfficiencyIncrease = 0;
    }

    @Override
    public void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.addIf(
            mEfficiency > 0,
            Section.EFFICIENCY,
            data -> { data.add(encode("GT5U.multiblock.scanner.efficiency", formatNumber(mEfficiency / 100.0F))); });
    }

    @Override
    public void appendInfoMap(@NotNull Map<String, String> map) {
        map.put("energyEfficiency", Double.toString(mEfficiency / 10_000F));
    }

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        tag.setFloat("efficiency", mEfficiency / 100.0F);
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        if (showInTerminal) {
            syncManager.syncValue("efficiency", new IntSyncValue(this::getEfficiency));
        }
    }

    @Override
    public void configureGui(@NotNull MultiblockGuiBuilder builder) {
        builder.terminalTexts()
            .addIf(showInTerminal, TerminalSection.EFFICIENCY, (panel, syncManager) -> {
                IntSyncValue efficiency = syncManager.findSyncHandler("efficiency", IntSyncValue.class);
                return IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                        + ": "
                        + formatNumber(efficiency.getValue() / 100D)
                        + "%"
                // enabled if active
                )
                    .asWidget()
                    .setEnabledIf(
                        (w) -> getMachine().getBaseMetaTileEntity()
                            .isActive());
            });
    }

    @FunctionalInterface
    public interface IMaxEfficiencyCalculator {

        @Range(from = 0, to = 10000)
        int getMaxEfficiency(@Nullable ItemStack aStack);
    }

    private IMaxEfficiencyCalculator calculator = stack -> 10000;
    private int mEfficiency = 0;
    private int mEfficiencyIncrease = 0;
    private int mEfficiencyDecrease = 1000;
    private boolean showInTerminal = false;

    public RecipeEfficiencyLogicComponent setShowInTerminal(boolean showInTerminal) {
        this.showInTerminal = showInTerminal;
        return this;
    }

    @Override
    public int getEfficiency() {
        return mEfficiency;
    }

    @Override
    public RecipeEfficiencyLogicComponent setEfficiency(int efficiency) {
        mEfficiency = Math.max(0, efficiency);
        return this;
    }

    @Override
    public int getBaseMaxEfficiency() {
        return calculator.getMaxEfficiency(getMachine().getControllerSlot());
    }

    @Override
    public int getEffectiveMaxEfficiency() {
        int max = calculator.getMaxEfficiency(getMachine().getControllerSlot());
        if (maintenanceLogic != null) {
            max -= maintenanceLogic.getMaintenanceEfficiencyPenalty();
        }
        return Math.max(0, max);
    }

    public RecipeEfficiencyLogicComponent setMaxEfficiencyCalculator(IMaxEfficiencyCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    @Override
    public RecipeEfficiencyLogicComponent setEfficiencyDecrease(int mEfficiencyDecrease) {
        this.mEfficiencyDecrease = mEfficiencyDecrease;
        return this;
    }

    @Override
    public RecipeEfficiencyLogicComponent setEfficiencyIncrease(int efficiencyIncrease) {
        this.mEfficiencyIncrease = efficiencyIncrease;
        return this;
    }

    @Override
    public @NotNull String getSaveKey() {
        return "eff";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var aNBT = new NBTTagCompound();
        aNBT.setInteger("mEfficiencyIncrease", mEfficiencyIncrease);
        aNBT.setInteger("mEfficiency", mEfficiency);
        return aNBT;
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound aNBT) {
        loadLegacyNBTData(aNBT);
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound aNBT) {
        mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
        mEfficiency = aNBT.getInteger("mEfficiency");
    }

    private @Nullable IMaintenanceLogicComponent maintenanceLogic;

    @Override
    public void onComponentsReady() {
        maintenanceLogic = tryGetComponent(IMaintenanceLogicComponent.KEY);
    }
}
