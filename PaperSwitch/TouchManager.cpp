#include "TouchManager.h"
//#include <avr/io.h>
#include <Arduino.h>
#include <avr/interrupt.h>
#include "GlobalVariables.h"


void TouchLibrary::initTouch() {

	Serial.begin(9600);
	i2c_init(); //Initialise the i2c bus

	pinMode(2, INPUT);
	attachInterrupt(0, readstatus, FALLING);
	//attachInterrupt(0, motion, FALLING); //motion sensor intrpt on pin 3
	//  longpresstimer1setup();
	//attachInterrupt(2,touch,FALLING); // touch intrpt on pin21
	TCCR1A = (1 << COM1A1) | (1 << WGM11); //non inverted PWM; top=icr
	TCCR1B = (1 << WGM13) | (1 << WGM12) | (1 << CS10); // MODE (FAST PWM) ;1 PRESCALER ;16khz frequency of pwm
	TCCR1C = 0;
	ICR1 = 999;

	Serial.print(TouchManager.read2120(0x02)); //TODO: need to write what we are printing
	Serial.println(TouchManager.read2120(0x03));
	Serial.println(TouchManager.read2120(0x04));
	isPrevKeydetected = false;
	isTouchedDataToBeProcessed = false;

	TimerManager.detectTouchedDataDelegate(setTouchedData, isSameInputDetected);
}

void TouchLibrary::setTouchDelegate(
		void (*touchDataCallback)(TouchDetectData)) {
	touchDataDelegate = touchDataCallback;
}

void TouchLibrary::monitorTouch() {
	while (!(digitalRead(8))) {
		TouchManager.read2120(0x02);
		TouchManager.read2120(0x03);
		TouchManager.read2120(0x04);
		delay(100);
	}

	if (isTouchedDataToBeProcessed && KeyDetected >= 0 && KeyDetected < 4) {
		//		Serial.print("key in mo");
		//		Serial.println(KeyDetected);
		TouchManager.touchDataDelegate(data);
		KeyDetected = -1; // reset all detection
		isTouchedDataToBeProcessed = false;
	}
	if (isTouchedDataToBeProcessed && KeyDetected >= 4 && KeyDetected <= 7) {
		Serial.print("change");
		Serial.println(KeyDetected);

		//		data.touchType = LEVEL_TYPE;
		data.touchedSwitch = KeyDetected;
		TouchManager.touchDataDelegate(data);
		KeyDetected = -1; // reset all detection
		isTouchedDataToBeProcessed = false;

	}
}

int TouchLibrary::read2120(char addr) {
	int ret = i2c_start((EEDEVADR << 1) | 0);
	if (ret) {
		i2c_stop();
	} else {
		i2c_write(addr);
		i2c_stop();
		i2c_start((EEDEVADR << 1) | 1);
		ret = i2c_readNak();
		i2c_stop();
	}

	return ret;
}

void TouchLibrary::write2120(char addr, char data) {
	int ret = i2c_start((EEDEVADR << 1) | 0);
	if (ret) {
		Serial.print(F("Failed"));
		i2c_stop();
	} else {
		i2c_write(addr);
		i2c_write(data);
		i2c_stop();
	}
}

int TouchLibrary::getKeyinDetect(char KeyStatus[]) {
	int keyinDetect = -1;

	if (KeyStatus[0] == 8) {
		keyinDetect = 0;
//		              Serial.println(F("k0 detected"));
	} else if (KeyStatus[0] == 16) {
		keyinDetect = 1;
		//		Serial.println(F("k1 detected"));
	} else if (KeyStatus[0] == 128) {
		keyinDetect = 2;
		//		Serial.println(F("k2 detected"));
	} else if (KeyStatus[1] == 1) {
		keyinDetect = 3;
		//		Serial.println(F("k3 detected"));
	} else if (KeyStatus[0] == 2) {
		keyinDetect = 4;
		//		Serial.println(F("k1.1 detected"));
	} else if (KeyStatus[0] == 32) {
		keyinDetect = 6;
		//		Serial.println(F("k1.2 detected"));
	} else if (KeyStatus[0] == 4) {
		keyinDetect = 5;
		//		Serial.println(F("k2.1 detected"));
	} else if (KeyStatus[0] == 64) {
		keyinDetect = 7;
		//		Serial.println(F("k2.2 detected"));
	} else {
		keyinDetect = 9;
//		Serial.print("no key pressed");
	}

	return (keyinDetect);
}

void TouchLibrary::calibrate(void) {
	//writeall(); // writting aks grp
	Serial.print("wrtng all");
	write2120(0x1C, 0x04);         //k0
	write2120(0x1D, 0x04);         //k1
	write2120(0x1E, 0x04);      //k2 cause problem sometimes
	write2120(0x1F, 0x04);     //k3
	write2120(0x20, 0x04);    //k4
	write2120(0x21, 0x04);    //k5
	write2120(0x22, 0x04);    //k6
	write2120(0x23, 0x04);    //k7
	write2120(0x24, 0x04);    //k8
	write2120(0x25, 0x01);
	write2120(0x26, 0x01);
	write2120(0x27, 0x01);
	// readall(); // reading aks
	Serial.print("rdng all");
	read2120(0x1C);
	read2120(0x1D);
	read2120(0x1E);
	read2120(0x1F);
	read2120(0x20);
	read2120(0x21);
	read2120(0x22);
	read2120(0x23);
	read2120(0x24);
	read2120(0x25);
	read2120(0x26);
	read2120(0x27);

	//  write2120(0x0B, 0x04); // DI to 8
	write2120(0x0C, 0x06); // stuck key
	//  write2120(0x31, 0x84,2);  // proximity setting k9
}


void readstatus() {
	//	Serial.print("i ");
	int DetectionStatus = TouchManager.read2120(0x02);
	Serial.print("i ");
	Serial.println(DetectionStatus);
	if ((DetectionStatus == 1) | (DetectionStatus == 129)) {
		//				Serial.println(DetectionStatus);
		char KeyStatus[2];
		KeyStatus[0] = (TouchManager.read2120(0x03));
		KeyStatus[1] = (TouchManager.read2120(0x04));
		if (KeyStatus[0] | KeyStatus[1]) {
			volatile int currentKeyDetected = TouchManager.getKeyinDetect(
					KeyStatus);
			if (currentKeyDetected < 8) {
				Serial.print("Key Detected: ");
				Serial.println(currentKeyDetected);
				if (currentKeyDetected == TouchManager.KeyDetected) {
					TouchManager.isPrevKeydetected = true;
				} else {
					TouchManager.isPrevKeydetected = false;
				}
				TouchManager.KeyDetected = currentKeyDetected;
			}
		}
	}
}

bool isSameInputDetected() {
	if(TouchManager.read2120(0x02)){
		return true;
	}else{
		return false;
	}
}

void setTouchedData(TouchDetectData touchDetectData) {
	TouchManager.isTouchedDataToBeProcessed = true;
	TouchManager.data = touchDetectData;
	if (TouchManager.KeyDetected < 4) {
		TouchManager.data.touchType = SINGLE_PRESS;
	} else {
		TouchManager.data.touchType = LEVEL_TYPE;
	}
	if (TouchManager.KeyDetected < 4) {
		TouchManager.data.touchType = LONG_PRESS;
	} else {
		TouchManager.data.touchType = LEVEL_TYPE_FAST;
	}

}

