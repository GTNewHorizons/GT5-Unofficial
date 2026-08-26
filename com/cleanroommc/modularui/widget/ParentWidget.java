package com.cleanroommc.modularui.widget;

import com.cleanroommc.modularui.api.widget.IParentWidget;
import com.cleanroommc.modularui.api.widget.IWidget;

/**
 * A widget which can hold any amount of children.
 *
 * @param <W> type of this widget
 */
public class ParentWidget<W extends ParentWidget<W>> extends AbstractParentWidget<IWidget, W> implements IParentWidget<IWidget, W> {

    public boolean addChild(IWidget child, int index) {
        return super.addChild(child, index);
    }

    @Override
    public boolean remove(IWidget child) {
        return super.remove(child);
    }

    @Override
    public boolean remove(int index) {
        return super.remove(index);
    }

    @Override
    public boolean removeAll() {
        return super.removeAll();
    }
}
