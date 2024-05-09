package sistemaHospedagem.entidades;

public class Quarto {
    private final int numero;

    private boolean ocupado;
    private boolean limpo;

    private boolean isReserved;

    public Quarto(int numero) {
        this.numero = numero;
        this.ocupado = false;
        this.limpo = true;
        this.isReserved = false;
    }

    public synchronized boolean estaDisponivel() {
        return !isReserved && limpo;
    }

    public synchronized void ocupar() {
        ocupado = true;
        limpo = false;
        isReserved = true;
    }

    public synchronized void desocupar() {
        ocupado = false;
        isReserved = false;
    }

    public synchronized void limpar() {
        limpo = true;
    }

    public synchronized boolean foiLimpo() {
        return limpo;
    }

    // FUNÇÃO PARA O HÓSPEDE SAIR DO QUARTO TEMPORARIAMENTE
    public synchronized void sair() {
        this.ocupado = false;
    }

    public synchronized void voltarAoQuarto() {
        this.ocupado = true;
    }


    public boolean getOcupado() {
        return ocupado;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidade() {
        return 4;
    }
}
