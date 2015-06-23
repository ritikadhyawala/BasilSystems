/*
 * MainController.h
 *
 *  Created on: Mar 22, 2015
 *      Author: Nirmala
 */

#include <Arduino.h>
#include "TouchManager.h"
#include "LedAndOuputManager.h"
#include "IRRemoteReceiver.h"
#include <SPI.h>

#ifndef MAINCONTROLLER_H_
#define MAINCONTROLLER_H_

#define MAX_LEVEL_VALUE 16

class MainController {
private:
	void checkForRadioData();
	void sendRadioData(int[]);

	int receivedData[2]; // 2 element array holding radio readings

public:
	void monitor();
	void initMainController();
};

extern "C" void processTouchOrIRData(TouchDetectData touchData);
extern "C" void processIRLevelData(int level, int switchNo);
extern "C" void changeApplianceStatus(int, TouchType);
extern "C" void onConfigureButtonPressed();
#endif /* MAINCONTROLLER_H_ */
