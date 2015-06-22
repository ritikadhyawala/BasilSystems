/*
 * MainController.cpp
 *
 *  Created on: Mar 22, 2015
 *      Author: Nirmala
 */
#include"MainController.h"
#include "GlobalVariables.h"
#include "TouchManager.h"
#include "LedAndOuputManager.h"
#include "WiFi.h"
#include "PubSubClient.h"
#include "LocalCommManager.h"

//switchInfo switches[NO_OF_APPLIANCES];
TouchLibrary TouchManager;
LedAndOutputManager ledManager;
LocalCommManager localCommManager;
WiFiClient wifiClient;
//PubSubClient client("192.168.1.247", 1883, mqtt_callback, wifiClient);
PubSubClient client("ec2-52-11-122-248.us-west-2.compute.amazonaws.com", 1883,mqtt_callback, wifiClient);
//ShiftRegOutputInfo shiftRegOutputInfoArr[MAX_SWITCHES];
int IsAwayMode;
int counttoggles;
void MainController::monitor() {
	TouchManager.monitorTouch();
		client.monitor();
	localCommManager.monitorLocalComm();
//	Serial.println("c");
//	TouchManager.monitorref(); // added by nirmala to monitor touch ref data
//	delay(100);
//	switchONOFF(); // added by nirmala to check ckt on/off number
//delay(3000);
}

void MainController:: switchONOFF()
{   counttoggles ++;
  switches[0].level = 9;
	switches[0].IsOn = !switches[0].IsOn;
	changeApplianceStatus(0, SINGLE_PRESS);
	Serial.println(counttoggles);
}
void MainController::initMainController() {
	IsAwayMode = 0;
	Serial.begin(115200);
	Serial1.begin(115200);
	TouchManager.initTouch();
	TouchManager.setTouchDelegate(processTouchOrWifiData);
	localCommManager.setUARTDelegate(processUARTData);
		client.init("BasilSystems", "ritnirnish");
//		client.init("wifi", "va3rdwif");
//		client.init("goodlands4", "9611166683");
//		client.init("Techhub1", "9876543210");
//	    client.init("AndroidAP","flbq6183");
//		client.init("Nishant", "nish123NISH");
		client.connect("energiaClient");
		if (client.subscribe("mac1")) {
			Serial.println("Sub success");
		} else {
			Serial.println("Sub fail");
		}
	//	timerInit();
}

void processTouchOrWifiData(TouchDetectData data) {
	if (data.touchedSwitch < 4) { //if they are not level indicators
		if (data.touchType == (TouchType) SINGLE_PRESS) {
			switches[data.touchedSwitch].IsOn =
					!switches[data.touchedSwitch].IsOn;
			//			if(switches[data.touchedSwitch].IsAuto){
			//				switches[data.touchedSwitch].IsAuto = false;
			//			}
		} else {
			switches[data.touchedSwitch].IsAuto =
					!switches[data.touchedSwitch].IsAuto; // need toggle auto here
		}
	} else {   // if they are level indicators
		//
		if (data.touchedSwitch == 4 || data.touchedSwitch == 5) { // if they are increasing level
			if (switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
					< MAX_LEVEL_VALUE) {
				if(data.touchType == (TouchType) LEVEL_TYPE){
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
							switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
							+ 1;
					//				Serial.print("Level: "); Serial.println(switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level);
				}
				else{
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
							switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
							+ 3;
					data.touchType = LEVEL_TYPE;
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level=(switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level<15)?switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level:15;
				}
			}

		} else if (data.touchedSwitch == 6 || data.touchedSwitch == 7) {
			if (switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
					> 0) {   // if they are decreasing level
				if(data.touchType == (TouchType) LEVEL_TYPE){
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
							switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
							- 1;
					//				Serial.print("Level: "); Serial.println(switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level);
				}
				else{
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
							switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
							- 3;
					data.touchType = LEVEL_TYPE;
					switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level= (switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level>0)?switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level:0;
				}
			}

		} else {
			// do handling for proximity
		}

	}
	changeApplianceStatus(data.touchedSwitch, data.touchType);
}

void changeApplianceStatus(int touchedSwitch, TouchType type) {
	//update the information of the switch array data
	// send the output to the appliances
	//control the led lights
	if (type == SINGLE_PRESS) {
		//		Serial.print("b4 ");Serial.println(millis());
		ledManager.controlAcOnOff(touchedSwitch);
		ledManager.controlOnOffDisplay(touchedSwitch);
		//		Serial.print("after ");Serial.println(millis());
		sendMessageToServer(touchedSwitch, "o",
				(int) switches[touchedSwitch].IsOn);
	} else if (type == LONG_PRESS) {
		ledManager.controlRingDisplay(touchedSwitch);
		sendMessageToServer(touchedSwitch, "a",
				(int) switches[touchedSwitch].IsAuto);
	} else {
		ledManager.controlAcLevelChange(touchedSwitch);
		ledManager.controlLevelDisplay(touchedSwitch);
		int mappedTouchedSWtich =
				(touchedSwitch == 4 || touchedSwitch == 6) ? 0 : 1;
		sendMessageToServer(mappedTouchedSWtich, "l",
				switches[mappedTouchedSWtich].level);
	}

}

void sendMessageToServer(int switchNo, char *operation, int status) {
	//	mainController.sendCount++;

	mainController.root = aJson.createObject();
	aJson.addNumberToObject(mainController.root, operation, status);
	aJson.addNumberToObject(mainController.root, "s", switchNo);
	char * message = aJson.print(mainController.root);
	//	char * message = "golu";
	//	Serial.print("send count"); Serial.println(mainController.sendCount);
	Serial.print("msg: ");
	Serial.println(message);
	client.publish("changeStatus", message);
	aJson.deleteItem(mainController.root);
	free(message);
}

void mqtt_callback(char* topic, byte* wifiData, unsigned int length) {
	//	Serial.print("Received message for topic ");
	//	Serial.print(topic);
	//	Serial.print("with length ");
	//	Serial.println(length);
	wifiData[length] = '\0';
	Serial.print("Rcvd: ");
	Serial.write((char*) wifiData);
	Serial.println();
	mainController.root = aJson.parse((char*) wifiData);
	if (mainController.root != NULL) {
		TouchDetectData touchedData;
		aJsonObject* query = aJson.getObjectItem(mainController.root, "s");
		if (query != NULL) {
			int switchNo = query->valueint;
			touchedData.touchedSwitch = switchNo - 49;
			query = aJson.getObjectItem(mainController.root, "tt");
			char * touchType = query->valuestring;
			if (strcmp(touchType, "sp") == 0) {
				touchedData.touchType = SINGLE_PRESS;
				processTouchOrWifiData(touchedData);
			} else if (strcmp(touchType, "lp") == 0) {
				touchedData.touchType = LONG_PRESS;
				Serial.println("long press from wifi");
				processTouchOrWifiData(touchedData);
			} else { //changed switchNo to touchedData.touchedSwitch
				query = aJson.getObjectItem(mainController.root, "l");
				int level = query->valueint;
//				Serial.print(" level  ");
//				Serial.println(switches[touchedData.touchedSwitch].level);
//				Serial.print("parsed level");
//				Serial.println(level);
				if (switches[touchedData.touchedSwitch].level > level) {
					Serial.print("in if");
					if (touchedData.touchedSwitch == 0) {
						touchedData.touchedSwitch = 6;
					} else {
						touchedData.touchedSwitch = 7;
					}
				} else {
					Serial.print("in else");
					if (touchedData.touchedSwitch == 0) {
						touchedData.touchedSwitch = 4;
					} else {
						touchedData.touchedSwitch = 5;
					}
				}
				switches[switchNo - 49].level = level;

				ledManager.syncAcLevel(switchNo - 49);
				//				ledManager.controlAcLevelChange(touchedData.touchedSwitch);
				ledManager.controlLevelDisplay(touchedData.touchedSwitch);
			}

		} else {
			query = aJson.getObjectItem(mainController.root, "T");
			if (query != NULL) {
				char* themeValue = query->valuestring;
				//				Serial.print("theme value");Serial.println(themeValue);
				if (strcmp(themeValue, "A")==0) {
					Serial.println("Away mode set");
					IsAwayMode = 1;
				} else if (strcmp(themeValue, "N")==0) {
					Serial.println("night mode set");
					IsAwayMode = 0;
					for(int i = 0; i<4;i++){
						switches[i].IsOn = false;
						changeApplianceStatus(i, SINGLE_PRESS);
					}
				}else if (strcmp(themeValue, "G")==0) {
					Serial.println("general mode set");
					IsAwayMode = 0;
				}

			}
		}
		//		Serial.print("parsed no"); Serial.println(touchedData.touchedSwitch);
		//		Serial.print("parsed type"); Serial.println(touchedData.touchType);
		//		query = aJson.getObjectItem( mainController.root, "t");
		//		if (query != NULL) {
		//
		//			query = aJson.getObjectItem( mainController.root, "a");
		//			IsAwayMode  = query->valueint;
		//
		//		}
	}

}

//void MainController:: timerInit(){
//	Serial.print("tmr init");
//
////	MAP_PRCMPeripheralClkEnable(PRCM_TIMERA0, PRCM_RUN_MODE_CLK);
////		MAP_PRCMPeripheralReset (PRCM_TIMERA0);
////		MAP_TimerConfigure(TIMERA0_BASE, TIMER_CFG_ONE_SHOT);
////		MAP_TimerPrescaleSet(TIMERA0_BASE, TIMER_B, 0);
//		TimerIntRegister(TIMERA0_BASE, TIMER_B, motionTimerIntHandler);
////		MAP_TimerIntEnable(TIMERA0_BASE, TIMER_TIMB_TIMEOUT);
//
//}

void processUARTData(char* UARTdata) {
//	Serial.println("mtn");
	Serial.println(UARTdata[0]);
	if (UARTdata[0] == 'M') {
		Serial.println(" m ");
		if(IsAwayMode){
			client.publish("i", "1");
			for(int i=0;i<3;i++){
				switches[i].IsOn = true;
				changeApplianceStatus(i, SINGLE_PRESS);}
			IsAwayMode=0;
		}
		for (int i = 0; i < 4; i++) {
			if (switches[i].IsAuto) {
				switches[i].IsOn = true;
				//			switches[i].IsOn= !switches[i].IsOn;
				changeApplianceStatus(i, SINGLE_PRESS);
				//				Serial.print("tmr set");
				//				MAP_TimerLoadSet(TIMERA0_BASE, TIMER_B, 80000000 * 1);
				//				// Enable the timer.
				//				MAP_TimerEnable(TIMERA0_BASE, TIMER_B);
			}
		}
	}

}

