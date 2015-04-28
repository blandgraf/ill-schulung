package at.faw.alfresco.schulung.ill.model;

import org.alfresco.service.namespace.QName;

public interface IllModel {
	
	public static final String ILL_NAMESPACE = "http://www.ill.co.at/alfresco/model";
	
	public static final QName TYPE_ILL_FOLDER = QName.createQName(ILL_NAMESPACE, "illFolder");
	
	public static final QName TYPE_MATERIAL_FOLDER = QName.createQName(ILL_NAMESPACE, "materialFolder");
	
	public static final QName PROP_ME_ID = QName.createQName(ILL_NAMESPACE, "meId");
	
	public static final QName PROP_HANDOVER_DATE = QName.createQName(ILL_NAMESPACE, "handoverDate");
	
	public static final QName PROP_SHIPMENT_DATE = QName.createQName(ILL_NAMESPACE, "shipmentDate");
	
	public static final QName TYPE_DELIVERY_FOLDER = QName.createQName(ILL_NAMESPACE, "deliveryFolder");
	
	public static final QName PROP_ORDER_NUMBER = QName.createQName(ILL_NAMESPACE, "orderNumber");
	
	public static final QName PROP_PICKUP_DATE = QName.createQName(ILL_NAMESPACE, "pickupDate");
	
	public static final QName PROP_TRANSPORT_MODE = QName.createQName(ILL_NAMESPACE, "transportMode");
	
	public static final QName ASPECT_HAS_EXTERNAL_REFERENCE = QName.createQName(ILL_NAMESPACE, "hasExternalReference");
	
	public static final QName PROP_EXTERNAL_REFERENCE = QName.createQName(ILL_NAMESPACE, "externalReference");

}
