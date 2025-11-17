package model;

public class User {
	
	private String nombreUsuario; // Identificador de Login
	private String nombre;
	private String apellido;
	private String correo;
	private String contraseñaHash;
	private String id; // Identificador Único (Cédula)
	private String ubicacion; 
	
	// Método constructor
	public User(String nombreUsuario, String nombre, String apellido, String correo, String contraseñaHash, String id, String ubicacion) {
		this.nombreUsuario = nombreUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.contraseñaHash = contraseñaHash;
		this.id = id;
		this.ubicacion = ubicacion;
	}

	// Getters y Setters
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContraseñaHash() {
		return contraseñaHash;
	}

	public void setContraseñaHash(String contraseñaHash) {
		this.contraseñaHash = contraseñaHash;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUbicacion() {
		return ubicacion;
	}
	
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
}