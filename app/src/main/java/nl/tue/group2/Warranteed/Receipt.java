package nl.tue.group2.Warranteed;

public class Receipt {

    //variables for holding results
    private String product;
    private String date;
    private String state;
    private String pdate;
    private String duration;

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
    public String getPdate()
    {
        return pdate;
    }
    public void setPdate(String date)
    {
        this.pdate = date;
    }

    public String getDuration()
    {
        return duration;
    }
    public void setDuration(String years)
    {
        this.duration = years;
    }
}
