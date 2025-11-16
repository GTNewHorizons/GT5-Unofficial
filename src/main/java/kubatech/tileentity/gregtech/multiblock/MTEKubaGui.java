package kubatech.tileentity.gregtech.multiblock;

import com.cleanroommc.modularui.api.drawable.IKey;
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
}
