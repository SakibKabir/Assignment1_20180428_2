package main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class LoadCIM {
	        
	public static void main(String[] args) throws Exception {
		
		try {
			
			// ---Load EQ file----//
			// ---Load SSH file---//
			File EQ = new File("Assignment_EQ_reduced.xml");
			File SSH = new File("Assignment_SSH_reduced.xml");
			DocumentBuilderFactory dbFactoryEQ = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory dbFactorySSH = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilderEQ = dbFactoryEQ.newDocumentBuilder();
			DocumentBuilder dBuilderSSH = dbFactorySSH.newDocumentBuilder();
			Document docEQ = dBuilderEQ.parse(EQ);
			Document docSSH = dBuilderSSH.parse(SSH);
			docEQ.getDocumentElement().normalize();
			docSSH.getDocumentElement().normalize();
			System.out.println("Load Assignment_EQ_reduced.xml successfully");
			System.out.println("Load Assignment_SSH_reduced.xml successfully");

			// ------Create Node List for each objectives------//
			// -----------------From EQ file-------------------//
			NodeList baseVoltList = docEQ.getElementsByTagName("cim:BaseVoltage");
			NodeList subsList = docEQ.getElementsByTagName("cim:Substation");
			NodeList voltageLevelList = docEQ.getElementsByTagName("cim:VoltageLevel");
			NodeList generatingUnit = docEQ.getElementsByTagName("cim:GeneratingUnit");
			NodeList syncMachList = docEQ.getElementsByTagName("cim:SynchronousMachine");
			NodeList regulCtrlListEQ = docEQ.getElementsByTagName("cim:RegulatingControl");
			NodeList powerTranList = docEQ.getElementsByTagName("cim:PowerTransformer");
			NodeList energyConsumerList = docEQ.getElementsByTagName("cim:EnergyConsumer");
			NodeList powerTranEndList = docEQ.getElementsByTagName("cim:PowerTransformerEnd");
			NodeList breakerList = docEQ.getElementsByTagName("cim:Breaker");
			NodeList ratioTapChangerList = docEQ.getElementsByTagName("cim:RatioTapChanger");

			// ----------------From SSH file-------------------//
			NodeList baseVoltListSSH = docSSH.getElementsByTagName("cim:BaseVoltage");
			NodeList subsListSSH = docSSH.getElementsByTagName("cim:Substation");
			NodeList voltageLevelListSSH = docSSH.getElementsByTagName("cim:VoltageLevel");
			NodeList generatingUnitSSH = docSSH.getElementsByTagName("cim:GeneratingUnit");
			NodeList syncMachListSSH = docSSH.getElementsByTagName("cim:SynchronousMachine");
			NodeList regulCtrlListSSH = docSSH.getElementsByTagName("cim:RegulatingControl");
			NodeList powerTranListSSH = docSSH.getElementsByTagName("cim:PowerTransformer");
			NodeList energyConsumerListSSH = docSSH.getElementsByTagName("cim:EnergyConsumer");
			NodeList powerTranEndListSSH = docSSH.getElementsByTagName("cim:PowerTransformerEnd");
			NodeList breakerListSSH = docSSH.getElementsByTagName("cim:Breaker");
			NodeList ratioTapChangerListSSH = docSSH.getElementsByTagName("cim:RatioTapChanger");
			
		
			SQLdatabase database = new SQLdatabase();
			database.StartUp();
		    database.createTables();
		  
			// ------------------------------//
			// ---Load base voltage info ----//
			// -- rdfID, nominal Voltage ----//
			// ---create table: BaseVoltage--//
			// ------------------------------//
			baseVoltage baseVolt = new baseVoltage();
			for (int i = 0; i < baseVoltList.getLength(); i++) {
				String baseVolt_rdfID = baseVolt.rdfID(baseVoltList.item(i));
				double baseVolt_nominalVoltage = (double) baseVolt.nominalVoltage(baseVoltList.item(i));
				SQLdatabase.BaseVoltageTable(baseVolt_rdfID, baseVolt_nominalVoltage);
			    System.out.println("BaseVoltage: rdfID: " + baseVolt_rdfID);
				System.out.println("             nominal Voltage: " + baseVolt_nominalVoltage + "kV");
			}

			// ------------------------------//
			// -----Load substation info-----//
			// --rdfID, name, region_rdfID---//
			// ---create table: Substation---//
			// ------------------------------//
			substation subs = new substation();
			for (int i = 0; i < subsList.getLength(); i++) {
				String subs_rdfID = subs.rdfID(subsList.item(i));
				String subs_name = subs.name(subsList.item(i));
				String subs_region_rdfID = subs.region_rdfID(subsList.item(i));
				SQLdatabase.SubstationTable(subs_rdfID, subs_name,subs_region_rdfID);
				System.out.println("Substation: rdfID: " + subs_rdfID);
				System.out.println("            name: " + subs_name);
				System.out.println("            region_rdfID: " + subs_region_rdfID);
			}

			// ------------------------------//
			// ---Load Voltage Level info----//
			// ---rdfID, name, maxP, minP----//
			// ---equipmentContainer_rdf:ID--//
			// --create table: VoltageLevel--//
			// ------------------------------//
			VoltageLevel voltageLevel = new VoltageLevel();
			for (int i = 0; i < voltageLevelList.getLength(); i++) {
				String voltageLevel_rdfID = voltageLevel.rdfID(voltageLevelList.item(i));
				String voltageLevel_name = voltageLevel.name(voltageLevelList.item(i));
				String voltageLevel_subs_rdfID = voltageLevel.substation_rdfID(voltageLevelList.item(i));
				String voltageLevel_baseVolt_rdfID = voltageLevel.baseVoltage_rdfID(voltageLevelList.item(i));
				SQLdatabase database1= new SQLdatabase();
				database1.VoltageLevelTable(voltageLevel_rdfID, voltageLevel_name, voltageLevel_subs_rdfID, voltageLevel_baseVolt_rdfID);
				System.out.println("Voltage Level: rdfID: " + voltageLevel_rdfID);
				System.out.println("               name: " + voltageLevel_name);
				System.out.println("               substation rdfID: " + voltageLevel_subs_rdfID);
				System.out.println("               base voltage rdfID: " + voltageLevel_baseVolt_rdfID);
			}

			// ------------------------------//
			// --Load Generating Unit info---//
			// --rdfID, substation_rdf:ID----//
			// ----name,baseVoltage_rdf:ID---//
			// ----create table: GeneUnit----//
			// ------------------------------//
			GeneratingUnit genunit = new GeneratingUnit();
			for (int i = 0; i < generatingUnit.getLength(); i++) {
				String GeneUnit_rdfID = genunit.rdfID(generatingUnit.item(i));
				String GeneUnit_name = genunit.name(generatingUnit.item(i));
				double GeneUnit_maxP = genunit.maxP(generatingUnit.item(i));
				double GeneUnit_minP = genunit.minP(generatingUnit.item(i));
				String GeneUnit_equipCont_rdfID = genunit.equipmentContainer_rdfID(generatingUnit.item(i));
				SQLdatabase.GeneratingUnitTable(GeneUnit_rdfID, GeneUnit_name, GeneUnit_maxP, GeneUnit_minP, GeneUnit_equipCont_rdfID );
				System.out.println("Generating Unit: rdfID: " + GeneUnit_rdfID);
				System.out.println("                 name: " + GeneUnit_name);
				System.out.println("                 maxP: " + GeneUnit_maxP);
				System.out.println("                 minP: " + GeneUnit_minP);
				System.out.println("                 equipmentContainer_rdfID: " + GeneUnit_equipCont_rdfID);
			}
			// ------------------------------//
						
			// ------------------------------//
			// Load synchronous machine info-//
			// ----rdfID, name, ratedS, P----//
			// Q,genUnit_rdf:ID,regControl_rdf:ID//
			// --equipmentContainer_rdf:ID---//
			// ----baseVoltage_ rdf:ID-------//
			// ----create table: SyncMach----//
			// ------------------------------//
			SynchronousMachine syncMach = new SynchronousMachine();
			for (int i = 0; i < syncMachList.getLength(); i++) {
				String syncMach_rdfID = syncMach.rdfID(syncMachList.item(i));
				String syncMach_name = syncMach.name(syncMachList.item(i));
				double syncMach_ratedS = syncMach.ratedS(syncMachList.item(i));
				double syncMach_P = syncMach.P(syncMachList.item(i));
				double syncMach_Q = syncMach.Q(syncMachList.item(i));
				String syncMach_genUnit_rdfID = syncMach.genUnit_rdfID(syncMachList.item(i));
				String syncMach_regControl_rdfID = syncMach.regControl_rdfID(syncMachList.item(i));
				String syncMach_equipmentContainer_rdfID = syncMach.equipmentContainer_rdfID(syncMachList.item(i));
				double syncMach_ratedU = syncMach.baseVoltage(syncMachList.item(i));
				String syncMach_baseVoltage_rdfID = "Null";
				for(int j=0; j<baseVoltList.getLength(); j++) {
					double baseVolt_nominalVoltage = (double) baseVolt.nominalVoltage(baseVoltList.item(j));
					String baseVolt_rdfID = baseVolt.rdfID(baseVoltList.item(j));
					if (syncMach_ratedU == baseVolt_nominalVoltage) {
						syncMach_baseVoltage_rdfID = baseVolt_rdfID;
					}
				}
				
	  SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name, syncMach_ratedS, syncMach_P, syncMach_Q,
   syncMach_genUnit_rdfID, syncMach_regControl_rdfID, syncMach_equipmentContainer_rdfID, syncMach_baseVoltage_rdfID);
		//SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name, syncMach_ratedS, syncMach_P, syncMach_Q, syncMach_genUnit_rdfID, syncMach_regControl_rdfID, syncMach_equipmentContainer_rdfID, syncMach_baseVoltage_rdfID);
				
				System.out.println("Synchronous Machine: rdfID: " + syncMach_rdfID);
				System.out.println("                     name: " + syncMach_name);
				System.out.println("                     ratedS: " + syncMach_ratedS);
				System.out.println("                     P: " + syncMach_P);
				System.out.println("                     Q: " + syncMach_Q);
				System.out.println("                     genUnit rdfId: " + syncMach_genUnit_rdfID);
				System.out.println("                     regControl_rdfID: " + syncMach_regControl_rdfID);
				System.out.println("                     equipmentContainer_rdf:ID: " + syncMach_equipmentContainer_rdfID);
				System.out.println("                     rated U: " + syncMach_ratedU);
				System.out.println("                     baseVoltage_ rdf:ID: " + syncMach_baseVoltage_rdfID);
				// SQLdatabase.SynchronousMachineTable(syncMach_rdfID, syncMach_name, syncMach_ratedS, syncMach_P, syncMach_Q, syncMach_genUnit_rdfID, syncMach_regControl_rdfID, syncMach_equipmentContainer_rdfID, syncMach_baseVoltage_rdfID);
			}
			// -Load Regulating Control info-//
			// ---rdfID, name, targetValue---//
			// ----create table: ReguCtrl----//
			// ------------------------------//
			RegulatingControl reglCtrl = new RegulatingControl();
			String reglCtrl_rdfID_temp, reglCtrl_rdfID_tempSSH, reglCtrl_name;
			double reglCtrl_targetVoltage;
			for (int i = 0; i < regulCtrlListEQ.getLength(); i++) {
				reglCtrl_rdfID_temp = reglCtrl.rdfIDEQ(regulCtrlListEQ.item(i));
				for (int j = 0; j < regulCtrlListSSH.getLength(); j++) {
					reglCtrl_rdfID_tempSSH = reglCtrl.rdfIDSSH(regulCtrlListSSH.item(j));
					if (reglCtrl_rdfID_temp.equals(reglCtrl_rdfID_tempSSH)) {
						reglCtrl_name = reglCtrl.name(regulCtrlListEQ.item(i));
						reglCtrl_targetVoltage = reglCtrl.targetValue(regulCtrlListSSH.item(j));
		     		    SQLdatabase.RegulatingControlTable(reglCtrl_rdfID_temp, reglCtrl_name, reglCtrl_targetVoltage);
						// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);
						System.out.println("RegulatingControl : rdfID: " + reglCtrl_rdfID_temp);
						System.out.println("                    name: " + reglCtrl_name);
						System.out.println("                    target voltage: " + reglCtrl_targetVoltage);
					}
				}

			}
			// -------------------------------------//
			// -----Load Power Transformer info-----//
			// -rdfID, name,quipmentContainer_rdf:ID//
			// ------create table: PowerTran--------//
			// -------------------------------------//
			PowerTrans powerTran = new PowerTrans();
			for (int i = 0; i < powerTranList.getLength(); i++) {
				String powerTran_rdfID = powerTran.rdfID(powerTranList.item(i));
				String powerTran_name = powerTran.name(powerTranList.item(i));
				String powerTran_equipCont_rdfID = powerTran.equipConta_rdfID(powerTranList.item(i));
                SQLdatabase.PowerTransformerTable(powerTran_rdfID, powerTran_name, powerTran_equipCont_rdfID);
				System.out.println("power Transformer: rdfID: " + powerTran_rdfID);
				System.out.println("                   name: " + powerTran_name);
				System.out.println("                   region_rdfID: " + powerTran_equipCont_rdfID);
			}

			// -------------------------------------//
			// ----Load Energy Consumer info--------//
			// ------------rdfID, name, P, Q--------//
			// equipmentContainer_rdf:ID, baseVoltage_ rdf:ID//
			// ------create table: RatioTapChan-----//
			// -------------------------------------//
			EnergyConsumer energyConsu = new EnergyConsumer();
			String energyConsu_rdfID_temp, energyConsu_rdfID_tempSSH, energyConsu_name,
					energyConsu_equipmentContainer_rdfID, baseVoltage_rdfID;
			double energyConsu_P, energyConsu_Q;
			for (int i = 0; i < energyConsumerList.getLength(); i++) {
				energyConsu_rdfID_temp = reglCtrl.rdfIDEQ(energyConsumerList.item(i));
				for (int j = 0; j < energyConsumerListSSH.getLength(); j++) {
					energyConsu_rdfID_tempSSH = energyConsu.rdfIDSSH(energyConsumerListSSH.item(j));
					if (energyConsu_rdfID_temp.equals(energyConsu_rdfID_tempSSH)) {
						energyConsu_name = energyConsu.name(energyConsumerList.item(i));
						energyConsu_P = energyConsu.P(energyConsumerListSSH.item(j));
						energyConsu_Q = energyConsu.Q(energyConsumerListSSH.item(j));
						energyConsu_equipmentContainer_rdfID = energyConsu.equipmentContainer_rdfID(energyConsumerList.item(i));
						baseVoltage_rdfID = energyConsu.baseVoltage_rdfID(energyConsumerListSSH.item(i));
                        SQLdatabase.EnergyConsumerTable(energyConsu_rdfID_temp, energyConsu_name, energyConsu_P, energyConsu_Q, energyConsu_equipmentContainer_rdfID, baseVoltage_rdfID);
						// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);
						System.out.println("Energy Consumer : rdfID: " + energyConsu_rdfID_temp);
						System.out.println("          name: " + energyConsu_name);
						System.out.println("          P: " + energyConsu_P);
						System.out.println("          Q: " + energyConsu_Q);
						System.out.println("          equipmentContainer rdfID " + energyConsu_equipmentContainer_rdfID);
						System.out.println("          base voltage rdfID " + baseVoltage_rdfID);
					}
				}

			}

			// -------------------------------------//
			// ---Load Power Transformer End info---//
			// ------------rdfID, name, P, Q--------//
			// ------------Transformer_rdf:ID-------//
			// ------------baseVoltage_ rdf:ID------//
			// ------Insert values to Power Transformer End Table-----//
			// -------------------------------------//
			PowerTransformerEnd powerTranEnd = new PowerTransformerEnd();
			for (int i = 0; i < ratioTapChangerList.getLength(); i++) {
				String powerTranEnd_rdfID = powerTranEnd.rdfID(powerTranEndList.item(i));
				String powerTranEnd_name = powerTranEnd.name(powerTranEndList.item(i));
				double powerTranEnd_r = powerTranEnd.r(powerTranEndList.item(i));
				double powerTranEnd_x = powerTranEnd.x(powerTranEndList.item(i));
				String powerTranEnd_Transformer_rdfID = powerTranEnd.Transformer_rdfID(powerTranEndList.item(i));
				String powerTranEnd_baseVoltage_rdfID = powerTranEnd.baseVoltage_rdfID(powerTranEndList.item(i));
                SQLdatabase.PowerTransformerEndTable(powerTranEnd_rdfID, powerTranEnd_name, powerTranEnd_r, powerTranEnd_x, powerTranEnd_Transformer_rdfID, powerTranEnd_baseVoltage_rdfID);
				System.out.println("power transformer end: rdfID: " + powerTranEnd_rdfID);
				System.out.println("                       name: " + powerTranEnd_name);
				System.out.println("                       r: " + powerTranEnd_r);
				System.out.println("                       x: " + powerTranEnd_x);
				System.out.println("                       Transformer rdfID: " + powerTranEnd_Transformer_rdfID);
				System.out.println("                       base voltage rdfID: " + powerTranEnd_baseVoltage_rdfID);
			}

			// -------------------------------------//
			// ------------Load Breaker info--------//
			// ------------rdfID, name, state-------//
			// equipmentContainer_rdf:ID, baseVoltage_ rdf:ID//
			// ------create table: RatioTapChan-----//
			// -------------------------------------//
			Breaker Breaker = new Breaker();
			String breaker_rdfID_temp, breaker_rdfID_tempSSH, breaker_name, breaker_equipmentContainer_rdfID;
			boolean breaker_state;
			for (int i = 0; i < breakerList.getLength(); i++) {
				breaker_rdfID_temp = reglCtrl.rdfIDEQ(breakerList.item(i));
				for (int j = 0; j < breakerListSSH.getLength(); j++) {
					breaker_rdfID_tempSSH = Breaker.rdfIDSSH(breakerListSSH.item(j));
					if (breaker_rdfID_temp.equals(breaker_rdfID_tempSSH)) {
						breaker_name = Breaker.name(breakerList.item(i));
						breaker_state = Breaker.state(breakerListSSH.item(j));
						breaker_equipmentContainer_rdfID = Breaker.equipmentContainer_rdfID(breakerList.item(i));
						// TO DO: breaker base voltage
				      //  SQLdatabase.BreakerTable(breaker_rdfID_temp, breaker_name, breaker_state, breaker_equipmentContainer_rdfID);
						// System.out.println("RegulatingControl : rdfID: " + rdfID_tempSSH);
						System.out.println("Breaker : rdfID: " + breaker_rdfID_temp);
						System.out.println("          name: " + breaker_name);
						System.out.println("          state: " + breaker_state);
						System.out.println("          equipmentContainer rdfID " + breaker_equipmentContainer_rdfID);
						System.out.println("          base voltage level ");
					}
				}

			}

			// -------------------------------------//
			// -----Load Ratio Tap Changer info-----//
			// ------------rdfID, name, step--------//
			// ------create table: RatioTapChan-----//
			// -------------------------------------//
			RatioTapChanger ratioTapChanger = new RatioTapChanger();
			for (int i = 0; i < ratioTapChangerList.getLength(); i++) {
				String ratioTapChang_rdfID = ratioTapChanger.rdfID(ratioTapChangerList.item(i));
				String ratioTapChang_name = ratioTapChanger.name(ratioTapChangerList.item(i));
				double ratioTapChang_step = ratioTapChanger.step(ratioTapChangerList.item(i));
				SQLdatabase.RatioTapChangerTable(ratioTapChang_rdfID, ratioTapChang_name, ratioTapChang_step);
				System.out.println("ratio Tap Changer: rdfID: " + ratioTapChang_rdfID);
				System.out.println("                   name: " + ratioTapChang_name);
				System.out.println("                   region_rdfID: " + ratioTapChang_step);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
