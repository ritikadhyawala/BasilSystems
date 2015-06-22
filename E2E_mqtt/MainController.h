/*
 * MainController.h
 *
 *  Created on: Mar 22, 2015
 *      Author: Nirmala
 */


#include <Energia.h>
#include "TouchManager.h"
#include "GlobalVariables.h"
#include "aJSON.h"
#ifndef MAINCONTROLLER_H_
#define MAINCONTROLLER_H_

#define MAX_LEVEL_VALUE 15
// typedef struct switchInfo{
//	  boolean IsOn;
//	  boolean IsAuto;
//	  int level;         //0-15
//	}switchInfo;



class MainController{
private:
	
public:
//	int sendCount;
	aJsonObject* root;
	void monitor();
	void initMainController();
//	void timerInit();
void switchONOFF(void);
};

extern MainController mainController;


extern "C" void processTouchOrWifiData(TouchDetectData touchData);
extern "C" void processWifiLevelData(int level, int switchNo);
//extern switchInfo switches[MAX_SWITCHES];
extern "C" void onWifiData(char *);
extern "C" void changeApplianceStatus(int, TouchType);
extern "C" void sendMessageToServer(int , char* , int);
extern "C" void mqtt_callback(char* topic, byte* payload, unsigned int length);
extern "C" void processUARTData(char* UARTdata);

#endif /* MAINCONTROLLER_H_ */
