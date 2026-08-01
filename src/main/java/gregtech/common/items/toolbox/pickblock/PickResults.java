package gregtech.common.items.toolbox.pickblock;

import java.util.List;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ToolboxSlot;

/**
 * Thin wrapper over a list that allows the {@link gregtech.common.items.toolbox.ToolboxPickBlockDecider} to suggest
 * deselecting a tool entirely, or provide a list of tools.
 *
 * @param forceDeselect     Set to true to make the toolbox deselect a tool
 * @param suggestedTools    A list of tools to suggest using. Items at the beginning of the list have higher priority
 * @param packedCoordinates A long packed with the coordinates for the block being clicked.
 */
@Desugar
public record PickResults(boolean forceDeselect, List<ToolboxSlot> suggestedTools, long packedCoordinates) {

    public PickResults(final List<ToolboxSlot> suggestedTools, long packedCoordinates) {
        this(false, suggestedTools, packedCoordinates);
    }

    public PickResults(final ToolboxSlot slot, long packedCoordinates) {
        this(false, ImmutableList.of(slot), packedCoordinates);
    }

    public PickResults(final boolean forceDeselect, long packedCoordinates) {
        this(forceDeselect, ImmutableList.of(), packedCoordinates);
    }

    public PickResults(final boolean forceDeselect) {
        this(forceDeselect, ImmutableList.of(), 0L);
    }
}
