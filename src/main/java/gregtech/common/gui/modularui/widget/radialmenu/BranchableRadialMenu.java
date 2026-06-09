package gregtech.common.gui.modularui.widget.radialmenu;

import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuBuilder.RadialMenuOptionBuilderBranch;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenuBuilder.RadialMenuOptionBuilderLeaf;

public interface BranchableRadialMenu {

    RadialMenuOptionBuilderLeaf<?> option();

    RadialMenuOptionBuilderBranch<?> branch();
}
