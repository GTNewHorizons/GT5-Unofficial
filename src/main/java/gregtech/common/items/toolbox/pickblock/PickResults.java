package gregtech.common.items.toolbox.pickblock;

import java.util.List;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ToolboxSlot;

/**
 * Thin wrapper over a list that allows the {@link gregtech.common.items.toolbox.ToolboxPickBlockDecider} to suggest
 * deselecting a tool entirely, or provide a list of tools.
 *
 * @param forceDeselect  Set to true to make the toolbox deselect a tool
 * @param suggestedTools A list of tools to suggest using. Items at the beginning of the list have higher priority
 */
@Desugar
public record PickResults(boolean forceDeselect, List<ToolboxSlot> suggestedTools) {

    public PickResults(final List<ToolboxSlot> suggestedTools) {
        this(false, suggestedTools);
    }

    public PickResults(final ToolboxSlot slot) {
        this(false, ImmutableList.of(slot));
    }

    public PickResults(final boolean forceDeselect) {
        this(forceDeselect, ImmutableList.of());
    }
}
