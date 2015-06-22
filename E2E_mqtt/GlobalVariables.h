/*
 * GlobalVariables.h
 *
 *  Created on: Mar 23, 2015
 *      Author: Nirmala
 */

#ifndef GLOBALVARIABLES_H_
#define GLOBALVARIABLES_H_
#define MAX_SWITCHES 8
#define NO_OF_APPLIANCES 4  // NO OF APPLIANCES CONTROLLED

enum TouchType {
	SINGLE_PRESS,
	LONG_PRESS,
	LEVEL_TYPE,
	LEVEL_TYPE_FAST
};

typedef struct switchInfo{
	  boolean IsOn;
	  boolean IsAuto;
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


#endif /* GLOBALVARIABLES_H_ */
