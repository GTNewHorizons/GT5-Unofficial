package gregtech.api.enums;

public class MaterialIconRegistry {

    public enum TextureType {
        BLOCK,
        BLOCK_WITH_ALPHA,
        ITEM,
    }

    /**
     * Order of these entries must remain intact,
     * Either change existing entries, VOID entries
     * or add new ones at the end.
     */
    public enum IconType {

        DUST_TINY("/dustTiny", TextureType.ITEM),
        DUST_SMALL("/dustSmall", TextureType.ITEM),
        DUST("/dust", TextureType.ITEM),
        DUST_IMPURE("/dustImpure", TextureType.ITEM),
        DUST_PURE("/dustPure", TextureType.ITEM),
        CRUSHED("/crushed", TextureType.ITEM),
        CRUSHED_PURIFIED("/crushedPurified", TextureType.ITEM),
        CRUSHED_CENTRIFUGED("/crushedCentrifuged", TextureType.ITEM),
        GEM("/gem", TextureType.ITEM),
        NUGGET("/nugget", TextureType.ITEM),
        CASING_SMALL("/casingSmall", TextureType.ITEM),
        INGOT("/ingot", TextureType.ITEM),
        INGOT_HOT("/ingotHot", TextureType.ITEM),

        VOID_01("/void", TextureType.ITEM),
        VOID_02("/void", TextureType.ITEM),
        VOID_03("/void", TextureType.ITEM),
        VOID_04("/void", TextureType.ITEM),

        PLATE("/plate", TextureType.ITEM),
        PLATE_DOUBLE("/plateDouble", TextureType.ITEM),
        PLATE_TRIPLE("/plateTriple", TextureType.ITEM),
        PLATE_QUADRUPLE("/plateQuadruple", TextureType.ITEM),
        PLATE_QUINTUPLE("/plateQuintuple", TextureType.ITEM),
        PLATE_DENSE("/plateDense", TextureType.ITEM),

        STICK("/stick", TextureType.ITEM),
        LENS("/lens", TextureType.ITEM),
        ROUND("/round", TextureType.ITEM),
        BOLT("/bolt", TextureType.ITEM),
        SCREW("/screw", TextureType.ITEM),
        RING("/ring", TextureType.ITEM),
        FOIL("/foil", TextureType.ITEM),
        CELL("/cell", TextureType.ITEM),
        CELL_PLASMA("/cellPlasma", TextureType.ITEM),

        VOID_05("/void", TextureType.ITEM),
        VOID_06("/void", TextureType.ITEM),
        VOID_07("/void", TextureType.ITEM),
        VOID_08("/void", TextureType.ITEM),
        VOID_09("/void", TextureType.ITEM),

        TOOL_HEAD_HAMMER("/toolHeadHammer", TextureType.ITEM),
        TOOL_HEAD_FILE("/toolHeadFile", TextureType.ITEM),
        TOOL_HEAD_SAW("/toolHeadSaw", TextureType.ITEM),
        TOOL_HEAD_DRILL("/toolHeadDrill", TextureType.ITEM),
        TOOL_HEAD_CHAINSAW("/toolHeadChainsaw", TextureType.ITEM),
        TOOL_HEAD_WRENCH("/toolHeadWrench", TextureType.ITEM),

        VOID_10("/void", TextureType.ITEM),
        VOID_11("/void", TextureType.ITEM),
        VOID_12("/void", TextureType.ITEM),
        VOID_13("/void", TextureType.ITEM),

        TOOL_HEAD_SCREWDRIVER("/toolHeadScrewdriver", TextureType.ITEM),
        TOOL_HEAD_BUZZSAW("/toolHeadBuzzSaw", TextureType.ITEM),
        TOOL_HEAD_SOLDERING("/toolHeadSoldering", TextureType.ITEM),

        NANITES("/nanites", TextureType.ITEM),
        WIRE_FINE("/wireFine", TextureType.ITEM),
        GEAR_GT_SMALL("/gearGtSmall", TextureType.ITEM),
        ROTOR("/rotor", TextureType.ITEM),
        STICK_LONG("/stickLong", TextureType.ITEM),
        SPRING_SMALL("/springSmall", TextureType.ITEM),
        SPRING("/spring", TextureType.ITEM),
        ARROW_GT_WOOD("/arrowGtWood", TextureType.ITEM),
        ARROW_GT_PLASTIC("/arrowGtPlastic", TextureType.ITEM),

        GEM_CHIPPED("/gemChipped", TextureType.ITEM),
        GEM_FLAWED("/gemFlawed", TextureType.ITEM),
        GEM_FLAWLESS("/gemFlawless", TextureType.ITEM),
        GEM_EXQUISITE("/gemExquisite", TextureType.ITEM),

        GEAR_GT("/gearGt", TextureType.ITEM),
        ORE_RAW("/oreRaw", TextureType.ITEM),

        VOID_14("/void", TextureType.BLOCK),
        VOID_15("/void", TextureType.BLOCK),

        ORE_SMALL("/oreSmall", TextureType.BLOCK_WITH_ALPHA),
        ORE("/ore", TextureType.BLOCK_WITH_ALPHA),
        WIRE("/wire", TextureType.BLOCK),
        FOIL_2("/foil", TextureType.BLOCK),

        BLOCK1("/block1", TextureType.BLOCK),
        BLOCK2("/block2", TextureType.BLOCK),
        BLOCK3("/block3", TextureType.BLOCK),
        BLOCK4("/block4", TextureType.BLOCK),
        BLOCK5("/block5", TextureType.BLOCK),
        BLOCK6("/block6", TextureType.BLOCK),

        PIPE_SIDE("/pipeSide", TextureType.BLOCK),
        PIPE_TINY("/pipeTiny", TextureType.BLOCK),
        PIPE_SMALL("/pipeSmall", TextureType.BLOCK),
        PIPE_MEDIUM("/pipeMedium", TextureType.BLOCK),
        PIPE_LARGE("/pipeLarge", TextureType.BLOCK),
        PIPE_HUGE("/pipeHuge", TextureType.BLOCK),

        FRAME_GT("/frameGt", TextureType.BLOCK),
        PIPE_QUADRUPLE("/pipeQuadruple", TextureType.BLOCK),
        PIPE_NONUPLE("/pipeNonuple", TextureType.BLOCK),

        SHEETMETAL("/sheetmetal", TextureType.BLOCK),

        VOID_16("/void", TextureType.BLOCK),
        VOID_17("/void", TextureType.BLOCK),
        VOID_18("/void", TextureType.BLOCK),
        VOID_19("/void", TextureType.BLOCK),
        VOID_20("/void", TextureType.BLOCK),
        VOID_21("/void", TextureType.BLOCK),
        VOID_22("/void", TextureType.BLOCK),
        VOID_23("/void", TextureType.BLOCK),
        VOID_24("/void", TextureType.BLOCK),

        CRATE_GT_DUST("/crateGtDust", TextureType.ITEM),
        CRATE_GT_INGOT("/crateGtIngot", TextureType.ITEM),
        CRATE_GT_GEM("/crateGtGem", TextureType.ITEM),
        CRATE_GT_PLATE("/crateGtPlate", TextureType.ITEM),

        TURBINE_BLADE("/turbineBlade", TextureType.ITEM),

        VOID_25("/void", TextureType.ITEM),
        VOID_26("/void", TextureType.ITEM),
        VOID_27("/void", TextureType.ITEM),
        VOID_28("/void", TextureType.ITEM),
        VOID_29("/void", TextureType.ITEM),
        VOID_30("/void", TextureType.ITEM),
        VOID_31("/void", TextureType.ITEM),
        VOID_32("/void", TextureType.ITEM),
        VOID_33("/void", TextureType.ITEM),
        VOID_34("/void", TextureType.ITEM),
        VOID_35("/void", TextureType.ITEM),
        VOID_36("/void", TextureType.ITEM),
        VOID_37("/void", TextureType.ITEM),
        VOID_38("/void", TextureType.ITEM),
        VOID_39("/void", TextureType.ITEM),
        VOID_40("/void", TextureType.ITEM),
        VOID_41("/void", TextureType.ITEM),
        VOID_42("/void", TextureType.ITEM),
        VOID_43("/void", TextureType.ITEM),
        VOID_44("/void", TextureType.ITEM),
        VOID_45("/void", TextureType.ITEM),
        VOID_46("/void", TextureType.ITEM),
        VOID_47("/void", TextureType.ITEM),
        VOID_48("/void", TextureType.ITEM),

        PLATE_SUPERDENSE("/plateSuperdense", TextureType.ITEM),
        HANDLE_MALLET("/handleMallet", TextureType.ITEM),
        TOOL_HEAD_MALLET("/toolHeadMallet", TextureType.ITEM),
        TOOL_TURBINE("/toolTurbine", TextureType.ITEM),
        TOOL_WRENCH("/toolWrench", TextureType.ITEM),
        TOOL_CROWBAR("/toolCrowbar", TextureType.ITEM),
        TOOL_WIRE_CUTTER("/toolWireCutter", TextureType.ITEM),
        TOOL_SCOOP("/toolScoop", TextureType.ITEM),
        TOOL_BRANCH_CUTTER("/toolBranchCutter", TextureType.ITEM),
        TOOL_KNIFE("/toolKnife", TextureType.ITEM),
        TOOL_KNIFE_BUTCHERY("/toolKnifeButchery", TextureType.ITEM),
        TOOL_PLUNGER("/toolPlunger", TextureType.ITEM),
        TOOL_JACKHAMMER("/toolJackHammer", TextureType.ITEM),

        POCKET_MULTI_TOOL_CLOSED("/pocketMultiToolClosed", TextureType.ITEM),
        POCKET_MULTI_TOOL_KNIFE("/pocketMultiToolKnife", TextureType.ITEM),
        POCKET_MULTI_TOOL_SAW("/pocketMultiToolSaw", TextureType.ITEM),
        POCKET_MULTI_TOOL_FILE("/pocketMultiToolFile", TextureType.ITEM),
        POCKET_MULTI_TOOL_SCREWDRIVER("/pocketMultiToolScrewdriver", TextureType.ITEM),
        POCKET_MULTI_TOOL_WIRE_CUTTER("/pocketMultiToolWireCutter", TextureType.ITEM),
        POCKET_MULTI_TOOL_BRANCH_CUTTER("/pocketMultiToolBranchCutter", TextureType.ITEM),

        TOOL_TROWEL("/toolTrowel", TextureType.ITEM),
        TOOL_HEAD_ANGLE_GRINDER("/toolHeadAngleGrinder", TextureType.ITEM),
        TOOL_HEAD_ELECTRIC_SNIPS("/toolHeadElectricSnips", TextureType.ITEM),

        HANDLE_FILE("/handleFile", TextureType.ITEM),
        HANDLE_TROWEL("/handleTrowel", TextureType.ITEM),
        HANDLE_SAW("/handleSaw", TextureType.ITEM),
        HANDLE_SCREWDRIVER("/handleScrewdriver", TextureType.ITEM),

        VOID_49("/void", TextureType.ITEM),

        TOOL_PROSPECTOR("/toolProspector", TextureType.ITEM),
        TOOL_PROSPECTOR_LUV("/toolProspectorElectricLuV", TextureType.ITEM),
        TOOL_PROSPECTOR_ZPM("/toolProspectorElectricZPM", TextureType.ITEM),
        TOOL_PROSPECTOR_UV("/toolProspectorElectricUV", TextureType.ITEM),
        TOOL_PROSPECTOR_UHV("/toolProspectorElectricUHV", TextureType.ITEM),

        // spotless:off
        ;
        // spotless:on

        public final String suffix;
        public final TextureType texture;

        IconType(String suffix, TextureType texture) {
            this.suffix = suffix;
            this.texture = texture;
        }
    }
}
