Сжигает {color_key:Ракетное топливо} для выработки энергии (минимум {var:min_fuel} {L}/с)
Верхнего предела выработки нет, кроме размера {white:Энерговывода}
Но при {color_eu_usage:{var:cap1} {EU/t}} и {color_eu_usage:{var:cap2} {EU/t}} (без форсирования) есть мягкие пороги, снижающие эффективность топлива
{color_danger:Не заливайте ракетное топливо в выключенный механизм: при запуске оно уничтожится!}
{hr}
Для сгорания нужны дополнительные ресурсы:
{color_amount:{var:lubricant_amount} {L}} {white:{var:lubricant}} в час (x{var:boost} при форсировании)
{color_amount:{var:air_percent}%} от текущей {EU/t} в виде {white:Воздуха} за тик (только через воздухозаборные шлюзы)
{color_amount:{var:coolant_percent}%} от текущей {EU/t} в виде {white:{var:coolant}} за тик для форсирования (необязательно)
{hr}
Если воздух кончится, механизм выключится, и его придётся перезапускать вручную
Форсирование поднимает мягкие пороги до {aqua:{var:cap1_boosted} {EU/t}} и {aqua:{var:cap2_boosted} {EU/t}}
Прогрев занимает {var:warmup_min}-{var:warmup_max} секунд в зависимости от текущей выработки
{hr}
Производит {dark_purple:{var:pollution}} загрязнения на каждые {aqua:{var:pollution_unit} {EU/t}}
