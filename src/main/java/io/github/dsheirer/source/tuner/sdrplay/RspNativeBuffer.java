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

import io.github.dsheirer.buffer.INativeBuffer;
import io.github.dsheirer.sample.complex.ComplexSamples;
import io.github.dsheirer.sample.complex.InterleavedComplexSamples;
import java.util.Iterator;

/**
 * Native buffer implementation for RSP tuner I/Q sample buffers.
 *
 * Note: in testing with API v3.07, the daemon returns 2016 samples in each of the I and Q arrays.
 */
public class RspNativeBuffer implements INativeBuffer
{
    private static final float SAMPLE_TO_FLOAT = 1.0f / 32768.0f;
    private short[] mISamples;
    private short[] mQSamples;
    private long mTimestamp;

    /**
     * Constructs an instance
     * @param i samples array
     * @param q samples array
     * @param timestamp for the first sample
     */
    public RspNativeBuffer(short[] i, short[] q, long timestamp)
    {
        mISamples = i;
        mQSamples = q;
        mTimestamp = timestamp;
    }

    /**
     * Iterator over samples that produces complex sample buffers.
     */
    @Override
    public Iterator<ComplexSamples> iterator()
    {
        return new SampleIterator();
    }

    /**
     * Iterator over samples that produces interleaved complex sample buffers
     */
    @Override
    public Iterator<InterleavedComplexSamples> iteratorInterleaved()
    {
        return new InterleavedSampleIterator();
    }

    @Override
    public int sampleCount()
    {
        return mISamples.length;
    }

    @Override
    public long getTimestamp()
    {
        return mTimestamp;
    }

    /**
     * Iterator providing (non-interleaved) complex sample buffers
     */
    private class SampleIterator implements Iterator<ComplexSamples>
    {
        private int mSamplePointer;

        @Override
        public boolean hasNext()
        {
            return mSamplePointer < mISamples.length;
        }

        @Override
        public ComplexSamples next()
        {
            float[] i = new float[mISamples.length];
            float[] q = new float[mISamples.length];

            for(int x = 0; x < mISamples.length; x++)
            {
                i[x] = mISamples[x] * SAMPLE_TO_FLOAT;
                q[x] = mQSamples[x] * SAMPLE_TO_FLOAT;
            }

            mSamplePointer += mISamples.length;

            return new ComplexSamples(i, q);
        }
    }

    /**
     * Interator providing interleaved sample buffers.
     */
    private class InterleavedSampleIterator implements Iterator<InterleavedComplexSamples>
    {
        private int mSamplePointer;

        @Override
        public boolean hasNext()
        {
            return mSamplePointer < mISamples.length;
        }

        @Override
        public InterleavedComplexSamples next()
        {
            float[] samples = new float[2 * mISamples.length];

            int index = 0;

            for(int x = 0; x < mISamples.length; x++)
            {
                samples[index++] = mISamples[x] * SAMPLE_TO_FLOAT;
                samples[index++] = mQSamples[x] * SAMPLE_TO_FLOAT;
            }

            mSamplePointer += mISamples.length;

            return new InterleavedComplexSamples(samples, mTimestamp);
        }
    }
}
