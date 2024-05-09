package sistemaHospedagem;

import sistemaHospedagem.entidades.Camareira;
import sistemaHospedagem.entidades.Hospede;
import sistemaHospedagem.entidades.Hotel;
import sistemaHospedagem.entidades.Recepcionista;

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();

        // Criando as threads
        for (Camareira camareira : hotel.getCamareiras()) {
            new Thread(camareira).start();
        }

        for (Recepcionista recepcionista : hotel.getRecepcionistas()) {
            new Thread(recepcionista).start();
        }

        for (Hospede hospede : hotel.getHospedes()) {
            new Thread(hospede).start();
        }
    }
}
