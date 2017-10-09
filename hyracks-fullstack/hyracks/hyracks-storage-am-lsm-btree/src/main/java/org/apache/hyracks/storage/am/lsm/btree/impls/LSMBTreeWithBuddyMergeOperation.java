/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hyracks.storage.am.lsm.btree.impls;

import java.util.List;

import org.apache.hyracks.api.io.FileReference;
import org.apache.hyracks.storage.am.common.api.ITreeIndexCursor;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMIOOperation;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMIOOperationCallback;
import org.apache.hyracks.storage.am.lsm.common.api.ILSMIndexAccessor;
import org.apache.hyracks.storage.am.lsm.common.impls.MergeOperation;

public class LSMBTreeWithBuddyMergeOperation extends MergeOperation {

    private final FileReference buddyBtreeMergeTarget;
    private final FileReference bloomFilterMergeTarget;
    private final boolean keepDeletedTuples;

    public LSMBTreeWithBuddyMergeOperation(ILSMIndexAccessor accessor, ITreeIndexCursor cursor, FileReference target,
            FileReference buddyBtreeMergeTarget, FileReference bloomFilterMergeTarget, ILSMIOOperationCallback callback,
            String indexIdentifier, boolean keepDeletedTuples, List<ILSMIOOperation> dependingOps) {
        super(accessor, target, callback, indexIdentifier, cursor, dependingOps);
        this.buddyBtreeMergeTarget = buddyBtreeMergeTarget;
        this.bloomFilterMergeTarget = bloomFilterMergeTarget;
        this.keepDeletedTuples = keepDeletedTuples;
    }

    public FileReference getBuddyBTreeTarget() {
        return buddyBtreeMergeTarget;
    }

    public FileReference getBloomFilterTarget() {
        return bloomFilterMergeTarget;
    }

    public boolean isKeepDeletedTuples() {
        return keepDeletedTuples;
    }

}
