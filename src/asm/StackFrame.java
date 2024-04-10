package asm;

import java.util.ArrayList;
import java.util.List;

public class StackFrame {
    private RegisterManager registerManager;
    private String name;
    private StringBuilder buffer;
    private StringBuilder endBuffer;
    private StringBuilder startBuffer;
    private ArrayList<StringBuilder> bufferList;

    public StackFrame(String name) {
        this.registerManager = new RegisterManager();
        this.name = name;
        this.buffer = new StringBuilder();
        this.endBuffer = new StringBuilder();
        this.startBuffer = new StringBuilder();
        bufferList = new ArrayList<StringBuilder>();
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public String getName() {
        return name;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public StringBuilder getEndBuffer() {
        return endBuffer;
    }

    public StringBuilder getStartBuffer() {
        return startBuffer;
    }

    private String bufferListToString() {
        StringBuilder bufferListString = new StringBuilder();
        for (StringBuilder buffer : bufferList) {
            bufferListString.append(buffer.toString());
            bufferListString.append(registerManager.getHighestUsedRegister());
        }
        return bufferListString.toString();
    }

    public void needRegisterValue() {
        bufferList.add(buffer);
        buffer = new StringBuilder();
    }

    public String toString(boolean isMain) {
        if (isMain) {
            return  startBuffer.toString() + name + "\n" +
                    "\tmov\tr11, r13\n" +
                    buffer.toString() +
                    "end\n\n";
        } else {
            return  name + "\t;Beginning of " + name + "\n" +
                    "\tstmfd\t" + registerManager.generateStmfd() + "\n" +
                    bufferListToString() + buffer.toString() + endBuffer.toString() +
                    "\tldmfd\t" + registerManager.generateLdmfd() + "\n\n";
        }
    }
}
