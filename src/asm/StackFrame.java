package asm;

public class StackFrame {
    private RegisterManager registerManager;
    private String name;
    private StringBuilder buffer;
    private StringBuilder endBuffer;
    private StringBuilder startBuffer;

    public StackFrame(String name) {
        this.registerManager = new RegisterManager();
        this.name = name;
        this.buffer = new StringBuilder();
        this.endBuffer = new StringBuilder();
        this.startBuffer = new StringBuilder();
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

    public String toString(boolean isMain) {
        if (isMain) {
            return  startBuffer.toString() + name + "\n" +
                    "\tmov\tr11, r13\n" +
                    buffer.toString() +
                    "end\n\n";
        } else {
            return  name + "\t;Beginning of " + name + "\n" +
                    "\tstmfd\t" + registerManager.generateStmfd() + "\n" +
                    buffer.toString() + endBuffer.toString() +
                    "\tldmfd\t" + registerManager.generateLdmfd() + "\n\n";
        }
    }
}
