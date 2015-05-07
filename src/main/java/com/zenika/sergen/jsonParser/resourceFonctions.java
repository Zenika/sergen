package com.zenika.sergen.jsonParser;

/**
 * Created by Zenika on 06/05/2015.
 */
public class ResourceFonctions {
    private String returnType;
    private String fonctionName;
    private String fonctionBody;
    private String fonctionPath;
    private String fonctionProduces;
    private String fonctionsConsumes;
    private String typeOfRequete;

    public String getFonctionName() {
        return fonctionName;
    }

    public void setFonctionName(String fonctionName) {
        this.fonctionName = fonctionName;
    }

    public String getFonctionBody() {
        return fonctionBody;
    }

    public void setFonctionBody(String fonctionBody) {
        this.fonctionBody = fonctionBody;
    }

    public String getFonctionPath() {
        return fonctionPath;
    }

    public void setFonctionPath(String fonctionPath) {
        this.fonctionPath = fonctionPath;
    }

    public String getFonctionProduces() {
        return fonctionProduces;
    }

    public void setFonctionProduces(String fonctionProduces) {
        this.fonctionProduces = fonctionProduces;
    }

    public String getFonctionsConsumes() {
        return fonctionsConsumes;
    }

    public void setFonctionsConsumes(String fonctionsConsumes) {
        this.fonctionsConsumes = fonctionsConsumes;
    }

    public String getTypeOfRequete() {
        return typeOfRequete;
    }

    public void setTypeOfRequete(String typeOfRequete) {
        this.typeOfRequete = typeOfRequete;
    }

    public String getReturnType() {

        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return getReturnType()+", "+ getFonctionName()+", "+ getFonctionBody()+", "+"," +
                ""+getFonctionPath()+", "+getFonctionProduces()+","+getFonctionsConsumes()+"," +getTypeOfRequete()
                ;
    }
}
