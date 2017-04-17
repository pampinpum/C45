package universidad.distrital.c45.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import universidad.distrital.c45.estructura.Nodo;

public class GexfGenerator {
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	Map<String, HashMap<String, String>> mapaArbol;
	int chambonada = 0;
	
	
	public GexfGenerator(){
		dbFactory = DocumentBuilderFactory.newInstance();
        try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        doc = dBuilder.newDocument();
        mapaArbol = new HashMap<String, HashMap<String, String>>();
	}

	private void generarMapa(Nodo arbol){
		if(arbol.id != null){
			HashMap<String, String> aristas;
			if(arbol.id.equals("total"))
			{
				aristas = new HashMap<String, String>();
				aristas.put(arbol.valor,null);
				mapaArbol.put(arbol.id + chambonada, aristas);
				chambonada++;
			} else {
			
				if(mapaArbol.containsKey(arbol.id)){
					aristas = mapaArbol.get(arbol.id);
					aristas.put(arbol.valor,(arbol.hijos.get(0).id.equals("total")) ? arbol.hijos.get(0).id + chambonada : arbol.hijos.get(0).id);
				} 
				else {
					aristas = new HashMap<String, String>();
					aristas.put(arbol.valor, (arbol.hijos.get(0).id.equals("total")) ? arbol.hijos.get(0).id + chambonada : arbol.hijos.get(0).id);
					mapaArbol.put(arbol.id, aristas);
				}
			}
		}
		
		 for(Nodo nodo : arbol.hijos)
			 generarMapa(nodo); 
		
	}
	
	private void recorrerMapa(Element nodes, Element edges) {		
		for (Map.Entry<String, HashMap<String, String>> entry : mapaArbol.entrySet())
		{
			Element node = doc.createElement("node");
			Attr nodeId = doc.createAttribute("id");
	        nodeId.setValue(entry.getKey());
	        node.setAttributeNode(nodeId);
	        Attr nodeLabel = doc.createAttribute("label");
	        if(entry.getKey().substring(0,5).equals("total"))
	        	nodeLabel.setValue(entry.getValue().entrySet().iterator().next().getKey());
	        else
	        	nodeLabel.setValue(entry.getKey());
	        

	        node.setAttributeNode(nodeLabel);
	        nodes.appendChild(node);
	        
	        for (Map.Entry<String, String> arista : entry.getValue().entrySet())
			{
	        	if(arista.getValue() != null){
	        	 Element edge = doc.createElement("edge");
		         Attr edgeId = doc.createAttribute("id");
		         edgeId.setValue(arista.getKey() + "-" + arista.getValue());
		         edge.setAttributeNode(edgeId);
		         Attr edgeSource = doc.createAttribute("source");
		         edgeSource.setValue(entry.getKey());
		         edge.setAttributeNode(edgeSource);
		         Attr edgeTarget = doc.createAttribute("target");
		         edgeTarget.setValue(arista.getValue());
		         edge.setAttributeNode(edgeTarget);
		         Attr edgeLabel = doc.createAttribute("label");
		         edgeLabel.setValue(arista.getKey());
		         edge.setAttributeNode(edgeLabel);
		         edges.appendChild(edge);
	        	}
			}
		}
	
	}
	
	public void generar(Nodo arbol, String conjuntoDatos, String precision){
	 try {

         // root element
         Element rootElement = doc.createElement("gexf");
         doc.appendChild(rootElement);
         Attr xmlns = doc.createAttribute("xmlns");
         xmlns.setValue("http://www.gexf.net/1.2draft");
         rootElement.setAttributeNode(xmlns);
         Attr version = doc.createAttribute("version");
         version.setValue("1.2");
         rootElement.setAttributeNode(version);
         
         //meta element
         Element meta = doc.createElement("meta");
         rootElement.appendChild(meta);
         Attr lastModDate = doc.createAttribute("lastmodifieddate");
         lastModDate.setValue("2017-04-07");
         meta.setAttributeNode(lastModDate);
         Element creator = doc.createElement("creator");
         creator.setTextContent("Niko");
         meta.appendChild(creator);
         Element description = doc.createElement("description");
         description.setTextContent("C4,5 ex");
         meta.appendChild(description);
         
         //graph element
         Element graph = doc.createElement("graph");
         rootElement.appendChild(graph);
         Attr mode = doc.createAttribute("mode");
         mode.setValue("static");
         graph.setAttributeNode(mode);
         Attr defaultedgetype = doc.createAttribute("defaultedgetype");
         defaultedgetype.setValue("directed");
         graph.setAttributeNode(defaultedgetype);
         
         //node element
         Element nodes = doc.createElement("nodes");
         graph.appendChild(nodes);
         
         //edge element
         Element edges = doc.createElement("edges");
         graph.appendChild(edges);
         
         generarMapa(arbol);
         recorrerMapa(nodes, edges);

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File("GefxModel" + conjuntoDatos + precision + ".gexf"));
         transformer.transform(source, result);
         // Output to console for testing
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);
      } catch (Exception e) {
         e.printStackTrace();
      }
	}
}

