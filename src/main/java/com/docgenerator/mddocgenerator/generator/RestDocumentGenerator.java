package com.docgenerator.mddocgenerator.generator;

import com.docgenerator.mddocgenerator.definition.FieldDefinition;
import com.docgenerator.mddocgenerator.definition.RestFulDefinition;

import java.util.List;

public class RestDocumentGenerator {

    private final RestFulDefinition definition;

    public RestDocumentGenerator(RestFulDefinition definition) {
        this.definition = definition;
    }

    public String generate() {
        StringBuffer docContent = new StringBuffer();
        docContent.append(interfaceNamePart());
        docContent.append("\n");
        docContent.append(urlPart());
        docContent.append("\n");
        docContent.append(methodPart());
        docContent.append("\n");
        docContent.append("\n");
        docContent.append(requestPart());
        docContent.append("\n");
        docContent.append(requestExample());
        docContent.append("\n");
        docContent.append(responsePart());
        docContent.append("\n");
        docContent.append(responseExample());
        return docContent.toString();
    }

    private String responseExample() {
        return "```json \n" + definition.getResponseExample() + "\n ```\n";
    }

    private String requestExample() {
        String requestExample = definition.getRequestExample();
        if (requestExample == null) {
            return "";
        }
        return "```json \n" + requestExample + "\n ```\n";
    }

    public String interfaceNamePart() {
        return "## " + this.definition.getDesc() + "\n";
    }

    public String urlPart() {
        return "> ����URL��`" + this.definition.getUri() + "`\n";
    }

    public String methodPart() {
        return "> ����ʽ��`" + this.definition.getHttpMethod().toUpperCase() + "`,`" + this.definition.getRequestBodyType().toString() + "`\n";
    }

    public String requestPart() {
        StringBuffer stringBuffer = new StringBuffer("### �������\n \n");
        List<FieldDefinition> fieldDefinitions = this.definition.getRequest();
        if (fieldDefinitions == null || fieldDefinitions.isEmpty()) {
            stringBuffer.append("�޲���\n");
            return stringBuffer.toString();
        }
        stringBuffer.append("|������|����|˵��|��ѡ|\n");
        stringBuffer.append("|:----    |:---|:----- |-----   |\n");
        stringBuffer.append(this.requestDefinitionTableBody(fieldDefinitions));
        return stringBuffer.toString();
    }

    public String responsePart() {
        StringBuffer stringBuffer = new StringBuffer("### ���ز���\n \n");
        stringBuffer.append("|������|����|˵��|\n");
        stringBuffer.append("|:----   |:----- |-----   |\n");
        List<FieldDefinition> fieldDefinitions = this.definition.getResponse();
        stringBuffer.append(this.responseDefinitionTableBody(fieldDefinitions));
        return stringBuffer.toString();
    }

    public String responseDefinitionTableBody(List<FieldDefinition> fieldDefinitions) {
        StringBuffer stringBuffer = new StringBuffer();
        if (fieldDefinitions == null || fieldDefinitions.isEmpty()) {
            return stringBuffer.toString();
        }
        for (FieldDefinition definition : fieldDefinitions) {
            String layerChat = this.getLayerChat(definition.getLayer());
            stringBuffer.append("|" + layerChat + definition.getName());
            stringBuffer.append("|" + definition.getType());
            stringBuffer.append("|" + definition.getDesc());
            stringBuffer.append("|\n");
            if (definition.getSubFieldDefinitions() != null && !definition.getSubFieldDefinitions().isEmpty()) {
                stringBuffer.append(this.responseDefinitionTableBody(definition.getSubFieldDefinitions()));
            }
        }
        return stringBuffer.toString();
    }

    public String requestDefinitionTableBody(List<FieldDefinition> fieldDefinitions) {
        StringBuffer stringBuffer = new StringBuffer();
        if (fieldDefinitions == null || fieldDefinitions.isEmpty()) {
            return stringBuffer.toString();
        }
        for (FieldDefinition definition : fieldDefinitions) {
            String layerChat = this.getLayerChat(definition.getLayer());
            stringBuffer.append("|" + layerChat + definition.getName());
            stringBuffer.append("|" + definition.getType());
            stringBuffer.append("|" + definition.getDesc());
            stringBuffer.append("|" + (definition.isRequire() ? "��" : ""));
            stringBuffer.append("|\n");
            if (definition.getSubFieldDefinitions() != null && !definition.getSubFieldDefinitions().isEmpty()) {
                stringBuffer.append(this.responseDefinitionTableBody(definition.getSubFieldDefinitions()));
            }
        }
        return stringBuffer.toString();
    }

    public String getLayerChat(int layer) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < layer; i++) {
            stringBuffer.append("--");
        }
        return stringBuffer.toString();
    }


}
