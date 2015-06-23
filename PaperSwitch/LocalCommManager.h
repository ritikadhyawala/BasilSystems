/*
 * LocalCommManager.h
 *
 *  Created on: Mar 28, 2015
 *      Author: Nirmala
 */

#ifndef LOCALCOMMMANAGER_H_
#define LOCALCOMMMANAGER_H_
#include <Arduino.h>
#include "GlobalVariables.h"
#include <SPI.h>


class LocalCommManager{
public:
	LocalCommManager();
	void setUARTDelegate(void (*)(SensorType, int[2]));
	void checkForRadioData();
	void sendRadioData(int []);
	uint64_t pipe;
private:
	void(*nrfDataFunction)(SensorType, int[2]);
	int receivedData[2]; // 2 element array holding radio readings
};

#endif /* LOCALCOMMMANAGER_H_ */
