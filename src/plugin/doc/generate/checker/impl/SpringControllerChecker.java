package plugin.doc.generate.checker.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import plugin.doc.generate.checker.EventChecker;
import plugin.doc.generate.utils.MyPsiSupport;

public class SpringControllerChecker implements EventChecker {

    @Override
    public boolean check(AnActionEvent event) {
        PsiClass[] psiClasses = MyPsiSupport.getPsiClass(event);
        if(psiClasses == null){
            return false;
        }
        boolean hasEntityAnnotation = false;
        for (PsiClass pClass : psiClasses) {
            for (PsiAnnotation psiAnnotation : pClass.getAnnotations()) {
                if (psiAnnotation.getQualifiedName().equals("org.springframework.web.bind.annotation.RestController")) {
                    hasEntityAnnotation = true;
                }
            }
        }
        return hasEntityAnnotation;
    }
}
