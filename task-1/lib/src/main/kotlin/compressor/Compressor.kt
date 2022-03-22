package compressor

public interface Compressor<I : Iterable<Byte>> {

    public fun I.compress(): I
}
