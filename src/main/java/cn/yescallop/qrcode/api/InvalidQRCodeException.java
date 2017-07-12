package cn.yescallop.qrcode.api;

public class InvalidQRCodeException extends RuntimeException {

    public InvalidQRCodeException(String message) {
        super(message);
    }
}
