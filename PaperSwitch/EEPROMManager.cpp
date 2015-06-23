/*
 * EEPROMManager.cpp
 *
 *  Created on: May 18, 2015
 *      Author: Ritika
 */
#include"EEPROMManager.h"

void EEPROMLibrary::EEPROMLibrary() {
	overFlowCounter = 0;
	counterForOVFCounter = 4;
	appStatusAdd = 2;

}
bool EEPROMLibrary::writeAppStatus(byte[] appStatus) {

	//read the counter from the eeprom
    EEPROM.get(COUNTER_FOR_OVF_COUNTER_ADD, overFlowCounter);
	if(overFlowCounter < MAX_WRITE) {
		overFlowCounter++;
		// increase the counter write eeprom counter
		EEPROM.write(appStatusAdd, appStatus[0]);
		EEPROM.write(appStatusAdd + 1, appStatus[1]);
	} else {
		//get the write counter value from
		if(counterForOVFCounter < MAX_WRITE) {
			// increase the counter write eeprom counter
			counterForOVFCounter++;
			// start the maxWriteOverflowCounter
			overFlowCounter = 0;
			appStatusAdd = appStatusAdd + 4;
		}
	}
	return false;
}

byte[] EEPROMLibrary::readAppStatus() {
	appStatus[0] = EEPROM.read(appStatusAdd);
	appStatus[1] = EEPROM.read(appStatusAdd + 1);
	return appStatus;
}

//This function will write a 3 byte (24bit) long to the eeprom at
//the specified address to adress + 3.
void EEPROMLibrary::EEPROMWritelong(int address, long value) {
	//Decomposition from a long to 3 bytes by using bitshift.
	//One = Most significant -> Three = Least significant byte
	byte three = ((value >> 8) & 0xFF);
	byte two = ((value >> 16) & 0xFF);
	byte one = ((value >> 24) & 0xFF);

	EEPROM.write(address, three);
	EEPROM.write(address + 1, two);
	EEPROM.write(address + 2, one);
}
