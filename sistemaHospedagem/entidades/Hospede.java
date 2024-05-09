package sistemaHospedagem.entidades;

import java.util.Random;

@SuppressWarnings("unused")
  
public class Hospede implements Runnable {
    private final Hotel hotel;
    private final String nome;
    private final int tamanhoGrupo;
    private int hospedesSemQuarto;
    private Chave chave;
    private int numeroQuarto;
    private int quantidadeTentativas;

    public Hospede(Hotel hotel, String nome, int tamanhoGrupo) {
        this.hotel = hotel;
        this.nome = nome;
        this.tamanhoGrupo = tamanhoGrupo;
        this.hospedesSemQuarto = tamanhoGrupo;
        this.quantidadeTentativas = 0;
    }

    @Override
    public void run() {
        Random random = new Random();
        Quarto quarto = null;
        try {
            Thread.sleep(random.nextInt(5000));

            alocarHospedeNoQuarto(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void alocarHospedeNoQuarto(Random random) throws InterruptedException {
        Quarto quarto = hotel.getQuartoDisponivel();

        if (quarto != null) {
            ocuparQuarto(quarto, random);

            if (this.hospedesSemQuarto <= 0) {
                return;
            } else {
                alocarHospedeNoQuarto(random);
            }
        } else {
            this.quantidadeTentativas += 1;

            if (quantidadeTentativas >= 2) {
                System.out.println("Hóspede " + this.nome + " não conseguiu um quarto e saiu reclamando");
                this.hotel.getHospedes().remove(this);

                return;
            } else {
                hotel.adicionarHospedeNaListaEspera(this);
                System.out.println(nome + " adicionado à lista de espera do hotel");
            }
        }
    }
}
