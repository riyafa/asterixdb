/*
 * Copyright 2009-2010 by The Regents of the University of California
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
package edu.uci.ics.asterix.result;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uci.ics.hyracks.api.comm.IFrameTupleAccessor;
import edu.uci.ics.hyracks.api.exceptions.HyracksDataException;
import edu.uci.ics.hyracks.dataflow.common.comm.util.ByteBufferInputStream;

public class ResultUtils {
    public static JSONArray getJSONFromBuffer(ByteBuffer buffer, IFrameTupleAccessor fta) throws HyracksDataException {
        JSONArray resultRecords = new JSONArray();
        ByteBufferInputStream bbis = new ByteBufferInputStream();

        try {
            fta.reset(buffer);
            for (int tIndex = 0; tIndex < fta.getTupleCount(); tIndex++) {
                int start = fta.getTupleStartOffset(tIndex);
                int length = fta.getTupleEndOffset(tIndex) - start;
                bbis.setByteBuffer(buffer, start);
                byte[] recordBytes = new byte[length];
                bbis.read(recordBytes, 0, length);
                resultRecords.put(new String(recordBytes, 0, length));
            }
        } finally {
            try {
                bbis.close();
            } catch (IOException e) {
                throw new HyracksDataException(e);
            }
        }
        return resultRecords;
    }

    public static JSONObject getErrorResponse(int errorCode, String errorMessage) {
        JSONObject errorResp = new JSONObject();
        JSONArray errorArray = new JSONArray();
        errorArray.put(errorCode);
        errorArray.put(errorMessage);
        try {
            errorResp.put("error-code", errorArray);
        } catch (JSONException e) {
            // TODO(madhusudancs): Figure out what to do when JSONException occurs while building the results.
        }
        return errorResp;
    }
}