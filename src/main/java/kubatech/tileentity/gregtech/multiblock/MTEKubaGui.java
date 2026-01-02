package kubatech.tileentity.gregtech.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kubatech.Tags;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.tileentity.gregtech.multiblock.modularui2.LockableCycleButtonWidget;

import static com.cleanroommc.modularui.drawable.UITexture.fullImage;

public abstract class MTEKubaGui<T extends KubaTechGTMultiBlockBase<T>> extends MTEMultiBlockBaseGui<T> {

    public MTEKubaGui(T multiblock) {
        super(multiblock);
    }

    public class KubaCycleButtonWidget extends LockableCycleButtonWidget {

        @Override
        public boolean isLocked() {
            return multiblock.mMaxProgresstime > 0;
        }

        protected IKey getTranslationMode() {
            return IKey.dynamic(() -> {
                if (isLocked()) return StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running");
                // else return StatCollector.translateToLocal("GT5U.gui.button.mode_switch");
                else return "";
            });
        }

        private IKey getDynamicFromI18nKey(String key) {
            return IKey.dynamic(() -> StatCollector.translateToLocal(key));
        }

        protected void addTranslatedLockableTooltips(RichTooltip t, String... key) {
            t.addLine(getDynamicFromI18nKey(key[0]));
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

    public static UITexture getKubaUITexture16x16(String name) {
        return fullImage(Tags.MODID, name);
    }
}
