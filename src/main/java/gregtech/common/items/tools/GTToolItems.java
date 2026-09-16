package gregtech.common.items.tools;

import java.util.ArrayList;
import java.util.List;

import gregtech.common.tools.ToolSoftMallet;
import gregtech.common.tools.ToolWrench;
import gregtech.common.tools.ToolWrenchHV;
import gregtech.common.tools.ToolWrenchLV;
import gregtech.common.tools.ToolWrenchMV;

/**
 * The standalone tool items: one registered Forge item per tool type and tier, with the crafting material encoded in
 * the metadata.
 * <p/>
 * Tool types move here from {@link gregtech.common.items.MetaGeneratedTool01} one at a time; the wrench is the pilot.
 */
public final class GTToolItems {

    public static ToolWrenchItem WRENCH;
    public static ToolWrenchElectricItem WRENCH_LV;
    public static ToolWrenchElectricItem WRENCH_MV;
    public static ToolWrenchElectricItem WRENCH_HV;
    public static ToolSoftMalletItem SOFT_MALLET;

    /**
     * Ore dictionary registrations that are waiting for {@link #flushOreDictRegistrations()}.
     * <p/>
     * Materials are declared from inside the ore dictionary handlers (see
     * {@link gregtech.loaders.oreprocessing.ProcessingToolOther} and friends), which run while
     * {@link gregtech.common.GTProxy#activateOreDictHandler()} is iterating its own event collection. Registering a
     * new ore from there re-enters {@link gregtech.common.GTProxy#onOreRegistration} and mutates that collection
     * mid-iteration, which throws a ConcurrentModificationException. So the registrations are collected here and
     * replayed once the handler is done.
     */
    private static final List<Runnable> PENDING_ORE_DICT_REGISTRATIONS = new ArrayList<>();

    private static boolean oreDictRegistrationsFlushed = false;

    private GTToolItems() {}

    /**
     * Must run during pre-init, alongside the other item registrations, and before any recipe loader asks for a
     * wrench stack.
     */
    public static void register() {
        WRENCH = new ToolWrenchItem(
            "tool.wrench",
            new ToolWrench(),
            "%material Wrench",
            "Hold Leftclick to dismantle Machines");
        WRENCH_LV = new ToolWrenchElectricItem(
            "tool.wrench_lv",
            new ToolWrenchLV(),
            "%material Wrench (LV)",
            "Hold Left Button to dismantle Machines",
            100_000L,
            32L,
            1);
        WRENCH_MV = new ToolWrenchElectricItem(
            "tool.wrench_mv",
            new ToolWrenchMV(),
            "%material Wrench (MV)",
            "Hold Left Button to dismantle Machines",
            400_000L,
            128L,
            2);
        WRENCH_HV = new ToolWrenchElectricItem(
            "tool.wrench_hv",
            new ToolWrenchHV(),
            "%material Wrench (HV)",
            "Hold Left Button to dismantle Machines",
            1_600_000L,
            512L,
            3);
        SOFT_MALLET = new ToolSoftMalletItem("tool.soft_mallet", new ToolSoftMallet(), "%material Soft Mallet");
    }

    /**
     * Runs an ore dictionary registration for a tool stack, either now or -- if the ore dictionary handler has not
     * finished yet -- once it has. See {@link #PENDING_ORE_DICT_REGISTRATIONS}.
     */
    static synchronized void registerOreDictEntry(Runnable registration) {
        if (oreDictRegistrationsFlushed) registration.run();
        else PENDING_ORE_DICT_REGISTRATIONS.add(registration);
    }

    /**
     * Registers every ore dictionary entry that was declared while the ore dictionary handler was running. Must be
     * called once, immediately after {@link gregtech.common.GTProxy#activateOreDictHandler()} returns; anything
     * declared after that point registers straight away.
     */
    public static synchronized void flushOreDictRegistrations() {
        oreDictRegistrationsFlushed = true;
        for (Runnable registration : PENDING_ORE_DICT_REGISTRATIONS) registration.run();
        PENDING_ORE_DICT_REGISTRATIONS.clear();
    }
}
