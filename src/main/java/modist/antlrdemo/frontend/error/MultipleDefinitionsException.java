package modist.antlrdemo.frontend.error;

import modist.antlrdemo.frontend.semantic.Symbol;

public class MultipleDefinitionsException extends CompileException {
    public MultipleDefinitionsException(Symbol symbol, Symbol previous) {
        super(String.format("Symbol '%s' redefined, previous definition at [%s]", symbol.name, previous.position), symbol.position);
    }

    @Override
    public String getErrorType() {
        return "Multiple Definitions";
    }
}
