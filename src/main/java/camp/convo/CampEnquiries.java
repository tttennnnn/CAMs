package camp.convo;

import java.util.ArrayList;

public class CampEnquiries {
    private ArrayList<Enquiry> enquiries;
    public CampEnquiries(ArrayList<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }
    public ArrayList<Enquiry> getEnquiries() { return enquiries; }
}