/**
 * Splitter for an IP String.
 *
 * This class takes an IP String and splits it up into each part. Contains methods to split
 * the ip into subnet, CIDR, and IP.
 *
 * @author Justin Rhude
 */
class IpSplitter {
    /**
     * Splits the subnet off the IP String and returns it along with the rest of the IP String.
     *
     * @param stringIP The String representation of an IP.
     * @return A SplitResponse which contains the split up subnet and the rest of the IP string.
     * @throws Exception If the IP String contains too many spaces.
     */
    static SplitResponse splitSubnet(String stringIP) throws Exception{
        String[] splitSubnet = stringIP.trim().split(" ");

        switch(splitSubnet.length){
            case 1:
                return null;
            case 2:
                return new SplitResponse(splitSubnet[0], splitSubnet[1].split("\\."));
            default:
                throw new Exception("too many spaces.");
        }
    }

    /**
     * Splits the CIDR value off the IP String and returns it along with the rest of the IP String.
     *
     * @param stringIP The String representation of an IP.
     * @return A SplitResponse which contains the split up subnet and the rest of the IP string.
     * @throws Exception If the IP String contains too many slashes.
     */
    static SplitResponse splitCIDR(String stringIP) throws Exception {
        String[] splitCIDR = stringIP.split("/");

        switch(splitCIDR.length){
            case 1:
                return null;
            case 2:
                String[] returnCIDR = {splitCIDR[1]};
                return new SplitResponse(splitCIDR[0], returnCIDR);
            default:
                throw new Exception("too many slashes.");
        }
    }

    /**
     * Splits up the IP octets and returns.
     *
     * @param stringIP The String representation of an IP.
     * @return String[] containing the split up octets of the IP.
     */
    static String[] splitIP(String stringIP){
        return stringIP.split("\\.");
    }
}
