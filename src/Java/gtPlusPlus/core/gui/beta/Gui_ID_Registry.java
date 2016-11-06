package gtPlusPlus.core.gui.beta;

import gtPlusPlus.core.interfaces.IGuiManagerMiscUtils;

import java.util.*;

public class Gui_ID_Registry
{
	private static final Map<Class<? extends IGuiManagerMiscUtils>, MU_GuiId> classMap = new HashMap();
	private static final Map<Integer, MU_GuiId> idMap = new HashMap();
	private static int nextId = 0;

	static
	{
		//registerGuiHandlers(Gui_Types.Tile, Arrays.asList(new Class[] {TileAlveary.class}));
		//registerGuiHandlers(MU_GuiType.Item, Arrays.asList(new Class[] { ItemBackpack.class, ItemBackpackNaturalist.class, ItemBeealyzer.class, ItemCatalogue.class, ItemFlutterlyzer.class, ItemHabitatLocator.class, ItemImprinter.class, ItemInfuser.class, ItemLetter.class, ItemSolderingIron.class, ItemTreealyzer.class }));
		//registerGuiHandlers(MU_GuiType.Entity, Arrays.asList(new Class[] { EntityMinecartApiary.class, EntityMinecartBeehouse.class }));
	}

	private static void registerGuiHandlers(Gui_Types MU_GuiType, List<Class<? extends IGuiManagerMiscUtils>> guiHandlerClasses)
	{
		for (Class<? extends IGuiManagerMiscUtils> tileGuiHandlerClass : guiHandlerClasses)
		{
			MU_GuiId guiId = new MU_GuiId(nextId++, MU_GuiType, tileGuiHandlerClass);
			classMap.put(tileGuiHandlerClass, guiId);
			idMap.put(Integer.valueOf(guiId.getId()), guiId);
		}
	}

	public static MU_GuiId getGuiIdForGuiHandler(IGuiManagerMiscUtils guiHandler)
	{
		Class<? extends IGuiManagerMiscUtils> guiHandlerClass = guiHandler.getClass();
		MU_GuiId guiId = (MU_GuiId)classMap.get(guiHandlerClass);
		if (guiId == null) {
			for (Map.Entry<Class<? extends IGuiManagerMiscUtils>, MU_GuiId> classGuiIdEntry : classMap.entrySet()) {
				if (((Class)classGuiIdEntry.getKey()).isAssignableFrom(guiHandlerClass))
				{
					guiId = (MU_GuiId)classGuiIdEntry.getValue();
					break;
				}
			}
		}
		if (guiId == null) {
			throw new IllegalStateException("No gui ID for gui handler: " + guiHandler);
		}
		return guiId;
	}

	public static MU_GuiId getGuiId(int id)
	{
		return (MU_GuiId)idMap.get(Integer.valueOf(id));
	}

}
