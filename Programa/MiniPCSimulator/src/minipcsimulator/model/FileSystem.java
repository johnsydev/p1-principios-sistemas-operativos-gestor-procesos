package minipcsimulator.model;

import minipcsimulator.utils.SystemConfig;

/**
 * Clase que representa un sistema de archivos simple para el MiniPCSimulator.
 * Permite crear, abrir, leer, escribir y eliminar archivos en un disco simulado.
 */
public class FileSystem {
    public enum FileSystemReturnCode {
        SUCCESS, // ordinal = 0
        FILE_NOT_FOUND,
        FILE_ALREADY_EXISTS
    }

    private Disk disk;
    private int lastIndexPosition = -1;

    /**
     * Constructor de la clase FileSystem.
     * @param disk El disco simulado donde se almacenarán los archivos.
     */
    public FileSystem(Disk disk) {
        this.disk = disk;
    }

    /**
     * Crea un archivo con el nombre especificado en el sistema de archivos.
     * @param fileName El nombre del archivo a crear.
     * @return Un código de retorno que indica el resultado de la operación.
     */
    public int createFile(String fileName) {
        if (getFileIndex(fileName) != null) {
            return FileSystemReturnCode.FILE_ALREADY_EXISTS.ordinal();
        }

        int freeIndex = -1;
        int startPosition = -1;
        try {
            freeIndex = Loader.getFreeSpaceInFileIndex(disk);
            startPosition = Loader.getFreeSpaceInDisk(1, disk);
        } catch (Exception e) {
            System.out.println("Error al intentar crear el archivo: " + e.getMessage());
            return FileSystemReturnCode.FILE_NOT_FOUND.ordinal();
        }
        
        FileIndex fileIndex = new FileIndex(fileName, startPosition, 1);
        disk.setPosition(freeIndex, new MemoryRegister(fileIndex));
        disk.setPosition(startPosition, new MemoryRegister("", "")); //guardo el espacio para el contenido del archivo
        return FileSystemReturnCode.SUCCESS.ordinal();
    }

    public int openFile(String fileName) {
        FileIndex fileIndex = getFileIndex(fileName);
        if (fileIndex == null) {
            return FileSystemReturnCode.FILE_NOT_FOUND.ordinal();
        }
        return FileSystemReturnCode.SUCCESS.ordinal();
    }

    public int writeFile(String fileName, String content) {
        FileIndex fileIndex = getFileIndex(fileName);
        if (fileIndex != null) {
            disk.setPosition(fileIndex.getStartPosition(), new MemoryRegister(content, content));
            return FileSystemReturnCode.SUCCESS.ordinal();
        } else {
            return FileSystemReturnCode.FILE_NOT_FOUND.ordinal();
        }
    }

    public String readFile(String fileName) {
        FileIndex fileIndex = getFileIndex(fileName);
        if (fileIndex != null) {
            MemoryRegister contentRegister = disk.getPosition(fileIndex.getStartPosition());
            if (contentRegister != null) {
                return contentRegister.getValueString();
            }
        }
        return null;
    }

    public int deleteFile(String fileName) {
        for (int i = 0; i < SystemConfig.getStartDiskForPrograms(); i++) {
            MemoryRegister register = disk.getPosition(i);
            if (register != null && register.fileIndex != null && register.fileIndex.getFileName().equals(fileName)) {
                int dataPosition = register.fileIndex.getStartPosition();
                
                disk.setPosition(dataPosition, null); // Elimina contenido
                disk.setPosition(i, null);            // Elimina el índice de la tabla
                return FileSystemReturnCode.SUCCESS.ordinal();
            }
        }
        System.out.println("FileSystem Error: No se pudo eliminar. El archivo '" + fileName + "' no existe.");
        return FileSystemReturnCode.FILE_NOT_FOUND.ordinal();
    }

    private FileIndex getFileIndex(String fileName) {
        for (int i = 0; i < SystemConfig.getStartDiskForPrograms(); i++) {
            MemoryRegister register = disk.getPosition(i);
            if (register != null && register.fileIndex != null && register.fileIndex.getFileName().equals(fileName)) {
                lastIndexPosition = i;
                return register.fileIndex;
            }
        }
        return null;
    }
}
