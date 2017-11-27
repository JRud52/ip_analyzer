import java.util.Arrays;
import java.util.List;

/**
 * Class representing an IP address.
 *
 * This class contains all the attributes that go with and IP address. Also contains methods to validate
 * and set each starting attribute.
 *
 * @author Justin Rhude
 */
class IP {
    private boolean hasCalculated;
    private int[] octet;
    private int[] subnetOctet;

    private String networkClass;
    private String subnetMask;
    private int cidr;
    private long hosts;
    private String networkAddress;
    private String broadcastAddress;
    private int hostBits;
    private int networkBits;

    IP(){
        hasCalculated = false;
        cidr = 0;
        octet = new int[4];
        subnetOctet = new int[4];

        networkAddress = "";
        broadcastAddress = "";
    }

    /**
     * Validates each octet of the subnet and stores it.
     *
     * @param splitSubnetOctets The split up String octets of the subnet.
     * @throws Exception If the splitSubnetOctets are malformed.
     */
    void validateAndSetSubnet(String[] splitSubnetOctets) throws Exception{
        List<Integer> validOctetValues = Arrays.asList(255, 254, 252, 248, 240, 224, 192, 128, 0);

        if (splitSubnetOctets.length != 4) throw new Exception("wrong number of decimals in the subnet.");

        subnetOctet[0] = Integer.parseInt(splitSubnetOctets[0]);
        subnetOctet[1] = Integer.parseInt(splitSubnetOctets[1]);
        subnetOctet[2] = Integer.parseInt(splitSubnetOctets[2]);
        subnetOctet[3] = Integer.parseInt(splitSubnetOctets[3]);

        int lastOctet = 255;
        for (int octet : subnetOctet){
            if (octet > 255) throw new Exception("subnet octets must be between 0 and 255.");
            if (!validOctetValues.contains(octet)) throw new Exception("network address must be all ones.");
            if (octet > lastOctet) throw new Exception("each octet in a subnet must be lower than the previous.");
            if (octet < 255 && octet != 0 && lastOctet != 255) throw new Exception("there can only be one octet below 255 that isn't zero.");
            lastOctet = octet;
        }

    }

    /**
     * Validates the cidr value and stores it.
     *
     * @param cidr The String version of the cidr value.
     * @throws Exception If the cidr value is malformed.
     */
    void validateAndSetCIDR(String cidr) throws Exception {
        this.cidr = Integer.parseInt(cidr);

        if (this.cidr < 1 || this.cidr > 31){
            throw new Exception("CIDR value must be between 1 and 31");
        }
    }

    /**
     * Validates each octet of the IP and stores it.
     *
     * @param splitIP The split up octets of the IP.
     * @throws Exception If the splitIP octets are malformed.
     */
    void validateAndSetIP(String[] splitIP) throws Exception{
        if (splitIP.length != 4) throw new Exception("wrong number of decimals in the ip address.");

        octet[0] = Integer.parseInt(splitIP[0]);
        octet[1] = Integer.parseInt(splitIP[1]);
        octet[2] = Integer.parseInt(splitIP[2]);
        octet[3] = Integer.parseInt(splitIP[3]);

        for (int octet : octet){
            if (octet < 0 || octet > 255) throw new Exception("each octet of the IP must be between 0 and 255.");
        }
    }

    /**
     * Packages each property into a String[] and returns. Calculates the remaining properties if
     * that has not been done already.
     *
     * @return Each property in String form.
     */
    String[] reportProperties() {
        String[] report = new String[8];

        if (!hasCalculated) {
            if (cidr == 0 && subnetOctet[0] == 0) calculateProperties();
            else calculateCIDRProperties();
        }

        report[0] = "Network Class: " + networkClass;
        report[1] = "Subnet Mask: " + subnetMask;
        report[2] = "CIDR: /" + cidr;
        report[3] = "Hosts Per subnet: " + hosts;
        report[4] = "Network Address: " + networkAddress;
        report[5] = "Broadcast Address: " + broadcastAddress;
        report[6] = "Bits in host: " + hostBits;
        report[7] = "Bits in Network: " + networkBits;

        return report;
    }

    /**
     * Calculates the properties of an IP based on just the IP address. Assumes there is no CIDR involved.
     */
    private void calculateProperties() {
        if (octet[0] < 128) {
            networkClass = "A";
            subnetMask = "255.0.0.0";
            cidr = 8;
            hosts = 16777214;
            networkAddress = octet[0] + ".0.0.0";
            broadcastAddress = octet[0] + ".255.255.255";
            hostBits = 24;
            networkBits = 8;

        }
        else if (octet[0] < 192) {
            networkClass = "B";
            subnetMask = "255.255.0.0";
            cidr = 16;
            hosts = 65534;
            networkAddress = octet[0] + "." + octet[1] + ".0.0";
            broadcastAddress = octet[0] + "." + octet[1] + ".255.255";
            hostBits = 16;
            networkBits = 16;
        }
        else if (octet[0] < 224) {
            networkClass = "C";
            subnetMask = "255.255.255.0";
            cidr = 24;
            hosts = 254;
            networkAddress = octet[0] + "." + octet[1] + "." + octet[2] + ".0";
            broadcastAddress = octet[0] + "." + octet[1] + "." + octet[2] + ".255";
            hostBits = 8;
            networkBits = 24;
        }
        else if (octet[0] < 240) {
            networkClass = "D - This class isn't generally used.";
        }
        else  {
            networkClass = "E - This class isn't generally used.";
        }
    }

    /**
     * Calculated the properties of an IP based on the IP address and either a CIDR value or a subnet mask.
     */
    private void calculateCIDRProperties() {
        networkClass = "None";

        if (subnetOctet[0] != 0) {
            for (int octet: subnetOctet){
                cidr += Integer.bitCount(octet);
            }
        } else {
            for (int i = 0; i < (cidr / 8); i++){
                subnetOctet[i] = 255;
            }
            for (int i = 7; i > 7 - (cidr % 8); i--){
                subnetOctet[cidr / 8] += Math.pow(2, i);
            }
        }

        subnetMask = subnetOctet[0] + "." + subnetOctet[1] + "." + subnetOctet[2] + "." + subnetOctet[3];
        hosts = (long)Math.pow(2, 32 - cidr) - 2;

        for (int i = 0; i < 4; i++){
            if (subnetOctet[i] ==  255) networkAddress += octet[i];
            else if (subnetOctet[i] == 0) networkAddress += "0";
            else {
                networkAddress += (subnetOctet[i] & octet[i]);
            }
            if (i != 3) networkAddress += ".";
        }

        for (int i = 0; i < 4; i++){
            if (subnetOctet[i] ==  255) broadcastAddress += octet[i];
            else if (subnetOctet[i] == 0) broadcastAddress += "255";
            else {

                broadcastAddress += (0xff & (~subnetOctet[i] | octet[i]));
            }
            if (i != 3) broadcastAddress += ".";
        }

        hostBits = 32 - cidr;
        networkBits = cidr;
    }
}
