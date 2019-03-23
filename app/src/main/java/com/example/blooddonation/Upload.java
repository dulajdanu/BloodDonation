package com.example.blooddonation;

public class Upload {
    private String imgName;
    private String imgUri;

    public Upload()
    {

    }

    public Upload(String imgName, String imgUri) {

        if(imgName.trim().equals(""))
        {
            imgName = "No name ";
        }

        this.imgName = imgName;
        this.imgUri = imgUri;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
