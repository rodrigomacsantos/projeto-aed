package pt.ulusofona.aed.deisiworldmeter;

public class Cidade {
    private String alfa2;
    private String nome;
    private Integer regiao;
    private Double popul;
    private Double latitude;
    private Double longitude;

    public Cidade(String alfa2, String nome, Integer regiao, Double popul, Double latitude, Double longitude) {
        this.alfa2 = alfa2;
        this.nome = nome;
        this.regiao = regiao;
        this.popul = popul;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAlfa2() {
        return alfa2;
    }

    public String getNome() {
        return nome;
    }

    public Integer getRegiao() {
        return regiao;
    }

    public Double getPopul() {
        return popul;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    private String fmtDouble(Double d) {
        if (d == null) {
            return "";
        }
        if (d.equals(Math.floor(d))) {
            return String.format("%.1f", d);
        } else {
            return d.toString();
        }
    }

    @Override
    public String toString() {
        String latStr = fmtDouble(latitude);
        String lonStr = fmtDouble(longitude);
        String alfa2Up = (alfa2 == null) ? "" : alfa2.toUpperCase();
        String populStr = (popul == null) ? "" : String.valueOf(popul.intValue());
        String regiaoStr = (regiao == null) ? "" : regiao.toString();
        return nome + " | " + alfa2Up + " | " + regiaoStr + " | " + populStr + " | (" + latStr + "," + lonStr + ")";
    }
}
