/*
 * GlobalVariables.h
 *
 *  Created on: Mar 23, 2015
 *      Author: Nirmala
 */
#ifndef GLOBALVARIABLES_H_
#define GLOBALVARIABLES_H_
#include <nRF24L01.h>
#include <RF24.h>
#define MAX_SWITCHES 8
#define NO_OF_APPLIANCES 4  // NO OF APPLIANCES CONTROLLED
#define NO_OF_LEVEL_APPPLIANCES 2 //no of appliances which has intensity level
#define NO_OF_AUTO_TIME_OUT 4 //no of settings for auto mode time out
#define CE_PIN   9
#define CSN_PIN 10
enum TouchType{
	SINGLE_PRESS,
	LONG_PRESS,
	LEVEL_TYPE,
	AUTO_TIME_OUT,
	LEVEL_TYPE_FAST
};

enum SensorType{
	MOTION
};

enum TimerApplication{
	AUTO_TIMEOUT,
	CONFIGURE_TIME_OUT
};

typedef struct switchInfo{
	  bool IsOn;
	  bool IsAuto;
	  int level;         //0-15
	}switchInfo;

typedef  struct TouchDetectData{
	int touchedSwitch;
	TouchType touchType;
}touchDetectData;

typedef struct {
	int shiftRegNoforTouchedKey;
	int shiftRegPinforOnOff;
	int shiftRegPinforRing;

} ShiftRegOutputInfo;


extern switchInfo switches[NO_OF_APPLIANCES];
extern ShiftRegOutputInfo shiftRegOutputInfoArr[MAX_SWITCHES];

extern bool isAutoModeTimedOut;

extern int autoModeTimeOut;
extern "C" bool isSendingModeOn;
extern RF24 radio;
extern uint32_t radioAddress;
extern uint64_t pipe;

#endif /* GLOBALVARIABLES_H_ */
