// java
package pt.ulusofona.aed.deisiworldmeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Paises> paiseslista = new ArrayList<>();
    static ArrayList<Cidade> cidades = new ArrayList<>();
    static ArrayList<Populacao> populacao = new ArrayList<>();
    static ArrayList<String> inputsInvalidos = new ArrayList<>();

    public static ArrayList getObjects(TipoEntidade tipo){
        switch (tipo){
            case PAIS:
                return new ArrayList<>(paiseslista);
            case CIDADE:
                return new ArrayList<>(cidades);
            case INPUT_INVALIDO:
                return new ArrayList<>(inputsInvalidos);
            default:
                return new ArrayList<>();
        }
    }

    public static boolean parseFiles(File folder)  {
        File filePaises = new File(folder, "paises.csv");
        File fileCidades = new File(folder, "cidades.csv");
        File filePopulacao = new File(folder, "populacao.csv");

        ArrayList<Paises> localPaises = new ArrayList<>();
        Scanner scanner1;
        try {
            scanner1 = new Scanner(filePaises);
        } catch (FileNotFoundException e){
            return false;
        }

        int paisesOk = 0;
        int paisesNok = 0;
        String paisesFirstNok = "";

        if (scanner1.hasNextLine()) {
            scanner1.nextLine(); // skip header
        }
        while (scanner1.hasNextLine()){
            String linha = scanner1.nextLine().trim();
            if (linha.isEmpty()) {
                continue;
            }
            String[] partes = linha.split(",", 4);
            if (partes.length < 4) {
                paisesNok++;
                if (paisesFirstNok.isEmpty()) {
                    paisesFirstNok = linha;
                }
                continue;
            }
            try {
                int id = Integer.parseInt(partes[0].trim());
                String alfa2 = partes[1].trim();
                String alfa3 = partes[2].trim();
                String nome = partes[3].trim();
                Paises paises = new Paises(id, alfa2, alfa3, nome);
                localPaises.add(paises);
                paisesOk++;
            } catch (NumberFormatException ex) {
                paisesNok++;
                if (paisesFirstNok.isEmpty()) {
                    paisesFirstNok = linha;
                }
            }
        }
        scanner1.close();

        ArrayList<Cidade> localCidades = new ArrayList<>();
        Scanner scanner2;
        try {
            scanner2 = new Scanner(fileCidades);
        } catch (FileNotFoundException e){
            return false;
        }

        int cidadesOk = 0;
        int cidadesNok = 0;
        String cidadesFirstNok = "";

        if (scanner2.hasNextLine()) {
            scanner2.nextLine(); // skip header
        }
        while (scanner2.hasNextLine()){
            String linha = scanner2.nextLine().trim();
            if (linha.isEmpty()) {
                continue;
            }
            String[] partes = linha.split(",", 6);
            if (partes.length < 6) {
                cidadesNok++;
                if (cidadesFirstNok.isEmpty()) {
                    cidadesFirstNok = linha;
                }
                continue;
            }
            try {
                String alfa2 = partes[0].trim();
                String nome = partes[1].trim();
                Integer regiao = Integer.valueOf(partes[2].trim());
                Double popul = Double.valueOf(partes[3].trim());
                Double latitude = Double.valueOf(partes[4].trim());
                Double longitude = Double.valueOf(partes[5].trim());
                Cidade cidade = new Cidade(alfa2, nome, regiao, popul, latitude, longitude);
                localCidades.add(cidade);
                cidadesOk++;
            } catch (NumberFormatException ex) {
                cidadesNok++;
                if (cidadesFirstNok.isEmpty()) {
                    cidadesFirstNok = linha;
                }
            }
        }
        scanner2.close();

        int popOk = 0;
        int popNok = 0;
        String popFirstNok = "";
        Scanner scanner3;
        try {
            scanner3 = new Scanner(filePopulacao);
            if (scanner3.hasNextLine()) {
                scanner3.nextLine(); // skip header
            }
            while (scanner3.hasNextLine()){
                String linha = scanner3.nextLine().trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] partes = linha.split(",", 3);
                if (partes.length < 3) {
                    popNok++;
                    if (popFirstNok.isEmpty()) {
                        popFirstNok = linha;
                    }
                    continue;
                }
                try {
                    Integer id = Integer.valueOf(partes[0].trim());
                    Double val = Double.valueOf(partes[1].trim());
                    popOk++;
                } catch (NumberFormatException ex) {
                    popNok++;
                    if (popFirstNok.isEmpty()) {
                        popFirstNok = linha;
                    }
                }
            }
            scanner3.close();
        } catch (FileNotFoundException e) {
            // file missing: consider as zero lines read but still provide entry
        }

        paiseslista = localPaises;
        cidades = localCidades;

        inputsInvalidos.clear();
        inputsInvalidos.add("paises.csv | " + paisesOk + " | " + paisesNok + " | " + (paisesFirstNok.isEmpty() ? "" : paisesFirstNok));
        inputsInvalidos.add("cidades.csv | " + cidadesOk + " | " + cidadesNok + " | " + (cidadesFirstNok.isEmpty() ? "" : cidadesFirstNok));
        inputsInvalidos.add("populacao.csv | " + popOk + " | " + popNok + " | " + (popFirstNok.isEmpty() ? "" : popFirstNok));

        return true;
    }

    public static void main(String[] args) {
        System.out.println("Bem-Vindo ao DEISI World Meter");

        long start = System.currentTimeMillis();
        boolean parseOk = parseFiles(new File("file"));

        if (!parseOk) {
            System.out.println("Erro na leitura dos ficheiros");
            return;
        }

        long end = System.currentTimeMillis();

        System.out.println("Ficheiros lidos com sucesso em " + (end - start) + "ms" );

        System.out.println();
        System.out.println("Informações sobre a leitura dos ficheiros:");
        System.out.println("nome | linhas OK | linhas NOK | primeira linha NOK");
        ArrayList inputsInvalidos = getObjects(TipoEntidade.INPUT_INVALIDO);
        if (inputsInvalidos.size() >= 3) {
            System.out.println(inputsInvalidos.get(0));
            System.out.println(inputsInvalidos.get(1));
            System.out.println(inputsInvalidos.get(2));
        }

        System.out.println();
        System.out.println("Alguns paises:");
        ArrayList paises = getObjects(TipoEntidade.PAIS);
        for (int i = 0; i < Math.min(4, paises.size()); i++) {
            System.out.println(paises.get(i).toString());
        }

        System.out.println();
        System.out.println("Algumas cidades:");
        ArrayList cidades = getObjects(TipoEntidade.CIDADE);
        for (int i = 0; i < Math.min(3, cidades.size()); i++) {
            System.out.println(cidades.get(i).toString());
        }
    }

}
