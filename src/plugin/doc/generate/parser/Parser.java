package plugin.doc.generate.parser;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import plugin.doc.generate.utils.MyPsiSupport;

public abstract class Parser {

    abstract public String parseDefinition();

    public PsiType getRealType(PsiType psiType, PsiField psiField) {
        PsiType fieldType = MyPsiSupport.getGenericsType(psiType, psiField);
        if (fieldType == null) {
            fieldType = psiField.getType();
        }
        return fieldType;
    }

}
