package plugin.doc.generate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import plugin.doc.generate.contact.SpringContact;
import plugin.doc.generate.definition.RestFulDefinition;
import plugin.doc.generate.delegate.DelegateFactory;
import plugin.doc.generate.delegate.GeneratorDelegate;
import plugin.doc.generate.generator.RestDocumentGenerator;
import plugin.doc.generate.parser.RestParser;
import plugin.doc.generate.utils.MyPsiSupport;
import plugin.doc.generate.utils.StringUtils;
import plugin.doc.generate.view.DocumentExportDialog;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerDocumentGenerator extends AnAction {

    private GeneratorDelegate delegate;

    private Set<PsiMethod> psiMethods;

    private GeneratorDelegate getDelegate() {
        if (delegate == null) {
            delegate = DelegateFactory.getGeneratorDelegate(this.getClass());
        }
        return delegate;
    }


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        List<RestFulDefinition> definitions = parseSelectedMethod(anActionEvent);
        if(definitions != null && definitions.size() != 0){
            StringBuffer builder = new StringBuffer();
            definitions.forEach(e->{
                RestDocumentGenerator generator = new RestDocumentGenerator(e);
                String content = generator.generate();
                builder.append(content);
                builder.append("\n");
            });
            openDialog(builder.toString());
        }
    }

    /**
     * 打开结果对话框
     *
     * @param result
     */
    protected void openDialog(String result) {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screensize.width * 0.3);
        int h = (int) (screensize.height * 0.3);

        DocumentExportDialog dialog = new DocumentExportDialog(result);
        dialog.setSize(w, h);
        dialog.pack();
        dialog.setLocation((int) (screensize.width * 0.5) - (int) (w * 0.5), (int) (screensize.height * 0.5) - (int) (h * 0.5));
        dialog.setVisible(true);

    }


    private List<RestFulDefinition> parseSelectedMethod(AnActionEvent anActionEvent){
        PsiJavaFile javaFile = MyPsiSupport.getPsiJavaFile(anActionEvent);
        //获得相应带有@RestController 的类
        List<PsiClass> targetClassList = MyPsiSupport.getPsiClasses(javaFile, SpringContact.ANNOTATION_RESCONTROLLER);
        this.psiMethods = this.getMethod(targetClassList);
        PsiMethod psiMethod = this.getPsiMethodSelecting(anActionEvent);
        if(psiMethod != null){
            RestParser parser = new RestParser(psiMethod);
            parser.parseDefinition();
            return Collections.singletonList(parser.getDefinition());
        }else {
            ArrayList<RestFulDefinition> list = new ArrayList<>();
            // 生成类文档
            return this.psiMethods.stream().map(e -> {
                RestParser parser = new RestParser(e);
                parser.parseDefinition();
                return parser.getDefinition();
            }).collect(Collectors.toList());
        }
    }

    private PsiMethod getPsiMethodSelecting(AnActionEvent anActionEvent) {
        String txt = MyPsiSupport.getSelectedText(anActionEvent);
        if (StringUtils.isEmpty(txt)) {
            return null;
        }
        for (PsiMethod method : this.psiMethods) {
            if (txt.trim().equals(method.getName())) {
                return method;
            }
        }
        return null;
    }


    private Set<PsiMethod> getMethod(List<PsiClass> classes) {
        List<PsiMethod> postMethods = MyPsiSupport.getPsiMethods(classes, SpringContact.ANNOTATION_POSTMAPPING);
        List<PsiMethod> requestMethods = MyPsiSupport.getPsiMethods(classes, SpringContact.ANNOTATION_REQUESTMAPPING);
        List<PsiMethod> getMethods = MyPsiSupport.getPsiMethods(classes, SpringContact.ANNOTATION_GETMAPPING);
        Set<PsiMethod> targetMethod = new HashSet<>();
        targetMethod.addAll(postMethods);
        targetMethod.addAll(getMethods);
        targetMethod.addAll(requestMethods);
        return targetMethod;
    }


    @Override
    public boolean isDumbAware() {
        return false;
    }


    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        this.getDelegate().doUpdate(e);
    }


}
