// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import java.util.Arrays;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.model.ObjectFactory;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddAlignmentConstraintsMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddBomDependencyMetadataRule;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Patch {

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract DependencyHandler getDependencies();

    /**
     * Adjust metadata of the given 'module'.
     */
    public PatchModule module(String module) {
        return getObjects().newInstance(PatchModule.class, module);
    }

    /**
     * Adjust metadata of the given 'module'.
     */
    public void module(String module, Action<PatchModule> action) {
        action.execute(getObjects().newInstance(PatchModule.class, module));
    }

    /**
     * Align the versions of all 'modules' by adding a platform dependency to each module that points at the given 'bom'.
     */
    public void alignWithBom(String bom, String... modules) {
        for (String module : modules) {
            getDependencies()
                    .getComponents()
                    .withModule(module, AddBomDependencyMetadataRule.class, r -> r.params(bom));
        }
    }

    /**
     * Align the versions of all 'modules' by adding version constraints for all modules to each module.
     */
    public void align(String... modules) {
        for (String module : modules) {
            getDependencies()
                    .getComponents()
                    .withModule(
                            module, AddAlignmentConstraintsMetadataRule.class, r -> r.params(Arrays.asList(modules)));
        }
    }
}
