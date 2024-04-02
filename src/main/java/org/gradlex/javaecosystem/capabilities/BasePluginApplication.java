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

package org.gradlex.javaecosystem.capabilities;

import org.gradle.api.Project;
import org.gradle.api.initialization.resolve.RulesMode;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.SettingsInternal;

final class BasePluginApplication {

    static BasePluginApplication of(Project project) {
        return new BasePluginApplication(project);
    }

    private final Project project;

    private BasePluginApplication(Project project) {
        this.project = project;
    }

    void handleRulesMode() {
        boolean baseAppliedViaSettings = isBaseAppliedViaSettings();
        RulesMode rulesMode = getRulesMode();
        String settingsFileName = getSettings().getSettingsScript().getFileName();
        switch(rulesMode) {
            // PREFER_PROJECT is the default if the user did not configure something else
            case PREFER_PROJECT:
                project.getPlugins().apply(JvmConflictDetectionPlugin.class);
                break;
            case PREFER_SETTINGS:
            case FAIL_ON_PROJECT_RULES:
                if (!baseAppliedViaSettings) {
                    throw new IllegalStateException(
                            "RulesMode is set to " + rulesMode + " in " + settingsFileName + " but the '" + JvmConflictDetectionPlugin.PLUGIN_ID + "' plugin was not applied via settings." +
                                    " As a result this plugin will not work." +
                                    " Fix this problem by either changing dependencyResolutionManagement.rulesMode to PREFER_PROJECT or by applying '" + JvmConflictDetectionPlugin.PLUGIN_ID + "' as a settings plugin in " + settingsFileName + "."
                    );
                }
                break;
            default:
                throw new IllegalStateException("Unknown RulesMode value '" + rulesMode + "'");
        }
    }

    private boolean isBaseAppliedViaSettings() {
        return getSettings().getPlugins().hasPlugin(JvmConflictDetectionPlugin.class);
    }

    private RulesMode getRulesMode() {
        return getSettings().getDependencyResolutionManagement().getRulesMode().get();
    }

    private SettingsInternal getSettings() {
        return ((GradleInternal) project.getGradle()).getSettings();
    }
}
