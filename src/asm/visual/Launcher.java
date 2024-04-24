package asm.visual;

import java.util.List;
import java.util.stream.Stream;

import visual.EmulatorLogFile;
import visual.HeadlessController;
public class Launcher {
    public static final int outputBufferLength = 0x100;
    private static final int instMemSize = 0x1000;
    /* we make sure that the output buffer is always the first symbol in memory */
    private static final int outputBufferAddress = instMemSize;
    /* VisUAL offsets line numbers by one for some reason */
    private static final List<Integer> breakpoints = List.of(20, 142);
    private Launcher() {}
    /* array of all word addresses in the output buffer */
    private static String[] getOutputRange() {
        return Stream.iterate(outputBufferAddress, n -> n + 4)
                .limit(outputBufferLength / 4)
                .map(n -> String.format("0x%X", n))
                .toArray(String[]::new);
    }
    public static void executeAndParseOutput(String assemblyFile) {
        EmulatorLogFile.configureLogging("", true, true, false, false, false, false,
                true, false, getOutputRange());
        HeadlessController.setLogMode(EmulatorLogFile.LogMode.BREAKPOINT);
        HeadlessController.setBreakpoints(breakpoints);
        HeadlessController.setInstMemSize(instMemSize);
        String logFile = String.format("%s_log.xml", assemblyFile);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            List<String> output = OutputParser.parseOutput(logFile);
            System.out.println("---- PROGRAM OUTPUT ----");
            output.forEach(System.out::print);
            System.out.println("---- END PROGRAM OUTPUT ----");
        }));

        HeadlessController.runFile(assemblyFile, logFile);
    }
    public static void run(String assemblyFile) {
        executeAndParseOutput(assemblyFile);
    }
}