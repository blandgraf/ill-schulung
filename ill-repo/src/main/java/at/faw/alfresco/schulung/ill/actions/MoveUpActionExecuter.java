package at.faw.alfresco.schulung.ill.actions;

import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

public class MoveUpActionExecuter extends ActionExecuterAbstractBase {

	public static final String NAME = "ill.actions.move-up";
	public static final String PARAM_LEVELS = "levels";

	private NodeService nodeService;
	
	// setter for dependency injection
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		int levels = (Integer)action.getParameterValue(PARAM_LEVELS);
		if (this.nodeService.exists(actionedUponNodeRef)) {
			NodeRef parent = this.nodeService.getPrimaryParent(actionedUponNodeRef).getParentRef();
			for(int i = 0; i < levels; i++){
				parent = this.nodeService.getPrimaryParent(parent).getParentRef();
			}

			if(this.nodeService.exists(parent)){
				String nodeName = (String)nodeService.getProperty(actionedUponNodeRef, ContentModel.PROP_NAME);
				this.nodeService.moveNode(actionedUponNodeRef, 
						parent, 
						ContentModel.ASSOC_CONTAINS, 
						QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, nodeName));
			}
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_LEVELS,
                DataTypeDefinition.INT, true,
                getParamDisplayLabel(PARAM_LEVELS)));

	}

}
