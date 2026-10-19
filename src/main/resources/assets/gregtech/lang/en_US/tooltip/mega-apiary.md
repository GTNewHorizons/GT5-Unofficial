The ideal home for your bees
{color_mode:Input Mode}:
- Does not take power, put queens in the {color_name:Input Bus} to store them in the internal buffer
{hr}
{color_mode:Output Mode}:
- Does not take power, returns bees to the {color_name:Output Bus}
- {color_note:Careful, you can easily void your bees}
{hr}
{color_mode:Operating Mode}:
- {color_mechanic:Normal}:
  - {color_time:{var:normal_seconds}s} per operation, {color_amount:{var:bee_acceleration}x} bee speed
  - Uses {color_amperage:1} {var:voltageTier_LuV} amp per queen
  - {color_amount:8} production upgrades and {color_name:Genetic Stabilizer} applied
  - Simulates a perfect environment
  - {color_key:Royal Jelly} grants {color_amount:{var:jelly_bonus}%} output per jelly, up to {color_amount:{var:max_jelly_bonus}%} (consumed each operation per bee)
- {color_mechanic:Swarmer}:
  - {color_amount:1} queen only, slowly produces {color_name:Ignoble Princesses}
  - {color_amount:{var:swarmer_jelly_cost}} {color_key:Royal Jelly} per operation
  - {color_time:{var:swarmer_seconds}s} base time, {color_amperage:1} {var:voltageTier_IV} amp
  - {color_mechanic:Can overclock}
{hr}
Right-click the controller with a {color_name:Screwdriver} to change {color_mode:mode} (Input/Output/Operating)
Shift-right-click the controller with a {color_name:Screwdriver} to change {color_mechanic:operation mode} (Normal/Swarmer)
