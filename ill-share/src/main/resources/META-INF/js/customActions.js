(function(){

	var Dom = YAHOO.util.Dom,
	Event = YAHOO.util.Event;

	var $html = Alfresco.util.encodeHTML,
	$combine = Alfresco.util.combinePaths,
	$siteURL = Alfresco.util.siteURL,
	$isValueSet = Alfresco.util.isValueSet;

	YAHOO.Bubbling.fire("registerAction",{   
		actionName: "onActionMoveUp",
		fn: function customActions_onActionMoveUp(record, owner){
			console.debug(this);
			
			var dialog = new YAHOO.widget.Dialog(Alfresco.util.generateDomId() + "-panel", {
				visible:false,
				modal: true,
				constraintoviewport: true,
				fixedcenter: "contained",
				postmethod: "none"
			});

			dialog.setHeader('Move Up');
			dialog.setBody(
					//input for level count
					'Levels: <input type="text" id="levels"/>' +
					//save- and cancel-buttons
					'<div class="form-buttons">' +
						'<input type="submit" id="' + this.id + '-saveButton" value="Verschieben"/>' +
						'<input type="cancel" id="' + this.id + '-cancelButton" value="Abbrechen"/>' +
					'</div>'
			);
			
			dialog.render(document.body);
			
			//create cancel button for the dialog
			var cancelButton = Alfresco.util.createYUIButton(this, "cancelButton", function(event){
				dialog.cancel();
				dialog.destroy();
			});
			
			var saveButton = Alfresco.util.createYUIButton(this, "saveButton", function(event){
				var repoActionParams = {};
				repoActionParams['levels'] = document.getElementById('levels').value;

				// Prepare genericAction configuration to call custom edit-categories Action
				var webscriptConfig = {
						webscript:{
							method: Alfresco.util.Ajax.POST,
							stem: Alfresco.constants.PROXY_URI + "api/",
							name: "actionQueue"
						},
						config: {
							requestContentType: Alfresco.util.Ajax.JSON,
							dataObj: {
								actionedUponNode: record.nodeRef,
								actionDefinitionName: 'move-up',
								parameterValues: repoActionParams
							}
						},
						success: {
							callback : {
								//method called when edit-categories Action executed successfully
								fn: function(response, dialog){
									//hide the dialog and destroy it
									dialog.cancel();
									dialog.destroy();
									
									//refresh the document library
									YAHOO.Bubbling.fire("metadataRefresh");
									
									//show success message
									Alfresco.util.PopupManager.displayMessage({
										text: 'Erfolg'
									}, document.body);
								},
								obj: dialog,
								scope: this
							}
						},
						failure: {
							callback : {
								//method called when edit-categories Action did not execute successfully
								fn: function(response, dialog){
									//hide the dialog and destroy it
									dialog.cancel();
									dialog.destroy();
									
									//show failure message
									Alfresco.util.PopupManager.displayPrompt({
										text: 'Fehler'
									}, document.body);
								},
								obj: dialog,
								scope: this
							}
						}
				};

				// Execute the custom edit-categories repo Action
				this.modules.actions.genericAction(webscriptConfig);
			});
			
			console.debug(dialog);
			dialog.show();
			console.debug(Alfresco.doclib.Actions);	
		}
	});
})();