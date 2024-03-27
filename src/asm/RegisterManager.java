package asm;

public class RegisterManager {
    private final boolean[] registers;
    private int highestUsedRegister;

    public RegisterManager() {
        registers = new boolean[10];
        highestUsedRegister = -1;
    }

    public int borrowRegister() {
        for (int i = 0; i < registers.length; i++) {
            if (!registers[i]) {
                registers[i] = true;
                highestUsedRegister = Math.max(highestUsedRegister, i);
                return i;
            }
        }
        throw new RuntimeException("No more registers available");
    }

    public void freeRegister(int register) {
        registers[register] = false;
    }

    public int getHighestUsedRegister() {
        return highestUsedRegister;
    }

    public void setHighestUsedRegister(int highestUsedRegister) {
        this.highestUsedRegister = highestUsedRegister;
    }
}
