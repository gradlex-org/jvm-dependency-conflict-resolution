package org.gradlex.jvm.dependency.conflict.test.patch

class AddAlignmentTest extends AbstractPatchTest {

    def "can add alignment"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    align(
                        "org.apache.poi:poi",
                        "org.apache.poi:poi-excelant",
                        "org.apache.poi:poi-ooxml",
                        "org.apache.poi:poi-scratchpad"
                    )
                }
            }
            dependencies {
                implementation("org.apache.poi:poi:5.2.5")
                implementation("org.apache.poi:poi-excelant")
                implementation("org.apache.poi:poi-ooxml")
                implementation("org.apache.poi:poi-scratchpad")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- org.apache.poi:poi:5.2.5
|    +--- commons-codec:commons-codec:1.16.0
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- org.apache.commons:commons-math3:3.6.1
|    +--- commons-io:commons-io:2.15.0
|    +--- com.zaxxer:SparseBitSet:1.3
|    +--- org.apache.logging.log4j:log4j-api:2.21.1
|    |    \\--- org.apache.logging.log4j:log4j-bom:2.21.1
|    |         \\--- org.apache.logging.log4j:log4j-api:2.21.1 (c)
|    +--- org.apache.poi:poi:5.2.5 (c)
|    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
+--- org.apache.poi:poi-excelant -> 5.2.5
|    +--- org.apache.ant:ant:1.10.14
|    |    \\--- org.apache.ant:ant-launcher:1.10.14
|    +--- org.apache.poi:poi-ooxml:5.2.5
|    |    +--- org.apache.poi:poi:5.2.5 (*)
|    |    +--- org.apache.poi:poi-ooxml-lite:5.2.5
|    |    |    \\--- org.apache.xmlbeans:xmlbeans:5.2.0
|    |    |         \\--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
|    |    +--- org.apache.xmlbeans:xmlbeans:5.2.0 (*)
|    |    +--- org.apache.commons:commons-compress:1.25.0
|    |    +--- commons-io:commons-io:2.15.0
|    |    +--- com.github.virtuald:curvesapi:1.08
|    |    +--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
|    |    +--- org.apache.commons:commons-collections4:4.4
|    |    +--- org.apache.poi:poi:5.2.5 (c)
|    |    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    |    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    |    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
|    +--- org.apache.poi:poi:5.2.5 (c)
|    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
+--- org.apache.poi:poi-ooxml -> 5.2.5 (*)
\\--- org.apache.poi:poi-scratchpad -> 5.2.5
     +--- org.apache.poi:poi:5.2.5 (*)
     +--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
     +--- org.apache.commons:commons-math3:3.6.1
     +--- commons-codec:commons-codec:1.16.0
     +--- org.apache.poi:poi:5.2.5 (c)
     +--- org.apache.poi:poi-excelant:5.2.5 (c)
     +--- org.apache.poi:poi-ooxml:5.2.5 (c)
     \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
'''
    }

    def "can add alignment via BOM"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    alignWithBom("org.ow2.asm:asm-bom",
                        "org.ow2.asm:asm",
                        "org.ow2.asm:asm-tree",
                        "org.ow2.asm:asm-analysis",
                        "org.ow2.asm:asm-util",
                        "org.ow2.asm:asm-commons"
                    )
                }
            }
            dependencies {
                implementation("org.ow2.asm:asm")
                implementation("org.ow2.asm:asm-tree:9.6")
                implementation("org.ow2.asm:asm-analysis")
                implementation("org.ow2.asm:asm-util")
                implementation("org.ow2.asm:asm-commons")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- org.ow2.asm:asm -> 9.6
|    \\--- org.ow2.asm:asm-bom:9.6
|         +--- org.ow2.asm:asm:9.6 (c)
|         +--- org.ow2.asm:asm-tree:9.6 (c)
|         +--- org.ow2.asm:asm-analysis:9.6 (c)
|         +--- org.ow2.asm:asm-util:9.6 (c)
|         \\--- org.ow2.asm:asm-commons:9.6 (c)
+--- org.ow2.asm:asm-tree:9.6
|    +--- org.ow2.asm:asm:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
+--- org.ow2.asm:asm-analysis -> 9.6
|    +--- org.ow2.asm:asm-tree:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
+--- org.ow2.asm:asm-util -> 9.6
|    +--- org.ow2.asm:asm:9.6 (*)
|    +--- org.ow2.asm:asm-tree:9.6 (*)
|    +--- org.ow2.asm:asm-analysis:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
\\--- org.ow2.asm:asm-commons -> 9.6
     +--- org.ow2.asm:asm:9.6 (*)
     +--- org.ow2.asm:asm-tree:9.6 (*)
     \\--- org.ow2.asm:asm-bom:9.6 (*)
'''
    }
}
