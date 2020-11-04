package com.github.technus.tectech.mechanics.structure;

public enum StructureIterationType {
    SPAWN_HINTS, //only spawn hint particles
    BUILD_TEMPLATE, //only builds template
    CHECK, //checks the structure skipping all unloaded chunks (for machines that were validated already)
    CHECK_FULLY, //checks the structure failing on unloaded chunks (for machines that are not valid currently)
}
