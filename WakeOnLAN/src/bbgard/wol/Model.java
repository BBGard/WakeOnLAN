package bbgard.wol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	/**
	 * Checks for a valid MAC address using the regex pattern
	 * Checks for MAC addresses in many formats
	 */
	public Boolean isMacValid(String mac) {
		
		Pattern p = Pattern.compile("([MACmac]?){1}([:.-]?){1}(([0-9A-Fa-f]{2,3}[:.-]?){4,6})");
        Matcher m = p.matcher(mac);
        return m.find();
		
	}
	
	/**
	 * Returns a list of all Active MAC addresses on the network
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public List<byte[]> getActiveMacs() throws SocketException, UnknownHostException, IOException {
		// Get the list of active IP addresses on the network
		List<InetAddress> activeIps = getActiveIps();
		List<byte[]> activeMacs = null;
		System.out.println("got ips");

		// Loop over the list of active IP addresses
		for (InetAddress ip : activeIps) {
		    try {
		        // Get the network interface associated with this IP address
		        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);

		        // Get the MAC address associated with this network interface
		        byte[] mac = networkInterface.getHardwareAddress();
		        activeMacs.add(mac);

		        // Print the MAC address
		        System.out.println(String.format("%02X:%02X:%02X:%02X:%02X:%02X", mac));
		    } catch (SocketException e) {
		        // Handle error
		    }
		}
		
		return activeMacs;
	}

	/**
	 *  Scans the network and returns a list of all active IP addresses
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	private List<InetAddress> getActiveIps() throws SocketException, UnknownHostException, IOException {
		
		// Set the starting and ending IP addresses to scan
		InetAddress startIp = getStartIp();
		InetAddress endIp = getBroadcastAddress();
		System.out.println("here 1");
		

//		// Convert the IP addresses to InetAddress objects
//		InetAddress start = InetAddress.getByName(startIp);
//		InetAddress end = InetAddress.getByName(endIp);

		// Create a list to store the active IP addresses
		List<InetAddress> activeIps = new ArrayList<>();

		// Loop over the IP addresses in the range
		for (long i = ipToLong(startIp); i <= ipToLong(endIp); i++) {
		    // Convert the long representation of the IP address back to InetAddress
		    InetAddress ip = longToIp(i);

		    // Try to ping the IP address
		    System.out.println("pinging");
		    if (ip.isReachable(50)) {
		    	System.out.println("success! " +ip);
		        // The IP address is active, so add it to the list
		        activeIps.add(ip);
		    }
		    else {
		    	System.out.println("fail "+ip);
		    }
		}
		
		System.out.println("returning");
		return activeIps;
	}

	/**
	 * Returns the start IP address for the current network
	 */
	
	private InetAddress getStartIp() throws UnknownHostException, SocketException {
		
		// Get the byte array representation of the  broadcast IP address
		byte[] bytes = getBroadcastAddress().getAddress();

		// Modify the last octet of the IP address - i.e. change 192.168.68.255 into 192.168.68.1
		bytes[3] = 1;

		// Convert the modified byte array back to an InetAddress object
		InetAddress startIp = InetAddress.getByAddress(bytes);
		
		return startIp;
	}
	
	/**
	 * Converts an InetAddress to a long
	 * Generated by ChatGPT....
	 */
	private static long ipToLong(InetAddress ip) {
	    byte[] octets = ip.getAddress();
	    long result = 0;
	    
	    for (byte octet : octets) {
	        result <<= 8;
	        result |= octet & 0xff;
	    }
	    return result;
	}

	/**
	 * Converts a long to an InetAddress
	 * Generated by ChatGPT....
	 */
	private static InetAddress longToIp(long i) {
	    byte[] octets = new byte[4];
	    for (int octet = 3; octet >= 0; octet--) {
	        octets[octet] = (byte) (i & 0xff);
	        i >>= 8;
	    }
	    try {
	        return InetAddress.getByAddress(octets);
	    } catch (UnknownHostException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
}
