package asm;

public class StackFrame {
    private RegisterManager registerManager;
    private String name;
    private StringBuilder buffer;

    public StackFrame(String name) {
        this.registerManager = new RegisterManager();
        this.name = name;
        this.buffer = new StringBuilder();
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

    public String toString(boolean isMain) {
        if (isMain) {
            return name + "\n" +
                    "\tmov\tr11, r13\n" +
                    buffer.toString() +
                    "end\n\n";
        } else {
            return name + "\t;Beginning of " + name + "\n" +
                    "\tstmfd\t" + registerManager.generateStmfd() + "\n" +
                    "\tmov\tr11, r13\n" +
                    buffer.toString() +
                    "\tmov\tr13, r11\n" +
                    "\tldmfd\t" + registerManager.generateLdmfd() + "\n\n";
        }
    }
}
