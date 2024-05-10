# AUTORES: 
- Efraim Costa
- Mariana Guarino
- Cauã Nunes
- Arthur Alves 

# DESCRIÇÃO

Este é um sistema de reserva e controle de quartos para um hotel. Ele foi projetado para ajudar na gestão 
eficiente dos quartos do hotel, permitindo que os clientes façam reservas de quarto e que os funcionários 
do hotel controlem o status dos quartos.
____________________________________________________________________________________________________________________________________

# FUNCIONALIDADES

## CLASSE HOTEL

A entiade hotel apresenta a implementação de um sistema de hospedagem, onde são gerenciados os quartos, hóspe
des, camareiras e recepcionistas.O sistema utiliza threads para simular o funcionamento simultâneo dos diferen
tes atores do hotel.

### CÓDIGO

'public class Hotel {
    private static final int NUM_QUARTOS = 10;
    private static final int NUM_HOSPEDES = 50;
    private static final int NUM_Camareiras = 10;
    private static final int NUM_RECEPCIONISTAS = 5;
}
'
____________________________________________________________________________________________________________________________________

## CLASSE QUARTO 

Cada quarto tem um número único de identificação. O estado de um quarto pode variar entre ocupado e desocupado, 
indicando se está atualmente sendo utilizado por hóspedes ou não. Além disso, um quarto pode estar limpo ou sujo, 
refletindo se foi higienizado e está prontopara receber novos hóspedes. Um quarto também pode ser reservado, indi
cando que está temporariamente alocado para um hóspede que ainda não chegou.

### CONSTRUTOR 
Quarto(int numero): É o construtor da classe. Recebe como parâmetro o número do quarto e inicializa os atributos 
numero, ocupado, limpo e isReserved. Por padrão, um quarto é criado desocupado, limpo e não reservado.


### CÓDIGO 

public Quarto(int numero) {
    this.numero = numero;
    this.ocupado = false;
    this.limpo = true;
    this.isReserved = false;
}
_____________________________________________________________________________________________________________________________________

## CLASSE CHAVE 

A classe Chave desempenha um papel crucial no sistema de hospedagem, pois é responsável por estabelecer a associação
direta entre um hóspede e um quarto específico. Aqui está uma explicação mais detalhada

### ATRIBUTOS
quarto: Um objeto do tipo Quarto, que representa o quarto associado à chave.
hospede: Um objeto do tipo Hospede, que representa o hóspede associado à chave.

### CONSTRUTOR
Chave(Hospede hospede, Quarto quarto): É o construtor da classe. Recebe como parâmetros um objeto Hospede e um objeto 
Quarto e associa-os à chave. A chave é criada para representar a relação entre o hóspede e o quarto.


### CÓDIGO

public class Chave {
    private Quarto quarto;
    private Hospede hospede;
    public Chave(Hospede hospede, Quarto quarto) {
        this.quarto = quarto;
    }
    public Quarto getQuarto() {
        return quarto;
    }
    public Hospede getHospede() {
        return hospede;
    }
}
________________________________________________________________________________________________________________________________________

## CLASSE RECEPCIONISTA 

A classe Recepcionista desempenha um papel importante no sistema de hospedagem do hotel, sendo responsável por gerenciar o check-in 
e check-out dos hóspedes. Além disso, ela lida com a alocação e desalocação das chaves dos quartos.

### CONSTRUTOR 
Recepcionista(Hotel hotel): Este é o construtor da classe Recepcionista. Recebe como parâmetro um objeto Hotel e o associa ao recep
cionista.

MÉTODO run():Este método é sobrescrito da interface Runnable e implementa o comportamento do recepcionista em uma thread separada.
synchronized (hotel.getTravaRecepcao()) {}: Acessa o método getTravaRecepcao() do hotel para obter um objeto de trava, garantindo que 
apenas uma thread por vez possa acessar a lista de espera do hotel.
Hospede hospede = hotel.pegarProximoHospedeDaListaEspera();: Chama o método pegarProximoHospedeDaListaEspera() do hotel para obter o
próximo hóspede da lista de espera. 
if (hospede != null) { hospede.run(); }: Verifica se há um hóspede disponível na lista de espera. Se houver, executa o método run() 
do hóspede.


### CÓDIGO

public void run() {
    Random random = new Random();
    while (true) {
        synchronized (hotel.getTravaRecepcao()) {
            Hospede hospede = hotel.pegarProximoHospedeDaListaEspera();
            if (hospede != null) {
                hospede.run();
            }
        }
    }
}

SEMÁFORO:
receberChaveDoHospede(Hospede hospede): Esse método é responsável por receber a chave do quarto de um hóspede ao fazer o check-out, 
adquirindo o semáforo para garantir acesso exclusivo à lista de chaves do recepcionista, adicionando a chave à lista e depois libera o 
semáforo. O check-in também é segue a mesma logica.

_________________________________________________________________________________________________________________________________________

## CLASSE HOSPEDE 

A classe Hospede representa os hóspedes que chegam ao hotel e interagem com o sistema de hospedagem. Cada instância de Hospede é executada 
em uma thread separada para simular a chegada e a estadia dos hóspedes no hotel. A classe lida com a alocação de quartos, permanência dos 
hóspedes e interações com o recepcionista.

### MÉTODOS

O método run():
Este método é executado quando a thread do hóspede é iniciada. Ela realiza a criação de um objeto Random para gerar um tempo de chegada alea
tório. Ela chama o método alocarHospedeNoQuarto() para tentar alocar o hóspede em um quarto disponível.

No método alocarHospedeNoQuarto(): Esse método garante alocar o hóspede em um quarto disponível. Se encontrar um quarto disponível, o hóspede 
o ocupa. Se ele não encontrar um quarto disponível, adiciona o hóspede à lista de espera do hotel.

O método ocuparQuarto(): o método é chamado quando o hóspede consegue ocupar um quarto. Quando o quarto é ocupado, atualiza a quantidade de 
hóspedes sem quarto e define a chave do quarto, simulando a permanência do hóspede no quarto com um tempo aleatório. Se ele decidir sair tem
porariamente, chama o método sairPorUmTempo().

O Método sairPorUmTempo(): Simula o hóspede saindo temporariamente do quarto, seleciona aleatoriamente um recepcionista para lidar com a chave 
do quarto. Quando o hóspede sai do quarto, o recepcionista recebe a chave, e depois a retorna com o método selecionarRecepcionista() que seleci
ona aleatoriamente um recepcionista para lidar com a chave do quarto.

___________________________________________________________________________________________________________________________________________

# CLASSE CAMAREIRA 

A classe Camareira representa um dos funcionários do hotel responsável pela limpeza e arrumação dos quartos. Esta classe é executada em uma
 thread separada e realiza a limpeza dos quartos que estão desocupados e sujos.

### SINCRONIZAÇÃO

A camareira utiliza duas trancas (synchronized) para acessar os quartos do hotel de forma segura.
A primeira trava (hotel.getTravaArrumacao()) garante que apenas uma camareira por vez possa acessar o processo de limpeza e arrumação dos quartos.
A segunda trava (hotel.getTravaRecepcao()) evita conflitos com outros funcionários que possam estar acessando os quartos, como os recepcionistas.


### CÓDIGO

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


________________________________________________________________________________________________________________________________________________________

## CLASSE MAIN

A classe ManagerHotel é responsável por instanciar a classe Hotel e iniciar as threads do programa, incluindo camareiras, recepcionistas e hóspedes.
Essas threads representam as diferentes partes do sistema de hospedagem do hotel, permitindo que funcionem independentemente.

### CÓDIGO

 public class ManagerHotel {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
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
