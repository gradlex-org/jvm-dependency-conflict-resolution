// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.samples;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import org.gradle.exemplar.model.Command;
import org.gradle.exemplar.model.Sample;
import org.gradle.exemplar.test.runner.SampleModifier;

public class PluginBuildLocationSampleModifier implements SampleModifier {
    @Override
    public Sample modify(Sample sampleIn) {
        Command cmd = sampleIn.getCommands().remove(0);
        File pluginProjectDir = new File(".");
        ArrayList<String> allArgs = new ArrayList<>(cmd.getArgs());
        allArgs.addAll(Arrays.asList(
                "dependencies",
                "--configuration=compileClasspath",
                "-s",
                "-q",
                "-PpluginLocation=" + pluginProjectDir.getAbsolutePath()));
        if (ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")) {
            allArgs.add("-Dorg.gradle.debug=true");
        }
        sampleIn.getCommands()
                .add(new Command(
                        new File(pluginProjectDir, "./gradlew").getAbsolutePath(),
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
