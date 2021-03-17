package nl.tue.group2.Warranteed;

public class Receipt {

    //variables for holding results
    private String product;
    private String expiration_date;
    private String state;
    private String purchase_date;
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
    public String getExpiration_date()
    {
        return expiration_date;
    }
    public void setExpiration_date(String warranty_date)
    {
        this.expiration_date = warranty_date;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String warranty_state)
    {
        this.state = warranty_state;
    }
    public String getPurchase_date()
    {
        return purchase_date;
    }
    public void setPurchase_date(String date)
    {
        this.purchase_date = date;
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
