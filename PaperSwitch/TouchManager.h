#ifndef TouchLibrary_h
#define TouchLibrary_h

#include <Arduino.h>
#include <ccspi.h>
#include <i2cmaster.h>
#include "GlobalVariables.h"
#include <SPI.h>
#include <string.h>
#include <EEPROM.h>
#define SLAVE_ADDRESS 0x1C
#define I2C_BASE I2CA0_BASE
#define EEDEVADR  0x1C
#include "TimerManager.h"

class TouchLibrary {
public:

	void setTouchDelegate(void (*)(TouchDetectData));
	void initTouch();
	int read2120(char addr);
	void write2120(char pucData, char data);
	void calibrate(void);
	int getKeyinDetect(char[]);
	void monitorTouch();
	void (*touchDataDelegate)(TouchDetectData);
	volatile int KeyDetected;
	TouchDetectData data;
	bool isTouchedDataToBeProcessed;
	bool isPrevKeydetected;


private:

};
extern "C" void readstatus();
extern "C" void setTouchedData(TouchDetectData);
extern "C" bool isSameInputDetected();
extern TouchLibrary TouchManager;

#endif

