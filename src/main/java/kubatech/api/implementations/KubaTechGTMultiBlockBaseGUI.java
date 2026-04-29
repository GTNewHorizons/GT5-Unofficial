package kubatech.api.implementations;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kubatech.Tags;

public class KubaTechGTMultiBlockBaseGUI<T extends KubaTechGTMultiBlockBase<?>> extends MTEMultiBlockBaseGui<T> {

    public static final UITexture PICTURE_KUBATECH_LOGO = UITexture.builder()
        .location(Tags.MODID, "gui/logo_18")
        .fullImage()
        .build();

    public KubaTechGTMultiBlockBaseGUI(T multiblock) {
        super(multiblock);
    }

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new IDrawable.DrawableWidget(IDrawable.EMPTY).size(18)
            .marginTop(4)
            .background(PICTURE_KUBATECH_LOGO)
            .tooltip(
                t -> t.textColor(Color.GREY.main)
                    .addLine("[" + Tags.MODNAME + "]")
                    .addStringLines(Arrays.asList(multiblock.getCreditsText())));
    }
}
