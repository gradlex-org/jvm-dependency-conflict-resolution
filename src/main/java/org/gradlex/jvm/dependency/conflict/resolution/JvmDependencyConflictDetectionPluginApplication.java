// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import org.gradle.api.Project;
import org.gradle.api.initialization.resolve.RulesMode;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.SettingsInternal;
import org.gradlex.jvm.dependency.conflict.detection.JvmDependencyConflictDetectionPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class JvmDependencyConflictDetectionPluginApplication {

    private static final String PLUGIN_ID = "org.gradlex.jvm-dependency-conflict-detection";

    static JvmDependencyConflictDetectionPluginApplication of(Project project) {
        return new JvmDependencyConflictDetectionPluginApplication(project);
    }

    private final Project project;

    private JvmDependencyConflictDetectionPluginApplication(Project project) {
        this.project = project;
    }

    void handleRulesMode() {
        boolean baseAppliedViaSettings = isBaseAppliedViaSettings();
        RulesMode rulesMode = getRulesMode();
        String settingsFileName = getSettings().getSettingsScript().getFileName();
        switch (rulesMode) {
            // PREFER_PROJECT is the default if the user did not configure something else
            case PREFER_PROJECT:
                project.getPlugins().apply(JvmDependencyConflictDetectionPlugin.class);
                break;
            case PREFER_SETTINGS:
            case FAIL_ON_PROJECT_RULES:
                if (!baseAppliedViaSettings) {
                    throw new IllegalStateException("RulesMode is set to " + rulesMode + " in " + settingsFileName
                            + " but the '" + PLUGIN_ID + "' plugin was not applied via settings."
                            + " As a result this plugin will not work."
                            + " Fix this problem by either changing dependencyResolutionManagement.rulesMode to PREFER_PROJECT or by applying '"
                            + PLUGIN_ID + "' as a settings plugin in " + settingsFileName + ".");
                }
                break;
            default:
                throw new IllegalStateException("Unknown RulesMode value '" + rulesMode + "'");
        }
    }

    private boolean isBaseAppliedViaSettings() {
        return getSettings().getPlugins().hasPlugin(JvmDependencyConflictDetectionPlugin.class);
    }

    private RulesMode getRulesMode() {
        return getSettings().getDependencyResolutionManagement().getRulesMode().get();
    }

    private SettingsInternal getSettings() {
        return ((GradleInternal) project.getGradle()).getSettings();
    }
}
