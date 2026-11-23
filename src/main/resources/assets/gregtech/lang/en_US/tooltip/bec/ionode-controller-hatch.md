Machine Type: {color_machine_type:BEC Hatch}
{color_mode:Pause Control}
Temporarily pauses a {color_key:{item:gregtech:gt.blockmachines:15757}} when the hatch receives a redstone signal
Nodes can be paused immediately, or upon entering the next step
Paused nodes do not consume condensate and do not fail due to contaminants, but they still consume power
They can also start a new recipe but idle immediately before making any progress
{hr}
{color_mode:Parallel Control}
Controls the max parallel for {color_key:{item:gregtech:gt.blockmachines:15757}} when processing recipes
The {color_mechanic:Mantissa} value is based on the strength of the received redstone signal
The {color_mechanic:Exponent} value is based on the supplied ghost circuit number
{color_parallel:Parallel}{color_formula: = Mantissa × 16^Exponent}
Multiple {color_key:{item:gregtech:gt.blockmachines:15759}} can be used and their {color_parallel:Parallel} values will be summed up together
