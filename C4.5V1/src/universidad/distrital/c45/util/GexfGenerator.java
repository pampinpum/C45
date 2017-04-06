package universidad.distrital.c45.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import universidad.distrital.c45.estructura.Nodo;

public class GexfGenerator {

	
	public void generar(Nodo arbol){
	 try {
		 //for each hijo in arbol 
		 //toma el id y crea un nodo 
		 //toma el valor y crea un edge hacia el hijo
		 
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();
         // root element
         Element rootElement = doc.createElement("gexf");
         doc.appendChild(rootElement);
         Attr xmlns = doc.createAttribute("xmlns");
         xmlns.setValue("http://www.gexf.net/1.2draft");
         rootElement.setAttributeNode(xmlns);
         Attr version = doc.createAttribute("version");
         version.setValue("1.2");
         rootElement.setAttributeNode(version);
         
         //graph element
         Element graph = doc.createElement("graph");
         rootElement.appendChild(graph);
         Attr mode = doc.createAttribute("mode");
         mode.setValue("static");
         graph.setAttributeNode(mode);
         Attr defaultedgetype = doc.createAttribute("defaultedgetype");
         defaultedgetype.setValue("directed");
         graph.setAttributeNode(defaultedgetype);
         
         
         Element nodes = doc.createElement("nodes");
         graph.appendChild(nodes);
         
         Element node = doc.createElement("node");
         Attr nodeId = doc.createAttribute("id");
         nodeId.setValue("0");
         node.setAttributeNode(nodeId);
         Attr nodeLabel = doc.createAttribute("label");
         nodeLabel.setValue("Outlook");
         node.setAttributeNode(nodeLabel);
         nodes.appendChild(node);
         
         Element edges = doc.createElement("edges");
         graph.appendChild(edges);
         Element edge = doc.createElement("edge");
         Attr edgeId = doc.createAttribute("id");
         edgeId.setValue("0");
         edge.setAttributeNode(edgeId);
         Attr edgeSource = doc.createAttribute("source");
         edgeSource.setValue("0");
         edge.setAttributeNode(edgeSource);
         Attr edgeTarget = doc.createAttribute("target");
         edgeTarget.setValue("1");
         edge.setAttributeNode(edgeTarget);
         Attr edgeLabel = doc.createAttribute("label");
         edgeLabel.setValue("Sunny");
         edge.setAttributeNode(edgeLabel);
         
         edges.appendChild(edge);         

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File("cars.gexf"));
         transformer.transform(source, result);
         // Output to console for testing
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);
      } catch (Exception e) {
         e.printStackTrace();
      }
	}
}

