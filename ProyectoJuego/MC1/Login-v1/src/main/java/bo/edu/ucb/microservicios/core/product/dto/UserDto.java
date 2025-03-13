package bo.edu.ucb.microservicios.core.product.dto;

import java.util.Date;

public class UserDto {
  //  private Long id;
    private String uid;
    private String nombre;
    private String apellido; 
    private String rol;
    private String correo;
    private String password;
    private Date fechaCreacion;

    public UserDto() {
        super();
    }

    public UserDto( String uid, String nombre, String apellido, String rol, String correo, String password, Date fechaCreacion) {
        super();
       // this.id = id;
        this.uid = uid;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.correo = correo;
        this.password = password;
        this.fechaCreacion = fechaCreacion;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
    

