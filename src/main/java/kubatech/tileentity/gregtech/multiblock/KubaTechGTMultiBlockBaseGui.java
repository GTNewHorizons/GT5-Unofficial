package kubatech.tileentity.gregtech.multiblock;

import static com.cleanroommc.modularui.drawable.UITexture.fullImage;

import java.util.function.Supplier;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kubatech.Tags;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

public abstract class KubaTechGTMultiBlockBaseGui<T extends KubaTechGTMultiBlockBase<T>>
    extends MTEMultiBlockBaseGui<T> {

    public final String KUBA_RUNNING = "kuba_running";
    public final BooleanSyncValue isRunning = new BooleanSyncValue(() -> multiblock.mMaxProgresstime > 0);

    public KubaTechGTMultiBlockBaseGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(KUBA_RUNNING, isRunning);
    }

    public class KubaCycleButtonWidget extends CycleButtonWidget {

        public KubaCycleButtonWidget(Supplier<IDrawable> supplier) {
            IDrawable baseOverlay = new DynamicDrawable(supplier);
            this.overlay(new DynamicDrawable(() -> {
                if (isLocked()) return new DrawableStack(baseOverlay, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
                return baseOverlay;
            }));
        }

        public boolean isLocked() {
            return isRunning.getValue();
        }

        public @NotNull Result onMousePressed(int mouseButton) {
            if (isLocked()) return Result.IGNORE;
            return super.onMousePressed(mouseButton);
        }

        private IKey getDynamicFromI18nKey(String key) {
            return IKey.dynamic(() -> StatCollector.translateToLocal(key));
        }

        protected void addTranslatedLockableTooltips(RichTooltip t, String... key) {
            t.addLine(getDynamicFromI18nKey(key[0]));
            if (isLocked()) t.addLine("GT5U.gui.button.forbidden_while_running");
            // t.addLine(getTranslationMode());
            for (int i = 1; i < key.length; i++) {
                t.addLine(getDynamicFromI18nKey(key[i]));
            }
        }
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalRightCornerColumn(panel, syncManager).child(
            new DynamicDrawable(() -> UITexture.fullImage(kubatech.Tags.MODID, "gui/logo_13x15_dark")).asWidget()
                .size(18, 18)
                .marginTop(4));
    }

    public static UITexture getKubaUITexture(String name) {
        return fullImage(Tags.MODID, name);
    }

    public static UITexture getKubaUITextureAlpha(String name) {
        return UITexture.builder()
            .location(Tags.MODID, name)
            .nonOpaque()
            .fullImage()
            .build();
    }
}
