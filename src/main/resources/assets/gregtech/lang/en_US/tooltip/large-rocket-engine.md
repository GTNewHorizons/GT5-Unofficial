Burns {gold:Rocket Fuel} to generate power
There is no upper limit on power output, other than the size of the {white:Dynamo Hatch}
But there are soft caps at {aqua:{var:cap1} EU/t} and {aqua:{var:cap2} EU/t} (unboosted) that reduce fuel efficiency
{dark_red:Do not insert Rocket Fuel while disabled - it will be voided on start!}
{yellow:Minimum fuel input is {var:min_fuel}/s}
{gray:{hr}}
The combustion process requires some additional inputs:
{aqua:{var:lubricant_amount}} of {gold:{var:lubricant}} per hour (x{var:boost} if boosted)
{aqua:{var:air_percent}%} of current EU/t in {gold:Air} per tick (only through air intake hatches)
{aqua:{var:coolant_percent}%} of current EU/t in {gold:{var:coolant}} per tick to boost (optional)
{gray:{hr}}
If air ever runs out, the machine shuts down and must be manually restarted
Boosting multiplies the soft caps to {aqua:{var:cap1_boosted} EU/t} and {aqua:{var:cap2_boosted} EU/t}
Takes {light_purple:{var:warmup_min}-{var:warmup_max} seconds} to warm up based on the current EU/t
{gray:{hr}}
Produces {dark_purple:{var:pollution}} pollution per {aqua:{var:pollution_unit} EU/t}
