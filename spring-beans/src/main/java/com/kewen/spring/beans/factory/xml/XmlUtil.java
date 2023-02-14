package com.kewen.spring.beans.factory.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-13 16:45
 */
public class XmlUtil {
    public static Document creatDocument(String path) {
        // Spring.xml的内容为上面所示XML代码内容
        try {
            File file = new File(path);
            // 创建文档解析的对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析文档，形成文档树，也就是生成Document对象
            return builder.parse(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document creatDocument(InputStream inputStream) {
        // Spring.xml的内容为上面所示XML代码内容
        try {
            // 创建文档解析的对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析文档，形成文档树，也就是生成Document对象
            return builder.parse(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Element> getChildren(Element element){

        List<Element> children = new ArrayList<>();

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE){
                children.add((Element) item);
            }
        }
        return children;
    }

    /**
     * 模拟解析文档内容，完整的流程如下，项目中为拆分后的逻辑
     * @param document
     */
    public static void parseSample(Document document) {
        try {
            // 获得根节点<beans>
            Element rootElement = document.getDocumentElement();
            // 获得根节点下的所有子节点
            NodeList beanList = rootElement.getChildNodes();
            for (int i = 0; i < beanList.getLength(); i++) {
                // 获得第i个子节点
                Node beanNode = beanList.item(i);
                // 由于节点多种类型，而一般我们需要处理的是元素节点
                // 元素节点就是非空的子节点，也就是还有孩子的子节点
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    //<bean>
                    Element childElement = (Element) beanNode;
                    System.out.printf(" Element: %s\n", childElement.getNodeName());
                    System.out.printf("  Attribute: id = %s\n", childElement.getAttribute("id"));
                    System.out.printf("  Value: class= %s\n", childElement.getAttribute("class"));

                    // 获得第二级子元素
                    NodeList propertyList = childElement.getChildNodes();
                    for (int j = 0; j < propertyList.getLength(); j++) {
                        Node propertyNode = propertyList.item(j);
                        if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element propertyElement = (Element) propertyNode;
                            if (propertyElement.getNodeName().equals("properties")) {
                                String value = propertyElement.getAttribute("value");
                                String ref = propertyElement.getAttribute("ref");
                                if (value != null && value.length() > 0) {
                                    System.out.printf("  sub Element %s:  name= %s value= %s\n", propertyElement.getNodeName(),
                                            propertyElement.getAttribute("name"), value);
                                } else {
                                    System.out.printf("  sub Element %s:  name= %s ref= %s\n", propertyElement.getNodeName(), propertyElement.getAttribute("name"),
                                            ref);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
