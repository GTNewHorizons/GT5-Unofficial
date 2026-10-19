{aqua:{bold:Water Tier: }}{white:{var:tier}}
Must be linked to a Purification Plant using a {white:{item:gregtech:gt.metaitem.01:32708}} to work
{gray:{hr}}
Initial pH value every cycle varies from {red:{var:ph_min}} - {red:{var:ph_max} pH}
If the pH value is within {red:{var:ph_deviation} pH} of {var:ph_neutral} pH at the end of the cycle, the recipe always succeeds
Otherwise, the recipe always fails
Use a {white:{var:sensor}} to read the current pH value
For safety, the machine will shut down if the pH goes below 0 or exceeds 14
{gray:{hr}}
Every {red:{var:interval}} ticks, consumes ALL {white:{var:alkaline}} and {white:{var:acid}} in the special hatches
{red:Raises} the pH value by {red:{var:ph_per_dust} pH} per {white:{var:dust}}
{red:Lowers} the pH value by {red:{var:ph_per_acid} pH} per {red:10 {var:unit}} of {white:{var:acid}}
{gray:{hr}}
{aqua:{italic:The fourth step of water purification is to neutralize the solution and bring its pH to exactly 7, rendering}}
{aqua:{italic:the solution inert with no hydrogen ion activity beyond water's natural amphiproticity. Acids and bases from soils}}
{aqua:{italic:and geology cause natural alkalinity variations in water which can cause corrosive reactions with sensitive}}
{aqua:{italic:materials. This necessitates the use of the corresponding neutralizing agents to pH balance the water.}}
