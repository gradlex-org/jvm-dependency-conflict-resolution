// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSetContainer;

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
