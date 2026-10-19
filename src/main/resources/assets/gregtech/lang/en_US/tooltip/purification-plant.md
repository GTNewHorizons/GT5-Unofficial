Main controller block for the Water Purification Plant
Freely place {yellow:Purification Units} within {red:{var:distance}} blocks along each axis
Left click this controller with a {white:{item:gregtech:gt.metaitem.01:32708}}, then right click a purification unit to link
Supplies power to linked purification units
{gray:{hr}}
Works in fixed time processing cycles of {red:{var:cycle}} seconds
All linked units follow this cycle
{gray:{hr}}
Every recipe has a base chance of success. Success rate can be boosted
by using a portion of the target output as a secondary input
{red:{var:boost_fluid}%} of output yield will be consumed in exchange for an
additive {red:{var:boost_chance}%} increase to success
On recipe failure, each purification unit has a {red:50%} chance
to return water of the same quality as the input or lower
{gray:{hr}}
Every purification unit has a configuration window to configure maximum parallel amount
This will only scale purified water input, ALL fluid output and power usage. Other catalysts and outputs are unchanged
Toggle debug mode to reduce cycle time to {red:{var:debug_cycle}} seconds but disable water I/O
{gray:{hr}}
{aqua:{italic:Contaminants and ionized particles in water can cause significant imperfections in delicate}}
{aqua:{italic:processes related to the cutting and engraving of silicon wafers and chips. It is crucial that}}
{aqua:{italic:the water is systematically purified through a series of increasingly precise and complex}}
{aqua:{italic:purification processes, and this multiblock is the heart of the operation}}
