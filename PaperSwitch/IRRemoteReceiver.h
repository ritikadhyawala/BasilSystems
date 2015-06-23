/*
 * IRRemoteReceiver.h
 *
 *  Created on: Apr 18, 2015
 *      Author: Nirmala
 */

#ifndef IRREMOTERECEIVER_H_
#define IRREMOTERECEIVER_H_

#include "GlobalVariables.h"
#include <IRremote.h>
#include "aJSON.h"
#include "TimerManager.h"

#define RECV_PIN  11
#define DEBUG
class IRRemoteReceiver {

public:

	void setIRDelegate(void (*irDataDelegate)(TouchDetectData));
	void initIR();
	void monitorIR();
private:
	decode_results results;
	unsigned long irData;

	// Storage for the recorded code
	int codeType; // The type of code
	unsigned long codeValue; // The code value if not raw
	unsigned int rawCodes[RAWBUF]; // The durations if raw
	int codeLen; // The length of the code

	void (*irDataCallback)(TouchDetectData);
	void processIRCode(decode_results *results);

	unsigned long applianceIRCode[NO_OF_APPLIANCES];
	unsigned long increaseLevelIRCode[NO_OF_LEVEL_APPPLIANCES];
	unsigned long decreaseLevelIRCode[NO_OF_LEVEL_APPPLIANCES];
	unsigned long autoModeTimeOutIRCode[NO_OF_AUTO_TIME_OUT];

};

extern "C" void setIRreceivedData(TouchDetectData);
extern "C" bool isSameIRInputDetected();

#endif /* IRREMOTERECEIVER_H_ */

