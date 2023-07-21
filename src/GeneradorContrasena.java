import java.util.Random;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorContrasena {
    char desplazamiento = 3;
    public static void main(String[] args) {
        //arreglo de caracteres
        String[] conjuntoCaracteres = {
            "abcdefghijklmnopqrstuvwxyz",  
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",  
            "+*-¿?%$#@/!=",
            "123456789" 
        };

        try {
            Scanner sc = new Scanner(System.in);
            Utils.outNoln("Bienvenido, Por favor digita tu nombre: ");
            String nombre = sc.nextLine();

            boolean incluirMinusculas = false;
            boolean incluirMayusculas = false;
            boolean incluirEspeciales = false;
            boolean incluirNumeros = false;
            
            //Parametros para generar la contraseña
            Utils.outNoln(nombre + " desea incluir letras en mayúsculas? (S/N): ");
            incluirMayusculas = obtenerRespuesta(sc, nombre);

            Utils.outNoln(nombre +" desea incluir letras en minúsculas? (S/N): ");
            incluirMinusculas = obtenerRespuesta(sc, nombre);

            Utils.outNoln(nombre + " desea incluir caracteres especiales? (S/N): ");
            incluirEspeciales = obtenerRespuesta(sc, nombre);

            Utils.outNoln(nombre + " desea incluir números? (S/N): ");
            incluirNumeros = obtenerRespuesta(sc, nombre);

            String contrasena = generarContrasena(conjuntoCaracteres, incluirMinusculas, incluirMayusculas, incluirEspeciales, incluirNumeros, nombre);

            Utils.out("Contraseña generada: " + contrasena);

            //Encriptar la contraseña
            Utils.outNoln(nombre + " desea encriptar la contraseña? (S/N): ");
            boolean encriptar = obtenerRespuesta(sc, nombre);

            String contrasenaEncriptada = null;
            if (encriptar) {
                contrasenaEncriptada = encriptarContrasena(contrasena);
                Utils.out("Contraseña encriptada: " + contrasenaEncriptada);
            }

            //El codigo recupera Contraseña
            if (contrasenaEncriptada != null) {
                Utils.outNoln(nombre + " desea recuperar la contraseña original? (S/N): ");
                boolean recuperar = obtenerRespuesta(sc, nombre);

                if (recuperar) {
                    Utils.outNoln(nombre + " por favor ingrese la contraseña encriptada: ");
                    String contrasenaEncriptadaIngresada = sc.nextLine().trim();

                    if (contrasenaEncriptadaIngresada.equals(contrasenaEncriptada)) {
                        String contrasenaOriginal = desencriptarContrasena(contrasenaEncriptada);
                        Utils.out("La contraseña original es: " + contrasenaOriginal);
                    } else {
                        Utils.out("La contraseña ingresada no coincide con la contraseña encriptada.");
                    }
                }
            }

            Utils.outNoln(nombre + " desea que se le genere un archivo con la contraseña? (S/N)");
            String incluirArchivos = sc.nextLine().trim().toUpperCase();

            if (incluirArchivos.equals("S")) {
                generadorArchivo(contrasena);
            }

        } catch (Exception e) {
            Utils.out("Error al generar la contraseña o al generar el archivo.");
        }
    }

    // El código agarra del arreglo los strings para la contraseña
    public static String generarContrasena(String[] conjuntoCaracteres, boolean incluirMinusculas, boolean incluirMayusculas, boolean incluirEspeciales, boolean incluirNumeros, String nombre) throws Exception {
        StringBuilder caracteres = new StringBuilder();
        if (incluirMinusculas) {
            caracteres.append(conjuntoCaracteres[0]);
        }
        if (incluirMayusculas) {
            caracteres.append(conjuntoCaracteres[1]);
        }
        if (incluirEspeciales) {
            caracteres.append(conjuntoCaracteres[2]);
        }
        if (incluirNumeros) {
            caracteres.append(conjuntoCaracteres[3]);
        }

        Scanner sc = new Scanner(System.in);
        int longitudContrasena;
        do {
            try {
                Utils.outNoln(nombre + " por favor ingrese la longitud deseada de la contraseña (entre 8 y 20): ");
                longitudContrasena = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                throw new Exception("Error " + nombre + " debe ingresar el valor de la longitud entre (8 y 20). ");
            }
        } while (longitudContrasena < 8 || longitudContrasena > 20);

        // Este random genera la contraseña del arreglo generado
        Random random = new Random();
        StringBuilder contrasenaGenerada = new StringBuilder();
        for (int i = 0; i < longitudContrasena; i++) {
            int conjuntoIndex = random.nextInt(caracteres.length());
            String conjunto = caracteres.substring(conjuntoIndex, conjuntoIndex + 1);
            int caracterIndex = random.nextInt(conjunto.length());
            contrasenaGenerada.append(conjunto.charAt(caracterIndex));
        }

        return contrasenaGenerada.toString();
    }

    // Este código genera el error ante una respuesta inválida
    private static boolean obtenerRespuesta(Scanner sc, String nombre) throws Exception {
        String respuesta = "";
        do {
            respuesta = sc.nextLine().trim().toUpperCase();
            if (!respuesta.equals("S") && !respuesta.equals("N")) {
                Utils.out(nombre + " debes ingresar solo 'S' o 'N' para responder.");
            }
        } while (!respuesta.equals("S") && !respuesta.equals("N"));

        return respuesta.equals("S");
    }

    // Este código genera el archivo.
    private static void generadorArchivo(String contrasena) throws IOException {

        String nombreArchivo = "Contraseña.txt";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nombreArchivo))) {
            bufferedWriter.write(contrasena);
            Utils.out("La contraseña fue escrita en el archivo con el nombre: " + nombreArchivo);
        } catch (IOException e) {
            Utils.out("Error al escribir el archivo: " + e.getMessage());
        }
    }

    // El codigo encripta la contraseña
    static char desplasamiento = 3;
    public static String encriptarContrasena(String contrasena) {
        StringBuilder contrasenaEncriptada = new StringBuilder();
        for (int i = 0; i < contrasena.length(); i++) {
            char c = contrasena.charAt(i);
            contrasenaEncriptada.append((char) (c + desplasamiento)); 
        }
        return contrasenaEncriptada.toString();
    }

    // El codigo desencripta la contraseña encriptada
    public static String desencriptarContrasena(String contrasenaEncriptada) { 
        StringBuilder contrasenaOriginal = new StringBuilder();
        for (int i = 0; i < contrasenaEncriptada.length(); i++) {
            char c = contrasenaEncriptada.charAt(i);
            contrasenaOriginal.append((char) (c - desplasamiento)); 
        }
        return contrasenaOriginal.toString();     
    }
}
