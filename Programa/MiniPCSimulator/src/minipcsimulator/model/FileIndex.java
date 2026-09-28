package minipcsimulator.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileIndex {
    String fileName;
    int startPosition;
    int size;

    public FileIndex(String fileName, int startPosition, int size) {
        this.fileName = fileName;
        this.startPosition = startPosition;
        this.size = size;
    }

    public FileIndex(String register) {
        //FORMATO: [INDEX] File: nombreArchivo.asm | StartAddress: 0 | Size: 0
        //regexp
        Pattern pattern = Pattern.compile("\\[INDEX\\] File: (.+) \\| StartAddress: (\\d+) \\| Size: (\\d+)");
        Matcher matcher = pattern.matcher(register);
        if (matcher.find()) {
            this.fileName = matcher.group(1);
            this.startPosition = Integer.parseInt(matcher.group(2));
            this.size = Integer.parseInt(matcher.group(3));
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getSize() {
        return size;
    }

    public String toString() {
        return "[INDEX] File: " + fileName + " | StartAddress: " + startPosition + " | Size: " + size;
    }
}
