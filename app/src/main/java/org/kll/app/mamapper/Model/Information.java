package org.kll.app.mamapper.Model;



/**
 * Created by Rahul Singh Maharjan on 11/25/16.
 * Project for Kathmandu Living Labs
 */

public class Information {
    private long infoID;
    private String infoName;
    private String infoDescription;
    private int infoContact;
    private String infoOperator;


    public Information(long infoID, String infoName, String infoDescription
    ,int infoContact, String infoOperator)
    {
        this.infoName = infoName;
        this.infoDescription = infoDescription;
        this.infoID = infoID;
        this.infoContact = infoContact;
        this.infoOperator = infoOperator;
    }

    public Information()
    {

    }

    public long getInfoID()
    {
        return infoID;
    }

    public void setInfoID(long infoID)
    {
        this.infoID = infoID;
    }

    public String getInfoName()
    {
        return infoName;
    }
    public String getInfoDescription()
    {
        return infoDescription;
    }

    public void setInfoName(String infoName)
    {
        this.infoName = infoName;
    }
    public void setInfoDescription(String infoDescription)
    {
        this.infoDescription = infoDescription;
    }


    public int getInfoContact()
    {
        return infoContact;
    }

    public void setInfoContact(int infoContact)
    {
        this.infoContact = infoContact;
    }

    public String getInfoOperator()
    {
        return infoOperator;
    }

    public void setInfoOperator(String infoOperator)
    {
        this.infoOperator = infoOperator;
    }


}
