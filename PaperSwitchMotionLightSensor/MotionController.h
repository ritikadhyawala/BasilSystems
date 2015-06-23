/*
 * MotionController.h
 *
 *  Created on: May 23, 2015
 *      Author: Ritika
 */

#ifndef MOTIONCONTROLLER_H_
#define MOTIONCONTROLLER_H_

#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
#include <Arduino.h>
/*-----( Declare Constants and Pin Numbers )-----*/
#define CE_PIN   9
#define CSN_PIN 10
#define CONFIG_TIMEOUT 60

class MotionManager {
private:

public:
	void initializeMotionSensorModule();
	void monitorMotionSensorModule();
	bool isRadioReceivingOn;
	uint32_t radioData;
	void initTimer();
	void setTimer();

};
extern "C" void onMotionDetected();
extern "C" void onConfigButPressedForMotionSensor();
extern "C" int timerCounter;
extern MotionManager MotionController;
extern "C" uint64_t pipe; // Define the transmit pipe

#endif /* MOTIONCONTROLLER_H_ */
