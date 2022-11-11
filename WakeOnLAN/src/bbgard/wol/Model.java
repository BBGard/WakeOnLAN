package bbgard.wol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This class holds all data and logic for WakeOnLAN
 * 
 * @author Benjamin Gardiner Adapted from: http://www.jibble.org/wake-on-lan/
 */

public class Model {
	private final int PORT = 9; // The port to send the magic packet to UDP port 9

	// Sends a magic packet to the specified IP and MAC address
	public void sendMagicPacket(String ipToWake, String macToWake) {

		// Takes a 6 byte MAC address and converts it into a 102 byte address
		// see: http://www.jibble.org/wake-on-lan/ for a basic explanation
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
			InetAddress address = InetAddress.getByName(ipToWake);
			// theData, the length of packet, the ipAddress, the port
			DatagramPacket packet = new DatagramPacket(mac102Bytes, mac102Bytes.length, address, PORT);
			DatagramSocket socket = new DatagramSocket();

			// Opens a socket to the IP address and sends the magic packet
			socket.send(packet);
			socket.close();

			System.out.println("Wake-on-LAN packet sent.");
		} catch (Exception e) {
			System.out.println("Failed to send Wake-on-LAN packet: + e");
			System.exit(1);
		}

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
