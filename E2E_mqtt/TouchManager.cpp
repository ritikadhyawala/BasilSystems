#include "TouchManager.h"
#include "GlobalVariables.h"

void TouchLibrary::initTouch() {

	Wire.begin();
	pinMode(8, INPUT);
	attachInterrupt(8, readstatus, FALLING);
	TouchManager.calibrate();
	Serial.print(TouchManager.read2120(0x02)); //TODO: need to write what we are printing
	Serial.println(TouchManager.read2120(0x03));
	Serial.println(TouchManager.read2120(0x04));
	prevKeydetected = -1;
	TouchManager.timerinit();
	isTimerTimedout=0;
}

void TouchLibrary::setTouchDelegate(void (*touchDataCallback)(TouchDetectData)){
	touchDataDelegate = touchDataCallback;
}

void TouchLibrary::monitorTouch(){
	while(!(digitalRead(8)))
	{
		TouchManager.read2120(0x02);
		TouchManager.read2120(0x03);
		TouchManager.read2120(0x04);
		delay(100);
	}
	if(isTimerTimedout && KeyDetected >= 0 && KeyDetected < 4){
		//		Serial.print("key in mo");
		//		Serial.println(KeyDetected);
		TouchManager.touchDataDelegate(data);
		KeyDetected =-1; // reset all detection
		isTimerTimedout = 0;
	}
	if (isTimerTimedout && KeyDetected >= 4 && KeyDetected <= 7){
		Serial.print("change");
		Serial.println(KeyDetected);

		//		data.touchType = LEVEL_TYPE;
		data.touchedSwitch = KeyDetected;
		TouchManager.touchDataDelegate(data);
		KeyDetected =-1; // reset all detection

	}
}

//void TouchLibrary:: monitorref(){
//	//	read2120(0x4C); // For key0
//	//	read2120(0x4D);
//	//	Serial.println(read2120(0x34)); // for Key0
//	//	Serial.println(read2120(0x35));
//	//	Serial.print(" refup0 ");
//	Serial.print("ref_k1 ");
//	Serial.print(read2120(0x4E)); // for Key1
//	Serial.print(" ");
//	Serial.print(read2120(0x4F));
//	//	Serial.print(" sigup0 ");
//	Serial.print(" sig_k1 ");
//	Serial.print(read2120(0x36)); // for Key1
//	Serial.print(" ");
//	Serial.print(read2120(0x37));
//	//	Serial.print("  refup1 ");
//	Serial.print("  ref_k2 ");
//	Serial.print(read2120(0x50)); // for Key2
//	Serial.print(" ");
//	Serial.print(read2120(0x51));
//	//	Serial.print(" sigup1 ");
//	Serial.print(" sig_k2 ");
//	Serial.print(read2120(0x38)); // for Key2
//	Serial.print(" ");
//	Serial.print(read2120(0x39));
//	//	Serial.print("  refk0 ");
//	Serial.print("  ref_k3 ");
//	Serial.print(read2120(0x52)); // for Key3
//	Serial.print(" ");
//	Serial.print(read2120(0x53));
//	//	Serial.print(" sigk0 ");
//	Serial.print(" sig_k3 ");
//	Serial.print(read2120(0x3A)); // for Key3
//	Serial.print(" ");
//	Serial.print(read2120(0x3B));
//	//	Serial.print("  refk1 ");
//	Serial.print("  ref_k4 ");
//	Serial.print(read2120(0x54)); // for Key4
//	Serial.print(" ");
//	Serial.print(read2120(0x55));
//	//	Serial.print(" sigk1 ");
//	Serial.print(" sig_k4 ");
//	Serial.print(read2120(0x3C)); // for Key4
//	Serial.print(" ");
//	Serial.print(read2120(0x3D));
//	//	Serial.print("  refdwn0 ");
//	Serial.print("  ref_k5 ");
//	Serial.print(read2120(0x56)); // for Key5
//	Serial.print(" ");
//	Serial.print(read2120(0x57));
//	//	Serial.print(" sigdwn0 ");
//	Serial.print(" sig_k5 ");
//	Serial.print(read2120(0x3E)); // for Key5
//	Serial.print(" ");
//	Serial.print(read2120(0x3F));
//	//	Serial.print("  refdwn1 ");
//	Serial.print("  ref_k6 ");
//	Serial.print(read2120(0x58)); // for Key6
//	Serial.print(" ");
//	Serial.print(read2120(0x59));
//	//	Serial.print(" sigdwn1 ");
//	Serial.print(" sig_k6 ");
//	Serial.print(read2120(0x40)); // for Key6
//	Serial.print(" ");
//	Serial.print(read2120(0x41));
//	//	Serial.print("  refk2 ");
//	Serial.print("  ref_k7 ");
//	Serial.print(read2120(0x5A)); // for Key7
//	Serial.print(" ");
//	Serial.print(read2120(0x5B));
//	//	Serial.print(" sigk2 ");
//	Serial.print(" sig_k7 ");
//	Serial.print(read2120(0x42)); // for Key7
//	Serial.print(" ");
//	Serial.print(read2120(0x43));
//	//	Serial.print("  refk3 ");
//	Serial.print("  ref_k8 ");
//	Serial.print(read2120(0x5C)); // for Key8
//	Serial.print(" ");
//	Serial.print(read2120(0x5D));
//	//	Serial.print(" sigk3 ");
//	Serial.print(" sig_k7 ");
//	Serial.print(read2120(0x44)); // for Key8
//	Serial.print(" ");
//	Serial.println(read2120(0x45));
//	//	read2120(0x5E); // for Key9
//	//	read2120(0x5F);
//	//	read2120(0x60); // for Key10
//	//	read2120(0x61);
//	//	read2120(0x62); // for Key11
//	//	read2120(0x63);
//}

void TouchLibrary:: monitorref(){
	byte a; byte b; int c;
	Serial.print("ref_k1 ");
	a= (read2120(0x4E)); // for Key1
	b = (read2120(0x4F));
	//	 c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x4E));
	//	Serial.print(" ");
	//	Serial.print(read2120(0x4F));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigup0 ");
	Serial.print(" sig_k1 ");
	a = (read2120(0x36)); // for Key1
	b = (read2120(0x37));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x36));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x37));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refup1 ");
	Serial.print("  ref_k2 ");
	a = (read2120(0x50)); // for Key2
	b = (read2120(0x51));
	//	c = (a*256) +b;
	//		Serial.print(c);
	//		Serial.print(read2120(0x50));
	//		Serial.print(" ");
	//			Serial.print(read2120(0x51));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigup1 ");
	Serial.print(" sig_k2 ");
	a = (read2120(0x38)); // for Key2
	b = (read2120(0x39));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x38));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x39));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refk0 ");
	Serial.print("  ref_k3 ");
	a = (read2120(0x52)); // for Key3
	b = (read2120(0x53));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x52));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x53));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigk0 ");
	Serial.print(" sig_k3 ");
	a = (read2120(0x3A)); // for Key3
	b = (read2120(0x3B));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x3A));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x3B));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refk1 ");
	Serial.print("  ref_k4 ");
	a = (read2120(0x54)); // for Key4
	b = (read2120(0x55));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x54));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x55));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigk1 ");
	Serial.print(" sig_k4 ");
	a = (read2120(0x3C)); // for Key4
	b = (read2120(0x3D));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x3C));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x3D));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refdwn0 ");
	Serial.print("  ref_k5 ");
	a = (read2120(0x56)); // for Key5
	b = (read2120(0x57));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x56));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x57));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigdwn0 ");
	Serial.print(" sig_k5 ");
	a = (read2120(0x3E)); // for Key5
	b = (read2120(0x3F));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x3E));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x3F));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refdwn1 ");
	Serial.print("  ref_k6 ");
	a = (read2120(0x58)); // for Key6
	b = (read2120(0x59));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x58));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x59));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigdwn1 ");
	Serial.print(" sig_k6 ");
	a = (read2120(0x40)); // for Key6
	b = (read2120(0x41));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x40));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x41));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refk2 ");
	Serial.print("  ref_k7 ");
	a = (read2120(0x5A)); // for Key7
	b = (read2120(0x5B));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x5A));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x5B));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigk2 ");
	Serial.print(" sig_k7 ");
	a = (read2120(0x42)); // for Key7
	b = (read2120(0x43));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x42));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x43));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print("  refk3 ");
	Serial.print("  ref_k8 ");
	a = (read2120(0x5C)); // for Key8
	b = (read2120(0x5D));
	//	c = (a*256) +b;
	//	Serial.print(c);
	//	Serial.print(read2120(0x5C));
	//	Serial.print(" ");
	//		Serial.print(read2120(0x5D));
	Serial.print(a);
	Serial.print(" ");
	Serial.print(b);
	//	Serial.print(" sigk3 ");
	Serial.print(" sig_k8 ");
	a = (read2120(0x44)); // for Key8
	b = (read2120(0x45));
	//	c = (a*256) +b;
	//	Serial.println(c);
	//	Serial.print(read2120(0x44));
	//	Serial.print(" ");
	//		Serial.println(read2120(0x45));
	Serial.print(a);
	Serial.print(" ");
	Serial.println(b);

}
int TouchLibrary::read2120(char addr) {
	I2CMasterSlaveAddrSet(I2C_BASE, SLAVE_ADDRESS, false);
	I2CMasterDataPut(I2C_BASE, addr);
	Wire.I2CTransact(I2C_MASTER_CMD_SINGLE_SEND);
	//Wire.I2CTransact(I2C_MASTER_CMD_BURST_SEND_STOP);
	I2CMasterSlaveAddrSet(I2C_BASE, SLAVE_ADDRESS, true);
	//Wire.I2CTransact(I2C_MASTER_CMD_BURST_RECEIVE_START);
	Wire.I2CTransact(0x00000007);
	char recvd = I2CMasterDataGet(I2C_BASE);
	//Wire.I2CTransact(I2C_MASTER_CMD_BURST_RECEIVE_FINISH);
	//Wire.I2CTransact(0x0000000F);

	//	Serial.println(int(recvd));
	return recvd;
}

void TouchLibrary::write2120(char pucData, char data, int ucLen) {
	I2CMasterSlaveAddrSet(I2C_BASE, SLAVE_ADDRESS, false);
	I2CMasterDataPut(I2C_BASE, pucData);
	Wire.I2CTransact(I2C_MASTER_CMD_BURST_SEND_START);
	ucLen--;
	pucData++;
	while (ucLen) {
		I2CMasterDataPut(I2C_BASE, data); // write next byte
		Wire.I2CTransact(I2C_MASTER_CMD_BURST_SEND_CONT); // Transact over I2C to send byte
		ucLen--; // Decrement the count and increment the data pointer
		pucData++;
	}
	Wire.I2CTransact(I2C_MASTER_CMD_BURST_SEND_STOP); // If stop bit is to be sent, send it.
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
	write2120(0x1C, 0x01, 2);         //k0
	write2120(0x1D, 0x04, 2);         //k1
	write2120(0x1E, 0x04, 2);      //k2 causes problem sometimes
	write2120(0x1F, 0x04, 2);     //k3
	write2120(0x20, 0x04, 2);    //k4
	write2120(0x21, 0x04, 2);    //k5
	write2120(0x22, 0x04, 2);    //k6
	write2120(0x23, 0x04, 2);    //k7
	write2120(0x24, 0x04, 2);    //k8
	write2120(0x25, 0x01, 2);
	write2120(0x26, 0x01, 2);
	write2120(0x27, 0x01, 2);
	write2120(0x29, 0x00, 2); // setting pulse scale for key1(change4) to 4 pulse scale 2
	write2120(0x2A, 0x00, 2);
	write2120(0x2B, 0x00, 2);
	write2120(0x2C, 0x00, 2);
	write2120(0x2D, 0x00, 2);
	write2120(0x2E, 0x00, 2);
	write2120(0x2F, 0x00, 2);
	write2120(0x30, 0x00, 2);
	write2120(0x0B, 0x04, 2); //setting DI to 12
	write2120(0x10, 0x14, 2); // dhtr count to 20 0
	write2120(0x11, 0x14, 2);//1
	write2120(0x12, 0x14, 2);//2
	write2120(0x13, 0x14, 2);//3
	write2120(0x14, 0x14, 2);//4
	write2120(0x15, 0x14, 2);//5
	write2120(0x16, 0x14, 2);//6
	write2120(0x17, 0x14, 2);//7
	write2120(0x18, 0x14, 2);//8

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
	Serial.println("dhtr");
	read2120(0x10);
	read2120(0x11);
	read2120(0x12);
	read2120(0x13);
	read2120(0x14);
	read2120(0x15);
	read2120(0x16);
	read2120(0x17);
	read2120(0x18);
	//  write2120(0x0B, 0x04); // DI to 8
	write2120(0x0C, 0x06, 2); // stuck key
	//  write2120(0x31, 0x84,2);  // proximity setting k9
}

void TouchLibrary::timerinit() {
	MAP_PRCMPeripheralClkEnable(PRCM_TIMERA0, PRCM_RUN_MODE_CLK);
	// Reset the Timer
	MAP_PRCMPeripheralReset (PRCM_TIMERA0);
	// Configure the Timer
	MAP_TimerConfigure(TIMERA0_BASE, TIMER_CFG_ONE_SHOT);
	MAP_TimerPrescaleSet(TIMERA0_BASE, TIMER_A, 0);
	MAP_TimerPrescaleSet(TIMERA0_BASE, TIMER_B, 0);
	// Register Timer Interrupt
	TimerIntRegister(TIMERA0_BASE, TIMER_A, TimerIntHandler);
	TimerIntRegister(TIMERA0_BASE, TIMER_B, motionTimerIntHandler);
	// MAP_TimerPrescaleMatchSet(TIMERA0_BASE, TIMER_A, 0);
	// Calculate the number of timer counts/microsecond
	// ticksPerMicrosecond = (F_CPU / 40) / 1000000;
	// Serial.print(ticksPerMicrosecond);
	// Setup the interrupt for the TIMERA0B timeout.
	MAP_TimerIntEnable(TIMERA0_BASE, TIMER_TIMA_TIMEOUT);
	MAP_TimerIntEnable(TIMERA0_BASE, TIMER_TIMB_TIMEOUT);

}

void TouchLibrary::setTimer() {

	// Initially load the timer with time (80m* time in seconds)
	MAP_TimerLoadSet(TIMERA0_BASE, TIMER_A, 80000000 * 1 / 10);
	// Enable the timer.
	MAP_TimerEnable(TIMERA0_BASE, TIMER_A);
	//	prevKeydetected = prev_key;
}



void TimerIntHandler(void) {

	MAP_TimerIntClear(TIMERA0_BASE, 0x00000001);
	TouchManager.timerCount++;

	if(TouchManager.timerCount>=5){ // count reached 5
		Serial.print("lp");
		//		TouchManager.prevKeydetected = -1;
		//		TouchManager.data.touchType = LONG_PRESS;
		//		TouchManager.data.touchedSwitch = TouchManager.prevKeydetected;
		if(TouchManager.KeyDetected < 4){
			TouchManager.data.touchType = LONG_PRESS;
		}
		else{
			TouchManager.data.touchType = LEVEL_TYPE_FAST;
		}
		TouchManager.data.touchedSwitch =TouchManager.KeyDetected;
		TouchManager.timerCount=0;
		TouchManager.isTimerTimedout=1;
		Serial.print("in tmr"); Serial.println(TouchManager.KeyDetected);
	}
	else{
		int DetectionStatus = TouchManager.read2120(0x02);
		//			Serial.print("d status");Serial.println(DetectionStatus);
		if(DetectionStatus) {
			TouchManager.setTimer();
		}
		else{
			if(TouchManager.KeyDetected < 4){
				TouchManager.data.touchType = SINGLE_PRESS;
			}
			else{
				TouchManager.data.touchType = LEVEL_TYPE;
			}
			//			TouchManager.data.touchedSwitch = TouchManager.prevKeydetected;
			TouchManager.data.touchedSwitch =TouchManager.KeyDetected;
			TouchManager.timerCount=0;
			TouchManager.isTimerTimedout=1;
			//			Serial.print("in tmr"); Serial.println(TouchManager.KeyDetected);
		}
	}

}

void readstatus(){
	//	Serial.print("i ");
	int DetectionStatus = TouchManager.read2120(0x02);
	Serial.print("i ");
	Serial.println(DetectionStatus);
	if((DetectionStatus ==1) | (DetectionStatus ==129))
	{
		//				Serial.println(DetectionStatus);
		char KeyStatus[2];
		KeyStatus[0] =(TouchManager.read2120(0x03));
		KeyStatus[1] =(TouchManager.read2120(0x04));
		if(KeyStatus[0] | KeyStatus[1]){
			TouchManager.KeyDetected = TouchManager.getKeyinDetect(KeyStatus);
			//			Serial.print("key in i");
			//			Serial.println(TouchManager.KeyDetected);
			//for keydetected greater than 9 handle in loop
		}
		if( TouchManager.KeyDetected < 8){ //set timer here
			Serial.print("Key Detected: ");
			Serial.println(TouchManager.KeyDetected);
			TouchManager.setTimer();
			//			MAP_TimerLoadSet(TIMERA0_BASE, TIMER_B, 80000000 * 1);
			// Enable the timer.
			//							MAP_TimerEnable(TIMERA0_BASE, TIMER_B);
			TouchManager.prevKeydetected = TouchManager.KeyDetected;
		}
	}
	TouchManager.isTimerTimedout = 0;
	TouchManager.monitorref();
}

void motionTimerIntHandler(){
	MAP_TimerIntClear(TIMERA0_BASE, 0x00000001);
	//	Serial.println('mtn tmr timeout');
	for(int i =0;i<4; i++ ){
		if(switches[i].IsAuto){
			switches[i].IsOn= false;
			//			switches[i].IsOn= !switches[i].IsOn;
			TouchManager.data.touchedSwitch =i;
			TouchManager.data.touchType = SINGLE_PRESS;
			TouchManager.touchDataDelegate(TouchManager.data);
		}

	}
}
