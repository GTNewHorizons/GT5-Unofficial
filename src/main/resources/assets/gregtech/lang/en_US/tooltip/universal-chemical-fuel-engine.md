BURNING BURNING BURNING
Reacts {yellow:Combustion Promoter} with {gold:Gas}, {gold:Diesel}, or {gold:Rocket Fuel} to generate power with up to {aqua:150%} efficiency
No soft caps or upper limits on power output, other than the size of the {white:Dynamo Hatch}
No power is produced without Combustion Promoter
Excess power is voided if the {white:Dynamo Hatch} is full or the output exceeds the dynamo throughput
{gray:{hr}}
Efficiency is determined by the ratio (R) of {yellow:Combustion Promoter} to {gold:Fuel}
The more Combustion Promoter, the higher the efficiency
Follows an exponential curve {aqua:exp(-C/R) * {var:efficiency_ceiling}}, where {white:C} is a constant based on the fuel type
{gold:Gas/Diesel Fuel}: {white:C={var:gas_coefficient}} | {gold:Rocket Fuel}: {white:C={var:rocket_coefficient}}
{gray:{hr}}
Produces {dark_purple:sqrt(EU/t)} pollution per second
