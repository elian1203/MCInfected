package net.urbanmc.mcinfected.object.string;

public class SingleInputString {
    public final static SingleInputString EMPTY = new SingleInputString("");

    private final String prefix;
    private final String suffix;

    public SingleInputString(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public SingleInputString(String prefix) {
        this(prefix, "");
    }

    public String build(String input) {
        return prefix + input + suffix;
    }

    public String build(int i) {
        return build(String.valueOf(i));
    }
}
