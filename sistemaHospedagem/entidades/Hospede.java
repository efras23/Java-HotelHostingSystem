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

    private void ocuparQuarto(Quarto quarto, Random random) throws InterruptedException {
        this.hospedesSemQuarto -= quarto.getCapacidade();
        
        synchronized (hotel.getTravaRecepcao()) {
            quarto.ocupar();
            this.chave = new Chave(this, quarto);
            this.numeroQuarto = quarto.getNumero();
            
            if (this.hospedesSemQuarto > 0) {
                System.out.println(nome + " ocupou o quarto " + quarto.getNumero() + " com um grupo de " + quarto.getCapacidade() + " hóspedes");
            } else {
                System.out.println(nome + " ocupou o quarto " + quarto.getNumero() + " com um grupo de " + this.tamanhoGrupo + " hóspedes");
            }
        }
        
        Thread.sleep(random.nextInt(2000)); // Tempo de permanência aleatório

        // Vai sair??
        if (random.nextBoolean()) {
            sairPorUmTempo(random);
        }
        
        Thread.sleep(random.nextInt(2000));

        synchronized (hotel.getTravaRecepcao()) {
            quarto.desocupar();
            System.out.println(nome + " desocupou o quarto " + quarto.getNumero());
        }
    }   
    
    // Passeio
    private void sairPorUmTempo(Random random) {
        System.out.println(nome + " saiu do quarto...");
        int index = this.hotel.getQuartos().indexOf(this.chave.getQuarto());
        Quarto quarto = this.hotel.getQuartos().get(index);
        quarto.sair();
        
        Recepcionista recepcionista = selecionarRecepcionista();
        
        recepcionista.receberChaveDoHospede(this);
        
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recepcionista.retornarChaveParaHospede(this);
        
        System.out.println(nome + " voltou ao quarto");
        quarto.voltarAoQuarto();
    }
    
    // Seleciona recepcionista aleatória
    private Recepcionista selecionarRecepcionista() {
        try {
            Random random = new Random();
            int i = random.nextInt(hotel.getRecepcionistas().size());
            return hotel.getRecepcionistas().get(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // GETTERS e SETTERS
    public String getNome() {
        return nome;
    }

    public int getTamanhoGrupo() {
        return tamanhoGrupo;
    }
    
    public void setChave(Chave chave) {
        this.chave = chave;
    }
    
    public Chave getChave() {
        return chave;
    }
    
    public int getNumeroQuarto() {
        return numeroQuarto;
    }
}
