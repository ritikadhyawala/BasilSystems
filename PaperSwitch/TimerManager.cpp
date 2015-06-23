/*
 * TimerManager.cpp
 *
 *  Created on: May 7, 2015
 *      Author: Nirmala
 */

#include"TimerManager.h"

TimerApplication currentTimer1Application;
void TimerLibrary::initTimer() {
	TCCR1B = 0x00;        //stop
	TCNT1H = 0x00;        //Counter higher 8 bit value
	TCNT1L = 0x00;        //Counter lower 8 bit value
	TCCR1A = 0x00;
	TCCR1C = 0x00;
	TIMSK1 = (1 << TOIE1); 	     //timer1 overflow interrupt enable

	//also init timer 0
}

//This timer is set for auto mode time out
void TimerLibrary::setTimer1(TimerApplication timerApplication) {
	TCNT1H = 0x0B;        //Counter higher 8 bit value
	TCNT1L = 0xDC;        //Counter lower 8 bit value
	TCCR1B = (1 << CS12) | (1 << CS10);        //start Timer
	currentTimer1Application = timerApplication;
}

//This timer is set for touch interrupt  or ir interrupt
void TimerLibrary::setTimer0() {
	if (autoModeTimeOut == 1) {

	} else if (autoModeTimeOut == 2) {

	} else if (autoModeTimeOut == 3) {

	} else {

	}
}

ISR(TIMER1_OVF_vect) {


	TimerManager.timerOverFlowCounter = TimerManager.timerOverFlowCounter + 4;
	if(currentTimer1Application == AUTO_TIMEOUT) {
//set a global variable to switch off all the appliances which are in auto mode
		if(TimerManager.timerOverFlowCounter >= autoModeTimeOut) {
			TCCR1B=0x00;
			isAutoModeTimedOut = true;
			Serial.println("auto mode timer completed");
			TimerManager.timerOverFlowCounter = 0;
		}
	} else if(currentTimer1Application == CONFIGURE_TIME_OUT) {
		Serial.println("4");
		if(TimerManager.timerOverFlowCounter >= 30) {
			TCCR1B=0x00;
			isSendingModeOn = false;
			radio.openReadingPipe(1, pipe + radioAddress);
			radio.startListening();
			uint64_t add = pipe + radioAddress;
			Serial.print("Timer off. Listening to: ");Serial.println(radioAddress);
			TimerManager.timerOverFlowCounter = 0;
		}
	}
}

//ISR(TIMER0_OVF_vect) {
//
//	TCCR0B=0x00;
//	TimerManager.timerCount++;
//
//	TouchDetectData data;
//	if(TimerManager.timerCount == 5) { // count reached 5
//		data.touchType = LONG_PRESS;
//		TimerManager.timerDataDelegate(data);
//	}
//	else {
//		bool DetectionStatus = TimerManager.isSameInputDetected();
//		if(DetectionStatus) {
//			TimerManager.setTimer0();
//		}
//		else if(TimerManager.timerCount >= 5) {
//			TimerManager.timerCount = 0;
//		} else {
//			data.touchType = SINGLE_PRESS;
//			TimerManager.timerCount = 0;
//			TimerManager.timerDataDelegate(data);
//		}
//	}
//}

void TimerLibrary::detectTouchedDataDelegate(
		void (*timerDataCallback)(TouchDetectData),
		bool (*isSameInputDetectedCallback)()) {
	timerDataDelegate = timerDataCallback;
	isSameInputDetected = isSameInputDetectedCallback;
}
