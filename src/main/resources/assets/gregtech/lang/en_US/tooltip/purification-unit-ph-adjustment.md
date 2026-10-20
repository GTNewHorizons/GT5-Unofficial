{aqua:{bold:Water Tier: }}{color_tier:{var:tier}}
Must be linked to a Purification Plant using a {color_name:{item:gregtech:gt.metaitem.01:32708}} to work
{hr}
Initial pH value every cycle varies from {color_misc:{var:ph_min}} - {color_misc:{var:ph_max} pH}
If the pH value is within {color_misc:{var:ph_deviation} pH} of {var:ph_neutral} pH at the end of the cycle, the recipe always succeeds
Otherwise, the recipe always fails
Use a {color_name:{var:sensor}} to read the current pH value
For safety, the machine will shut down if the pH goes below 0 or exceeds 14
{hr}
Every {color_time:{var:interval}} ticks, consumes ALL {color_name:{var:alkaline}} and {color_name:{var:acid}} in the special hatches
{color_mechanic:Raises} the pH value by {color_amount:{var:ph_per_dust} pH} per {color_name:{var:dust}}
{color_mechanic:Lowers} the pH value by {color_amount:{var:ph_per_acid} pH} per {color_amount:10 {L}} of {color_name:{var:acid}}
{hr}
{aqua:{italic:The fourth step of water purification is to neutralize the solution and bring its pH to exactly 7, rendering}}
{aqua:{italic:the solution inert with no hydrogen ion activity beyond water's natural amphiproticity. Acids and bases from soils}}
{aqua:{italic:and geology cause natural alkalinity variations in water which can cause corrosive reactions with sensitive}}
{aqua:{italic:materials. This necessitates the use of the corresponding neutralizing agents to pH balance the water.}}
