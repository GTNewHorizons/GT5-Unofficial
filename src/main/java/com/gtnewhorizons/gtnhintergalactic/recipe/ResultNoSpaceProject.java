package com.gtnewhorizons.gtnhintergalactic.recipe;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.recipe.check.CheckRecipeResult;

/**
 * Check recipe error that will be displayed when the user doesn't have the right space project
 *
 * @author minecraft7771
 */
public class ResultNoSpaceProject implements CheckRecipeResult {

    /** Max length to read from network buffer */
    private static final int MAX_STRING_LENGTH = 256;

    /** Project that is needed to run the recipe */
    private String neededProject;
    /** Location of the project that is needed to run the recipe */
    private String neededLocation;

    /**
     * Create new instance of this error
     *
     * @param neededProject  Project that is needed to run the recipe
     * @param neededLocation Location of the project that is needed to run the recipe
     */
    public ResultNoSpaceProject(String neededProject, String neededLocation) {
        this.neededProject = neededProject;
        this.neededLocation = neededLocation;
    }

    /**
     * @return Unique error ID
     */
    @Override
    public String getID() {
        return "missing_space_project";
    }

    /**
     * @return True if this is a sucess code, else false
     */
    @Override
    public boolean wasSuccessful() {
        return false;
    }

    /**
     * @return The translated error message
     */
    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.missing_project",
                StatCollector.translateToLocal(neededProject),
                StatCollector.translateToLocal(neededLocation));
    }

    /**
     * @return New instance of this error without configured parameters
     */
    @Override
    public CheckRecipeResult newInstance() {
        return new ResultNoSpaceProject("", "");
    }

    /**
     * Encode this error to a buffer
     *
     * @param buffer Buffer to encode to
     */
    @Override
    public void encode(PacketBuffer buffer) {
        NetworkUtils.writeStringSafe(buffer, neededLocation);
        NetworkUtils.writeStringSafe(buffer, neededProject);
    }

    /**
     * Decode this error from a buffer
     *
     * @param buffer Buffer to decode from
     */
    @Override
    public void decode(PacketBuffer buffer) {
        neededLocation = NetworkUtils.readStringSafe(buffer, MAX_STRING_LENGTH);
        neededProject = NetworkUtils.readStringSafe(buffer, MAX_STRING_LENGTH);
    }

    /**
     * Check if this error equals another
     *
     * @param o Object to be compared
     * @return True if the object are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultNoSpaceProject that = (ResultNoSpaceProject) o;
        return neededLocation.equals(that.neededLocation) && neededProject.equals(that.neededProject);
    }
}
