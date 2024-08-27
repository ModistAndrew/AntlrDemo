package modist.antlrdemo.backend.metadata;

public enum Register {
    ZERO, RA, SP, GP, TP,
    T0, T1, T2,
    S0, S1,
    A0, A1, A2, A3, A4, A5, A6, A7,
    S2, S3, S4, S5, S6, S7, S8, S9, S10, S11,
    T3, T4, T5, T6;

    public static final int BYTE_SIZE = 4;
    public static final Register[] TEMP_REGISTERS = {T0, T1, T2, T3, T4, T5, T6};
    public static final Register[] ARG_REGISTERS = {A0, A1, A2, A3, A4, A5, A6, A7};
    private static int tempRegisterIndex = 0;

    public static Register newTempRegister() {
        return TEMP_REGISTERS[tempRegisterIndex++];
    }

    public static void resetTempRegisters() {
        tempRegisterIndex = 0;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
