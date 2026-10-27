3 режима: {light_purple:Лёгкий} | {blue:Обычный} | {dark_green:Тяжёлый}
{hr}
{color_parallel:{var:parallels} параллели} за {color_name:сумму тиров турбин}
{color_speed:Скорость {var:speed}%}
{color_efficiency:Потребление {EU} {var:eu_eff}%}
{color_name:Турбины меньше огромных} имеют пониженную эффективность...
Даёт {var:slots_per_tier} {color_name:слота под турбины} за тир {color_name:структуры}
Для работы требует {aqua:(тир рецепта * {var:fluid_per_tier} {L}/с)} {color_key:Керосина}
Подавайте вместо него {color_key:Биокаталитическое ракетное топливо}, чтобы получить множитель параллелей {color_parallel:{var:tier2_mult}x}
{color_note:Разгон ограничен тиром шлюза + 1}
{hr}
{light_purple:Лёгкий режим}:
- {color_speed:+{var:light_speed}% к скорости}
- Максимальный тир рецепта: {aqua:(тир вольтажа - {var:light_offset})}
{hr}
{blue:Обычный режим}: без изменений
{hr}
{dark_green:Тяжёлый режим}: требует {color_key:Биокаталитическое ракетное топливо}
- Делит {color_parallel:параллели} на {color_parallel:{var:heavy_div}}
- Умножает {red:стоимость EU} на {red:{var:heavy_eu}}
- {light_purple:Разгон без ограничений}
- Некоторые рецепты {dark_red:требуют} {dark_green:Тяжёлый режим}
