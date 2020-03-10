package bo.com.ahosoft.visit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("bo.com.ahosoft.visit");

        noClasses()
            .that()
                .resideInAnyPackage("bo.com.ahosoft.visit.service..")
            .or()
                .resideInAnyPackage("bo.com.ahosoft.visit.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..bo.com.ahosoft.visit.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
