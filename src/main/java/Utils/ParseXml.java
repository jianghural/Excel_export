package Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author awl
 * @date 2023/6/11 19:33
 */
public class ParseXml {



    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        ParseXml getXmlDemo = new ParseXml();
        System.out.println(getXmlDemo.parseXML().toString());
    }

    public Map<String,Map<String,String>> parseXML() throws IOException, ParserConfigurationException, SAXException {
        Map<String,Map<String,String>> filesMap = new HashMap();

        Document document = this.getDocument();
        //获取根节点
        Element root = document.getDocumentElement();
        //获取根节点下的子节点集合
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Map<String,String> sheetsMap = new HashMap<>();
            String fileName = null;
            //获取父节点下每一个子节点
            Node item = childNodes.item(i);
            //判断该节点类型是否为元素节点。这里可以避免下一步操作因空格符导致的类转换失败
            if(item.getNodeType() == Node.ELEMENT_NODE){
                //将Node类型转换为Element类型
                Element ele = (Element) item;
                fileName = ele.getAttribute("fileName");
                sheetsMap = getChildNodes(ele.getChildNodes());
                filesMap.put(fileName,sheetsMap);
            }

        }
        return filesMap;
    }

    private Document getDocument() throws IOException, SAXException, ParserConfigurationException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sql.xml");
        //step1 创建 创建解析器工厂
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //step2 创建解析器对象 DoucmentBuilder可以将xml文件解析成一个Doument对象，通过这个对象获取数据
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        //step3 设置解析的xml文件
        Document document = documentBuilder.parse(inputStream);

        return document;
    }

    private void getNodes(Document document) {
        //获取根节点
        Element root = document.getDocumentElement();
        //获取根节点下的子节点集合
        NodeList childNodes = root.getChildNodes();
        //调用遍历子节点集合的方法
        getChildNodes(childNodes);
    }

    private Map<String,String> getChildNodes(NodeList childNodes) {
        Map<String,String> sheetsMap = new HashMap<>();
        String sheetName = null;
        String sqlValue = null;
        //遍历子节点集合
        for (int i = 0; i < childNodes.getLength(); i++) {
            //获取父节点下每一个子节点
            Node item = childNodes.item(i);
            //判断该节点类型是否为元素节点。这里可以避免下一步操作因空格符导致的类转换失败
            if(item.getNodeType() == Node.ELEMENT_NODE){
                //将Node类型转换为Element类型
                Element ele = (Element) item;
                for (int j = 0; j < ele.getChildNodes().getLength(); j++) {
                    Node childItem = ele.getChildNodes().item(j);
                    if(childItem.getNodeType() == Node.ELEMENT_NODE){
                        Element childEle = (Element) childItem;
                        if ("sheetName" == childEle.getTagName()) {
                            sheetName = childEle.getTextContent();
                        } else if ("sqlValue" == childEle.getTagName()) {
                            sqlValue = childEle.getTextContent();
                        }
                    }
                    if (sheetName != null && sqlValue != null){
                        sheetsMap.put(sheetName, sqlValue);
                    }
                }

            }
        }
//        if (sheetName != null && sqlValue != null) {
        //sheetsMap.put(sheetName, sqlValue);
//        }

        return sheetsMap;
    }




    /*public  GetXmlDemo(){

        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sql.xml");
            //step1 创建 创建解析器工厂
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //step2 创建解析器对象 DoucmentBuilder可以将xml文件解析成一个Doument对象，通过这个对象获取数据
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //step3 设置解析的xml文件
            Document document = documentBuilder.parse(inputStream);
            //step4 获取节点
            NodeList fileNodeList = document.getElementsByTagName("file");
            for (int i = 0; i < fileNodeList.getLength(); i++) {
                Node fileNode = fileNodeList.item(i);
                if (fileNode.getNodeType() == Node.ELEMENT_NODE){
                    Element fileElement = (Element) fileNode;
                    String fileName = fileElement.getAttribute("name");
                    System.out.println(fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/



}
