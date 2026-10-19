Burns {color_key:Rocket Fuel} to generate power (min {var:min_fuel} {L}/s)
There is no upper limit on power output, other than the size of the {white:Dynamo Hatch}
But there are soft caps at {color_eu_usage:{var:cap1} {EU/t}} and {color_eu_usage:{var:cap2} {EU/t}} (unboosted) that reduce fuel efficiency
{color_danger:Do not insert Rocket Fuel while disabled - it will be voided on start!}
{hr}
The combustion process requires some additional inputs:
{color_amount:{var:lubricant_amount}{var:unit}} of {white:{var:lubricant}} per hour (x{var:boost} if boosted)
{color_amount:{var:air_percent}%} of current {EU/t} in {white:Air} per tick (only through air intake hatches)
{color_amount:{var:coolant_percent}%} of current {EU/t} in {white:{var:coolant}} per tick to boost (optional)
{hr}
If air ever runs out, the machine shuts down and must be manually restarted
Boosting multiplies the soft caps to {aqua:{var:cap1_boosted} {EU/t}} and {aqua:{var:cap2_boosted} {EU/t}}
Takes {var:warmup_min}-{var:warmup_max} seconds to warm up based on the current power output
{hr}
Produces {dark_purple:{var:pollution}} pollution per {aqua:{var:pollution_unit} {EU/t}}
