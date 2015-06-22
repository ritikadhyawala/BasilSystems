#ifndef TouchLibrary_h
#define TouchLibrary_h

#include <Energia.h>
#include <Wire.h>
#include "driverlib/i2c.h"
#include "inc/hw_memmap.h"
#include <driverlib/prcm.h>
#include <driverlib/timer.h>
#include "GlobalVariables.h"
#define SLAVE_ADDRESS 0x1C
#define I2C_BASE I2CA0_BASE


class TouchLibrary {
public:

	void setTouchDelegate(void (*)(TouchDetectData));
	void initTouch();
//	void detectTouchedKeystatus();
	int read2120 (char addr);
	void write2120(char pucData,char data, int ucLen);
	void calibrate(void);
	int getKeyinDetect(char[]);
	void timerinit();
	void setTimer();
	void monitorTouch();
	void monitorref(void);
	void (*touchDataDelegate)(TouchDetectData);
	volatile int KeyDetected;
	TouchDetectData data;
	int isTimerTimedout;
	int prevKeydetected;
	int timerCount;

private:




};
extern  TouchLibrary TouchManager;
extern "C" void TimerIntHandler(void);
extern "C" void readstatus();
extern "C" void motionTimerIntHandler(void);
#endif

