package main;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RatioTapChanger {
	
	private String rdfID;
	private String name;
	private double step;
	
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
	
	// -----Return step--- //
	public double step(Node node) {
		search(node);
		return step;
	}
	
	public Element search(Node node) {
		Element element = (Element) node;
		this.rdfID = element.getAttribute("rdf:ID");
		this.name = element.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
		this.step =  Double.parseDouble(element.getElementsByTagName("cim:TapChanger.normalStep").item(0).getTextContent());
		// System.out.println("rdfID: " + rdfID + "\n" + "objectName: " + name + "\n" +
		// "region_rdf:ID: " + region_rdfID + "\n");
		return element;
	}

}
