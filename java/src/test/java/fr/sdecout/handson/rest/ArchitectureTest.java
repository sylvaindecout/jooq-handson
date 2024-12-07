package fr.sdecout.handson.rest;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Tag;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@Tag("Architecture")
@AnalyzeClasses(packages = "fr.sdecout.handson.rest")
class ArchitectureTest {

    @ArchTest
    private static final ArchRule should_be_free_of_cyclic_dependencies = slices()
            .matching("fr.sdecout.handson.rest.(*)..").namingSlices("'$1' controller")
            .should().beFreeOfCycles()
            .as("REST controllers should not depend on one another");

    @ArchTest
    private static final ArchRule should_not_depend_on_JPA_or_jOOQ = classes()
        .that().resideInAPackage("fr.sdecout.handson.rest..")
        .should().onlyDependOnClassesThat().resideOutsideOfPackages(
            "org.springframework.data.jpa..",
            "jakarta.persistence..",
            "org.jooq.."
        )
        .as("REST controllers should not depend on persistence technology")
        .because("we want them to remain the same when we replace JPA with jOOQ.");

}

