package asm.visual;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
public final class OutputParser {
    private OutputParser() {
    }

    private static boolean hexStringToByteArray(ByteArrayOutputStream bytes, String hex) {
        String hexValue = hex.substring(2);
        int l = hexValue.length();
        if (l % 2 == 1) {
            /* pad to a pair number of hex digits */
            hexValue = "0" + hexValue;
            l++;
        }
        for (int i = l - 2; i >= 0; i -= 2) {
            int currentByte = (Character.digit(hexValue.charAt(i), 16) << 4)
                    + Character.digit(hexValue.charAt(i + 1), 16);
            if (currentByte == 0) return false;
            bytes.write(currentByte);
        }
        return true;
    }

    private static String parseLine(Node line) {
        NodeList children = line.getChildNodes();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        for (int j = 0; j < children.getLength(); j++) {
            Node child = children.item(j);
            if (child.getNodeName().equals("word")) {
                boolean more = hexStringToByteArray(bytes, child.getTextContent());
                if (!more) break;
            }
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    public static List<String> parseOutput(String XMLPath) {
        List<String> outputs = new ArrayList<>();
        try {
            File XMLFile = new File(XMLPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFile);
            doc.getDocumentElement().normalize();
            NodeList lines = doc.getElementsByTagName("line");
            for (int i = 0; i < lines.getLength(); i++) {
                Node line = lines.item(i);
                outputs.add(parseLine(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputs;
    }
}