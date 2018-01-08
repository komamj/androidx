/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.arch.background.workmanager;

import android.arch.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An opaque class that allows you to chain together {@link Work}.
 */

public abstract class WorkContinuation {

    /**
     * Adds new {@link Work} items that depend on the successful completion of all previously added
     * {@link Work}.
     *
     * @param work One or more {@link Work} to add to the {@link WorkContinuation}
     * @return A {@link WorkContinuation} that allows for further chaining of dependent {@link Work}
     */
    public abstract WorkContinuation then(Work... work);

    /**
     * Adds new {@link Work} items, created with the {@link Worker} classes, that depend on the
     * successful completion of all previously added {@link Work}.
     *
     * Each {@link Work} is created with no {@link Arguments} or {@link Constraints}.
     *
     * @param workerClasses One or more {@link Worker}s to add to the {@link WorkContinuation}
     * @return A {@link WorkContinuation} that allows for further chaining of dependent {@link Work}
     */
    @SafeVarargs
    public final WorkContinuation then(Class<? extends Worker>... workerClasses) {
        return then(Arrays.asList(workerClasses));
    }

    /**
     * Returns a {@link LiveData} mapping of work identifiers to their statuses for all work in this
     * chain.  Whenever the status of one of the work enqueued in this chain changes, any attached
     * {@link android.arch.lifecycle.Observer}s will trigger.
     *
     * @return A {@link LiveData} containing a map of work identifiers to their corresponding
     * {@link BaseWork.WorkStatus}
     */
    public abstract LiveData<Map<String, Integer>> getStatuses();

    /**
     * Enqueues the instance of {@link WorkContinuation} on the background thread.
     */
    public abstract void enqueue();

    protected abstract WorkContinuation then(List<Class<? extends Worker>> workerClasses);
}
