/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Variables {

    public static final double ln4 = Math.log(4d);
    public static final double ln2 = Math.log(2d);

    public static final NumberFormat numberFormatScientific = new DecimalFormat("0.00E0");
    public static final NumberFormat numberFormat = NumberFormat.getInstance();
}
