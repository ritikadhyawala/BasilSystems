/*
 * EEPROMManager.h
 *
 *  Created on: May 18, 2015
 *      Author: Nirmala
 */

#ifndef EEPROMMANAGER_H_
#define EEPROMMANAGER_H_

#define MAX_WRITE 100000
#define COUNTER_FOR_OVF_COUNTER_ADD 5

#include"Arduino.h"

class EEPROMLibrary{
private:
	int overFlowCounter;
	long counterForOVFCounter;
	int appStatusAdd;
	byte[2] appStatus;

	void EEPROMWritelong(int address, long value);
public:

    EEPROMLibrary();
	bool writeAppStatus(byte[] appStatus);
	byte[] readAppStatus();

};
extern EEPROMLibrary EEPROMManager;

#endif /* EEPROMMANAGER_H_ */
