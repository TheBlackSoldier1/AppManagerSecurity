package models;

public class UserPass {

    private String idTarjeta;
    private String id;
    private String apuntes;
    private String pass;
    private String nombrePagina;
    private String nombreUsuario;

    // Constructor vacío necesario para Firebase
    public UserPass() {
    }

    // Constructor con todos los parámetros
    public UserPass(String idTarjeta, String id, String apuntes, String pass, String nombrePagina, String nombreUsuario) {
        this.idTarjeta = idTarjeta;
        this.id = id;
        this.apuntes = apuntes;
        this.pass = pass;
        this.nombrePagina = nombrePagina;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y Setters

    public String getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApuntes() {
        return apuntes;
    }

    public void setApuntes(String apuntes) {
        this.apuntes = apuntes;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombrePagina() {
        return nombrePagina;
    }

    public void setNombrePagina(String nombrePagina) {
        this.nombrePagina = nombrePagina;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}