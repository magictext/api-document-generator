package plugin.doc.generate.delegate;

import plugin.doc.generate.ControllerDocumentGenerator;
import plugin.doc.generate.delegate.impl.ControllerDocumentDelegate;
import plugin.doc.generate.delegate.impl.DefaultDelegateFactory;

public class DelegateFactory {

    public static GeneratorDelegate getGeneratorDelegate(Class clazz) {
        if (clazz.equals(ControllerDocumentGenerator.class)) {
            return new ControllerDocumentDelegate();
        }
        return new DefaultDelegateFactory();
    }

}
