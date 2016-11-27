package org.kll.app.meromapv1.Model;

import android.icu.text.IDNA;

/**
 * Created by Rahul Singh Maharjan on 11/25/16.
 * Project for Kathmandu Living Labs
 */

public class Information {
    private long infoID;
    private String infoName;
    private String infoDescription;

    public Information(long infoID, String infoName, String infoDescription)
    {
        this.infoName = infoName;
        this.infoDescription = infoDescription;
        this.infoID = infoID;
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


}
