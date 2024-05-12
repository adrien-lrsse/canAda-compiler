package asm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StackFrame {
    private RegisterManager registerManager;
    private String name;
    private StringBuilder beforeVarBuffer;
    private boolean varBufferSwitch = false;
    private StringBuilder varBuffer;
    private ArrayList<StringBuilder> varBufferList;
    private StringBuilder buffer;
    private StringBuilder endBuffer;
    private StringBuilder startBuffer;
    private ArrayList<StringBuilder> bufferList;
    private boolean isVarGen = false;

    public StackFrame(String name) {
        this.registerManager = new RegisterManager();
        this.name = name;
        this.beforeVarBuffer = new StringBuilder();
        this.varBuffer = new StringBuilder();
        this.buffer = new StringBuilder();
        this.endBuffer = new StringBuilder();
        this.startBuffer = new StringBuilder();
        bufferList = new ArrayList<StringBuilder>();
        varBufferList = new ArrayList<StringBuilder>();
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

    public StringBuilder getVarBuffer() {
        return varBuffer;
    }

    public void switchVarBuffer() {
        if(!varBufferSwitch) {
            varBufferSwitch = true;
            beforeVarBuffer = buffer;
            buffer = new StringBuilder();
        }
    }

    public void setIsVarGen(boolean isVarGen) {
        this.isVarGen = isVarGen;
    }

    private String bufferListToString() {
        StringBuilder bufferListString = new StringBuilder();
        for (StringBuilder buffer : bufferList) {
            bufferListString.append(buffer.toString());
            bufferListString.append(registerManager.getHighestUsedRegister());
        }
        return bufferListString.toString();
    }

    private String varBufferListToString() {
        StringBuilder varBufferListString = new StringBuilder();
        for (StringBuilder varBuffer : varBufferList) {
            varBufferListString.append(varBuffer.toString());
            varBufferListString.append(registerManager.getHighestUsedRegister());
        }
        return varBufferListString.toString();
    }

    public void needRegisterValue() {
        if(isVarGen) {
            varBufferList.add(varBuffer);
            varBuffer = new StringBuilder();
        } else {
            bufferList.add(buffer);
            buffer = new StringBuilder();
        }
    }

    public String toString(boolean isMain) {
        if (isMain) {
            return  startBuffer.toString() + name + "\n" +
                    "\tmov\tr11, r13\n" +
                    beforeVarBuffer.toString() +
                    varBufferListToString() +
                    varBuffer.toString() +
                    buffer.toString() +
                    "end\n\n";
        } else {
            return  name + "\t;Beginning of " + name + "\n" +
                    "\tstmfd\t" + registerManager.generateStmfd() + "\n" +
                    beforeVarBuffer.toString() +
                    varBufferListToString() +
                    varBuffer.toString() +
                    bufferListToString() +
                    buffer.toString() + endBuffer.toString() +
                    "\tldmfd\t" + registerManager.generateLdmfd() + "\n\n";
        }
    }
}
