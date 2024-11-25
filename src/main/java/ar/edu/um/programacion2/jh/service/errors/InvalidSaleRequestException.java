package ar.edu.um.programacion2.jh.service.errors;

public class InvalidSaleRequestException extends RuntimeException {

    public InvalidSaleRequestException(String message) {
        super(message);
    }
}
