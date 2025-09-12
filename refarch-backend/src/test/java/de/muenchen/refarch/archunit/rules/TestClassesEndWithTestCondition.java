package de.muenchen.refarch.archunit.rules;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Optional;

public class TestClassesEndWithTestCondition extends ArchCondition<JavaMethod> {

    TestClassesEndWithTestCondition() {
        super("have top enclosing class name ending with `Test`");
    }

    public static final ArchCondition<JavaMethod> haveTopEnclosingClassEndingWithTest = new TestClassesEndWithTestCondition();

    @Override
    public void check(JavaMethod method, ConditionEvents events) {
        var topEnclosingClass = getTopEnclosingClass(method.getOwner());

        if (topEnclosingClass.isPresent() && !topEnclosingClass.get().getFullName().endsWith("Test")) {
            events.add(SimpleConditionEvent.violated(method, "test " + method.getName() + " is not inside of test class"));
        }
    }

    private Optional<JavaClass> getTopEnclosingClass(JavaClass item) {
        JavaClass enclosingClass = null;
        while (item.getEnclosingClass().isPresent()) {
            item = item.getEnclosingClass().orElseThrow();
            enclosingClass = item;
        }

        enclosingClass = enclosingClass == null ? item : enclosingClass;
        return Optional.of(enclosingClass);
    }
}
