import java.util.Scanner;

/**
 * Determines the properties of an IP address.
 *
 * Takes user input to get an IP address. Splits and validates the string. Calculates each of
 * the other properties and reports them to the user.
 *
 * @author Justin Rhude
 */
public class IpClassifier {
    public static void main(String args[]) {
        while(true) {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter an IP address (enter 'exit' to exit): ");
            String stringIP = in.nextLine();

            IP ip = new IP();

            if (stringIP.toLowerCase().equals("exit")) break;

            try {
                SplitResponse splitSubnet = IpSplitter.splitSubnet(stringIP);
                if (splitSubnet != null){
                    stringIP = splitSubnet.remainingIP;
                    ip.validateAndSetSubnet(splitSubnet.response);
                }

                SplitResponse splitCIDR = IpSplitter.splitCIDR(stringIP);
                if (splitCIDR != null){
                    stringIP = splitCIDR.remainingIP;
                    ip.validateAndSetCIDR(splitCIDR.response[0]);
                }

                ip.validateAndSetIP(IpSplitter.splitIP(stringIP));

                System.out.println();
                System.out.println("That is a valid address: ");

                for (String propertyReport: ip.reportProperties()) {
                    System.out.println(propertyReport);
                }
                System.out.println();

            } catch (Exception e) {
                System.out.println();
                System.out.println("That was not a valid IP address: " + e.getMessage());
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("Bye!");
    }




}
