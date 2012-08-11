/*
 * Copyright 2009-2012 by The Regents of the University of California
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License from
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.hyracks.storage.am.common.datagen;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.uci.ics.hyracks.api.dataflow.value.ISerializerDeserializer;
import edu.uci.ics.hyracks.dataflow.common.data.accessors.ITupleReference;

@SuppressWarnings("rawtypes")
public class TupleBatch {
    private final int size;
    private final TupleGenerator[] tupleGens;
    public final AtomicBoolean inUse = new AtomicBoolean(false);
    
    public TupleBatch(int size, IFieldValueGenerator[] fieldGens, ISerializerDeserializer[] fieldSerdes, int payloadSize) {        
        this.size = size;
        tupleGens = new TupleGenerator[size];
        for (int i = 0; i < size; i++) {
            tupleGens[i] = new TupleGenerator(fieldGens, fieldSerdes, payloadSize);
        }
    }
    
    public void generate() throws IOException {
        for(TupleGenerator tupleGen : tupleGens) {
            tupleGen.next();
        }
    }
    
    public int size() {
        return size;
    }
    
    public ITupleReference get(int ix) {
        return tupleGens[ix].get();
    }
}
