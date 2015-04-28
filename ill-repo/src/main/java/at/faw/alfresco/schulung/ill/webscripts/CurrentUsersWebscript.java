package at.faw.alfresco.schulung.ill.webscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.alfresco.repo.security.authentication.TicketComponent;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CurrentUsersWebscript extends DeclarativeWebScript{
	
	private TicketComponent ticketComponent;
	
	public void setTicketComponent(TicketComponent ticketComponent) {
		this.ticketComponent = ticketComponent;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache){
		Map<String, Object> model = new HashMap<String, Object>();
		
		Set<String> usernames = ticketComponent.getUsersWithTickets(true);
		model.put("users", usernames);
		
		return model;
	}

}
