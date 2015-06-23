/*
 * LedManager.h
 *
 *  Created on: Mar 24, 2015
 *      Author: Ritika
 */
#include <Arduino.h>
#include "GlobalVariables.h"
#ifndef LEDMANAGER_H_
#define LEDMANAGER_H_

#define DISPLAY_LEVELS 5  // NO OF LEVELS USED IN DISPLAY
#define DISPLAY_DIVIDER 3
#define SHIFTREG_NOS  5 // NO OF SHIFT REGISTERS USED

#define SHIFTREG_DATAPIN 3

#define SHIFTREG_CLOCK_1 5
#define SHIFTREG_CLOCK_2 6
#define SHIFTREG_CLOCK_3 8
#define SHIFTREG_CLOCK_4 4
#define SHIFTREG_CLOCK_5 0

#define SHIFTREG_SELECT_1 5
#define SHIFTREG_SELECT_2 7
#define SHIFTREG_SELECT_3 27
#define SHIFTREG_SELECT_4 30
#define SHIFTREG_SELECT_5 11

enum LedColour{
	RED, BLUE, GREEN
} ;

enum LedOperation{
	ON, OFF, BLINK
} ;

struct shiftRegInfoForAcOutput {
	int shiftRegNo;
	int onOffShiftRegPin;
};

struct shiftRegInfoForAcLevelOutput{
	int shiftRegNo;
	int levelPinsArray[4];
};

class LedAndOutputManager {
public:

	LedAndOutputManager();
	void controlOnOffDisplay(int);
	void controlRingDisplay(int);
	void controlLevelDisplay(int);
	void displayBoardConnStatus(LedColour, LedOperation);

	void controlAcOnOff(int);
	void controlAcLevelChange(int);

private:
	int shiftRegControlPins[SHIFTREG_NOS];
	int shiftRegClockPins[SHIFTREG_NOS];
	int shiftRegPinForLevels[DISPLAY_LEVELS];
	void writeStatus(byte, int, int);
	byte shiftRegByteStatus[SHIFTREG_NOS]; // updated status of shift reg byte
	byte shiftRegAcOutByteStatus[SHIFTREG_NOS];
	shiftRegInfoForAcOutput shiftRegInfoForAcOutputArr[4];
	shiftRegInfoForAcLevelOutput shiftRegInfoForAcLevelOutputArray[2];

};

#endif /* LEDMANAGER_H_ */
