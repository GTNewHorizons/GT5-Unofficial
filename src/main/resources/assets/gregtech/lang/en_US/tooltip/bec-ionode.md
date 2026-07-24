Teleports items into and out of the {gold:{item:gregtech:gt.blockmachines:15756}}.
Recipe logic is the same as all other multiblocks.
Condensate does not have to be present for a recipe to start.
Link it to {gold:{item:gregtech:gt.blockmachines:15756}} using {gold:{item:gregtech:gt.blockmachines:15481}}.
{dark_gray:{hr}}
Each item slot in a recipe has an associated nanite tier.
For the teleportation node to progress through an item slot, it must receive a nanite with the same or higher tier.
Excess nanites linearly increase the speed at which recipes are crafted.
Providing a higher tier nanite than is requested slows progress by {italic:2^(provided tier - requested tier)} times.
This multiblock's EU/t remains constant even when it is paused, stalled, or slowed down.
Each recipe crafted in parallel increases the EU/t consumed linearly.
This multiblock does not overclock.
{dark_gray:{hr}}
The min and max parallels can be configured in the parameters to guarantee exact recipe timings.
Recipes can be forcefully slowed down to compensate for slow automation by the {gold:{lang:GT5U.gui.text.bec-speed-divisor}} parameter.
{dark_gray:{hr}}
The {gold:{item:gregtech:gt.blockmachines:15758}} can be used to detect the teleportation node's requested nanites.
The {gold:{item:gregtech:gt.blockmachines:15759}} can be used to temporarily pause the teleportation node.
