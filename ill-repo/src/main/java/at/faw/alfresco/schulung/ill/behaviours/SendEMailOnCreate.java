package at.faw.alfresco.schulung.ill.behaviours;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.cmr.site.SiteInfo;
import org.alfresco.service.cmr.site.SiteService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

import at.faw.alfresco.schulung.ill.model.IllModel;

public class SendEMailOnCreate implements NodeServicePolicies.OnCreateNodePolicy {

	private static final String ADMIN_USERNAME = "admin";
	
	// Dependencies
	private NodeService nodeService;
	private PolicyComponent policyComponent;
	private SiteService siteService;
	private PersonService personService;
	private AuthenticationService authenticationService;
	
	private Behaviour onCreateNode;
	
	//bind behaviour
	public void init(){
		 this.onCreateNode = new JavaBehaviour(this, "onCreateNode", NotificationFrequency.TRANSACTION_COMMIT);
		 
		 this.policyComponent.bindClassBehaviour(
				 QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateNode"), 
				 IllModel.TYPE_DELIVERY_FOLDER, 
				 this.onCreateNode);
	}
	
	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		NodeRef parent = childAssocRef.getParentRef();
		NodeRef node = childAssocRef.getChildRef();
		if(nodeService.exists(node)){
			NodeRef admin = personService.getPerson(ADMIN_USERNAME);
			String toEmail = (String) nodeService.getProperty(admin, ContentModel.PROP_EMAIL);
			
			NodeRef currentUser = personService.getPerson(authenticationService.getCurrentUserName());
			String fromEMail = (String) nodeService.getProperty(currentUser, ContentModel.PROP_EMAIL);
			
			StringBuilder emailContent = new StringBuilder();
			emailContent.append("FROM: " + fromEMail + "\n");
			emailContent.append("TO: " + toEmail + "\n");
			emailContent.append("Information: ");
			
			SiteInfo siteInfo = siteService.getSite(node);
			if(siteInfo != null){
				emailContent.append("In der Site " + siteInfo.getTitle());
			}else{
				String parentName = (String)nodeService.getProperty(parent, ContentModel.PROP_NAME);
				emailContent.append("Im Ordner " + parentName);
			}
			
			String nodeName = (String)nodeService.getProperty(node, ContentModel.PROP_NAME);
			emailContent.append(" wurde ein neuer Versandakt mit dem Namen " + nodeName + " angelegt");
			System.out.println(emailContent.toString());
		}
		
		
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	

}
