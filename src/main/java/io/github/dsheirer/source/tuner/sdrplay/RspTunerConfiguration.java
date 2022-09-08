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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dsheirer.sdrplay.parameter.tuner.GainReduction;
import com.github.dsheirer.sdrplay.parameter.tuner.SampleRate;
import io.github.dsheirer.source.tuner.configuration.TunerConfiguration;

/**
 * Abstract RSP tuner configuration
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Rsp1aTunerConfiguration.class, name = "rsp1aTunerConfiguration"),
        @JsonSubTypes.Type(value = Rsp2TunerConfiguration.class, name = "rsp2TunerConfiguration"),
        @JsonSubTypes.Type(value = RspDuoTuner1Configuration.class, name = "rspDuoTuner1Configuration"),
        @JsonSubTypes.Type(value = RspDuoTuner2Configuration.class, name = "rspDuoTuner2Configuration"),
        @JsonSubTypes.Type(value = RspDxTunerConfiguration.class, name = "rspDxTunerConfiguration"),
})
public abstract class RspTunerConfiguration extends TunerConfiguration
{
    private SampleRate mSampleRate = SampleRate.RATE_10_000;
    private int mGain = 10;

    /**
     * JAXB Constructor
     */
    public RspTunerConfiguration()
    {
    }

    /**
     * Constructs an instance with the specified unique id
     * @param uniqueId for the tuner
     */
    public RspTunerConfiguration(String uniqueId)
    {
        super(uniqueId);
    }

    /**
     * Sample rate for the tuner
     */
    @JacksonXmlProperty(isAttribute = true, localName = "sampleRate")
    public SampleRate getSampleRate()
    {
        return mSampleRate;
    }

    /**
     * Sets the sample rate for the tuner
     */
    public void setSampleRate(SampleRate sampleRate)
    {
        mSampleRate = sampleRate;
    }

    /**
     * Gain index to use
     * @return gain index, 0 - 28
     */
    @JacksonXmlProperty(isAttribute = true, localName = "gain")
    public int getGain()
    {
        return mGain;
    }

    /**
     * Sets the gain index
     * @param gain index, 0 - 28
     */
    public void setGain(int gain)
    {
        if(GainReduction.MIN_GAIN_INDEX <= gain && gain <= GainReduction.MAX_GAIN_INDEX)
        {
            mGain = gain;
        }
    }
}
