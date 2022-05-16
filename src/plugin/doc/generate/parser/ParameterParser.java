package plugin.doc.generate.parser;

import com.intellij.psi.*;
import plugin.doc.generate.contact.CommonContact;
import plugin.doc.generate.contact.MyContact;
import plugin.doc.generate.definition.FieldDefinition;
import plugin.doc.generate.parser.translator.TypeTranslator;
import plugin.doc.generate.utils.JavaDocUtils;
import plugin.doc.generate.utils.MyPsiSupport;

import java.util.ArrayList;
import java.util.List;

public class ParameterParser extends Parser {

    private final PsiParameter[] psiParameters;
    private final PsiMethod psiMethod;
    private List<FieldDefinition> fieldDefinitions = new ArrayList<>();

    public ParameterParser(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        this.psiParameters = psiMethod.getParameterList().getParameters();
    }

    public List<FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public String parseDefinition() {

        if (psiParameters == null || psiParameters.length == 0) {
            return null;
        }
        /*PsiParameter firstParamter = this.psiParameters[0];
        PsiClass psiClass = MyPsiSupport.getPsiClass(firstParamter.getType());
        if (psiClass != null && TypeTranslator.docTypeTranslate(psiClass.getQualifiedName()).equals(TypeTranslator.TYPE_OBJ)) {
            ObjectParser objectParser = new ObjectParser(firstParamter.getType(), firstParamter.getProject(), 0);
            objectParser.parseDefinition();
            this.fieldDefinitions = objectParser.getFieldDefinitions();
            return null;
        }*/
        doParse();
        return null;
    }

    public void doParse(){
        if (psiParameters == null || psiParameters.length == 0) {
            return;
        }
        for (PsiParameter psiParameter : this.psiParameters){
            FieldDefinition definition = this.parseSingleParameterDefinition(psiParameter);
            if(definition!=null){
                this.fieldDefinitions.add(definition);
            }
        }
    }


    public FieldDefinition parseSingleParameterDefinition(PsiParameter psiParameter){
        if(psiParameter == null){
            return null;
        }
        String paramName = psiParameter.getName();
        String desc = JavaDocUtils.getParamsDesc(psiMethod.getDocComment(),paramName);
        FieldDefinition definition = new FieldDefinition();
        definition.setName(paramName);
        definition.setLayer(0);
        definition.setDesc(desc);
        PsiType fieldType = psiParameter.getType();
        PsiClass fieldClass = MyPsiSupport.getPsiClass(fieldType);
        if (fieldClass == null) {
            definition.setType(TypeTranslator.docTypeTranslate(fieldType.getCanonicalText()));
        } else {
            definition.setType(TypeTranslator.docTypeTranslate(fieldClass.getQualifiedName()));
        }
        boolean require = MyPsiSupport.getPsiAnnotation(psiParameter, MyContact.VALIDATOR_NOTEMPTYCHECK) != null;
        if (!require) {
            require = MyPsiSupport.getPsiAnnotation(psiParameter, CommonContact.CONSTRAINTS_NOTNULL) != null;
        }
        definition.setRequire(require);
        return definition;
    }
}
