package sistemaHospedagem.entidades;

public class Camareira implements Runnable {
    private final Hotel hotel;

    public Camareira(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (hotel.getTravaArrumacao()) {
                synchronized (hotel.getTravaRecepcao()) {
                    for (Quarto quarto : hotel.getQuartos()) {
                        if (!quarto.getOcupado() && !quarto.foiLimpo()) {
                            quarto.limpar();
                            System.out.println("Camareira limpou o quarto " + quarto.getNumero());
                        }
                    }
                }
            }
        }
    }
}