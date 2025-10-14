import controller.CargoController;
import controller.FuncionarioController;
import model.Cargo;
import model.Funcionario;

import java.util.List;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        FuncionarioController funcionarioController = new FuncionarioController();
        CargoController cargoController = new CargoController();
        Scanner scanner = new Scanner(System.in);

        // Menu de opções
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar Funcionário");
            System.out.println("2 - Listar Funcionários");
            System.out.println("3 - Buscar Funcionário por ID");
            System.out.println("4 - Atualizar Funcionário");
            System.out.println("5 - Deletar Funcionário");
            System.out.println("6 - Criar Cargo");
            System.out.println("7 - Listar Cargos");
            System.out.println("8 - Buscar Cargo por ID");
            System.out.println("9 - Atualizar Cargo");
            System.out.println("10 - Deletar Cargo");
            System.out.println("11 - Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir o newline

            switch (opcao) {
                case 1:
                    // Criar novo funcionário
                    System.out.println("Nome do funcionário:");
                    String nome = scanner.nextLine();
                    System.out.println("Email do funcionário:");
                    String email = scanner.nextLine();
                    System.out.println("Senha:");
                    String senha = scanner.nextLine();
                    System.out.println("Recebe vale transporte (true/false):");
                    boolean recebeVale = scanner.nextBoolean();
                    System.out.println("ID do cargo:");
                    int cargoId = scanner.nextInt();

                    boolean criadoFuncionario = funcionarioController.createController(nome, email, senha, recebeVale, cargoId);
                    if (criadoFuncionario) {
                        System.out.println("Funcionário criado com sucesso.");
                    } else {
                        System.out.println("Erro ao criar funcionário.");
                    }
                    break;

                case 2:
                    // Listar todos os funcionários
                    List<Funcionario> funcionarios = funcionarioController.readAllController();
                    for (Funcionario funcionario : funcionarios) {
                        System.out.println(funcionario);
                    }
                    break;

                case 3:
                    // Buscar funcionário por ID
                    System.out.println("ID do funcionário:");
                    int idBusca = scanner.nextInt();
                    Funcionario funcionario = funcionarioController.readByIdController(idBusca);
                    if (funcionario != null) {
                        System.out.println(funcionario);
                    } else {
                        System.out.println("Funcionário não encontrado.");
                    }
                    break;

                case 4:
                    // Atualizar funcionário
                    System.out.println("ID do funcionário a ser atualizado:");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline
                    System.out.println("Novo nome:");
                    String novoNome = scanner.nextLine();
                    System.out.println("Novo email:");
                    String novoEmail = scanner.nextLine();
                    System.out.println("Nova senha:");
                    String novaSenha = scanner.nextLine();
                    System.out.println("Recebe vale transporte (true/false):");
                    boolean novoRecebeVale = scanner.nextBoolean();
                    System.out.println("Novo ID do cargo:");
                    int novoCargoId = scanner.nextInt();

                    boolean atualizado = funcionarioController.updateController(idAtualizar, novoNome, novoEmail, novaSenha, novoRecebeVale, novoCargoId);
                    if (atualizado) {
                        System.out.println("Funcionário atualizado com sucesso.");
                    } else {
                        System.out.println("Erro ao atualizar funcionário.");
                    }
                    break;

                case 5:
                    // Deletar funcionário
                    System.out.println("ID do funcionário a ser deletado:");
                    int idDeletar = scanner.nextInt();
                    boolean deletado = funcionarioController.deleteController(idDeletar);
                    if (deletado) {
                        System.out.println("Funcionário deletado com sucesso.");
                    } else {
                        System.out.println("Erro ao deletar funcionário.");
                    }
                    break;

                case 6:
                    // Criar cargo
                    System.out.println("Nome do cargo:");
                    String nomeCargo = scanner.nextLine();
                    boolean criadoCargo = cargoController.createController(nomeCargo);
                    if (criadoCargo) {
                        System.out.println("Cargo criado com sucesso.");
                    } else {
                        System.out.println("Erro ao criar cargo.");
                    }
                    break;

                case 7:
                    // Listar todos os cargos
                    List<Cargo> cargos = cargoController.readAllController();
                    for (Cargo cargo : cargos) {
                        System.out.println(cargo);
                    }
                    break;

                case 8:
                    // Buscar cargo por ID
                    System.out.println("ID do cargo:");
                    int idCargoBusca = scanner.nextInt();
                    Cargo cargo = cargoController.readByIdController(idCargoBusca);
                    if (cargo != null) {
                        System.out.println(cargo);
                    } else {
                        System.out.println("Cargo não encontrado.");
                    }
                    break;

                case 9:
                    // Atualizar cargo
                    System.out.println("ID do cargo a ser atualizado:");
                    int idCargoAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline
                    System.out.println("Novo nome do cargo:");
                    String novoNomeCargo = scanner.nextLine();

                    boolean atualizadoCargo = cargoController.updateController(idCargoAtualizar, novoNomeCargo);
                    if (atualizadoCargo) {
                        System.out.println("Cargo atualizado com sucesso.");
                    } else {
                        System.out.println("Erro ao atualizar cargo.");
                    }
                    break;

                case 10:
                    // Deletar cargo
                    System.out.println("ID do cargo a ser deletado:");
                    int idCargoDeletar = scanner.nextInt();
                    boolean deletadoCargo = cargoController.deleteController(idCargoDeletar);
                    if (deletadoCargo) {
                        System.out.println("Cargo deletado com sucesso.");
                    } else {
                        System.out.println("Erro ao deletar cargo.");
                    }
                    break;

                case 11:
                    // Sair
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
