/**
 * Response class for IpSplitter.
 *
 * This class acts as a container for the IpSplitter response.
 * @see IpSplitter
 *
 * @author Justin Rhude
 */
class SplitResponse {
    String remainingIP;
    String[] response;

    SplitResponse(String remainingIP, String[] response){
        this.remainingIP = remainingIP;
        this.response = response;
    }
}
