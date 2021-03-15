package nl.tue.group2.Warranteed;

public class Receipt {

    // Variable to store data corresponding
    // to product name in database
    private String product;

    // Variable to store data corresponding
    // to lastname keyword in database
    private String date;

    // Variable to store data corresponding
    // to state keyword in database
    private String state;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public Receipt() {}

    // Getter and setter method
    public String getProduct()
    {
        return product;
    }
    public void setProduct(String name)
    {
        this.product = name;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String warranty_date)
    {
        this.date = warranty_date;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String warranty_state)
    {
        this.state = warranty_state;
    }
}
