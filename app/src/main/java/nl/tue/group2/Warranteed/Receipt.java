package nl.tue.group2.Warranteed;

public class Receipt {

    //variables for holding results
    private String name;
    private String name_insensitive;
    private String product;
    private String expiration_date;
    private String state;
    private String purchase_date;
    private String duration;
    private String image;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public Receipt() {}

    // Getter and setter method
    public String getProduct()
    {
        return product;
    }
    public void setProduct(String product)
    {
        this.product = product;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getNameInsensitive() {return name_insensitive;}
    public void setNameInsensitive(String name) {this.name_insensitive = name;}
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
        String duration = getDuration();
        int time =  Integer.parseInt(duration.replaceAll("\\D+",""));
        if (duration.contains("days")){
            this.state = "Expiring";
        } else if (time <= 0){
            this.state = "Void";
        } else {
            this.state = "Valid";
        }

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
    public String getImage()
    {
        return image;
    }
    public void setImage(String picture)
    {
        this.image = picture;
    }
}
