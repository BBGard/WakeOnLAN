package bbgard.wol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import bbgard.wol.ui.Feedback;

/**
 * This class holds all data and logic for WakeOnLAN * 
 * @author Benjamin Gardiner 
 * Adapted from: http://www.jibble.org/wake-on-lan/
 */

public class Model {
	private final int PORT = 9; // The port to send the magic packet to UDP port 9

	/** Sends a magic packet to the specified IP and MAC address
	 * Takes a 6 byte MAC address and converts it into a 102 byte address
	 * Creating a Magic Packet to wake a device
	 * Essentially fills the first 6 bytes with 0xff
	 * Then repeats the MAC address 6 times
	 * @param macToWake The MAC address of the computer to wake
	 * @return String message of the result
	 */
	public String sendMagicPacket(String macToWake) {
		
		try {
			byte[] mac6Bytes = getMacBytes(macToWake);
			byte[] mac102Bytes = new byte[6 + 16 * mac6Bytes.length];

			// Fills first 6 bytes with 0xff
			for (int i = 0; i < 6; i++) {
				mac102Bytes[i] = (byte) 0xff;
			}

			// Fills remaining bytes with the MAC address
			for (int i = 6; i < mac102Bytes.length; i += mac6Bytes.length) {
				System.arraycopy(mac6Bytes, 0, mac102Bytes, i, mac6Bytes.length);
			}

			// Constructs the magic packet
			InetAddress broadcast = getBroadcastAddress();
			
			// If address is localHost, something went wrong - couldn't find broadcast address
			if(broadcast == InetAddress.getLocalHost()) {
				return Feedback.NO_BROADCAST_ADDRESS;
			}
			
			DatagramPacket packet = new DatagramPacket(mac102Bytes, mac102Bytes.length, broadcast, PORT);
			DatagramSocket socket = new DatagramSocket();

			// Opens a socket to the IP address and sends the magic packet
			socket.send(packet);
			socket.close();

			return "Wake-on-LAN packet sent to " + macToWake;
		} catch (Exception e) {			
			//System.exit(1);
			return e.getMessage();
		}

	}

	/**
	 * Attempts to get and return the broadcast IP address for the current network
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	private InetAddress getBroadcastAddress() throws SocketException, UnknownHostException {
		
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) 
		{
		    NetworkInterface networkInterface = interfaces.nextElement();
		    if (networkInterface.isLoopback())
		        continue;    // Do not want to use the loopback interface.
		    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) 
		    {
		        InetAddress broadcast = interfaceAddress.getBroadcast();
		        if (broadcast == null)
		            continue;
		        
		        return broadcast;
		    }
		}
		
		return InetAddress.getLocalHost();
	}

	// Splits up the provided MAC address into bytes
	// Accepts addresses using : or /
	// Example 10:2A:23:CD:67 or 10-2A-23-CD-67
	private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(\\:|\\-)");
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}
		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address.");
		}
		return bytes;
	}
}
