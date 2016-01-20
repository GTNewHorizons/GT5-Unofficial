package binnie.craftgui.minecraft;

import binnie.core.machines.transfer.TransferRequest;

public abstract interface IWindowAffectsShiftClick
{
  public abstract void alterRequest(TransferRequest paramTransferRequest);
}
