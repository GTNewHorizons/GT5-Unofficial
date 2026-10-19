{aqua:{bold:Water Tier: }}{white:{var:tier}}
Must be linked to a Purification Plant using a {white:{item:gregtech:gt.metaitem.01:32708}} to work
{gray:{hr}}
Insert {white:Quark Releasing Catalysts} into the input bus while running
Every recipe cycle, a different combination of {red:2} different {white:Quark Releasing Catalysts}
will correctly identify the lone quark and succeed the recipe
The order of catalysts in the combination does not matter, so {blue:Up}/{light_purple:Down} can be identified by inserting {light_purple:Down}/{blue:Up}
{gray:{hr}}
Every {red:20} ticks, consumes ALL catalysts in the input bus
The base cost of inserting a catalyst is {red:{var:catalyst_cost} {var:unit}} of {white:{var:infinity}}
For every duplicate occurrence of an inserted catalyst in the sequence, this cost is doubled
{gray:{hr}}
Keeps track of the entire sequence of catalysts inserted this recipe
If the correct catalyst combination is in the sequence of inserted catalysts, immediately outputs {red:{var:output} {var:unit}} of {white:{var:baryonic}}
At the end of a successful recipe, outputs additional {red:{var:extra_output} {var:unit}} of {white:{var:baryonic}} per parallel
At the end of the recipe, returns all incorrectly inserted catalysts in the output bus
{gray:{hr}}
{aqua:{italic:The final stage of water purification goes beyond subatomic particles and identifies the smallest}}
{aqua:{italic:possible imperfections within the baryons themselves. By correctly identifying which pairs of quark}}
{aqua:{italic:flavors are required, the unit will activate the catalysts, stabilizing the errant particles.}}
{aqua:{italic:This ultimately creates both Stabilised Baryonic Matter and, most importantly, absolutely perfectly purified water.}}
