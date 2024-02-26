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

package org.gradlex.javaecosystem.capabilities.samples;

import org.gradle.exemplar.model.Command;
import org.gradle.exemplar.model.Sample;
import org.gradle.exemplar.test.runner.SampleModifier;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class PluginBuildLocationSampleModifier implements SampleModifier {
    @Override
    public Sample modify(Sample sampleIn) {
        Command cmd = sampleIn.getCommands().remove(0);
        File pluginProjectDir = new File(".");
        ArrayList<String> allArgs = new ArrayList<>(cmd.getArgs());
        allArgs.addAll(Arrays.asList("dependencies", "--configuration=compileClasspath", "-s", "-q", "-PpluginLocation=" + pluginProjectDir.getAbsolutePath()));
        if (ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")) {
            allArgs.add("-Dorg.gradle.debug=true");
        }
        sampleIn.getCommands().add(
                new Command(new File(pluginProjectDir, "./gradlew").getAbsolutePath(),
                        cmd.getExecutionSubdirectory(),
                        allArgs,
                        cmd.getFlags(),
                        cmd.getExpectedOutput(),
                        cmd.isExpectFailure(),
                        true,
                        cmd.isAllowDisorderedOutput(),
                        cmd.getUserInputs()));
        return sampleIn;
    }
}
