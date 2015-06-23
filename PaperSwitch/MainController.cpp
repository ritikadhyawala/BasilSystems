/*
 * MainController.cpp
 *
 *  Created on: Mar 22, 2015
 *      Author: Nirmala
 */
#include"MainController.h"

TouchLibrary TouchManager;
LedAndOutputManager ledManager;
IRRemoteReceiver irReceiver;
bool isAutoModeTimedOut;
bool isSendingModeOn;
uint32_t radioAddress;
uint64_t pipe;
RF24 radio(CE_PIN, CSN_PIN); // Create a Radio
void MainController::monitor() {
//	TouchManager.monitorTouch();
	irReceiver.monitorIR();

	if (isAutoModeTimedOut) {
		//switch off the appliances which were in auto mode
		isAutoModeTimedOut = false;
	}

	if (isSendingModeOn) {
		radio.write(&radioAddress, sizeof(radioAddress));
	} else {
		checkForRadioData();
	}
}
void MainController::initMainController() {

//	TouchManager.initTouch();
//	TouchManager.setTouchDelegate(processTouchOrIRData);
//	irReceiver.initIR();
//	irReceiver.setIRDelegate(processTouchOrIRData);
	autoModeTimeOut = 60;

	pinMode(2, INPUT);
	//init the interrupt pin on which the motion sensor data will arrive
	attachInterrupt(0, onConfigureButtonPressed, FALLING);

	isSendingModeOn = false;

	Serial.println("Initialized..");

	pipe = 0xE8E8F0F0E1LL;
	radio.begin();
	TimerManager.initTimer();

	//check in eeprom whether random no is already there or not
	EEPROM.get(0, radioAddress);
	if (radioAddress == 0xffff) {
		// if not generate a random number and store it in eeprom
		radioAddress = random(0, 0xffff);
		EEPROM.put(0, radioAddress);
		Serial.print("The random radio add is : ");
		Serial.println(radioAddress);
	} else {
		// if yes, then read it from the eeprom.
		EEPROM.get(0, radioAddress);

	}
	uint64_t add = pipe + radioAddress;
	radio.openReadingPipe(1, add);
	radio.startListening();
	Serial.print("Listening to: "); Serial.println((long)add);

}

void processTouchOrIRData(TouchDetectData data) {
	Serial.print("In process IRData");
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
		if (data.touchedSwitch == 4 || data.touchedSwitch == 5) { // if they are increasing level
			if (switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
					< MAX_LEVEL_VALUE) {
				switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
						switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
								+ 1;
				//				Serial.print("Level: "); Serial.println(switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level);
			}
		} else if (data.touchedSwitch == 6 || data.touchedSwitch == 7) {
			if (switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
					> 0) {   // if they are decreasing level
				switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level =
						switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level
								- 1;
				//				Serial.print("Level: "); Serial.println(switches[shiftRegOutputInfoArr[data.touchedSwitch].shiftRegNoforTouchedKey].level);
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
		ledManager.controlOnOffDisplay(touchedSwitch);
	} else if (type == LONG_PRESS) {
		ledManager.controlRingDisplay(touchedSwitch);
	} else {
		ledManager.controlLevelDisplay(touchedSwitch);
	}
}

void onConfigureButtonPressed() {

//	Serial.println("Got sensor data");
//	TimerManager.setTimer1();

	Serial.println("Config button pressed");
	//put the paperswitch into sending mode
	radio.openWritingPipe(pipe);

	Serial.println("Sending the configuration address");
//	Serial.print("The radioAddress added is :"); Serial.print(radioAddress[0], HEX);Serial.println(radioAddress[1], HEX);

	isSendingModeOn = true;
	TimerManager.setTimer1(CONFIGURE_TIME_OUT);
	//keep sending the random number generated for 30 sec.

}

void MainController::checkForRadioData() {

	if (radio.available()) {
		Serial.println("aaya");
		// Read the data payload until we've received everything
		bool done = false;
		while (!done) {
			// Fetch the data payload
			done = radio.read(receivedData, sizeof(receivedData));
			Serial.print(receivedData[0]);
			Serial.print(" ");
			Serial.println(receivedData[1]);
//	      nrfDataFunction(MOTION, receivedData);
		}
	}
}

void MainController::sendRadioData(int data[]) {
	radio.write(data, sizeof(data));
}

