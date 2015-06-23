/*
 * LocalCommManager.cpp
 *
 *  Created on: Mar 28, 2015
 *      Author: Nirmala
 */

#include "LocalCommManager.h"


LocalCommManager::LocalCommManager() {

	pipe = 0xE8E8F0F0E1LL;
	radio.begin();
	radio.openWritingPipe(pipe);
}
void LocalCommManager::setUARTDelegate(void (*mainCallBack)(SensorType, int[2])) {
	nrfDataFunction = mainCallBack;
}

void LocalCommManager::checkForRadioData() {

	if ( radio.available() )
	  {
	    // Read the data payload until we've received everything
	    bool done = false;
	    while (!done)
	    {
	      // Fetch the data payload
	      done = radio.read( receivedData, sizeof(receivedData) );
	      Serial.print(receivedData[0]);
	      Serial.print(" ");
	      Serial.println(receivedData[1]);

	      nrfDataFunction(MOTION, receivedData);
	    }
	  }
	  else
	  {
	      Serial.println("No radio available");
	  }
}

void LocalCommManager::sendRadioData(int data[]){
	radio.write( data, sizeof(data) );
}


