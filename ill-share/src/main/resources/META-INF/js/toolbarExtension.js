(function() {
	YAHOO.lang
			.augmentObject(
					Alfresco.DocListToolbar.prototype,
					{
						onNewDeliveryFolder : function(e, p_obj) {
							return this.onNewCustomFolder(e, p_obj,
									'ill:deliveryFolder');
						},

						onNewMaterialFolder : function(e, p_obj) {
							return this.onNewCustomFolder(e, p_obj,
									'ill:materialFolder');
						},

						onNewCustomFolder : function(e, p_obj,
								type) {
							var destination = this.doclistMetadata.parent.nodeRef;

							// Intercept before dialog show
							var doBeforeDialogShow = function DLTB_onNewFolder_doBeforeDialogShow(
									p_form, p_dialog) {
								Dom.get(p_dialog.id + "-dialogTitle").innerHTML = this
										.msg("label.new-folder.title");
								Dom.get(p_dialog.id + "-dialogHeader").innerHTML = this
										.msg("label.new-folder.header");
							};

							var templateUrl = YAHOO.lang
									.substitute(
											Alfresco.constants.URL_SERVICECONTEXT
													+ "components/form?itemKind={itemKind}&itemId={itemId}&destination={destination}&mode={mode}&submitType={submitType}&formId={formId}&showCancelButton=true",
											{
												itemKind : "type",
												itemId : type,
												destination : destination,
												mode : "create",
												submitType : "json",
												formId : "doclib-common"
											});

							// Using Forms Service, so always create new
							// instance
							var createFolder = new Alfresco.module.SimpleDialog(
									this.id + "-createFolder");
							createFolder
									.setOptions({
										width : "33em",
										templateUrl : templateUrl,
										actionUrl : null,
										destroyOnHide : true,
										doBeforeDialogShow : {
											fn : doBeforeDialogShow,
											scope : this
										},
										onSuccess : {
											fn : function DLTB_onNewFolder_success(
													response) {
												var activityData;
												var folderName = response.config.dataObj["prop_cm_name"];
												var folderNodeRef = response.json.persistedObject;

												activityData = {
													fileName : folderName,
													nodeRef : folderNodeRef,
													path : this.currentPath
															+ (this.currentPath !== "/" ? "/"
																	: "")
															+ folderName
												};
												this.modules.actions
														.postActivity(
																this.options.siteId,
																"folder-added",
																"documentlibrary",
																activityData);

												YAHOO.Bubbling
														.fire(
																"folderCreated",
																{
																	name : folderName,
																	parentNodeRef : destination
																});
												Alfresco.util.PopupManager
														.displayMessage({
															text : this
																	.msg(
																			"message.new-folder.success",
																			folderName)
														});
											},
											scope : this
										},
										onFailure : {
											fn : function DLTB_onNewFolder_failure(
													response) {
												if (response) {
													var folderName = response.config.dataObj["prop_cm_name"];
													Alfresco.util.PopupManager
															.displayMessage({
																text : this
																		.msg(
																				"message.new-folder.failure",
																				folderName)
															});
												} else {
													Alfresco.util.PopupManager
															.displayMessage({
																text : this
																		.msg("message.failure")
															});
												}
												createFolder.widgets.cancelButton
														.set("disabled", false);
											},
											scope : this
										}
									});
							createFolder.show();
							return createFolder;
						}
					});

})();