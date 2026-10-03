package minipcsimulator.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase que se encarga de manejar la carga de archivos .asm.
 * @author johnsydev
 */
public class FileManager {
    /**
     * Carga un archivo .asm y devuelve sus líneas como un ArrayList de Strings.
     * @return Un ArrayList de Strings con las líneas del archivo .asm.
     */
    public static List<Map.Entry<ArrayList<String>, ArrayList<String>>> loadFiles() {
        return openFileExplorer();
    }

    /**
     * Abre el explorador de archivos y devuelve las líneas del archivo seleccionado como un ArrayList de Strings.
     * @return Un ArrayList de Strings con las líneas del archivo seleccionado.
     */
    private static List<Map.Entry<ArrayList<String>, ArrayList<String>>> openFileExplorer() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Ensamblador (*.asm)", "asm"));


        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File[] files = fileChooser.getSelectedFiles();
            
            if (files == null || files.length == 0) {
                throw new RuntimeException("No se seleccionó ningún archivo.");
            }

            List<Map.Entry<ArrayList<String>, ArrayList<String>>> filesData = new ArrayList<>();

            for (java.io.File file : files) {
                if (!isValidExtension(file.getAbsolutePath())) {
                    throw new RuntimeException("El archivo " + file.getName() + " no tiene la extensión .asm.");
                }
                
                ArrayList<String> fileData = new ArrayList<>();
                fileData.add(file.getAbsolutePath());
                fileData.add(file.getName());

                ArrayList<String> lines = readFileLines(file);
                filesData.add(Map.entry(fileData, lines));
            }
            
            return filesData;
        } else {
            throw new RuntimeException("No se seleccionó ningún archivo.");
        }
    }

    /**
     * Verifica si la extensión del archivo es válida (.asm).
     * @param filePath La ruta del archivo.
     * @return true si la extensión es válida, false en caso contrario.
     */
    public static boolean isValidExtension(String filePath) {
        return filePath.endsWith(".asm");
    }

    /**
     * Lee las líneas de un archivo y las devuelve como un ArrayList de Strings.
     * @param file El archivo a leer.
     * @return Un ArrayList de Strings con las líneas del archivo.
     */
    private static ArrayList<String> readFileLines(java.io.File file) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
