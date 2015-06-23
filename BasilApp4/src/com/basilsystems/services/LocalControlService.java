package com.basilsystems.services;

import android.content.Context;
import android.widget.Toast;

import com.basilsystems.local.control.TCPClient;
import com.basilsystems.local.control.TCPClient.OnMessageReceived;
import com.basilsystems.local.control.TCPClient.TCPConnectionStatus;
import com.basilsystems.local.control.UDPSocketHandler;
import com.basilsystems.local.control.UDPSocketHandler.UdpMessageProvider;

public class LocalControlService {


	private String ipaddress;

	public static TCPClient tcpClient;


	private Context context = null;
	public LocalControlService(Context context){
		this.context = context;
	}

	public static interface UDPReply{
		public void onUDPReply( boolean gotReply);
	}
	private void getLocalServerIPAddress(final UDPReply udpReply){
		UDPSocketHandler udpSocketHandler = new UDPSocketHandler(context);

		udpSocketHandler.sendUdpPackets("local");

		//		udpSocketHandler.listenUdpPackets(new Handler(new Callback() {
		//
		//			@Override
		//			public boolean handleMessage(Message msg) {
		//
		//				if(msg != null){
		//					Bundle data = msg.getData();
		//					if(data != null){
		//						if(data.containsKey("Response")){
		//							String string = data.getString("Response");
		////							Toast.makeText(context, "Server gave : " + string, Toast.LENGTH_SHORT).show();
		//						}
		//						if(data.containsKey("ipaddress")){
		//							String string = data.getString("ipaddress");
		////							Toast.makeText(context, "Server ip address : " + string, Toast.LENGTH_SHORT).show();
		//							setIpaddress(string);
		//							udpReply.onUDPReply(true);
		//							
		//						}
		//					}else{
		//						udpReply.onUDPReply(false);
		//					}
		//				}
		//				return true;
		//			}
		//		}));
		//	}

		udpSocketHandler.listenUdpPackets(new UdpMessageProvider() {

			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub
				if(message != null){
//					Toast.makeText(context, "Server ip address : " + message, Toast.LENGTH_SHORT).show();
					setIpaddress(message.substring(1));
					udpReply.onUDPReply(true);
				}
			}
		});
	}

		public static interface onMessageFromLocalServer{
			public void onMessage(String message);
		}

		public void establishTCPConnection(){
			getLocalServerIPAddress(new UDPReply() {

				@Override
				public void onUDPReply(boolean gotReply) {
					// TODO Auto-generated method stub

					tcpClient = new TCPClient(new OnMessageReceived() {

						@Override
						public void messageReceived(String message) {
							Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							//						onMessageFromLocalServer.onMessage(message);
							// broadcast it to activities

						}
					}, getIpaddress());

					tcpClient.run(new TCPConnectionStatus() {

						@Override
						public void onConnectionStatus(String status) {
//							Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
							//						onMessageFromLocalServer.onMessage(status);
							//broadcast it to activities
						}
					});
				}
			});
		}

		public void sendMessageToLocalServer(String message){

			if(tcpClient != null){

				tcpClient.sendMessage(message);

			}
		}

		public String getIpaddress() {
			return ipaddress;
		}

		public void setIpaddress(String ipaddress) {
			this.ipaddress = ipaddress;
		}

	}
