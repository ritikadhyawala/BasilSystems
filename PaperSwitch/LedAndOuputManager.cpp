/*
 * LedManager.cpp
 *
 *  Created on: Mar 24, 2015
 *      Author: Ritika
 */
#include "LedAndOuputManager.h"

ShiftRegOutputInfo shiftRegOutputInfoArr[MAX_SWITCHES];
switchInfo switches[NO_OF_APPLIANCES];
int autoModeTimeOut;

LedAndOutputManager::LedAndOutputManager() {
	shiftRegControlPins[0] = SHIFTREG_SELECT_1;
	shiftRegControlPins[1] = SHIFTREG_SELECT_2;
	shiftRegControlPins[2] = SHIFTREG_SELECT_3;
	shiftRegControlPins[3] = SHIFTREG_SELECT_4;
	shiftRegControlPins[4] = SHIFTREG_SELECT_4;

	shiftRegOutputInfoArr[0].shiftRegNoforTouchedKey = 0;
	shiftRegOutputInfoArr[0].shiftRegPinforOnOff = 0;
	shiftRegOutputInfoArr[0].shiftRegPinforRing = 1;
	shiftRegOutputInfoArr[4].shiftRegNoforTouchedKey = 0;
	shiftRegOutputInfoArr[6].shiftRegNoforTouchedKey = 0;

	shiftRegOutputInfoArr[1].shiftRegNoforTouchedKey = 1;
	shiftRegOutputInfoArr[1].shiftRegPinforOnOff = 0;
	shiftRegOutputInfoArr[1].shiftRegPinforRing = 1;
	shiftRegOutputInfoArr[5].shiftRegNoforTouchedKey = 1;
	shiftRegOutputInfoArr[7].shiftRegNoforTouchedKey = 1;

	shiftRegOutputInfoArr[2].shiftRegNoforTouchedKey = 2;
	shiftRegOutputInfoArr[2].shiftRegPinforOnOff = 4;
	shiftRegOutputInfoArr[2].shiftRegPinforRing = 5;

	shiftRegOutputInfoArr[3].shiftRegNoforTouchedKey = 2;
	shiftRegOutputInfoArr[3].shiftRegPinforOnOff = 6;
	shiftRegOutputInfoArr[3].shiftRegPinforRing = 7;

	shiftRegPinForLevels[0] = 3;
	shiftRegPinForLevels[1] = 4;
	shiftRegPinForLevels[2] = 5;
	shiftRegPinForLevels[3] = 6;
	shiftRegPinForLevels[4] = 7;

	shiftRegByteStatus[0] = 0;
	shiftRegByteStatus[1] = 0;
	shiftRegByteStatus[2] = 0;
	shiftRegByteStatus[3] = 0;
	shiftRegByteStatus[4] = 0;

	shiftRegClockPins[0] = SHIFTREG_CLOCK_1;
	shiftRegClockPins[1] = SHIFTREG_CLOCK_2;
	shiftRegClockPins[2] = SHIFTREG_CLOCK_3;
	shiftRegClockPins[3] = SHIFTREG_CLOCK_4;
	shiftRegClockPins[4] = SHIFTREG_CLOCK_5;

	shiftRegInfoForAcLevelOutputArray[0].levelPinsArray[0] = 0;
	shiftRegInfoForAcLevelOutputArray[0].levelPinsArray[1] = 6;
	shiftRegInfoForAcLevelOutputArray[0].levelPinsArray[2] = 5;
	shiftRegInfoForAcLevelOutputArray[0].levelPinsArray[3] = 4;
	shiftRegInfoForAcLevelOutputArray[0].shiftRegNo = 3;

	shiftRegInfoForAcLevelOutputArray[1].levelPinsArray[0] = 5;
	shiftRegInfoForAcLevelOutputArray[1].levelPinsArray[1] = 4;
	shiftRegInfoForAcLevelOutputArray[1].levelPinsArray[2] = 3;
	shiftRegInfoForAcLevelOutputArray[1].levelPinsArray[3] = 2;
	shiftRegInfoForAcLevelOutputArray[1].shiftRegNo = 4;

	shiftRegInfoForAcOutputArr[0].onOffShiftRegPin = 1;
	shiftRegInfoForAcOutputArr[0].shiftRegNo = 3;

	shiftRegInfoForAcOutputArr[1].onOffShiftRegPin = 6;
	shiftRegInfoForAcOutputArr[1].shiftRegNo = 4;

	shiftRegInfoForAcOutputArr[2].onOffShiftRegPin = 2;
	shiftRegInfoForAcOutputArr[2].shiftRegNo = 3;

	shiftRegInfoForAcOutputArr[3].onOffShiftRegPin = 3;
	shiftRegInfoForAcOutputArr[3].shiftRegNo = 3;

	pinMode(SHIFTREG_CLOCK_1, OUTPUT);
	pinMode(SHIFTREG_CLOCK_2, OUTPUT);
	pinMode(SHIFTREG_CLOCK_3, OUTPUT);
	pinMode(SHIFTREG_CLOCK_4, OUTPUT);
	pinMode(SHIFTREG_CLOCK_5, OUTPUT);
	pinMode(SHIFTREG_DATAPIN, OUTPUT);

	pinMode(SHIFTREG_SELECT_1, OUTPUT);
	pinMode(SHIFTREG_SELECT_2, OUTPUT);
	pinMode(SHIFTREG_SELECT_3, OUTPUT);
	pinMode(SHIFTREG_SELECT_4, OUTPUT);
	pinMode(SHIFTREG_SELECT_5, OUTPUT);
}

void LedAndOutputManager::controlOnOffDisplay(int switchNo) {
//	Serial.print("Switch no selected : ");
//	Serial.println(switchNo);
//	Serial.print("Switch reg no : ");
//	Serial.println(shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey);
//	Serial.print("Shift reg pin no : ");
//	Serial.println(shiftRegControlPins[switchNo]);

	if (switches[switchNo].IsOn) {
		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				1 << shiftRegOutputInfoArr[switchNo].shiftRegPinforOnOff
						| shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
	} else {
		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				~(1 << shiftRegOutputInfoArr[switchNo].shiftRegPinforOnOff)
						& shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];

	}
	int outputByte =
			shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
	//	Serial.print("Ouput byte : "); Serial.println(outputByte);
	writeStatus(outputByte,
			shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey,
			shiftRegClockPins[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey]);
}

void LedAndOutputManager::controlRingDisplay(int switchNo) {

	if (switches[switchNo].IsAuto) {
		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				1 << shiftRegOutputInfoArr[switchNo].shiftRegPinforRing
						| shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
	} else {

		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				~(1 << shiftRegOutputInfoArr[switchNo].shiftRegPinforRing)
						& shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];

	}
	int outputByte =
			shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
	writeStatus(outputByte,
			shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey,
			shiftRegClockPins[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey]);

}
void LedAndOutputManager::controlLevelDisplay(int switchNo) {

	int displayLevel =
			(int) (switches[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey].level)
					/ DISPLAY_DIVIDER;

//	Serial.print("Level display :");
//	Serial.println(displayLevel);
	int i = 1;
	while (i <= displayLevel) {
		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				1 << shiftRegPinForLevels[i - 1]
						| shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
		i++;
	}

	i = displayLevel + 1;
	while (i <= DISPLAY_LEVELS) {
		shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey] =
				~(1 << shiftRegPinForLevels[i - 1])
						& shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
		i++;
	}
	int outputByte =
			shiftRegByteStatus[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey];
	writeStatus(outputByte,
			shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey,
			shiftRegClockPins[shiftRegOutputInfoArr[switchNo].shiftRegNoforTouchedKey]);

}

void LedAndOutputManager::controlAcOnOff(int switchNo) {
	if (switches[switchNo].IsOn) {
			shiftRegAcOutByteStatus[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo] =
					1 << shiftRegInfoForAcOutputArr[switchNo].onOffShiftRegPin
							| shiftRegAcOutByteStatus[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo];
		} else {
			shiftRegAcOutByteStatus[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo] =
					~(1 << shiftRegInfoForAcOutputArr[switchNo].onOffShiftRegPin)
							& shiftRegAcOutByteStatus[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo];

		}
		int outputByte =
				shiftRegAcOutByteStatus[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo];
		//	Serial.print("Ouput byte : "); Serial.println(outputByte);
		writeStatus(outputByte,
				shiftRegInfoForAcOutputArr[switchNo].shiftRegNo,
				shiftRegClockPins[shiftRegInfoForAcOutputArr[switchNo].shiftRegNo]);


}
void LedAndOutputManager::controlAcLevelChange(int switchNo) {

int level = switches[switchNo].level - 1; // to make it to the scale of 0-15
for(int i = 0 ; i < 4 ; i++){
	if((1<<i)&level){
		shiftRegAcOutByteStatus[shiftRegInfoForAcLevelOutputArray[switchNo].shiftRegNo] =
							1 << shiftRegInfoForAcLevelOutputArray[switchNo].levelPinsArray[i]
									| shiftRegAcOutByteStatus[shiftRegInfoForAcLevelOutputArray[switchNo].shiftRegNo];
	}else{
		shiftRegAcOutByteStatus[shiftRegInfoForAcLevelOutputArray[switchNo].shiftRegNo] =
									~(1 << shiftRegInfoForAcLevelOutputArray[switchNo].levelPinsArray[i])
											& shiftRegAcOutByteStatus[shiftRegInfoForAcLevelOutputArray[switchNo].shiftRegNo];
	}
}
}

void LedAndOutputManager::writeStatus(byte status, int select, int clockPin) {
	int i;
//	Serial.print("Select : ");
//	Serial.println(shiftRegControlPins[select]);
//	Serial.print("Status : ");
//	Serial.println((int) status);
//	Serial.print("clock : ");
//	Serial.println(clockPin);

	digitalWrite(shiftRegControlPins[select], LOW);
	delay(1);
	digitalWrite(shiftRegControlPins[select], HIGH);
	for (i = 0; i < 8; i++) {
		if (status & 0b10000000) {
			//MSB is 1 so output high
			//			Serial.println("in 1");
			digitalWrite(SHIFTREG_DATAPIN, HIGH);
		} else {
			//MSB is 0 so output high
			//			Serial.println("in 0");
			digitalWrite(SHIFTREG_DATAPIN, LOW);
		}

		//Pulse  the Clock line
		digitalWrite(clockPin, HIGH);
		delayMicroseconds(10);
		digitalWrite(clockPin, LOW);
		status = status << 1;   // bring next bit
	}
}

