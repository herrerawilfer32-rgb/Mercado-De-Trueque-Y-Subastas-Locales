package persistence;

import java.io.*;

public class Persistencia {

    /**
     * Guarda un objeto serializable en la ruta especificada.
     * 
     * @param ruta   Ruta del archivo (ej. "users.dat")
     * @param objeto Objeto a guardar (debe implementar Serializable)
     * @throws IOException Si ocurre un error de escritura
     */
    public static void guardarObjeto(String ruta, Object objeto) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(objeto);
        }
    }

    /**
     * Carga un objeto desde la ruta especificada.
     * 
     * @param ruta Ruta del archivo
     * @return El objeto cargado
     * @throws IOException            Si ocurre un error de lectura
     * @throws ClassNotFoundException Si la clase del objeto no se encuentra
     */
    public static Object cargarObjeto(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return ois.readObject();
        }
    }
}
