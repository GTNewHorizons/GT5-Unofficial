Machine Type: {color_machine_type:BEC Hatch}
{color_mode:Pause Control}
Temporarily pauses a {color_key:{item:gregtech:gt.blockmachines:15757}} when the hatch receives a redstone signal
Nodes can be paused immediately, or upon entering the next step
Paused nodes do not consume condensate and do not fail due to contaminants, but they still consume power
They can also start a new recipe but idle immediately before making any progress
{hr}
{color_mode:Parallel Control}
Controls the max parallel for {color_key:{item:gregtech:gt.blockmachines:15757}} when processing recipes
The {color_parallel:Parallel} value is retrieved from the strength of the received redstone signal
{color_parallel:Parallel} with value {color_amount:0} will be ignored
{color_name:Add Parallel} will sum up all other parallels together
{color_name:Multiply Parallel} will multiply all other parallels together
{color_parallel:Max Parallel}{color_formula: = Multiplied Parallels + Summed Parallels}
