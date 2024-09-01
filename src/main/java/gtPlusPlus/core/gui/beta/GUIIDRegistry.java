package gtPlusPlus.core.gui.beta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gtPlusPlus.core.interfaces.IGuiManagerMiscUtils;

public class GUIIDRegistry {

    private static final Map<Class<? extends IGuiManagerMiscUtils>, MUGuild> classMap = new HashMap<>();
    private static final Map<Integer, MUGuild> idMap = new HashMap<>();
    private static int nextId = 0;

    static {}

    private static void registerGuiHandlers(final GUITypes MU_GuiType,
        final List<Class<? extends IGuiManagerMiscUtils>> guiHandlerClasses) {
        for (final Class<? extends IGuiManagerMiscUtils> tileGuiHandlerClass : guiHandlerClasses) {
            final MUGuild guiId = new MUGuild(nextId++, MU_GuiType, tileGuiHandlerClass);
            classMap.put(tileGuiHandlerClass, guiId);
            idMap.put(Integer.valueOf(guiId.getId()), guiId);
        }
    }

    public static MUGuild getGuiIdForGuiHandler(final IGuiManagerMiscUtils guiHandler) {
        final Class<? extends IGuiManagerMiscUtils> guiHandlerClass = guiHandler.getClass();
        MUGuild guiId = classMap.get(guiHandlerClass);
        if (guiId == null) {
            for (final Map.Entry<Class<? extends IGuiManagerMiscUtils>, MUGuild> classGuiIdEntry : classMap
                .entrySet()) {
                if (((Class<?>) classGuiIdEntry.getKey()).isAssignableFrom(guiHandlerClass)) {
                    guiId = classGuiIdEntry.getValue();
                    break;
                }
            }
        }
        if (guiId == null) {
            throw new IllegalStateException("No gui ID for gui handler: " + guiHandler);
        }
        return guiId;
    }

    public static MUGuild getGuiId(final int id) {
        return idMap.get(Integer.valueOf(id));
    }
}
