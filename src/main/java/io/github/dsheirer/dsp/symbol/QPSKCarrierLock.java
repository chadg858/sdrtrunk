/*
 * *****************************************************************************
 *  Copyright (C) 2014-2020 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * ****************************************************************************
 */

package io.github.dsheirer.dsp.symbol;

/**
 * QPSK carrier lock elements that indicate if a PLL is locked normally, or if it has locked at +/- 90 degrees,
 * or inverted 180 degrees.  Abnormal PLL lock is normally detected by rotated sync patterns.
 *
 * Each enumeration entry provides a correct(dibit) method that supports correcting dibits that were captured with a
 * misaligned PLL lock to what they should be if the PLL were locked correctly.  This allows detected (yet misaligned)
 * messages to become usable through dibit correction even though the PLL was misaligned.
 */
public enum QPSKCarrierLock
{
    /**
     * Normal PLL lock indication
     */
    NORMAL
    {
        @Override
        public Dibit correct(Dibit dibit)
        {
            //No correction needed
            return dibit;
        }
    },

    /**
     * PLL lock at plus 90 degrees to the carrier, requiring a PLL adjustment of -90 degrees or -PI/2.
     */
    PLUS_90
    {
        @Override
        public Dibit correct(Dibit dibit)
        {
            switch(dibit)
            {
                case D00_PLUS_1:
                    return Dibit.D10_MINUS_1;
                case D01_PLUS_3:
                    return Dibit.D00_PLUS_1;
                case D10_MINUS_1:
                    return Dibit.D11_MINUS_3;
                case D11_MINUS_3:
                default:
                    return Dibit.D01_PLUS_3;
            }
        }
    },

    /**
     * PLL lock at minus 90 degrees to the carrier, requiring a PLL adjustment of +90 degrees, or +PI/2.
     */
    MINUS_90
    {
        @Override
        public Dibit correct(Dibit dibit)
        {
            switch(dibit)
            {
                case D00_PLUS_1:
                    return Dibit.D01_PLUS_3;
                case D01_PLUS_3:
                    return Dibit.D11_MINUS_3;
                case D10_MINUS_1:
                    return Dibit.D00_PLUS_1;
                case D11_MINUS_3:
                default:
                    return Dibit.D10_MINUS_1;
            }
        }
    },

    /**
     * PLL lock at 180 degrees or inverted to the carrier, requiring a PLL adjustment of 180 degrees or PI.
     */
    INVERTED
    {
        @Override
        public Dibit correct(Dibit dibit)
        {
            switch(dibit)
            {
                case D00_PLUS_1:
                    return Dibit.D11_MINUS_3;
                case D01_PLUS_3:
                    return Dibit.D10_MINUS_1;
                case D10_MINUS_1:
                    return Dibit.D01_PLUS_3;
                case D11_MINUS_3:
                default:
                    return Dibit.D00_PLUS_1;
            }
        }
    };

    /**
     * Method for each entry to provide dibit correction solutions
     * @param dibit that was generated by a misaligned QPSK Phase locked loop
     * @return dibit that is corrected to the dibit for a correctly aligned QPSK Phase Locked Loop
     */
    public abstract Dibit correct(Dibit dibit);
}