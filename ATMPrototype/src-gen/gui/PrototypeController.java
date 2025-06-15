package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		atmsystem_service = ServiceManager.createATMSystem();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		withdrawcashservice_service = ServiceManager.createWithdrawCashService();
		checkbalanceservice_service = ServiceManager.createCheckBalanceService();
		replenishcashservice_service = ServiceManager.createReplenishCashService();
		manageauditlogsservice_service = ServiceManager.createManageAuditLogsService();
		transferfundsservice_service = ServiceManager.createTransferFundsService();
		changepinservice_service = ServiceManager.createChangePINService();
		updatefirmwareservice_service = ServiceManager.createUpdateFirmwareService();
		verifypinservice_service = ServiceManager.createVerifyPINService();
		displayhistoryservice_service = ServiceManager.createDisplayHistoryService();
		securityauthenticationservice_service = ServiceManager.createSecurityAuthenticationService();
		settransactionlimitsservice_service = ServiceManager.createSetTransactionLimitsService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = WithdrawCashServiceImpl.opINVRelatedEntity.get("insertDebitCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("insertDebitCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WithdrawCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("insertDebitCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WithdrawCashServiceImpl.opINVRelatedEntity.get("inputPIN");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputPIN" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WithdrawCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputPIN", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WithdrawCashServiceImpl.opINVRelatedEntity.get("selectWithdrawal");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectWithdrawal" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WithdrawCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectWithdrawal", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WithdrawCashServiceImpl.opINVRelatedEntity.get("chooseAmount");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseAmount" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WithdrawCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseAmount", v);
		
		v = new VBox();
		
		//entities invariants
		entities = WithdrawCashServiceImpl.opINVRelatedEntity.get("takeCash");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("takeCash" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("WithdrawCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("takeCash", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CheckBalanceServiceImpl.opINVRelatedEntity.get("selectBalanceInquiry");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectBalanceInquiry" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CheckBalanceService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectBalanceInquiry", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReplenishCashServiceImpl.opINVRelatedEntity.get("insertPhysicalKey");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("insertPhysicalKey" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReplenishCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("insertPhysicalKey", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReplenishCashServiceImpl.opINVRelatedEntity.get("inputCredentials");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputCredentials" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReplenishCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputCredentials", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReplenishCashServiceImpl.opINVRelatedEntity.get("loadBanknoteBundle");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("loadBanknoteBundle" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReplenishCashService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("loadBanknoteBundle", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageAuditLogsServiceImpl.opINVRelatedEntity.get("insertFIPSToken");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("insertFIPSToken" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageAuditLogsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("insertFIPSToken", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageAuditLogsServiceImpl.opINVRelatedEntity.get("selectLogRange");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectLogRange" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageAuditLogsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectLogRange", v);
		
		v = new VBox();
		
		//entities invariants
		entities = TransferFundsServiceImpl.opINVRelatedEntity.get("selectTransferMenu");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectTransferMenu" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("TransferFundsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectTransferMenu", v);
		
		v = new VBox();
		
		//entities invariants
		entities = TransferFundsServiceImpl.opINVRelatedEntity.get("chooseTargetAccount");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseTargetAccount" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("TransferFundsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseTargetAccount", v);
		
		v = new VBox();
		
		//entities invariants
		entities = TransferFundsServiceImpl.opINVRelatedEntity.get("chooseAndConfirmAmount");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseAndConfirmAmount" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("TransferFundsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseAndConfirmAmount", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableUser_UserID = new TableColumn<Map<String, String>, String>("UserID");
		tableUser_UserID.setMinWidth("UserID".length()*10);
		tableUser_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
		    }
		});	
		tableUser.getColumns().add(tableUser_UserID);
		TableColumn<Map<String, String>, String> tableUser_Name = new TableColumn<Map<String, String>, String>("Name");
		tableUser_Name.setMinWidth("Name".length()*10);
		tableUser_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Name);
		TableColumn<Map<String, String>, String> tableUser_Role = new TableColumn<Map<String, String>, String>("Role");
		tableUser_Role.setMinWidth("Role".length()*10);
		tableUser_Role.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Role"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Role);
		
		//table data
		ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
		List<User> rsUser = EntityManager.getAllInstancesOf("User");
		for (User r : rsUser) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getUserID() != null)
				unit.put("UserID", String.valueOf(r.getUserID()));
			else
				unit.put("UserID", "");
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getRole() != null)
				unit.put("Role", String.valueOf(r.getRole()));
			else
				unit.put("Role", "");

			dataUser.add(unit);
		}
		
		tableUser.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUser.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("User", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUser.setItems(dataUser);
		allObjectTables.put("User", tableUser);
		
		TableView<Map<String, String>> tableDebitCard = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableDebitCard_CardID = new TableColumn<Map<String, String>, String>("CardID");
		tableDebitCard_CardID.setMinWidth("CardID".length()*10);
		tableDebitCard_CardID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CardID"));
		    }
		});	
		tableDebitCard.getColumns().add(tableDebitCard_CardID);
		TableColumn<Map<String, String>, String> tableDebitCard_UserID = new TableColumn<Map<String, String>, String>("UserID");
		tableDebitCard_UserID.setMinWidth("UserID".length()*10);
		tableDebitCard_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
		    }
		});	
		tableDebitCard.getColumns().add(tableDebitCard_UserID);
		TableColumn<Map<String, String>, String> tableDebitCard_AccountNumber = new TableColumn<Map<String, String>, String>("AccountNumber");
		tableDebitCard_AccountNumber.setMinWidth("AccountNumber".length()*10);
		tableDebitCard_AccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AccountNumber"));
		    }
		});	
		tableDebitCard.getColumns().add(tableDebitCard_AccountNumber);
		TableColumn<Map<String, String>, String> tableDebitCard_ExpiryDate = new TableColumn<Map<String, String>, String>("ExpiryDate");
		tableDebitCard_ExpiryDate.setMinWidth("ExpiryDate".length()*10);
		tableDebitCard_ExpiryDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ExpiryDate"));
		    }
		});	
		tableDebitCard.getColumns().add(tableDebitCard_ExpiryDate);
		TableColumn<Map<String, String>, String> tableDebitCard_PINHash = new TableColumn<Map<String, String>, String>("PINHash");
		tableDebitCard_PINHash.setMinWidth("PINHash".length()*10);
		tableDebitCard_PINHash.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("PINHash"));
		    }
		});	
		tableDebitCard.getColumns().add(tableDebitCard_PINHash);
		
		//table data
		ObservableList<Map<String, String>> dataDebitCard = FXCollections.observableArrayList();
		List<DebitCard> rsDebitCard = EntityManager.getAllInstancesOf("DebitCard");
		for (DebitCard r : rsDebitCard) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getCardID() != null)
				unit.put("CardID", String.valueOf(r.getCardID()));
			else
				unit.put("CardID", "");
			if (r.getUserID() != null)
				unit.put("UserID", String.valueOf(r.getUserID()));
			else
				unit.put("UserID", "");
			if (r.getAccountNumber() != null)
				unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
			else
				unit.put("AccountNumber", "");
			if (r.getExpiryDate() != null)
				unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
			else
				unit.put("ExpiryDate", "");
			if (r.getPINHash() != null)
				unit.put("PINHash", String.valueOf(r.getPINHash()));
			else
				unit.put("PINHash", "");

			dataDebitCard.add(unit);
		}
		
		tableDebitCard.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableDebitCard.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("DebitCard", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableDebitCard.setItems(dataDebitCard);
		allObjectTables.put("DebitCard", tableDebitCard);
		
		TableView<Map<String, String>> tableAccount = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableAccount_AccountNumber = new TableColumn<Map<String, String>, String>("AccountNumber");
		tableAccount_AccountNumber.setMinWidth("AccountNumber".length()*10);
		tableAccount_AccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AccountNumber"));
		    }
		});	
		tableAccount.getColumns().add(tableAccount_AccountNumber);
		TableColumn<Map<String, String>, String> tableAccount_UserID = new TableColumn<Map<String, String>, String>("UserID");
		tableAccount_UserID.setMinWidth("UserID".length()*10);
		tableAccount_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
		    }
		});	
		tableAccount.getColumns().add(tableAccount_UserID);
		TableColumn<Map<String, String>, String> tableAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
		tableAccount_Balance.setMinWidth("Balance".length()*10);
		tableAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
		    }
		});	
		tableAccount.getColumns().add(tableAccount_Balance);
		TableColumn<Map<String, String>, String> tableAccount_FrozenBalance = new TableColumn<Map<String, String>, String>("FrozenBalance");
		tableAccount_FrozenBalance.setMinWidth("FrozenBalance".length()*10);
		tableAccount_FrozenBalance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("FrozenBalance"));
		    }
		});	
		tableAccount.getColumns().add(tableAccount_FrozenBalance);
		
		//table data
		ObservableList<Map<String, String>> dataAccount = FXCollections.observableArrayList();
		List<Account> rsAccount = EntityManager.getAllInstancesOf("Account");
		for (Account r : rsAccount) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getAccountNumber() != null)
				unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
			else
				unit.put("AccountNumber", "");
			if (r.getUserID() != null)
				unit.put("UserID", String.valueOf(r.getUserID()));
			else
				unit.put("UserID", "");
			unit.put("Balance", String.valueOf(r.getBalance()));
			unit.put("FrozenBalance", String.valueOf(r.getFrozenBalance()));

			dataAccount.add(unit);
		}
		
		tableAccount.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableAccount.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Account", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableAccount.setItems(dataAccount);
		allObjectTables.put("Account", tableAccount);
		
		TableView<Map<String, String>> tableTransaction = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableTransaction_TransactionID = new TableColumn<Map<String, String>, String>("TransactionID");
		tableTransaction_TransactionID.setMinWidth("TransactionID".length()*10);
		tableTransaction_TransactionID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TransactionID"));
		    }
		});	
		tableTransaction.getColumns().add(tableTransaction_TransactionID);
		TableColumn<Map<String, String>, String> tableTransaction_AccountNumber = new TableColumn<Map<String, String>, String>("AccountNumber");
		tableTransaction_AccountNumber.setMinWidth("AccountNumber".length()*10);
		tableTransaction_AccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AccountNumber"));
		    }
		});	
		tableTransaction.getColumns().add(tableTransaction_AccountNumber);
		TableColumn<Map<String, String>, String> tableTransaction_Amount = new TableColumn<Map<String, String>, String>("Amount");
		tableTransaction_Amount.setMinWidth("Amount".length()*10);
		tableTransaction_Amount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Amount"));
		    }
		});	
		tableTransaction.getColumns().add(tableTransaction_Amount);
		TableColumn<Map<String, String>, String> tableTransaction_Timestamp = new TableColumn<Map<String, String>, String>("Timestamp");
		tableTransaction_Timestamp.setMinWidth("Timestamp".length()*10);
		tableTransaction_Timestamp.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Timestamp"));
		    }
		});	
		tableTransaction.getColumns().add(tableTransaction_Timestamp);
		TableColumn<Map<String, String>, String> tableTransaction_TransactionType = new TableColumn<Map<String, String>, String>("TransactionType");
		tableTransaction_TransactionType.setMinWidth("TransactionType".length()*10);
		tableTransaction_TransactionType.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TransactionType"));
		    }
		});	
		tableTransaction.getColumns().add(tableTransaction_TransactionType);
		
		//table data
		ObservableList<Map<String, String>> dataTransaction = FXCollections.observableArrayList();
		List<Transaction> rsTransaction = EntityManager.getAllInstancesOf("Transaction");
		for (Transaction r : rsTransaction) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getTransactionID() != null)
				unit.put("TransactionID", String.valueOf(r.getTransactionID()));
			else
				unit.put("TransactionID", "");
			if (r.getAccountNumber() != null)
				unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
			else
				unit.put("AccountNumber", "");
			unit.put("Amount", String.valueOf(r.getAmount()));
			if (r.getTimestamp() != null)
				unit.put("Timestamp", r.getTimestamp().format(dateformatter));
			else
				unit.put("Timestamp", "");
			if (r.getTransactionType() != null)
				unit.put("TransactionType", String.valueOf(r.getTransactionType()));
			else
				unit.put("TransactionType", "");

			dataTransaction.add(unit);
		}
		
		tableTransaction.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableTransaction.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Transaction", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableTransaction.setItems(dataTransaction);
		allObjectTables.put("Transaction", tableTransaction);
		
		TableView<Map<String, String>> tableCashInventory = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCashInventory_Denomination = new TableColumn<Map<String, String>, String>("Denomination");
		tableCashInventory_Denomination.setMinWidth("Denomination".length()*10);
		tableCashInventory_Denomination.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Denomination"));
		    }
		});	
		tableCashInventory.getColumns().add(tableCashInventory_Denomination);
		TableColumn<Map<String, String>, String> tableCashInventory_Count = new TableColumn<Map<String, String>, String>("Count");
		tableCashInventory_Count.setMinWidth("Count".length()*10);
		tableCashInventory_Count.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Count"));
		    }
		});	
		tableCashInventory.getColumns().add(tableCashInventory_Count);
		TableColumn<Map<String, String>, String> tableCashInventory_LastUpdated = new TableColumn<Map<String, String>, String>("LastUpdated");
		tableCashInventory_LastUpdated.setMinWidth("LastUpdated".length()*10);
		tableCashInventory_LastUpdated.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LastUpdated"));
		    }
		});	
		tableCashInventory.getColumns().add(tableCashInventory_LastUpdated);
		
		//table data
		ObservableList<Map<String, String>> dataCashInventory = FXCollections.observableArrayList();
		List<CashInventory> rsCashInventory = EntityManager.getAllInstancesOf("CashInventory");
		for (CashInventory r : rsCashInventory) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Denomination", String.valueOf(r.getDenomination()));
			unit.put("Count", String.valueOf(r.getCount()));
			if (r.getLastUpdated() != null)
				unit.put("LastUpdated", r.getLastUpdated().format(dateformatter));
			else
				unit.put("LastUpdated", "");

			dataCashInventory.add(unit);
		}
		
		tableCashInventory.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCashInventory.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CashInventory", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCashInventory.setItems(dataCashInventory);
		allObjectTables.put("CashInventory", tableCashInventory);
		
		TableView<Map<String, String>> tableMaintenancePersonnel = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableMaintenancePersonnel_OperatorID = new TableColumn<Map<String, String>, String>("OperatorID");
		tableMaintenancePersonnel_OperatorID.setMinWidth("OperatorID".length()*10);
		tableMaintenancePersonnel_OperatorID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OperatorID"));
		    }
		});	
		tableMaintenancePersonnel.getColumns().add(tableMaintenancePersonnel_OperatorID);
		TableColumn<Map<String, String>, String> tableMaintenancePersonnel_Name = new TableColumn<Map<String, String>, String>("Name");
		tableMaintenancePersonnel_Name.setMinWidth("Name".length()*10);
		tableMaintenancePersonnel_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableMaintenancePersonnel.getColumns().add(tableMaintenancePersonnel_Name);
		TableColumn<Map<String, String>, String> tableMaintenancePersonnel_KeyID = new TableColumn<Map<String, String>, String>("KeyID");
		tableMaintenancePersonnel_KeyID.setMinWidth("KeyID".length()*10);
		tableMaintenancePersonnel_KeyID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("KeyID"));
		    }
		});	
		tableMaintenancePersonnel.getColumns().add(tableMaintenancePersonnel_KeyID);
		TableColumn<Map<String, String>, String> tableMaintenancePersonnel_AuthCredentials = new TableColumn<Map<String, String>, String>("AuthCredentials");
		tableMaintenancePersonnel_AuthCredentials.setMinWidth("AuthCredentials".length()*10);
		tableMaintenancePersonnel_AuthCredentials.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AuthCredentials"));
		    }
		});	
		tableMaintenancePersonnel.getColumns().add(tableMaintenancePersonnel_AuthCredentials);
		
		//table data
		ObservableList<Map<String, String>> dataMaintenancePersonnel = FXCollections.observableArrayList();
		List<MaintenancePersonnel> rsMaintenancePersonnel = EntityManager.getAllInstancesOf("MaintenancePersonnel");
		for (MaintenancePersonnel r : rsMaintenancePersonnel) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getOperatorID() != null)
				unit.put("OperatorID", String.valueOf(r.getOperatorID()));
			else
				unit.put("OperatorID", "");
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getKeyID() != null)
				unit.put("KeyID", String.valueOf(r.getKeyID()));
			else
				unit.put("KeyID", "");
			if (r.getAuthCredentials() != null)
				unit.put("AuthCredentials", String.valueOf(r.getAuthCredentials()));
			else
				unit.put("AuthCredentials", "");

			dataMaintenancePersonnel.add(unit);
		}
		
		tableMaintenancePersonnel.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableMaintenancePersonnel.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("MaintenancePersonnel", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableMaintenancePersonnel.setItems(dataMaintenancePersonnel);
		allObjectTables.put("MaintenancePersonnel", tableMaintenancePersonnel);
		
		TableView<Map<String, String>> tableMaintenanceLog = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableMaintenanceLog_LogID = new TableColumn<Map<String, String>, String>("LogID");
		tableMaintenanceLog_LogID.setMinWidth("LogID".length()*10);
		tableMaintenanceLog_LogID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LogID"));
		    }
		});	
		tableMaintenanceLog.getColumns().add(tableMaintenanceLog_LogID);
		TableColumn<Map<String, String>, String> tableMaintenanceLog_OperatorID = new TableColumn<Map<String, String>, String>("OperatorID");
		tableMaintenanceLog_OperatorID.setMinWidth("OperatorID".length()*10);
		tableMaintenanceLog_OperatorID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OperatorID"));
		    }
		});	
		tableMaintenanceLog.getColumns().add(tableMaintenanceLog_OperatorID);
		TableColumn<Map<String, String>, String> tableMaintenanceLog_Denomination = new TableColumn<Map<String, String>, String>("Denomination");
		tableMaintenanceLog_Denomination.setMinWidth("Denomination".length()*10);
		tableMaintenanceLog_Denomination.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Denomination"));
		    }
		});	
		tableMaintenanceLog.getColumns().add(tableMaintenanceLog_Denomination);
		TableColumn<Map<String, String>, String> tableMaintenanceLog_Count = new TableColumn<Map<String, String>, String>("Count");
		tableMaintenanceLog_Count.setMinWidth("Count".length()*10);
		tableMaintenanceLog_Count.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Count"));
		    }
		});	
		tableMaintenanceLog.getColumns().add(tableMaintenanceLog_Count);
		TableColumn<Map<String, String>, String> tableMaintenanceLog_Timestamp = new TableColumn<Map<String, String>, String>("Timestamp");
		tableMaintenanceLog_Timestamp.setMinWidth("Timestamp".length()*10);
		tableMaintenanceLog_Timestamp.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Timestamp"));
		    }
		});	
		tableMaintenanceLog.getColumns().add(tableMaintenanceLog_Timestamp);
		
		//table data
		ObservableList<Map<String, String>> dataMaintenanceLog = FXCollections.observableArrayList();
		List<MaintenanceLog> rsMaintenanceLog = EntityManager.getAllInstancesOf("MaintenanceLog");
		for (MaintenanceLog r : rsMaintenanceLog) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getLogID() != null)
				unit.put("LogID", String.valueOf(r.getLogID()));
			else
				unit.put("LogID", "");
			if (r.getOperatorID() != null)
				unit.put("OperatorID", String.valueOf(r.getOperatorID()));
			else
				unit.put("OperatorID", "");
			unit.put("Denomination", String.valueOf(r.getDenomination()));
			unit.put("Count", String.valueOf(r.getCount()));
			if (r.getTimestamp() != null)
				unit.put("Timestamp", r.getTimestamp().format(dateformatter));
			else
				unit.put("Timestamp", "");

			dataMaintenanceLog.add(unit);
		}
		
		tableMaintenanceLog.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableMaintenanceLog.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("MaintenanceLog", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableMaintenanceLog.setItems(dataMaintenanceLog);
		allObjectTables.put("MaintenanceLog", tableMaintenanceLog);
		
		TableView<Map<String, String>> tableSecurityToken = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSecurityToken_TokenID = new TableColumn<Map<String, String>, String>("TokenID");
		tableSecurityToken_TokenID.setMinWidth("TokenID".length()*10);
		tableSecurityToken_TokenID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TokenID"));
		    }
		});	
		tableSecurityToken.getColumns().add(tableSecurityToken_TokenID);
		TableColumn<Map<String, String>, String> tableSecurityToken_Type = new TableColumn<Map<String, String>, String>("Type");
		tableSecurityToken_Type.setMinWidth("Type".length()*10);
		tableSecurityToken_Type.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Type"));
		    }
		});	
		tableSecurityToken.getColumns().add(tableSecurityToken_Type);
		TableColumn<Map<String, String>, String> tableSecurityToken_ExpiryDate = new TableColumn<Map<String, String>, String>("ExpiryDate");
		tableSecurityToken_ExpiryDate.setMinWidth("ExpiryDate".length()*10);
		tableSecurityToken_ExpiryDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ExpiryDate"));
		    }
		});	
		tableSecurityToken.getColumns().add(tableSecurityToken_ExpiryDate);
		
		//table data
		ObservableList<Map<String, String>> dataSecurityToken = FXCollections.observableArrayList();
		List<SecurityToken> rsSecurityToken = EntityManager.getAllInstancesOf("SecurityToken");
		for (SecurityToken r : rsSecurityToken) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getTokenID() != null)
				unit.put("TokenID", String.valueOf(r.getTokenID()));
			else
				unit.put("TokenID", "");
			if (r.getType() != null)
				unit.put("Type", String.valueOf(r.getType()));
			else
				unit.put("Type", "");
			if (r.getExpiryDate() != null)
				unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
			else
				unit.put("ExpiryDate", "");

			dataSecurityToken.add(unit);
		}
		
		tableSecurityToken.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSecurityToken.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("SecurityToken", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSecurityToken.setItems(dataSecurityToken);
		allObjectTables.put("SecurityToken", tableSecurityToken);
		
		TableView<Map<String, String>> tableAuditLog = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableAuditLog_LogID = new TableColumn<Map<String, String>, String>("LogID");
		tableAuditLog_LogID.setMinWidth("LogID".length()*10);
		tableAuditLog_LogID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LogID"));
		    }
		});	
		tableAuditLog.getColumns().add(tableAuditLog_LogID);
		TableColumn<Map<String, String>, String> tableAuditLog_OperationType = new TableColumn<Map<String, String>, String>("OperationType");
		tableAuditLog_OperationType.setMinWidth("OperationType".length()*10);
		tableAuditLog_OperationType.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OperationType"));
		    }
		});	
		tableAuditLog.getColumns().add(tableAuditLog_OperationType);
		TableColumn<Map<String, String>, String> tableAuditLog_Timestamp = new TableColumn<Map<String, String>, String>("Timestamp");
		tableAuditLog_Timestamp.setMinWidth("Timestamp".length()*10);
		tableAuditLog_Timestamp.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Timestamp"));
		    }
		});	
		tableAuditLog.getColumns().add(tableAuditLog_Timestamp);
		TableColumn<Map<String, String>, String> tableAuditLog_UserID = new TableColumn<Map<String, String>, String>("UserID");
		tableAuditLog_UserID.setMinWidth("UserID".length()*10);
		tableAuditLog_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
		    }
		});	
		tableAuditLog.getColumns().add(tableAuditLog_UserID);
		TableColumn<Map<String, String>, String> tableAuditLog_Details = new TableColumn<Map<String, String>, String>("Details");
		tableAuditLog_Details.setMinWidth("Details".length()*10);
		tableAuditLog_Details.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Details"));
		    }
		});	
		tableAuditLog.getColumns().add(tableAuditLog_Details);
		
		//table data
		ObservableList<Map<String, String>> dataAuditLog = FXCollections.observableArrayList();
		List<AuditLog> rsAuditLog = EntityManager.getAllInstancesOf("AuditLog");
		for (AuditLog r : rsAuditLog) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getLogID() != null)
				unit.put("LogID", String.valueOf(r.getLogID()));
			else
				unit.put("LogID", "");
			if (r.getOperationType() != null)
				unit.put("OperationType", String.valueOf(r.getOperationType()));
			else
				unit.put("OperationType", "");
			if (r.getTimestamp() != null)
				unit.put("Timestamp", r.getTimestamp().format(dateformatter));
			else
				unit.put("Timestamp", "");
			if (r.getUserID() != null)
				unit.put("UserID", String.valueOf(r.getUserID()));
			else
				unit.put("UserID", "");
			if (r.getDetails() != null)
				unit.put("Details", String.valueOf(r.getDetails()));
			else
				unit.put("Details", "");

			dataAuditLog.add(unit);
		}
		
		tableAuditLog.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableAuditLog.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("AuditLog", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableAuditLog.setItems(dataAuditLog);
		allObjectTables.put("AuditLog", tableAuditLog);
		
		TableView<Map<String, String>> tableTransferRecord = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableTransferRecord_TransferID = new TableColumn<Map<String, String>, String>("TransferID");
		tableTransferRecord_TransferID.setMinWidth("TransferID".length()*10);
		tableTransferRecord_TransferID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TransferID"));
		    }
		});	
		tableTransferRecord.getColumns().add(tableTransferRecord_TransferID);
		TableColumn<Map<String, String>, String> tableTransferRecord_SenderAccount = new TableColumn<Map<String, String>, String>("SenderAccount");
		tableTransferRecord_SenderAccount.setMinWidth("SenderAccount".length()*10);
		tableTransferRecord_SenderAccount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SenderAccount"));
		    }
		});	
		tableTransferRecord.getColumns().add(tableTransferRecord_SenderAccount);
		TableColumn<Map<String, String>, String> tableTransferRecord_ReceiverAccount = new TableColumn<Map<String, String>, String>("ReceiverAccount");
		tableTransferRecord_ReceiverAccount.setMinWidth("ReceiverAccount".length()*10);
		tableTransferRecord_ReceiverAccount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ReceiverAccount"));
		    }
		});	
		tableTransferRecord.getColumns().add(tableTransferRecord_ReceiverAccount);
		TableColumn<Map<String, String>, String> tableTransferRecord_Amount = new TableColumn<Map<String, String>, String>("Amount");
		tableTransferRecord_Amount.setMinWidth("Amount".length()*10);
		tableTransferRecord_Amount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Amount"));
		    }
		});	
		tableTransferRecord.getColumns().add(tableTransferRecord_Amount);
		TableColumn<Map<String, String>, String> tableTransferRecord_Status = new TableColumn<Map<String, String>, String>("Status");
		tableTransferRecord_Status.setMinWidth("Status".length()*10);
		tableTransferRecord_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableTransferRecord.getColumns().add(tableTransferRecord_Status);
		
		//table data
		ObservableList<Map<String, String>> dataTransferRecord = FXCollections.observableArrayList();
		List<TransferRecord> rsTransferRecord = EntityManager.getAllInstancesOf("TransferRecord");
		for (TransferRecord r : rsTransferRecord) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getTransferID() != null)
				unit.put("TransferID", String.valueOf(r.getTransferID()));
			else
				unit.put("TransferID", "");
			if (r.getSenderAccount() != null)
				unit.put("SenderAccount", String.valueOf(r.getSenderAccount()));
			else
				unit.put("SenderAccount", "");
			if (r.getReceiverAccount() != null)
				unit.put("ReceiverAccount", String.valueOf(r.getReceiverAccount()));
			else
				unit.put("ReceiverAccount", "");
			unit.put("Amount", String.valueOf(r.getAmount()));
			if (r.getStatus() != null)
				unit.put("Status", String.valueOf(r.getStatus()));
			else
				unit.put("Status", "");

			dataTransferRecord.add(unit);
		}
		
		tableTransferRecord.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableTransferRecord.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("TransferRecord", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableTransferRecord.setItems(dataTransferRecord);
		allObjectTables.put("TransferRecord", tableTransferRecord);
		
		TableView<Map<String, String>> tableBalanceInquiry = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableBalanceInquiry_InquiryID = new TableColumn<Map<String, String>, String>("InquiryID");
		tableBalanceInquiry_InquiryID.setMinWidth("InquiryID".length()*10);
		tableBalanceInquiry_InquiryID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("InquiryID"));
		    }
		});	
		tableBalanceInquiry.getColumns().add(tableBalanceInquiry_InquiryID);
		TableColumn<Map<String, String>, String> tableBalanceInquiry_AccountNumber = new TableColumn<Map<String, String>, String>("AccountNumber");
		tableBalanceInquiry_AccountNumber.setMinWidth("AccountNumber".length()*10);
		tableBalanceInquiry_AccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AccountNumber"));
		    }
		});	
		tableBalanceInquiry.getColumns().add(tableBalanceInquiry_AccountNumber);
		TableColumn<Map<String, String>, String> tableBalanceInquiry_Timestamp = new TableColumn<Map<String, String>, String>("Timestamp");
		tableBalanceInquiry_Timestamp.setMinWidth("Timestamp".length()*10);
		tableBalanceInquiry_Timestamp.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Timestamp"));
		    }
		});	
		tableBalanceInquiry.getColumns().add(tableBalanceInquiry_Timestamp);
		TableColumn<Map<String, String>, String> tableBalanceInquiry_Result = new TableColumn<Map<String, String>, String>("Result");
		tableBalanceInquiry_Result.setMinWidth("Result".length()*10);
		tableBalanceInquiry_Result.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Result"));
		    }
		});	
		tableBalanceInquiry.getColumns().add(tableBalanceInquiry_Result);
		
		//table data
		ObservableList<Map<String, String>> dataBalanceInquiry = FXCollections.observableArrayList();
		List<BalanceInquiry> rsBalanceInquiry = EntityManager.getAllInstancesOf("BalanceInquiry");
		for (BalanceInquiry r : rsBalanceInquiry) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getInquiryID() != null)
				unit.put("InquiryID", String.valueOf(r.getInquiryID()));
			else
				unit.put("InquiryID", "");
			if (r.getAccountNumber() != null)
				unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
			else
				unit.put("AccountNumber", "");
			if (r.getTimestamp() != null)
				unit.put("Timestamp", r.getTimestamp().format(dateformatter));
			else
				unit.put("Timestamp", "");
			if (r.getResult() != null)
				unit.put("Result", String.valueOf(r.getResult()));
			else
				unit.put("Result", "");

			dataBalanceInquiry.add(unit);
		}
		
		tableBalanceInquiry.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableBalanceInquiry.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("BalanceInquiry", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableBalanceInquiry.setItems(dataBalanceInquiry);
		allObjectTables.put("BalanceInquiry", tableBalanceInquiry);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateUserTable(List<User> rsUser) {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getRole() != null)
					unit.put("Role", String.valueOf(r.getRole()));
				else
					unit.put("Role", "");
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateDebitCardTable(List<DebitCard> rsDebitCard) {
			ObservableList<Map<String, String>> dataDebitCard = FXCollections.observableArrayList();
			for (DebitCard r : rsDebitCard) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getCardID() != null)
					unit.put("CardID", String.valueOf(r.getCardID()));
				else
					unit.put("CardID", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				if (r.getPINHash() != null)
					unit.put("PINHash", String.valueOf(r.getPINHash()));
				else
					unit.put("PINHash", "");
				dataDebitCard.add(unit);
			}
			
			allObjectTables.get("DebitCard").setItems(dataDebitCard);
	}
	public void updateAccountTable(List<Account> rsAccount) {
			ObservableList<Map<String, String>> dataAccount = FXCollections.observableArrayList();
			for (Account r : rsAccount) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("FrozenBalance", String.valueOf(r.getFrozenBalance()));
				dataAccount.add(unit);
			}
			
			allObjectTables.get("Account").setItems(dataAccount);
	}
	public void updateTransactionTable(List<Transaction> rsTransaction) {
			ObservableList<Map<String, String>> dataTransaction = FXCollections.observableArrayList();
			for (Transaction r : rsTransaction) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getTransactionID() != null)
					unit.put("TransactionID", String.valueOf(r.getTransactionID()));
				else
					unit.put("TransactionID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				unit.put("Amount", String.valueOf(r.getAmount()));
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getTransactionType() != null)
					unit.put("TransactionType", String.valueOf(r.getTransactionType()));
				else
					unit.put("TransactionType", "");
				dataTransaction.add(unit);
			}
			
			allObjectTables.get("Transaction").setItems(dataTransaction);
	}
	public void updateCashInventoryTable(List<CashInventory> rsCashInventory) {
			ObservableList<Map<String, String>> dataCashInventory = FXCollections.observableArrayList();
			for (CashInventory r : rsCashInventory) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Denomination", String.valueOf(r.getDenomination()));
				unit.put("Count", String.valueOf(r.getCount()));
				if (r.getLastUpdated() != null)
					unit.put("LastUpdated", r.getLastUpdated().format(dateformatter));
				else
					unit.put("LastUpdated", "");
				dataCashInventory.add(unit);
			}
			
			allObjectTables.get("CashInventory").setItems(dataCashInventory);
	}
	public void updateMaintenancePersonnelTable(List<MaintenancePersonnel> rsMaintenancePersonnel) {
			ObservableList<Map<String, String>> dataMaintenancePersonnel = FXCollections.observableArrayList();
			for (MaintenancePersonnel r : rsMaintenancePersonnel) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getOperatorID() != null)
					unit.put("OperatorID", String.valueOf(r.getOperatorID()));
				else
					unit.put("OperatorID", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getKeyID() != null)
					unit.put("KeyID", String.valueOf(r.getKeyID()));
				else
					unit.put("KeyID", "");
				if (r.getAuthCredentials() != null)
					unit.put("AuthCredentials", String.valueOf(r.getAuthCredentials()));
				else
					unit.put("AuthCredentials", "");
				dataMaintenancePersonnel.add(unit);
			}
			
			allObjectTables.get("MaintenancePersonnel").setItems(dataMaintenancePersonnel);
	}
	public void updateMaintenanceLogTable(List<MaintenanceLog> rsMaintenanceLog) {
			ObservableList<Map<String, String>> dataMaintenanceLog = FXCollections.observableArrayList();
			for (MaintenanceLog r : rsMaintenanceLog) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getLogID() != null)
					unit.put("LogID", String.valueOf(r.getLogID()));
				else
					unit.put("LogID", "");
				if (r.getOperatorID() != null)
					unit.put("OperatorID", String.valueOf(r.getOperatorID()));
				else
					unit.put("OperatorID", "");
				unit.put("Denomination", String.valueOf(r.getDenomination()));
				unit.put("Count", String.valueOf(r.getCount()));
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				dataMaintenanceLog.add(unit);
			}
			
			allObjectTables.get("MaintenanceLog").setItems(dataMaintenanceLog);
	}
	public void updateSecurityTokenTable(List<SecurityToken> rsSecurityToken) {
			ObservableList<Map<String, String>> dataSecurityToken = FXCollections.observableArrayList();
			for (SecurityToken r : rsSecurityToken) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getTokenID() != null)
					unit.put("TokenID", String.valueOf(r.getTokenID()));
				else
					unit.put("TokenID", "");
				if (r.getType() != null)
					unit.put("Type", String.valueOf(r.getType()));
				else
					unit.put("Type", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				dataSecurityToken.add(unit);
			}
			
			allObjectTables.get("SecurityToken").setItems(dataSecurityToken);
	}
	public void updateAuditLogTable(List<AuditLog> rsAuditLog) {
			ObservableList<Map<String, String>> dataAuditLog = FXCollections.observableArrayList();
			for (AuditLog r : rsAuditLog) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getLogID() != null)
					unit.put("LogID", String.valueOf(r.getLogID()));
				else
					unit.put("LogID", "");
				if (r.getOperationType() != null)
					unit.put("OperationType", String.valueOf(r.getOperationType()));
				else
					unit.put("OperationType", "");
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getDetails() != null)
					unit.put("Details", String.valueOf(r.getDetails()));
				else
					unit.put("Details", "");
				dataAuditLog.add(unit);
			}
			
			allObjectTables.get("AuditLog").setItems(dataAuditLog);
	}
	public void updateTransferRecordTable(List<TransferRecord> rsTransferRecord) {
			ObservableList<Map<String, String>> dataTransferRecord = FXCollections.observableArrayList();
			for (TransferRecord r : rsTransferRecord) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getTransferID() != null)
					unit.put("TransferID", String.valueOf(r.getTransferID()));
				else
					unit.put("TransferID", "");
				if (r.getSenderAccount() != null)
					unit.put("SenderAccount", String.valueOf(r.getSenderAccount()));
				else
					unit.put("SenderAccount", "");
				if (r.getReceiverAccount() != null)
					unit.put("ReceiverAccount", String.valueOf(r.getReceiverAccount()));
				else
					unit.put("ReceiverAccount", "");
				unit.put("Amount", String.valueOf(r.getAmount()));
				if (r.getStatus() != null)
					unit.put("Status", String.valueOf(r.getStatus()));
				else
					unit.put("Status", "");
				dataTransferRecord.add(unit);
			}
			
			allObjectTables.get("TransferRecord").setItems(dataTransferRecord);
	}
	public void updateBalanceInquiryTable(List<BalanceInquiry> rsBalanceInquiry) {
			ObservableList<Map<String, String>> dataBalanceInquiry = FXCollections.observableArrayList();
			for (BalanceInquiry r : rsBalanceInquiry) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getInquiryID() != null)
					unit.put("InquiryID", String.valueOf(r.getInquiryID()));
				else
					unit.put("InquiryID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getResult() != null)
					unit.put("Result", String.valueOf(r.getResult()));
				else
					unit.put("Result", "");
				dataBalanceInquiry.add(unit);
			}
			
			allObjectTables.get("BalanceInquiry").setItems(dataBalanceInquiry);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateUserTable() {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			List<User> rsUser = EntityManager.getAllInstancesOf("User");
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getRole() != null)
					unit.put("Role", String.valueOf(r.getRole()));
				else
					unit.put("Role", "");
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateDebitCardTable() {
			ObservableList<Map<String, String>> dataDebitCard = FXCollections.observableArrayList();
			List<DebitCard> rsDebitCard = EntityManager.getAllInstancesOf("DebitCard");
			for (DebitCard r : rsDebitCard) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getCardID() != null)
					unit.put("CardID", String.valueOf(r.getCardID()));
				else
					unit.put("CardID", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				if (r.getPINHash() != null)
					unit.put("PINHash", String.valueOf(r.getPINHash()));
				else
					unit.put("PINHash", "");
				dataDebitCard.add(unit);
			}
			
			allObjectTables.get("DebitCard").setItems(dataDebitCard);
	}
	public void updateAccountTable() {
			ObservableList<Map<String, String>> dataAccount = FXCollections.observableArrayList();
			List<Account> rsAccount = EntityManager.getAllInstancesOf("Account");
			for (Account r : rsAccount) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("FrozenBalance", String.valueOf(r.getFrozenBalance()));
				dataAccount.add(unit);
			}
			
			allObjectTables.get("Account").setItems(dataAccount);
	}
	public void updateTransactionTable() {
			ObservableList<Map<String, String>> dataTransaction = FXCollections.observableArrayList();
			List<Transaction> rsTransaction = EntityManager.getAllInstancesOf("Transaction");
			for (Transaction r : rsTransaction) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getTransactionID() != null)
					unit.put("TransactionID", String.valueOf(r.getTransactionID()));
				else
					unit.put("TransactionID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				unit.put("Amount", String.valueOf(r.getAmount()));
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getTransactionType() != null)
					unit.put("TransactionType", String.valueOf(r.getTransactionType()));
				else
					unit.put("TransactionType", "");
				dataTransaction.add(unit);
			}
			
			allObjectTables.get("Transaction").setItems(dataTransaction);
	}
	public void updateCashInventoryTable() {
			ObservableList<Map<String, String>> dataCashInventory = FXCollections.observableArrayList();
			List<CashInventory> rsCashInventory = EntityManager.getAllInstancesOf("CashInventory");
			for (CashInventory r : rsCashInventory) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Denomination", String.valueOf(r.getDenomination()));
				unit.put("Count", String.valueOf(r.getCount()));
				if (r.getLastUpdated() != null)
					unit.put("LastUpdated", r.getLastUpdated().format(dateformatter));
				else
					unit.put("LastUpdated", "");
				dataCashInventory.add(unit);
			}
			
			allObjectTables.get("CashInventory").setItems(dataCashInventory);
	}
	public void updateMaintenancePersonnelTable() {
			ObservableList<Map<String, String>> dataMaintenancePersonnel = FXCollections.observableArrayList();
			List<MaintenancePersonnel> rsMaintenancePersonnel = EntityManager.getAllInstancesOf("MaintenancePersonnel");
			for (MaintenancePersonnel r : rsMaintenancePersonnel) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getOperatorID() != null)
					unit.put("OperatorID", String.valueOf(r.getOperatorID()));
				else
					unit.put("OperatorID", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getKeyID() != null)
					unit.put("KeyID", String.valueOf(r.getKeyID()));
				else
					unit.put("KeyID", "");
				if (r.getAuthCredentials() != null)
					unit.put("AuthCredentials", String.valueOf(r.getAuthCredentials()));
				else
					unit.put("AuthCredentials", "");
				dataMaintenancePersonnel.add(unit);
			}
			
			allObjectTables.get("MaintenancePersonnel").setItems(dataMaintenancePersonnel);
	}
	public void updateMaintenanceLogTable() {
			ObservableList<Map<String, String>> dataMaintenanceLog = FXCollections.observableArrayList();
			List<MaintenanceLog> rsMaintenanceLog = EntityManager.getAllInstancesOf("MaintenanceLog");
			for (MaintenanceLog r : rsMaintenanceLog) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getLogID() != null)
					unit.put("LogID", String.valueOf(r.getLogID()));
				else
					unit.put("LogID", "");
				if (r.getOperatorID() != null)
					unit.put("OperatorID", String.valueOf(r.getOperatorID()));
				else
					unit.put("OperatorID", "");
				unit.put("Denomination", String.valueOf(r.getDenomination()));
				unit.put("Count", String.valueOf(r.getCount()));
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				dataMaintenanceLog.add(unit);
			}
			
			allObjectTables.get("MaintenanceLog").setItems(dataMaintenanceLog);
	}
	public void updateSecurityTokenTable() {
			ObservableList<Map<String, String>> dataSecurityToken = FXCollections.observableArrayList();
			List<SecurityToken> rsSecurityToken = EntityManager.getAllInstancesOf("SecurityToken");
			for (SecurityToken r : rsSecurityToken) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getTokenID() != null)
					unit.put("TokenID", String.valueOf(r.getTokenID()));
				else
					unit.put("TokenID", "");
				if (r.getType() != null)
					unit.put("Type", String.valueOf(r.getType()));
				else
					unit.put("Type", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				dataSecurityToken.add(unit);
			}
			
			allObjectTables.get("SecurityToken").setItems(dataSecurityToken);
	}
	public void updateAuditLogTable() {
			ObservableList<Map<String, String>> dataAuditLog = FXCollections.observableArrayList();
			List<AuditLog> rsAuditLog = EntityManager.getAllInstancesOf("AuditLog");
			for (AuditLog r : rsAuditLog) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getLogID() != null)
					unit.put("LogID", String.valueOf(r.getLogID()));
				else
					unit.put("LogID", "");
				if (r.getOperationType() != null)
					unit.put("OperationType", String.valueOf(r.getOperationType()));
				else
					unit.put("OperationType", "");
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getUserID() != null)
					unit.put("UserID", String.valueOf(r.getUserID()));
				else
					unit.put("UserID", "");
				if (r.getDetails() != null)
					unit.put("Details", String.valueOf(r.getDetails()));
				else
					unit.put("Details", "");
				dataAuditLog.add(unit);
			}
			
			allObjectTables.get("AuditLog").setItems(dataAuditLog);
	}
	public void updateTransferRecordTable() {
			ObservableList<Map<String, String>> dataTransferRecord = FXCollections.observableArrayList();
			List<TransferRecord> rsTransferRecord = EntityManager.getAllInstancesOf("TransferRecord");
			for (TransferRecord r : rsTransferRecord) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getTransferID() != null)
					unit.put("TransferID", String.valueOf(r.getTransferID()));
				else
					unit.put("TransferID", "");
				if (r.getSenderAccount() != null)
					unit.put("SenderAccount", String.valueOf(r.getSenderAccount()));
				else
					unit.put("SenderAccount", "");
				if (r.getReceiverAccount() != null)
					unit.put("ReceiverAccount", String.valueOf(r.getReceiverAccount()));
				else
					unit.put("ReceiverAccount", "");
				unit.put("Amount", String.valueOf(r.getAmount()));
				if (r.getStatus() != null)
					unit.put("Status", String.valueOf(r.getStatus()));
				else
					unit.put("Status", "");
				dataTransferRecord.add(unit);
			}
			
			allObjectTables.get("TransferRecord").setItems(dataTransferRecord);
	}
	public void updateBalanceInquiryTable() {
			ObservableList<Map<String, String>> dataBalanceInquiry = FXCollections.observableArrayList();
			List<BalanceInquiry> rsBalanceInquiry = EntityManager.getAllInstancesOf("BalanceInquiry");
			for (BalanceInquiry r : rsBalanceInquiry) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getInquiryID() != null)
					unit.put("InquiryID", String.valueOf(r.getInquiryID()));
				else
					unit.put("InquiryID", "");
				if (r.getAccountNumber() != null)
					unit.put("AccountNumber", String.valueOf(r.getAccountNumber()));
				else
					unit.put("AccountNumber", "");
				if (r.getTimestamp() != null)
					unit.put("Timestamp", r.getTimestamp().format(dateformatter));
				else
					unit.put("Timestamp", "");
				if (r.getResult() != null)
					unit.put("Result", String.valueOf(r.getResult()));
				else
					unit.put("Result", "");
				dataBalanceInquiry.add(unit);
			}
			
			allObjectTables.get("BalanceInquiry").setItems(dataBalanceInquiry);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 user = new ClassInfo("User", EntityManager.getAllInstancesOf("User").size());
	 	 classInfodata.add(user);
	 	 debitcard = new ClassInfo("DebitCard", EntityManager.getAllInstancesOf("DebitCard").size());
	 	 classInfodata.add(debitcard);
	 	 account = new ClassInfo("Account", EntityManager.getAllInstancesOf("Account").size());
	 	 classInfodata.add(account);
	 	 transaction = new ClassInfo("Transaction", EntityManager.getAllInstancesOf("Transaction").size());
	 	 classInfodata.add(transaction);
	 	 cashinventory = new ClassInfo("CashInventory", EntityManager.getAllInstancesOf("CashInventory").size());
	 	 classInfodata.add(cashinventory);
	 	 maintenancepersonnel = new ClassInfo("MaintenancePersonnel", EntityManager.getAllInstancesOf("MaintenancePersonnel").size());
	 	 classInfodata.add(maintenancepersonnel);
	 	 maintenancelog = new ClassInfo("MaintenanceLog", EntityManager.getAllInstancesOf("MaintenanceLog").size());
	 	 classInfodata.add(maintenancelog);
	 	 securitytoken = new ClassInfo("SecurityToken", EntityManager.getAllInstancesOf("SecurityToken").size());
	 	 classInfodata.add(securitytoken);
	 	 auditlog = new ClassInfo("AuditLog", EntityManager.getAllInstancesOf("AuditLog").size());
	 	 classInfodata.add(auditlog);
	 	 transferrecord = new ClassInfo("TransferRecord", EntityManager.getAllInstancesOf("TransferRecord").size());
	 	 classInfodata.add(transferrecord);
	 	 balanceinquiry = new ClassInfo("BalanceInquiry", EntityManager.getAllInstancesOf("BalanceInquiry").size());
	 	 classInfodata.add(balanceinquiry);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 user.setNumber(EntityManager.getAllInstancesOf("User").size());
	 	 debitcard.setNumber(EntityManager.getAllInstancesOf("DebitCard").size());
	 	 account.setNumber(EntityManager.getAllInstancesOf("Account").size());
	 	 transaction.setNumber(EntityManager.getAllInstancesOf("Transaction").size());
	 	 cashinventory.setNumber(EntityManager.getAllInstancesOf("CashInventory").size());
	 	 maintenancepersonnel.setNumber(EntityManager.getAllInstancesOf("MaintenancePersonnel").size());
	 	 maintenancelog.setNumber(EntityManager.getAllInstancesOf("MaintenanceLog").size());
	 	 securitytoken.setNumber(EntityManager.getAllInstancesOf("SecurityToken").size());
	 	 auditlog.setNumber(EntityManager.getAllInstancesOf("AuditLog").size());
	 	 transferrecord.setNumber(EntityManager.getAllInstancesOf("TransferRecord").size());
	 	 balanceinquiry.setNumber(EntityManager.getAllInstancesOf("BalanceInquiry").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> User_association_data = FXCollections.observableArrayList();
		AssociationInfo User_associatition_Own = new AssociationInfo("User", "DebitCard", "Own", true);
		User_association_data.add(User_associatition_Own);
		AssociationInfo User_associatition_Holds = new AssociationInfo("User", "Account", "Holds", true);
		User_association_data.add(User_associatition_Holds);
		AssociationInfo User_associatition_Generates = new AssociationInfo("User", "AuditLog", "Generates", true);
		User_association_data.add(User_associatition_Generates);
		
		allassociationData.put("User", User_association_data);
		
		ObservableList<AssociationInfo> DebitCard_association_data = FXCollections.observableArrayList();
		AssociationInfo DebitCard_associatition_Linked = new AssociationInfo("DebitCard", "Account", "Linked", false);
		DebitCard_association_data.add(DebitCard_associatition_Linked);
		
		allassociationData.put("DebitCard", DebitCard_association_data);
		
		ObservableList<AssociationInfo> Account_association_data = FXCollections.observableArrayList();
		AssociationInfo Account_associatition_Has = new AssociationInfo("Account", "Transaction", "Has", true);
		Account_association_data.add(Account_associatition_Has);
		AssociationInfo Account_associatition_Have = new AssociationInfo("Account", "BalanceInquiry", "Have", true);
		Account_association_data.add(Account_associatition_Have);
		AssociationInfo Account_associatition_Generates = new AssociationInfo("Account", "TransferRecord", "Generates", true);
		Account_association_data.add(Account_associatition_Generates);
		
		allassociationData.put("Account", Account_association_data);
		
		ObservableList<AssociationInfo> Transaction_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("Transaction", Transaction_association_data);
		
		ObservableList<AssociationInfo> CashInventory_association_data = FXCollections.observableArrayList();
		AssociationInfo CashInventory_associatition_Update = new AssociationInfo("CashInventory", "MaintenanceLog", "Update", true);
		CashInventory_association_data.add(CashInventory_associatition_Update);
		
		allassociationData.put("CashInventory", CashInventory_association_data);
		
		ObservableList<AssociationInfo> MaintenancePersonnel_association_data = FXCollections.observableArrayList();
		AssociationInfo MaintenancePersonnel_associatition_Performs = new AssociationInfo("MaintenancePersonnel", "MaintenanceLog", "Performs", true);
		MaintenancePersonnel_association_data.add(MaintenancePersonnel_associatition_Performs);
		
		allassociationData.put("MaintenancePersonnel", MaintenancePersonnel_association_data);
		
		ObservableList<AssociationInfo> MaintenanceLog_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("MaintenanceLog", MaintenanceLog_association_data);
		
		ObservableList<AssociationInfo> SecurityToken_association_data = FXCollections.observableArrayList();
		AssociationInfo SecurityToken_associatition_Secures = new AssociationInfo("SecurityToken", "AuditLog", "Secures", true);
		SecurityToken_association_data.add(SecurityToken_associatition_Secures);
		
		allassociationData.put("SecurityToken", SecurityToken_association_data);
		
		ObservableList<AssociationInfo> AuditLog_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("AuditLog", AuditLog_association_data);
		
		ObservableList<AssociationInfo> TransferRecord_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("TransferRecord", TransferRecord_association_data);
		
		ObservableList<AssociationInfo> BalanceInquiry_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("BalanceInquiry", BalanceInquiry_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("insertDebitCard", "true");
		preconditions_map.put("inputPIN", "true");
		preconditions_map.put("selectWithdrawal", "true");
		preconditions_map.put("chooseAmount", "true");
		preconditions_map.put("takeCash", "true");
		preconditions_map.put("selectBalanceInquiry", "true");
		preconditions_map.put("insertPhysicalKey", "true");
		preconditions_map.put("inputCredentials", "true");
		preconditions_map.put("loadBanknoteBundle", "true");
		preconditions_map.put("insertFIPSToken", "true");
		preconditions_map.put("selectLogRange", "true");
		preconditions_map.put("selectTransferMenu", "true");
		preconditions_map.put("chooseTargetAccount", "true");
		preconditions_map.put("chooseAndConfirmAmount", "true");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("insertDebitCard", "result = true");
		postconditions_map.put("inputPIN", "result = true");
		postconditions_map.put("selectWithdrawal", "result = true");
		postconditions_map.put("chooseAmount", "result = true");
		postconditions_map.put("takeCash", "result = true");
		postconditions_map.put("selectBalanceInquiry", "result = true");
		postconditions_map.put("insertPhysicalKey", "result = true");
		postconditions_map.put("inputCredentials", "result = true");
		postconditions_map.put("loadBanknoteBundle", "result = true");
		postconditions_map.put("insertFIPSToken", "result = true");
		postconditions_map.put("selectLogRange", "result = true");
		postconditions_map.put("selectTransferMenu", "result = true");
		postconditions_map.put("chooseTargetAccount", "result = true");
		postconditions_map.put("chooseAndConfirmAmount", "result = true");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_insertDebitCard ====================
		 GridPane insertDebitCard = new GridPane();
		 insertDebitCard.setHgap(4);
		 insertDebitCard.setVgap(6);
		 insertDebitCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> insertDebitCard_content = insertDebitCard.getChildren();
		 Label insertDebitCard_cardID_label = new Label("cardID:");
		 insertDebitCard_cardID_label.setMinWidth(Region.USE_PREF_SIZE);
		 insertDebitCard_content.add(insertDebitCard_cardID_label);
		 GridPane.setConstraints(insertDebitCard_cardID_label, 0, 0);
		 
		 insertDebitCard_cardID_t = new TextField();
		 insertDebitCard_content.add(insertDebitCard_cardID_t);
		 insertDebitCard_cardID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(insertDebitCard_cardID_t, 1, 0);
		 operationPanels.put("insertDebitCard", insertDebitCard);
		 
		 // ==================== GridPane_inputPIN ====================
		 GridPane inputPIN = new GridPane();
		 inputPIN.setHgap(4);
		 inputPIN.setVgap(6);
		 inputPIN.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputPIN_content = inputPIN.getChildren();
		 Label inputPIN_pinCode_label = new Label("pinCode:");
		 inputPIN_pinCode_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputPIN_content.add(inputPIN_pinCode_label);
		 GridPane.setConstraints(inputPIN_pinCode_label, 0, 0);
		 
		 inputPIN_pinCode_t = new TextField();
		 inputPIN_content.add(inputPIN_pinCode_t);
		 inputPIN_pinCode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputPIN_pinCode_t, 1, 0);
		 operationPanels.put("inputPIN", inputPIN);
		 
		 // ==================== GridPane_selectWithdrawal ====================
		 GridPane selectWithdrawal = new GridPane();
		 selectWithdrawal.setHgap(4);
		 selectWithdrawal.setVgap(6);
		 selectWithdrawal.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectWithdrawal_content = selectWithdrawal.getChildren();
		 Label selectWithdrawal_optionID_label = new Label("optionID:");
		 selectWithdrawal_optionID_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectWithdrawal_content.add(selectWithdrawal_optionID_label);
		 GridPane.setConstraints(selectWithdrawal_optionID_label, 0, 0);
		 
		 selectWithdrawal_optionID_t = new TextField();
		 selectWithdrawal_content.add(selectWithdrawal_optionID_t);
		 selectWithdrawal_optionID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectWithdrawal_optionID_t, 1, 0);
		 operationPanels.put("selectWithdrawal", selectWithdrawal);
		 
		 // ==================== GridPane_chooseAmount ====================
		 GridPane chooseAmount = new GridPane();
		 chooseAmount.setHgap(4);
		 chooseAmount.setVgap(6);
		 chooseAmount.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseAmount_content = chooseAmount.getChildren();
		 Label chooseAmount_selectedAmount_label = new Label("selectedAmount:");
		 chooseAmount_selectedAmount_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseAmount_content.add(chooseAmount_selectedAmount_label);
		 GridPane.setConstraints(chooseAmount_selectedAmount_label, 0, 0);
		 
		 chooseAmount_selectedAmount_t = new TextField();
		 chooseAmount_content.add(chooseAmount_selectedAmount_t);
		 chooseAmount_selectedAmount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseAmount_selectedAmount_t, 1, 0);
		 operationPanels.put("chooseAmount", chooseAmount);
		 
		 // ==================== GridPane_takeCash ====================
		 GridPane takeCash = new GridPane();
		 takeCash.setHgap(4);
		 takeCash.setVgap(6);
		 takeCash.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> takeCash_content = takeCash.getChildren();
		 Label takeCash_label = new Label("This operation is no intput parameters..");
		 takeCash_label.setMinWidth(Region.USE_PREF_SIZE);
		 takeCash_content.add(takeCash_label);
		 GridPane.setConstraints(takeCash_label, 0, 0);
		 operationPanels.put("takeCash", takeCash);
		 
		 // ==================== GridPane_selectBalanceInquiry ====================
		 GridPane selectBalanceInquiry = new GridPane();
		 selectBalanceInquiry.setHgap(4);
		 selectBalanceInquiry.setVgap(6);
		 selectBalanceInquiry.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectBalanceInquiry_content = selectBalanceInquiry.getChildren();
		 Label selectBalanceInquiry_menuOption_label = new Label("menuOption:");
		 selectBalanceInquiry_menuOption_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectBalanceInquiry_content.add(selectBalanceInquiry_menuOption_label);
		 GridPane.setConstraints(selectBalanceInquiry_menuOption_label, 0, 0);
		 
		 selectBalanceInquiry_menuOption_t = new TextField();
		 selectBalanceInquiry_content.add(selectBalanceInquiry_menuOption_t);
		 selectBalanceInquiry_menuOption_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectBalanceInquiry_menuOption_t, 1, 0);
		 operationPanels.put("selectBalanceInquiry", selectBalanceInquiry);
		 
		 // ==================== GridPane_insertPhysicalKey ====================
		 GridPane insertPhysicalKey = new GridPane();
		 insertPhysicalKey.setHgap(4);
		 insertPhysicalKey.setVgap(6);
		 insertPhysicalKey.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> insertPhysicalKey_content = insertPhysicalKey.getChildren();
		 Label insertPhysicalKey_keyID_label = new Label("keyID:");
		 insertPhysicalKey_keyID_label.setMinWidth(Region.USE_PREF_SIZE);
		 insertPhysicalKey_content.add(insertPhysicalKey_keyID_label);
		 GridPane.setConstraints(insertPhysicalKey_keyID_label, 0, 0);
		 
		 insertPhysicalKey_keyID_t = new TextField();
		 insertPhysicalKey_content.add(insertPhysicalKey_keyID_t);
		 insertPhysicalKey_keyID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(insertPhysicalKey_keyID_t, 1, 0);
		 operationPanels.put("insertPhysicalKey", insertPhysicalKey);
		 
		 // ==================== GridPane_inputCredentials ====================
		 GridPane inputCredentials = new GridPane();
		 inputCredentials.setHgap(4);
		 inputCredentials.setVgap(6);
		 inputCredentials.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputCredentials_content = inputCredentials.getChildren();
		 Label inputCredentials_operatorID_label = new Label("operatorID:");
		 inputCredentials_operatorID_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputCredentials_content.add(inputCredentials_operatorID_label);
		 GridPane.setConstraints(inputCredentials_operatorID_label, 0, 0);
		 
		 inputCredentials_operatorID_t = new TextField();
		 inputCredentials_content.add(inputCredentials_operatorID_t);
		 inputCredentials_operatorID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputCredentials_operatorID_t, 1, 0);
		 operationPanels.put("inputCredentials", inputCredentials);
		 
		 // ==================== GridPane_loadBanknoteBundle ====================
		 GridPane loadBanknoteBundle = new GridPane();
		 loadBanknoteBundle.setHgap(4);
		 loadBanknoteBundle.setVgap(6);
		 loadBanknoteBundle.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> loadBanknoteBundle_content = loadBanknoteBundle.getChildren();
		 Label loadBanknoteBundle_denomination_label = new Label("denomination:");
		 loadBanknoteBundle_denomination_label.setMinWidth(Region.USE_PREF_SIZE);
		 loadBanknoteBundle_content.add(loadBanknoteBundle_denomination_label);
		 GridPane.setConstraints(loadBanknoteBundle_denomination_label, 0, 0);
		 
		 loadBanknoteBundle_denomination_t = new TextField();
		 loadBanknoteBundle_content.add(loadBanknoteBundle_denomination_t);
		 loadBanknoteBundle_denomination_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(loadBanknoteBundle_denomination_t, 1, 0);
		 Label loadBanknoteBundle_count_label = new Label("count:");
		 loadBanknoteBundle_count_label.setMinWidth(Region.USE_PREF_SIZE);
		 loadBanknoteBundle_content.add(loadBanknoteBundle_count_label);
		 GridPane.setConstraints(loadBanknoteBundle_count_label, 0, 1);
		 
		 loadBanknoteBundle_count_t = new TextField();
		 loadBanknoteBundle_content.add(loadBanknoteBundle_count_t);
		 loadBanknoteBundle_count_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(loadBanknoteBundle_count_t, 1, 1);
		 operationPanels.put("loadBanknoteBundle", loadBanknoteBundle);
		 
		 // ==================== GridPane_insertFIPSToken ====================
		 GridPane insertFIPSToken = new GridPane();
		 insertFIPSToken.setHgap(4);
		 insertFIPSToken.setVgap(6);
		 insertFIPSToken.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> insertFIPSToken_content = insertFIPSToken.getChildren();
		 Label insertFIPSToken_token_label = new Label("token:");
		 insertFIPSToken_token_label.setMinWidth(Region.USE_PREF_SIZE);
		 insertFIPSToken_content.add(insertFIPSToken_token_label);
		 GridPane.setConstraints(insertFIPSToken_token_label, 0, 0);
		 
		 insertFIPSToken_token_t = new TextField();
		 insertFIPSToken_content.add(insertFIPSToken_token_t);
		 insertFIPSToken_token_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(insertFIPSToken_token_t, 1, 0);
		 operationPanels.put("insertFIPSToken", insertFIPSToken);
		 
		 // ==================== GridPane_selectLogRange ====================
		 GridPane selectLogRange = new GridPane();
		 selectLogRange.setHgap(4);
		 selectLogRange.setVgap(6);
		 selectLogRange.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectLogRange_content = selectLogRange.getChildren();
		 Label selectLogRange_start_label = new Label("start (yyyy-MM-dd):");
		 selectLogRange_start_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectLogRange_content.add(selectLogRange_start_label);
		 GridPane.setConstraints(selectLogRange_start_label, 0, 0);
		 
		 selectLogRange_start_t = new TextField();
		 selectLogRange_content.add(selectLogRange_start_t);
		 selectLogRange_start_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectLogRange_start_t, 1, 0);
		 Label selectLogRange_end_label = new Label("end (yyyy-MM-dd):");
		 selectLogRange_end_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectLogRange_content.add(selectLogRange_end_label);
		 GridPane.setConstraints(selectLogRange_end_label, 0, 1);
		 
		 selectLogRange_end_t = new TextField();
		 selectLogRange_content.add(selectLogRange_end_t);
		 selectLogRange_end_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectLogRange_end_t, 1, 1);
		 operationPanels.put("selectLogRange", selectLogRange);
		 
		 // ==================== GridPane_selectTransferMenu ====================
		 GridPane selectTransferMenu = new GridPane();
		 selectTransferMenu.setHgap(4);
		 selectTransferMenu.setVgap(6);
		 selectTransferMenu.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectTransferMenu_content = selectTransferMenu.getChildren();
		 Label selectTransferMenu_optionID_label = new Label("optionID:");
		 selectTransferMenu_optionID_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectTransferMenu_content.add(selectTransferMenu_optionID_label);
		 GridPane.setConstraints(selectTransferMenu_optionID_label, 0, 0);
		 
		 selectTransferMenu_optionID_t = new TextField();
		 selectTransferMenu_content.add(selectTransferMenu_optionID_t);
		 selectTransferMenu_optionID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectTransferMenu_optionID_t, 1, 0);
		 operationPanels.put("selectTransferMenu", selectTransferMenu);
		 
		 // ==================== GridPane_chooseTargetAccount ====================
		 GridPane chooseTargetAccount = new GridPane();
		 chooseTargetAccount.setHgap(4);
		 chooseTargetAccount.setVgap(6);
		 chooseTargetAccount.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseTargetAccount_content = chooseTargetAccount.getChildren();
		 Label chooseTargetAccount_accountNo_label = new Label("accountNo:");
		 chooseTargetAccount_accountNo_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseTargetAccount_content.add(chooseTargetAccount_accountNo_label);
		 GridPane.setConstraints(chooseTargetAccount_accountNo_label, 0, 0);
		 
		 chooseTargetAccount_accountNo_t = new TextField();
		 chooseTargetAccount_content.add(chooseTargetAccount_accountNo_t);
		 chooseTargetAccount_accountNo_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseTargetAccount_accountNo_t, 1, 0);
		 operationPanels.put("chooseTargetAccount", chooseTargetAccount);
		 
		 // ==================== GridPane_chooseAndConfirmAmount ====================
		 GridPane chooseAndConfirmAmount = new GridPane();
		 chooseAndConfirmAmount.setHgap(4);
		 chooseAndConfirmAmount.setVgap(6);
		 chooseAndConfirmAmount.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseAndConfirmAmount_content = chooseAndConfirmAmount.getChildren();
		 Label chooseAndConfirmAmount_amount_label = new Label("amount:");
		 chooseAndConfirmAmount_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseAndConfirmAmount_content.add(chooseAndConfirmAmount_amount_label);
		 GridPane.setConstraints(chooseAndConfirmAmount_amount_label, 0, 0);
		 
		 chooseAndConfirmAmount_amount_t = new TextField();
		 chooseAndConfirmAmount_content.add(chooseAndConfirmAmount_amount_t);
		 chooseAndConfirmAmount_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseAndConfirmAmount_amount_t, 1, 0);
		 operationPanels.put("chooseAndConfirmAmount", chooseAndConfirmAmount);
		 
	}	

	public void actorTreeViewBinding() {
		
		 

		TreeItem<String> treeRootadministrator = new TreeItem<String>("Root node");
		
		TreeItem<String> subTreeRoot_User = new TreeItem<String>("manageUser");
					 		subTreeRoot_User.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createUser"),
					 			 		new TreeItem<String>("queryUser"),
					 			 		new TreeItem<String>("modifyUser"),
					 			 		new TreeItem<String>("deleteUser")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_DebitCard = new TreeItem<String>("manageDebitCard");
					 		subTreeRoot_DebitCard.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createDebitCard"),
					 			 		new TreeItem<String>("queryDebitCard"),
					 			 		new TreeItem<String>("modifyDebitCard"),
					 			 		new TreeItem<String>("deleteDebitCard")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_Account = new TreeItem<String>("manageAccount");
					 		subTreeRoot_Account.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createAccount"),
					 			 		new TreeItem<String>("queryAccount"),
					 			 		new TreeItem<String>("modifyAccount"),
					 			 		new TreeItem<String>("deleteAccount")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_Transaction = new TreeItem<String>("manageTransaction");
					 		subTreeRoot_Transaction.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createTransaction"),
					 			 		new TreeItem<String>("queryTransaction"),
					 			 		new TreeItem<String>("modifyTransaction"),
					 			 		new TreeItem<String>("deleteTransaction")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_CashInventory = new TreeItem<String>("manageCashInventory");
					 		subTreeRoot_CashInventory.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createCashInventory"),
					 			 		new TreeItem<String>("queryCashInventory"),
					 			 		new TreeItem<String>("modifyCashInventory"),
					 			 		new TreeItem<String>("deleteCashInventory")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_MaintenancePersonnel = new TreeItem<String>("manageMaintenancePersonnel");
					 		subTreeRoot_MaintenancePersonnel.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createMaintenancePersonnel"),
					 			 		new TreeItem<String>("queryMaintenancePersonnel"),
					 			 		new TreeItem<String>("modifyMaintenancePersonnel"),
					 			 		new TreeItem<String>("deleteMaintenancePersonnel")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_MaintenanceLog = new TreeItem<String>("manageMaintenanceLog");
					 		subTreeRoot_MaintenanceLog.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createMaintenanceLog"),
					 			 		new TreeItem<String>("queryMaintenanceLog"),
					 			 		new TreeItem<String>("modifyMaintenanceLog"),
					 			 		new TreeItem<String>("deleteMaintenanceLog")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_SecurityToken = new TreeItem<String>("manageSecurityToken");
					 		subTreeRoot_SecurityToken.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createSecurityToken"),
					 			 		new TreeItem<String>("querySecurityToken"),
					 			 		new TreeItem<String>("modifySecurityToken"),
					 			 		new TreeItem<String>("deleteSecurityToken")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_AuditLog = new TreeItem<String>("manageAuditLog");
					 		subTreeRoot_AuditLog.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createAuditLog"),
					 			 		new TreeItem<String>("queryAuditLog"),
					 			 		new TreeItem<String>("modifyAuditLog"),
					 			 		new TreeItem<String>("deleteAuditLog")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_TransferRecord = new TreeItem<String>("manageTransferRecord");
					 		subTreeRoot_TransferRecord.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createTransferRecord"),
					 			 		new TreeItem<String>("queryTransferRecord"),
					 			 		new TreeItem<String>("modifyTransferRecord"),
					 			 		new TreeItem<String>("deleteTransferRecord")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_BalanceInquiry = new TreeItem<String>("manageBalanceInquiry");
					 		subTreeRoot_BalanceInquiry.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createBalanceInquiry"),
					 			 		new TreeItem<String>("queryBalanceInquiry"),
					 			 		new TreeItem<String>("modifyBalanceInquiry"),
					 			 		new TreeItem<String>("deleteBalanceInquiry")					 			 	
					 			 	));							 		
		
					 			
						 		
		treeRootadministrator.getChildren().addAll(Arrays.asList(
		 	subTreeRoot_User,
		 	subTreeRoot_DebitCard,
		 	subTreeRoot_Account,
		 	subTreeRoot_Transaction,
		 	subTreeRoot_CashInventory,
		 	subTreeRoot_MaintenancePersonnel,
		 	subTreeRoot_MaintenanceLog,
		 	subTreeRoot_SecurityToken,
		 	subTreeRoot_AuditLog,
		 	subTreeRoot_TransferRecord,
		 	subTreeRoot_BalanceInquiry
				));	
				
	 			treeRootadministrator.setExpanded(true);

		actor_treeview_administrator.setShowRoot(false);
		actor_treeview_administrator.setRoot(treeRootadministrator);
	 		
		actor_treeview_administrator.getSelectionModel().selectedItemProperty().addListener(
		 				 (observable, oldValue, newValue) -> { 
		 				 								
		 				 							 //clear the previous return
		 											 operation_return_pane.setContent(new Label());
		 											 
		 				 							 clickedOp = newValue.getValue();
		 				 							 GridPane op = operationPanels.get(clickedOp);
		 				 							 VBox vb = opInvariantPanel.get(clickedOp);
		 				 							 
		 				 							 //op pannel
		 				 							 if (op != null) {
		 				 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
		 				 								 
		 				 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
		 				 								 choosenOperation = new LinkedList<TextField>();
		 				 								 for (Node n : l) {
		 				 								 	 if (n instanceof TextField) {
		 				 								 	 	choosenOperation.add((TextField)n);
		 				 								 	  }
		 				 								 }
		 				 								 
		 				 								 definition.setText(definitions_map.get(newValue.getValue()));
		 				 								 precondition.setText(preconditions_map.get(newValue.getValue()));
		 				 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
		 				 								 
		 				 						     }
		 				 							 else {
		 				 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
		 				 								 l.setPadding(new Insets(8, 8, 8, 8));
		 				 								 operation_paras.setContent(l);
		 				 							 }	
		 				 							 
		 				 							 //op invariants
		 				 							 if (vb != null) {
		 				 							 	ScrollPane scrollPane = new ScrollPane(vb);
		 				 							 	scrollPane.setFitToWidth(true);
		 				 							 	invariants_panes.setMaxHeight(200); 
		 				 							 	//all_invariant_pane.setContent(scrollPane);	
		 				 							 	
		 				 							 	invariants_panes.setContent(scrollPane);
		 				 							 } else {
		 				 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
		 				 							     l.setPadding(new Insets(8, 8, 8, 8));
		 				 							     invariants_panes.setContent(l);
		 				 							 }
		 				 							 
		 				 							 //reset pre- and post-conditions area color
		 				 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
		 				 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
		 				 							 //reset condition panel title
		 				 							 precondition_pane.setText("Precondition");
		 				 							 postcondition_pane.setText("Postcondition");
		 				 						} 
		 				 				);

		
		
		 
		TreeItem<String> treeRootcustomer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_withdrawCash = new TreeItem<String>("withdrawCash");
			subTreeRoot_withdrawCash.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("insertDebitCard"),
					 	new TreeItem<String>("inputPIN"),
					 	new TreeItem<String>("selectWithdrawal"),
					 	new TreeItem<String>("chooseAmount"),
					 	new TreeItem<String>("takeCash")
				 		));	
			TreeItem<String> subTreeRoot_checkBalance = new TreeItem<String>("checkBalance");
			subTreeRoot_checkBalance.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("selectBalanceInquiry")
				 		));	
			TreeItem<String> subTreeRoot_changePIN = new TreeItem<String>("changePIN");
			subTreeRoot_changePIN.getChildren().addAll(Arrays.asList(			 		    
				 		));	
			TreeItem<String> subTreeRoot_transferFunds = new TreeItem<String>("transferFunds");
			subTreeRoot_transferFunds.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("selectTransferMenu"),
					 	new TreeItem<String>("chooseTargetAccount"),
					 	new TreeItem<String>("chooseAndConfirmAmount")
				 		));	
		
		treeRootcustomer.getChildren().addAll(Arrays.asList(
			subTreeRoot_withdrawCash,
			subTreeRoot_checkBalance,
			subTreeRoot_changePIN,
			subTreeRoot_transferFunds
					));
		
		treeRootcustomer.setExpanded(true);

		actor_treeview_customer.setShowRoot(false);
		actor_treeview_customer.setRoot(treeRootcustomer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_customer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootmaintenance = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_replenishCash = new TreeItem<String>("replenishCash");
			subTreeRoot_replenishCash.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("insertPhysicalKey"),
					 	new TreeItem<String>("inputCredentials"),
					 	new TreeItem<String>("loadBanknoteBundle")
				 		));	
			TreeItem<String> subTreeRoot_updateFirmware = new TreeItem<String>("updateFirmware");
			subTreeRoot_updateFirmware.getChildren().addAll(Arrays.asList(			 		    
				 		));	
		
		treeRootmaintenance.getChildren().addAll(Arrays.asList(
			subTreeRoot_replenishCash,
			subTreeRoot_updateFirmware
					));
		
		treeRootmaintenance.setExpanded(true);

		actor_treeview_maintenance.setShowRoot(false);
		actor_treeview_maintenance.setRoot(treeRootmaintenance);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_maintenance.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootsystemadmin = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_manageAuditLogs = new TreeItem<String>("manageAuditLogs");
			subTreeRoot_manageAuditLogs.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("insertFIPSToken"),
					 	new TreeItem<String>("selectLogRange")
				 		));	
			TreeItem<String> subTreeRoot_setTransactionLimits = new TreeItem<String>("setTransactionLimits");
			subTreeRoot_setTransactionLimits.getChildren().addAll(Arrays.asList(			 		    
				 		));	
		
		treeRootsystemadmin.getChildren().addAll(Arrays.asList(
			subTreeRoot_manageAuditLogs,
			subTreeRoot_setTransactionLimits
					));
		
		treeRootsystemadmin.setExpanded(true);

		actor_treeview_systemadmin.setShowRoot(false);
		actor_treeview_systemadmin.setRoot(treeRootsystemadmin);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_systemadmin.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "insertDebitCard" : insertDebitCard(); break;
		case "inputPIN" : inputPIN(); break;
		case "selectWithdrawal" : selectWithdrawal(); break;
		case "chooseAmount" : chooseAmount(); break;
		case "takeCash" : takeCash(); break;
		case "selectBalanceInquiry" : selectBalanceInquiry(); break;
		case "insertPhysicalKey" : insertPhysicalKey(); break;
		case "inputCredentials" : inputCredentials(); break;
		case "loadBanknoteBundle" : loadBanknoteBundle(); break;
		case "insertFIPSToken" : insertFIPSToken(); break;
		case "selectLogRange" : selectLogRange(); break;
		case "selectTransferMenu" : selectTransferMenu(); break;
		case "chooseTargetAccount" : chooseTargetAccount(); break;
		case "chooseAndConfirmAmount" : chooseAndConfirmAmount(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void insertDebitCard() {
		
		System.out.println("execute insertDebitCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: insertDebitCard in service: WithdrawCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(withdrawcashservice_service.insertDebitCard(
			insertDebitCard_cardID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputPIN() {
		
		System.out.println("execute inputPIN");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputPIN in service: WithdrawCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(withdrawcashservice_service.inputPIN(
			inputPIN_pinCode_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectWithdrawal() {
		
		System.out.println("execute selectWithdrawal");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectWithdrawal in service: WithdrawCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(withdrawcashservice_service.selectWithdrawal(
			selectWithdrawal_optionID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseAmount() {
		
		System.out.println("execute chooseAmount");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseAmount in service: WithdrawCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(withdrawcashservice_service.chooseAmount(
			Integer.valueOf(chooseAmount_selectedAmount_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void takeCash() {
		
		System.out.println("execute takeCash");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: takeCash in service: WithdrawCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(withdrawcashservice_service.takeCash(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectBalanceInquiry() {
		
		System.out.println("execute selectBalanceInquiry");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectBalanceInquiry in service: CheckBalanceService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(checkbalanceservice_service.selectBalanceInquiry(
			selectBalanceInquiry_menuOption_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void insertPhysicalKey() {
		
		System.out.println("execute insertPhysicalKey");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: insertPhysicalKey in service: ReplenishCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(replenishcashservice_service.insertPhysicalKey(
			insertPhysicalKey_keyID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputCredentials() {
		
		System.out.println("execute inputCredentials");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputCredentials in service: ReplenishCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(replenishcashservice_service.inputCredentials(
			inputCredentials_operatorID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void loadBanknoteBundle() {
		
		System.out.println("execute loadBanknoteBundle");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: loadBanknoteBundle in service: ReplenishCashService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(replenishcashservice_service.loadBanknoteBundle(
			loadBanknoteBundle_denomination_t.getText(),
			Float.valueOf(loadBanknoteBundle_count_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void insertFIPSToken() {
		
		System.out.println("execute insertFIPSToken");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: insertFIPSToken in service: ManageAuditLogsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageauditlogsservice_service.insertFIPSToken(
			insertFIPSToken_token_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectLogRange() {
		
		System.out.println("execute selectLogRange");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectLogRange in service: ManageAuditLogsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageauditlogsservice_service.selectLogRange(
			LocalDate.parse(selectLogRange_start_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			LocalDate.parse(selectLogRange_end_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectTransferMenu() {
		
		System.out.println("execute selectTransferMenu");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectTransferMenu in service: TransferFundsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(transferfundsservice_service.selectTransferMenu(
			selectTransferMenu_optionID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseTargetAccount() {
		
		System.out.println("execute chooseTargetAccount");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseTargetAccount in service: TransferFundsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(transferfundsservice_service.chooseTargetAccount(
			chooseTargetAccount_accountNo_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseAndConfirmAmount() {
		
		System.out.println("execute chooseAndConfirmAmount");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseAndConfirmAmount in service: TransferFundsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(transferfundsservice_service.chooseAndConfirmAmount(
			Float.valueOf(chooseAndConfirmAmount_amount_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_customer;
	@FXML
	TreeView<String> actor_treeview_maintenance;
	@FXML
	TreeView<String> actor_treeview_systemadmin;
	
	@FXML
	TreeView<String> actor_treeview_administrator;


	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	ATMSystem atmsystem_service;
	ThirdPartyServices thirdpartyservices_service;
	WithdrawCashService withdrawcashservice_service;
	CheckBalanceService checkbalanceservice_service;
	ReplenishCashService replenishcashservice_service;
	ManageAuditLogsService manageauditlogsservice_service;
	TransferFundsService transferfundsservice_service;
	ChangePINService changepinservice_service;
	UpdateFirmwareService updatefirmwareservice_service;
	VerifyPINService verifypinservice_service;
	DisplayHistoryService displayhistoryservice_service;
	SecurityAuthenticationService securityauthenticationservice_service;
	SetTransactionLimitsService settransactionlimitsservice_service;
	
	ClassInfo user;
	ClassInfo debitcard;
	ClassInfo account;
	ClassInfo transaction;
	ClassInfo cashinventory;
	ClassInfo maintenancepersonnel;
	ClassInfo maintenancelog;
	ClassInfo securitytoken;
	ClassInfo auditlog;
	ClassInfo transferrecord;
	ClassInfo balanceinquiry;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML 
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField insertDebitCard_cardID_t;
	TextField inputPIN_pinCode_t;
	TextField selectWithdrawal_optionID_t;
	TextField chooseAmount_selectedAmount_t;
	TextField selectBalanceInquiry_menuOption_t;
	TextField insertPhysicalKey_keyID_t;
	TextField inputCredentials_operatorID_t;
	TextField loadBanknoteBundle_denomination_t;
	TextField loadBanknoteBundle_count_t;
	TextField insertFIPSToken_token_t;
	TextField selectLogRange_start_t;
	TextField selectLogRange_end_t;
	TextField selectTransferMenu_optionID_t;
	TextField chooseTargetAccount_accountNo_t;
	TextField chooseAndConfirmAmount_amount_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
