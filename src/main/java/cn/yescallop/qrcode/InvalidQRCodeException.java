package cn.yescallop.qrcode;

public class InvalidQRCodeException extends RuntimeException {

    public InvalidQRCodeException(String message) {
        super(message);
    }
}
