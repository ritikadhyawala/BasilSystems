/*
 * MotionSensorMainController.cpp
 *
 *  Created on: May 23, 2015
 *      Author: Nirmala
 */

#include"MotionController.h"
#include <EEPROM.h>

RF24 radio(CE_PIN, CSN_PIN); // Create a Radio

uint64_t pipe;
int timerCounter;
int data[2];
bool isMotionDetected = false;
void MotionManager::initializeMotionSensorModule() {

	Serial.begin(9600);
	pipe = 0xE8E8F0F0E1LL;
	radio.begin();
	//initialize the motion sensor interrupt
	attachInterrupt(0, onMotionDetected, RISING);
	// initialize the configure button interrupt
	attachInterrupt(1, onConfigButPressedForMotionSensor, FALLING);

	EEPROM.get(0, radioData);

	if (radioData != 0xFFFF) {
		uint64_t add = radioData + pipe;
		Serial.print("radio address is : ");
		Serial.println((long)add);
		radio.openWritingPipe(add);
		isRadioReceivingOn = false;
	}
	initTimer();
	data[0] = 1;
	data[1] = 2;
	radio.setRetries(15,15);
}
void MotionManager::monitorMotionSensorModule() {

	if (isRadioReceivingOn) {

		if (radio.available()) {
			// Read the data payload until we've received everything
			bool done = false;
			while (!done) {
				// Fetch the data payload
				done = radio.read(&radioData, sizeof(radioData));
				Serial.print(radioData);
				//add the radioData to the pipe and save it in eeprom
				EEPROM.put(0, radioData);
				//put the motion sensor back to sending mode
				uint64_t add = radioData + pipe;
				Serial.print("radio address is : ");
				Serial.println(radioData);
				radio.openWritingPipe(add);
				isRadioReceivingOn = false;
				TCCR1B = 0x00;
				Serial.println("Received Confign");
			}
		}

	}
//	radio.write(data, sizeof(data));
	if(isMotionDetected){
		Serial.println("Sending");
		radio.write(data, sizeof(data));
		delay(10);
		radio.write(data, sizeof(data));
		delay(10);
		radio.write(data, sizeof(data));
		isMotionDetected = false;
	}
}

void onMotionDetected() {

//	EEPROM.get(0, MotionController.radioData);

//	if (MotionController.radioData != 0xFFFF) {
	Serial.println("m");
	isMotionDetected = true;
	//if eeprom has a address, then send the motion data and the light sensor data


//	}
	//else don't send the data
}

void onConfigButPressedForMotionSensor() {

	//switch to receiving mode of radio
	Serial.println("Config button pressed");
	MotionController.isRadioReceivingOn = true;
	radio.openReadingPipe(1, pipe);
	radio.startListening();
	//set a timer about 1 minute for this
	MotionController.setTimer();
}

void MotionManager::initTimer() {
	TCCR1B = 0x00;        //stop
	TCNT1H = 0x00;        //Counter higher 8 bit value
	TCNT1L = 0x00;        //Counter lower 8 bit value
	TCCR1A = 0x00;
	TCCR1C = 0x00;
	TIMSK1 = (1 << TOIE1); 	     //timer1 overflow interrupt enable

	//also init timer 0
}

void MotionManager::setTimer() {
	TCNT1H = 0x0B;        //Counter higher 8 bit value
	TCNT1L = 0xDC;        //Counter lower 8 bit value
	TCCR1B = (1 << CS12) | (1 << CS10);        //start Timer
}

ISR(TIMER1_OVF_vect) {
//set a global variable to switch off all the appliances which are in auto mode
	Serial.println("4");
	timerCounter = timerCounter + 4;
	if(timerCounter >= CONFIG_TIMEOUT) {
		TCCR1B=0x00;
		Serial.println("Config time completed");
		timerCounter = 0;
		// change the radio back to sending mode
		radio.openWritingPipe(pipe);
	}
}
