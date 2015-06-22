/*
 * LocalCommManager.cpp
 *
 *  Created on: Mar 28, 2015
 *      Author: Nirmala
 */

#include "LocalCommManager.h"
LocalCommManager::LocalCommManager() {
	Serial1.begin(115200);
}
void LocalCommManager::setUARTDelegate(void (*mainCallBack)(char *)) {
	uartDataFunction = mainCallBack;
}
void LocalCommManager::monitorLocalComm() {

	if (Serial1.available() > 0) {
//		Serial.print("monitor localcom");
		readLine();
		dataptr = databuffer;
		uartDataFunction(databuffer);
	}

}

void LocalCommManager::readLine() {
	dataptr = databuffer;
	char c;
	while (Serial1.available() && (dataptr < &databuffer[UART_DATA_LENGTH - 2])) {
		c = Serial1.read();
		if (c == 0) {
			;
		} else {
//			Serial.print("in readline else");
			*dataptr++ = c;
		}
	}
	*dataptr = 0;
}

