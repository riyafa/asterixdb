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
package org.apache.hyracks.storage.am.lsm.common.api;

import java.util.List;

import org.apache.hyracks.storage.am.common.api.IIndexOperationContext;
import org.apache.hyracks.storage.am.common.tuples.PermutingTupleReference;
import org.apache.hyracks.storage.common.IModificationOperationCallback;
import org.apache.hyracks.storage.common.ISearchOperationCallback;
import org.apache.hyracks.storage.common.ISearchPredicate;
import org.apache.hyracks.storage.common.MultiComparator;

public interface ILSMIndexOperationContext extends IIndexOperationContext {
    List<ILSMComponent> getComponentHolder();

    List<ILSMDiskComponent> getComponentsToBeMerged();

    ISearchOperationCallback getSearchOperationCallback();

    IModificationOperationCallback getModificationCallback();

    void setCurrentMutableComponentId(int currentMutableComponentId);

    void setSearchPredicate(ISearchPredicate searchPredicate);

    ISearchPredicate getSearchPredicate();

    List<ILSMDiskComponent> getComponentsToBeReplicated();

    /**
     * @return true if this operation entered the components. Otherwise false.
     */
    boolean isAccessingComponents();

    void setAccessingComponents(boolean accessingComponents);

    PermutingTupleReference getIndexTuple();

    PermutingTupleReference getFilterTuple();

    MultiComparator getFilterCmp();

    List<ILSMIOOperation> getDependingOps();

    void setDependingOps(List<ILSMIOOperation> dependingOps);
}
