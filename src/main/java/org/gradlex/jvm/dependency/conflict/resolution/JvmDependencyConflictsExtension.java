/*
 * Copyright the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.jvm.dependency.conflict.resolution;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;

public abstract class JvmDependencyConflictsExtension {

    private final ConflictResolution conflictResolution;
    private final Logging logging;
    private final Patch patch;
    private final ConsistentResolution consistentResolution;

    @Inject
    public JvmDependencyConflictsExtension(SourceSetContainer sourceSets) {
        conflictResolution = getObjects().newInstance(ConflictResolution.class);
        logging = getObjects().newInstance(Logging.class);
        patch = getObjects().newInstance(Patch.class);
        consistentResolution = getObjects().newInstance(ConsistentResolution.class, sourceSets);
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    public ConflictResolution getConflictResolution() {
        return conflictResolution;
    }

    public void conflictResolution(Action<ConflictResolution> action) {
        action.execute(conflictResolution);
    }

    public Logging getLogging() {
        return logging;
    }

    public void logging(Action<Logging> action) {
        action.execute(logging);
    }

    public Patch getPatch() {
        return patch;
    }

    public void patch(Action<Patch> action) {
        action.execute(patch);
    }

    public ConsistentResolution getConsistentResolution() {
        return consistentResolution;
    }

    public void consistentResolution(Action<ConsistentResolution> action) {
        action.execute(consistentResolution);
    }
}
