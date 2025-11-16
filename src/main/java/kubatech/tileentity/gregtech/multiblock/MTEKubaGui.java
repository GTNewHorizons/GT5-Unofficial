package kubatech.tileentity.gregtech.multiblock;

import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.HoverableIcon;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.tileentity.gregtech.multiblock.modularui2.LockableCycleButtonWidget;
import net.minecraft.util.StatCollector;

public abstract class MTEKubaGui<T extends KubaTechGTMultiBlockBase<T>> extends MTEMultiBlockBaseGui<T>{
    public MTEKubaGui (T multiblock) {
        super(multiblock);
    }

    public class KubaCycleButtonWidget extends LockableCycleButtonWidget {
        @Override
        public boolean isLocked() {
            return multiblock.mProgresstime > 0;
        }

        protected IKey getTranslationMode(){
            return IKey.dynamic(() -> {
                if (isLocked()) return StatCollector.translateToLocal("kubatech.gui.text.running_button");
                else return StatCollector.translateToLocal("GT5U.gui.button.mode_switch");
            });
        }
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalRightCornerColumn(panel, syncManager)
            .child(
                new DynamicDrawable(() -> UITexture.fullImage(kubatech.Tags.MODID,"gui/logo_13x15_dark"))
                    .asWidget()
                    .size(18, 18)
                    .marginTop(4)
            );
    }
}
