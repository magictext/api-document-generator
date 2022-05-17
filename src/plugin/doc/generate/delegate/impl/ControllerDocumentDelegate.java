package plugin.doc.generate.delegate.impl;

import plugin.doc.generate.checker.impl.EditorAvailableChecker;
import plugin.doc.generate.checker.impl.JavaFileChecker;
import plugin.doc.generate.checker.impl.SpringControllerChecker;
import plugin.doc.generate.delegate.GeneratorDelegate;

public class ControllerDocumentDelegate extends GeneratorDelegate {

    public ControllerDocumentDelegate() {
        this.addChecker(new EditorAvailableChecker());
        this.addChecker(new JavaFileChecker());
        this.addChecker(new SpringControllerChecker());
    }

}
