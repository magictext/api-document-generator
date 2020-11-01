package plugin.doc.generate.checker.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiJavaFile;
import plugin.doc.generate.checker.EventChecker;
import plugin.doc.generate.utils.MyPsiSupport;

public class JavaFileChecker implements EventChecker {
    @Override
    public boolean check(AnActionEvent event) {
        //不是JAVA类型不显示
        PsiJavaFile javaFile = MyPsiSupport.getPsiJavaFile(event);
        return javaFile != null;
    }
}
