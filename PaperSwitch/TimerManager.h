/*
 * TimerManager.h
 *
 *  Created on: May 7, 2015
 *      Author: Nirmala
 */

#ifndef TIMERMANAGER_H_
#define TIMERMANAGER_H_

#include "GlobalVariables.h"
#include "Arduino.h"

class TimerLibrary {

public:
	void initTimer();
	void setTimer1(TimerApplication);
	void setTimer0();
	void detectTouchedDataDelegate(void (*)(TouchDetectData), bool (*)(void));

	int isTimerTimedout;
	int prevKeydetected;
	int timerCount;

	int timerOverFlowCounter;

	void (*timerDataDelegate)(TouchDetectData);
	bool (*isSameInputDetected)();
private:

};

extern "C" TimerApplication currentTimer1Application;
extern TimerLibrary TimerManager;

#endif /* TIMERMANAGER_H_ */
