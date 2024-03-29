package plugin.doc.generate.parser;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import plugin.doc.generate.parser.translator.TypeInstanceTranslator;

public class ParamsJsonParser extends Parser {
    private PsiParameter[] psiParameter;
    private String example;

    public ParamsJsonParser(PsiParameter[] psiParameter) {
        this.psiParameter = psiParameter;
    }

    @Override
    public String parseDefinition() {
        JSONObject obj = JSONUtil.createObj();
        for (PsiParameter parameter : psiParameter) {
            PsiType type = parameter.getType();
            String canonicalText = type.getCanonicalText();
            Object translate = TypeInstanceTranslator.translate(canonicalText);
            if (translate.equals("")) continue;
            String name = parameter.getName();
            obj.put(name, translate);
        }
        return obj.toString();
    }

    public String getExample() {
        return example;
    }
}
