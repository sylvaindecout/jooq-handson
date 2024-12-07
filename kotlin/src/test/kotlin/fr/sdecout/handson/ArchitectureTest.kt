package fr.sdecout.handson

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.Tag

@Tag("Architecture")
@AnalyzeClasses(packages = ["fr.sdecout.handson"])
class ArchitectureTest {

    @ArchTest
    val layers: ArchRule = layeredArchitecture()
        .consideringAllDependencies()
        .layer("rest").definedBy("..rest..")
        .layer("persistence").definedBy("..persistence..")
        .whereLayer("persistence").mayNotBeAccessedByAnyLayer()
        .whereLayer("rest").mayOnlyBeAccessedByLayers("persistence")
        .`as`("Layers should be clearly defined")
        .because("we want to work on a single layer with no impact on the other ones.")

}
