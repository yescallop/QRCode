package cn.yescallop.qrcode;

@FunctionalInterface
public interface MatrixConsumer {
    void accept(int x, int y, boolean b);
}