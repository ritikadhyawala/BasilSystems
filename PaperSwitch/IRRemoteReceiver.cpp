/*
 * IRRemoteReceiver.cpp
 *
 *  Created on: May 4, 2015
 *      Author: Nirmala
 */

#include "IRRemoteReceiver.h"

IRrecv irrecv(RECV_PIN);

TouchDetectData touchedData;
TimerLibrary TimerManager;
bool isReceivedDataToBeProcessed = false;
void IRRemoteReceiver::setIRDelegate(void (*irDataDelegate)(TouchDetectData)) {
	irDataCallback = irDataDelegate;
}

void IRRemoteReceiver::initIR() {

	codeType = -1;
	irrecv.enableIRIn(); // Start the receiver

	applianceIRCode[0] = 0xFF9867;
	applianceIRCode[1] = 0xFFD827;
	applianceIRCode[2] = 0xFF8877;
	applianceIRCode[3] = 0xFFA857;

	increaseLevelIRCode[0] = 0xFF906F;
	increaseLevelIRCode[1] = 0xFFB847;

	decreaseLevelIRCode[0] = 0xFFE817;
	decreaseLevelIRCode[1] = 0xFF48B7;

	autoModeTimeOutIRCode[0] = 0xFF7A85;
	autoModeTimeOutIRCode[1] = 0xFF609F;
	autoModeTimeOutIRCode[2] = 0xFFA05F;
	autoModeTimeOutIRCode[3] = 0xFFC03F;

	TimerManager.initTimer();
	TimerManager.detectTouchedDataDelegate(setIRreceivedData,
			isSameIRInputDetected);

}
void IRRemoteReceiver::monitorIR() {

	if (isReceivedDataToBeProcessed) {
		isReceivedDataToBeProcessed = false;
		processIRCode(&results);
		bool irRequestDetected = false;
		TouchDetectData inputIRData;
		//make irData from the result and send it to irDataCallback
		//check for on/off or auto of the appliance
		for (int i = 0; i < NO_OF_APPLIANCES; i++) {
			if (irrecv.receivedData == applianceIRCode[i]) {
				irRequestDetected = true;
				inputIRData.touchType = touchedData.touchType;
				inputIRData.touchedSwitch = i;
				if (touchedData.touchType == SINGLE_PRESS) {
					Serial.print("Single press : ");
				} else {
					Serial.print("Long press : ");
				}
				Serial.println(i);
				break;
			}
		}

		//check for the change in level of any appliance
		if (!irRequestDetected) {

			for (int i = 0; i < NO_OF_LEVEL_APPPLIANCES; i++) {
				if (irrecv.receivedData == increaseLevelIRCode[i]) {
					irRequestDetected = true;
					//increase of level detected
					inputIRData.touchType = LEVEL_TYPE;
					if (i == 0) {
						inputIRData.touchedSwitch = 6;
					} else {
						inputIRData.touchedSwitch = 4;
					}
					Serial.print("increase level : ");
					Serial.println(i);
					break;
				}
				if (irrecv.receivedData == decreaseLevelIRCode[i]) {
					irRequestDetected = true;
					//decrease of level detected
					inputIRData.touchType = LEVEL_TYPE;
					if (i == 0) {
						inputIRData.touchedSwitch = 7;
					} else {
						inputIRData.touchedSwitch = 5;
					}
					Serial.print("decrease level : ");
					Serial.println(i);
					break;
				}
			}
		}

		//check for the settings of different auto mode timeout
		if (!irRequestDetected) {
			for (int i = 0; i < NO_OF_AUTO_TIME_OUT; i++) {
				if (irrecv.receivedData == autoModeTimeOutIRCode[i]) {
					irRequestDetected = true;
					//send the request to set auto mode time out
					inputIRData.touchType = AUTO_TIME_OUT;
					autoModeTimeOut = i+1;
					Serial.print("auto time out : ");
					Serial.println(i);
					break;
				}
			}
		}

		if (irRequestDetected && results.value > 0) {
			irDataCallback(inputIRData);
			//make inputIRData to null
		}
	}
	if (irrecv.decode(&results)) {
		TimerManager.setTimer0();
		irrecv.resume(); // resume receiver
	}

}

void IRRemoteReceiver::processIRCode(decode_results *results) {
	codeType = results->decode_type;
	int count = results->rawlen;
	if (codeType == UNKNOWN) {
//	if(1){
		Serial.println("Received unknown code, saving as raw");
		codeLen = results->rawlen - 1;
		// To store raw codes:
		// Drop first value (gap)
		// Convert from ticks to microseconds
		// Tweak marks shorter, and spaces longer to cancel out IR receiver distortion
		for (int i = 1; i <= codeLen; i++) {
			if (i % 2) {
				// Mark
				rawCodes[i - 1] =
						results->rawbuf[i] * USECPERTICK - MARK_EXCESS;
				Serial.print(" m");
			} else {
				// Space
				rawCodes[i - 1] =
						results->rawbuf[i] * USECPERTICK + MARK_EXCESS;
				Serial.print(" s");
			}
			Serial.print(rawCodes[i - 1], DEC);
		}
		Serial.println("");
	} else {
		if (codeType == NEC) {
			Serial.print("Received NEC: ");
			if (results->value == REPEAT) {
				// Don't record a NEC repeat value as that's useless.
				Serial.println("repeat; ignoring.");
				return;
			}
		}
		Serial.println(results->value, HEX);
		codeValue = results->value;
		codeLen = results->bits;
	}
}

void setIRreceivedData(TouchDetectData data) {
	isReceivedDataToBeProcessed = true;
	touchedData.touchType = data.touchType;
}

int ir_signal_counter = 0;
bool isSameIRInputDetected() {
	if (irrecv.ir_signal_counter > ir_signal_counter) {
		ir_signal_counter = irrecv.ir_signal_counter;
		return true;
	} else {
		ir_signal_counter = 0;
	}
	return false;
}
