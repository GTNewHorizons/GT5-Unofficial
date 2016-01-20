package binnie;

import binnie.core.ManagerBase;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.item.ManagerItem;
import binnie.core.language.ManagerLanguage;
import binnie.core.liquid.ManagerLiquid;
import binnie.core.machines.ManagerMachine;
import binnie.core.mod.config.ManagerConfig;
import binnie.core.resource.ManagerResource;
import java.util.ArrayList;
import java.util.List;

public final class Binnie
{
  public static final List<ManagerBase> Managers = new ArrayList();
  public static final ManagerLanguage Language = new ManagerLanguage();
  public static final ManagerGenetics Genetics = new ManagerGenetics();
  public static final ManagerConfig Configuration = new ManagerConfig();
  public static final ManagerLiquid Liquid = new ManagerLiquid();
  public static final ManagerMachine Machine = new ManagerMachine();
  public static final ManagerItem Item = new ManagerItem();
  public static final ManagerResource Resource = new ManagerResource();
}
