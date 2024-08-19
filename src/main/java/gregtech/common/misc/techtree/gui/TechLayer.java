package gregtech.common.misc.techtree.gui;

import java.util.ArrayList;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.layout.Column;

import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechLayer extends Column {

    private final ArrayList<ITechnology> containedTechs = new ArrayList<>();

    public void addTechnology(ITechnology tech, IWidget widget) {
        containedTechs.add(tech);
        child(widget);
    }

    public ArrayList<ITechnology> getTechnologies() {
        return containedTechs;
    }
}
