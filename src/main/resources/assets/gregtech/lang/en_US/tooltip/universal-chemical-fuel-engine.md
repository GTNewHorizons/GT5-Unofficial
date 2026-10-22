BURNING BURNING BURNING
Reacts {yellow:Combustion Promoter} with {color_key:Gas}, {color_key:Diesel}, or {color_key:Rocket Fuel} to generate power with up to {color_efficiency:150%} efficiency
No soft caps or upper limits on power output, other than the size of the {color_name:Dynamo Hatch}
No power is produced without Combustion Promoter
Excess power is voided if the {color_name:Dynamo Hatch} is full or the output exceeds the dynamo throughput
{hr}
Efficiency is determined by the ratio ({color_formula:R}) of {yellow:Combustion Promoter} to {gold:Fuel}
The more Combustion Promoter, the higher the efficiency
Follows an exponential curve, {color_formula:exp(-C / R) * {var:efficiency_ceiling}}, where {color_formula:C} is a constant based on the fuel type
{color_key:Gas/Diesel Fuel}: {color_key:Gas/Diesel Fuel}: {color_formula:C = {var:gas_coefficient}} | {color_key:Rocket Fuel}: {color_formula:C = {var:rocket_coefficient}}
{hr}
Produces {color_pollution:sqrt({EU/t})} pollution per second
