/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.view.tools.map.MovePlanDialog', {
    extend: 'Ext.window.Window',
    alias: 'widget.moveplandialog',
    mixins: ['Deft.mixin.Injectable', 'Deft.mixin.Controllable'],
	controller: 'Ssp.controller.tool.map.MovePlanDialogController',
	inject: {
	    formUtils: 'formRendererUtils',
    	currentMapPlan: 'currentMapPlan',
    	textStore: 'sspTextStore'
	},
    height: 220,
    width: 380,
    resizable: true,
    parentGrid: null,
    enableFields: true,
    modal: true,
    initComponent: function() {
		var me=this;
		Ext.apply(me, 
				{
					layout: {
                align: 'stretch',
                type: 'vbox'
            },
            title: me.currentMapPlan.get("isTemplate") == true ? me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.template-title','Move Template') : me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.plan-title','Move Plan'),
            items:[{
                xtype: 'form',
                flex: 1,
                border: 0,
                frame: false,
                layout: {
                    align: 'stretch',
                    type: 'vbox'
                },
                width: '100%',
                height: '100%',
                bodyPadding: 10,
                autoScroll: true,
                itemId: 'coursenotesForm',
                fieldDefaults: {
                        msgTarget: 'side',
                        labelAlign: 'left',
                        labelWidth: 100
                    },
				    items: [	{
	                        xtype: 'combobox',
	                        itemId: 'selectAction',
							name: 'selectAction',
	                        fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.select-action','Select Action'),
							queryMode: 'local',
							displayField: 'name',
							valueField: 'action',
							hidden: true
	                    },
							{
	                        xtype: 'combobox',
	                        itemId: 'termCodeToBump',
							name: 'termCodeToBump',
	                        emptyText: me.textStore.getValueByCode('ssp.empty-text.map.move-plan-dialog.start-term','Start Term'),
	                        valueField: 'code',
	                        displayField: 'name',
	                        queryMode: 'local',
	                        typeAhead: true,
	                        allowBlank: true,
	                        width: 250,
							labelWidth: 200,
							labelSeparator : "",
							fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.start-term','Start Term: Choose the first term that you want to move. All subsequent terms will also be moved.')
			            },
						{
	                        xtype: 'combobox',
	                        itemId: 'termCodeEnd',
							name: 'termCodeEnd',
	                        emptyText: me.textStore.getValueByCode('ssp.empty-text.map.move-plan-dialog.target-term','Target Term'),
	                        valueField: 'code',
	                        displayField: 'name',
	                        queryMode: 'local',
	                        typeAhead: true,
	                        allowBlank: true,
	                        width: 250,
							labelWidth: 200,
							labelSeparator : "",
							fieldLabel: me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.target-term','Target Term: Choose the target term to the move term selected above (start term). All term and course information will be moved into the new terms in the same order.')

			            }],
				    dockedItems: [{
		                xtype: 'toolbar',
		                dock: 'top',
		                items: [{
		                    xtype: 'button',
		                    itemId: 'movePlanButton',
		                    text: me.currentMapPlan.get("isTemplate") == true ? me.textStore.getValueByCode('ssp.label.move-template-button','Move Template') : me.textStore.getValueByCode('ssp.label.move-plan-button','Move Plan')
		                }, '-', {
		                    xtype: 'button',
		                    itemId: 'cancelButton',
		                    text: me.textStore.getValueByCode('ssp.label.cancel-button','Cancel'),
		                    
		                    listeners: {
		                    	click:function(){
		                    		me = this;
		                    		me.close();
		                    	},
		                    	scope: me
		                    }
					
		                },{
			                xtype: 'tbspacer',
			                flex: 1
			            },
						{
			                xtype: 'container',
			                border: 0,
			                padding: '0 0 0 0',
			                title: '',
			                defaultType: 'displayfield',
			                layout: 'vbox',
			                defaults: {
			                    anchor: '100%'
			                },
			                items: [
		                {
							 xtype: 'button',
							 width: 20,
			                 height: 20,
			    	         cls: 'helpIconSmall',
			    	         tooltip: me.textStore.getValueByCode('ssp.label.map.move-plan-dialog.help','Start Term: Choose the first term that you want to move. All subsequent terms will also be moved. Target Term: Choose the target term to he move term selected abose (start term). All term and course information will be moved into the new terms in the same order.')
			    	     }]}]
       
		            }]
		     }],
		     listeners: {
                  afterrender: function(c){
                      c.el.dom.setAttribute('role', 'dialog');
                  }
		     }
		});
		
		return me.callParent(arguments);
	}
});
