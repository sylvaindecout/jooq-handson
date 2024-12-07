package fr.sdecout.handson.rest

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.Tag

@Tag("Architecture")
@AnalyzeClasses(packages = ["fr.sdecout.handson.rest"])
class ArchitectureTest {

    @ArchTest
    val `should be free of cyclic dependencies`: ArchRule = slices()
        .matching("fr.sdecout.handson.rest.(*)..").namingSlices("'$1' controller")
        .should().beFreeOfCycles()
        .`as`("REST controllers should not depend on one another")

    @ArchTest
    val `should not depend on JPA or jOOQ`: ArchRule = classes()
        .that().resideInAPackage("fr.sdecout.handson.rest..")
        .should().onlyDependOnClassesThat().resideOutsideOfPackages(
            "org.springframework.data.jpa..",
            "jakarta.persistence..",
            "org.jooq..",
        )
        .`as`("REST controllers should not depend on persistence technology")
        .because("we want them to remain the same when we replace JPA with jOOQ.")

}
