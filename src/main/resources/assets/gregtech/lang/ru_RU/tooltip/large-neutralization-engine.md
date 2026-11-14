(Рас)творяет все ваши проблемы!
{hr}
Можно подавать {aqua:основание}, чтобы повысить {color_mechanic:эффективность}:
{aqua:{var:sodium_hydroxide_rate}/мин} {aqua:гидроксида натрия} : {color_mechanic:{var:sodium_hydroxide_eff}%}
{aqua:{var:potassium_hydroxide_rate}/мин} {aqua:гидроксида калия} : {color_mechanic:{var:potassium_hydroxide_eff}%}
{aqua:{var:caesium_hydroxide_rate}/мин} {aqua:гидроксида цезия} : {color_mechanic:{var:caesium_hydroxide_eff}%}
{aqua:{var:francium_hydroxide_rate}/мин} {aqua:гидроксида франция} : {color_mechanic:{var:francium_hydroxide_eff}%}
{hr}
Производит {dark_green:токсичный остаток} при сжигании кислот
Если {dark_green:токсичный остаток} превысит {dark_aqua:вместимость}, механизм {color_danger:взрывается}
Каждый тик {dark_green:токсичный остаток} растёт на {green:0.05 * плотность топлива ({EU}/{L})^0.8 * расход топлива ({L}) * rand({var:residue_rand_min}, {var:residue_rand_max})}
Каждый тик {dark_green:токсичный остаток} убывает на {green:базовый распад * ускорение распада * токсичный остаток^0.08}
{yellow:При выключенном мультиблоке остаток распадается в 10 раз медленнее}
{hr}
Вставьте до {blue:{var:robot_arm_amount_for_decay_boost} роборук}, чтобы повысить {blue:ускорение распада}:
{white:Ускорение распада =} {green:sqrt(роборуки) * N^(тир роборуки - 1)}, где {green:N={var:robot_arm_bonus_low}} ({tier-color:arm_tier_low:{VN:arm_tier_low}-}) или {green:N={var:robot_arm_bonus_high}} ({tier-color:arm_tier_high:{VN:arm_tier_high}+})
Каждую минуту с шансом {green:роборуки / (45 * (тир роборуки + 1))} уничтожается ровно одна {blue:роборука}
{hr}
У структуры {color_mode:3 тира}, которые определяют {blue:базовый распад} и {dark_aqua:вместимость}:
{color_mode:T1} : {color_mode:Укреплённый неодушевлённый корпус} : {blue:{var:tier1_decay}} : {dark_aqua:{var:tier1_capacity}}
{color_mode:T2} : {color_mode:Прецизионный стационарный корпус} : {blue:{var:tier2_decay}} : {dark_aqua:{var:tier2_capacity}}
{color_mode:T3} : {color_mode:Ультимативный статичный корпус} : {blue:{var:tier3_decay}} : {dark_aqua:{var:tier3_capacity}}
