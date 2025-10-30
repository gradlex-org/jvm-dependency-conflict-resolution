// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.samples;

import org.gradle.exemplar.test.runner.SampleModifiers;
import org.gradle.exemplar.test.runner.SamplesRoot;
import org.gradle.exemplar.test.runner.SamplesRunner;
import org.junit.runner.RunWith;

@RunWith(SamplesRunner.class)
@SamplesRoot("samples")
@SampleModifiers(PluginBuildLocationSampleModifier.class)
public class SamplesTest {}
