/*
 * *****************************************************************************
 * Copyright (C) 2014-2022 Dennis Sheirer
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

package io.github.dsheirer.source.tuner.sdrplay;

import com.github.dsheirer.sdrplay.DeviceDescriptor;
import com.github.dsheirer.sdrplay.SDRplay;
import com.github.dsheirer.sdrplay.device.DeviceType;
import io.github.dsheirer.source.tuner.TunerClass;
import io.github.dsheirer.source.tuner.manager.DiscoveredTuner;

/**
 * Discovered SDRplay RSP tuner
 */
public class DiscoveredRspTuner extends DiscoveredTuner
{
    private SDRplay mSDRplayApi;
    private DeviceDescriptor mDeviceDescriptor;

    /**
     * Constructs an instance
     * @param sdrplayApi
     * @param deviceDescriptor
     */
    public DiscoveredRspTuner(SDRplay sdrplayApi, DeviceDescriptor deviceDescriptor)
    {
        mSDRplayApi = sdrplayApi;
        mDeviceDescriptor = deviceDescriptor;
    }

    @Override
    public TunerClass getTunerClass()
    {
        return TunerClass.RSP;
    }

    /**
     * Type of RSP tuner device
     */
    public DeviceType getDeviceType()
    {
        return mDeviceDescriptor.getDeviceType();
    }

    @Override
    public String getId()
    {
        return mDeviceDescriptor.getSerialNumber();
    }

    @Override
    public void start()
    {
        setErrorMessage("Not finished yet");
    }
}