/*
 * LocalCommManager.h
 *
 *  Created on: Mar 28, 2015
 *      Author: Nirmala
 */

#ifndef LOCALCOMMMANAGER_H_
#define LOCALCOMMMANAGER_H_
#include <Energia.h>
#define UART_DATA_LENGTH 128

class LocalCommManager{
public:
	LocalCommManager();
	void setUARTDelegate(void (*)(char *));
	void monitorLocalComm();
private:
	void(*uartDataFunction)(char *);
	char *dataptr;
	char databuffer[UART_DATA_LENGTH];
	void readLine();

};


#endif /* LOCALCOMMMANAGER_H_ */
