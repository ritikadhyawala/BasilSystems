package com.basilsystems.local.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class UDPSocketHandler {

	private static final int UDP_SERVER_PORT = 2004;

	private DatagramSocket udpSocket = null;
	private DatagramPacket udpPacket;
	private boolean listenServer = true;
	private Context context;

	public UDPSocketHandler(Context context){
		this.context = context;

	}

	public static interface ServerAddressProvider{
		public void getServerIPAddress(byte[] ipAddress);
	}

	public static interface UdpMessageProvider{
		public void onMessage(String message);
	}

	public void sendUdpPackets(final String data){

		Thread thread = new Thread(){
			public void run() {
				try {
					udpSocket = new DatagramSocket(UDP_SERVER_PORT);
					DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
							getBroadcastAddress(), 2562);
					udpSocket.send(packet);
					//					this.stop();
				} catch (IOException e) {
					Log.d("UDP Socket", e.getMessage());
					e.printStackTrace();
				}

			};
		};
		thread.start();
		//		new AsyncTask<Void, Void, Void>() {
		//
		//			@Override
		//			protected Void doInBackground(Void... params) {
		//				try {
		//					socket = new DatagramSocket(UDP_SERVER_PORT);
		//					packet = new DatagramPacket(data.getBytes(), data.length(),
		//							getBroadcastAddress(), 2562);
		//					socket.send(packet);
		//				} catch (IOException e) {
		//					Log.d("UDP Socket", e.getMessage());
		//					e.printStackTrace();
		//				}
		//				return null;
		//
		//			}
		//		}.execute();

	}

	private InetAddress getBroadcastAddress() throws IOException {
		WifiManager mWifi =  (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = mWifi.getDhcpInfo();
		if (dhcp == null) {
			return null;
		}
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public void stopUdpConnection(){
		listenServer = false;
		if(udpSocket != null){
			udpSocket.close();
		}
	}

	public void listenUdpPackets(final UdpMessageProvider udpMessageProvider){

		new Thread(){
			public void run() {

				listenServer = true;
				while(listenServer) {
					try {
						if(udpSocket != null){
							byte[] buf = new byte[1024];
							udpPacket = new DatagramPacket(buf, buf.length);
							udpSocket.receive(udpPacket);
							if(udpPacket != null && udpPacket.getLength() > 0){
								String response = new String(buf, 0, udpPacket.getLength());
								if(response.length() > 0){
									Bundle b = new Bundle();
									b.putString("Response", response);
									b.putString("ipaddress", udpPacket.getAddress().toString() );
									Message msg = new Message();
									msg.setData(b);
								    udpMessageProvider.onMessage(udpPacket.getAddress().toString());
//									handler.sendMessage(msg);
								}
								//								if("acknowledged".equals(response)){
								//									if(serverAddressProvider != null){
								//										serverAddressProvider.getServerIPAddress(packet.getAddress().getAddress());
								//									}
								//								}else{
								//									if(udpMessageProvider != null){
								//										udpMessageProvider.onMessage(response);
								//									}
								//								}
							}
						}

					} catch (IOException e) {
						e.printStackTrace();
						Log.d("UDP Socket Error", e.getMessage());
					}
				}


			};
		}.start();
		//		new AsyncTask<Void, Void, Void>() {
		//
		//			@Override
		//			protected Void doInBackground(Void... params) {}
		//
		//		}.execute();
	}

}
