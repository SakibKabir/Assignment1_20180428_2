package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PowerTransformerEnd {
	private String rdfID;
	private String name;
	private double r;
	private double x;
	private String Transformer_rdfID;
	private String baseVoltage_rdfID;
	
	// --------Return rdfID------//
	public String rdfID(Node node) {
		search(node);
		return rdfID;
	}

	// --------Return name-------//
	public String name(Node node) {
		search(node);
		return name;
	}
	
	// --------Return r------//
	public double r(Node node) {
		search(node);
		return r;
	}

	// --------Return x-------//
	public double x(Node node) {
		search(node);
		return x;
	}
	
	// --------Return Transformer_rdfID------//
	public String Transformer_rdfID(Node node) {
		search(node);
		return Transformer_rdfID;
	}

	// --------Return baseVoltage_rdfID-------//
	public String baseVoltage_rdfID(Node node) {
		search(node);
		return baseVoltage_rdfID;
	}
	//---------Search the node----//
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		
		this.r =  Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.r").item(0).getTextContent());
		this.x =  Double.parseDouble(element.getElementsByTagName("cim:PowerTransformerEnd.x").item(0).getTextContent());
		
		this.Transformer_rdfID = element.getElementsByTagName("cim:PowerTransformerEnd.PowerTransformer").item(0).getAttributes().item(0).getTextContent();
		this.baseVoltage_rdfID = element.getElementsByTagName("cim:TransformerEnd.BaseVoltage").item(0).getAttributes().item(0).getTextContent();
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}
}
