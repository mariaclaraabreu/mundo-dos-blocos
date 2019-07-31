package mundodosblocos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Maria Clara
 */
public class Mundo {

    ArrayList< String> acoes = new ArrayList<>();
    ArrayList< String> copiaAcoes = new ArrayList<>();
    ArrayList< String> preCond = new ArrayList<>();
    ArrayList< String> posCond = new ArrayList<>();
    ArrayList< String> estadoInicial = new ArrayList<>();
    ArrayList< String> estadoFinal = new ArrayList<>();
    //ArrayList< String> estadoAtual = new ArrayList< String>();
    ArrayList<Integer> naoUsado = new ArrayList<>();
    ArrayList<Integer> usado = new ArrayList<>();
    ArrayList<Integer> propag;
    ArrayList<Integer> propagNaoUsado;
    ArrayList<String> programa = new ArrayList<>();

    static String[] estadoF;

    static int nivel = 1;
    static int clausula = 1;
    static int numClau;
    static int contClau = 0;
    static int contLinha = 0;

    HashMap<String, Integer> clauses = new HashMap<>();

    public void mapeamento() {
        try {
            FileReader arq = new FileReader("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\blo.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            int cont = 1;
            while (!linha.trim().equals("")) {
                if (cont == 1) {
                    acoes.add(linha);
                    contClau++;
                    cont++;
                } else if (cont == 2 || cont == 3) {
                    if (cont == 3) {
                        posCond.add(linha);
                        cont = 1;
                    } else {
                        preCond.add(linha);
                        cont++;
                    }
                    mapearPrePos(linha);

                    linha = lerArq.readLine();
                }
            }
            mapearAcoes();
            //System.out.println("PEGANDO OS ESTADOS INICIAIS E FINAIS");
            if (linha.trim().equals("")) {
                for (int j = 0; j < 1; j++) {
                    linha = lerArq.readLine();
                    armazenarEstados(linha, 1);
                    linha = lerArq.readLine();
                    armazenarEstados(linha, 2);
                }
            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }

    }

    public void mapearPrePos(String linha) {
        //int cont=1;
        String[] b = linha.split(";");
        for (int i = 0; i < b.length; i++) {
            if (b[i].length() > 1 && b[i].charAt(0) != '~') {
                naoUsado.add(clauses.get(b[i]));
                if (clauses.containsKey(b[i]) == false) {
                    clauses.put(b[i], clausula);
                    //System.out.println("comando " + b[i] + "     clausula " + clausula);
                    clausula++;
                    contClau++;
                }
            }
        }
    }

    public void mapearAcoes() {
        //System.out.println("MAPEANDO AS ACOES");
        for (int i = 0; i < acoes.size(); i++) {
            String s = acoes.get(i);
            if (clauses.containsKey(s) == false) {
                clauses.put(acoes.get(i), clausula);
                //System.out.println("Acao " + acoes.get(i) + "     clausula " + clausula);
                clausula++;
            }
        }

    }

    public void armazenarEstados(String linha, int IouF) {
        //System.out.println("PEGANDO OS ESTADOS INICIAIS E FINAIS");
        if (linha.trim().equals("")) {
            for (int j = 0; j < 1; j++) {
                //linha = lerArq.readLine();
                String[] c = linha.split(";");
                //linha = lerArq.readLine();
                //System.out.println(linha);
                //String[] d = linha.split(";");
                if (IouF == 1) {
                    for (int i = 0; i < c.length; i++) {
                        estadoInicial.add(c[i]);
                    }
                } else if (IouF == 2) {
                    for (int i = 0; i < c.length; i++) {
                        estadoFinal.add(c[i]);
                    }
                }

            }

        }
    }

    public void usados() throws FileNotFoundException, IOException {
        try {
            FileReader arq = new FileReader("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\blo.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            int cont = 1;
            while (!linha.trim().equals("")) {

                String[] b;
                b = linha.split(";");
                //if1:
                if (b.length == 1) {
                    //break if1;
                }
                if (cont == 1) {
                    cont++;
                } else if (cont == 2 || cont == 3) {
                    if (cont == 3) {
                        cont = 1;
                    } else {
                        cont++;
                    }
                    if (b.length > 1) {
                        for (int i = 0; i < b.length; i++) {
                            for (int j = 0; j < estadoInicial.size(); j++) {
                                if (b[i].equals(estadoInicial.get(j))) {
                                    if (usado.size() == 0) {
                                        usado.add(clauses.get(b[i]));
                                        programa.add(clauses.get(b[i]) + " " + 0 + "\n");
                                    } else {
                                        int numver = 0;
                                        for (int k = 0; k < usado.size(); k++) {
                                            if (clauses.get(b[i]) == (usado.get(k))) {
                                                numver++;
                                            }
                                        }
                                        if (numver == 0) {
                                            usado.add(clauses.get(b[i]));
                                            programa.add(clauses.get(b[i]) + " " + 0 + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    linha = lerArq.readLine();

                }

                arq.close();
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }

    }

    public void naoUsados() {
        for (int i = 0;
                i < naoUsado.size();
                i++) {

            int numver = 0;
            for (int j = 0; j < usado.size(); j++) {
                if (naoUsado.get(i) == usado.get(j)) {
                    numver++;
                }
            }
            if (numver == 0) {
                int n = naoUsado.get(i) * (-1);

                programa.add(n + " " + 0 + "\n");

            }
        }
    }

    public void inserirAcoes() {
        for (int i = 0; i < acoes.size(); i++) {
            programa.add(clauses.get(acoes.get(i)) + " ");

        }
        programa.add(" " + 0 + "\n");
    }

    public void combinarAcoes() {
        for (int i = 1 + 1; i < acoes.size(); i++) {
            for (int k = i; k < acoes.size(); k++) {
                programa.add(clauses.get(acoes.get(i)) * (-1) + " " + (clauses.get(acoes.get(k))) * (-1) + " " + 0 + "\n");
            }
        }
    }

    public void propagarAcoes() {

    }

    public void propagarPrePosUsados() {
        String p[] = null;
        for (int i = 0; i < acoes.size(); i++) {
            propag = new ArrayList<>();
            propagNaoUsado = new ArrayList<>();
            for (int j = 0; j < preCond.size(); j++) {
                if (j == i) {
                    p = preCond.get(j).split(";");
                    for (int c = 0; c < p.length; c++) {
                        if (p[c].length() > 1 && p[c].charAt(0) != '~') {
                            if (clauses.containsKey(p[c]) == true) {
                                programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + clauses.get(p[c]) + " " + 0 + "\n");
                                propag.add(clauses.get(p[c]));
                            }
                        } else {
                            if (!p[c].isEmpty()) {
                                String palavra = p[c].substring(1);
                                if (clauses.containsKey(palavra) == true) {
                                    programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + (clauses.get(palavra) * (-1)) + " " + 0 + "\n");
                                }
                            }

                        }

                    }
                    for (int k = 0; k < posCond.size(); k++) {
                        if (k == i) {
                            p = posCond.get(k).split(";");
                            for (int c = 0; c < p.length; c++) {
                                if (p[c].length() > 1 && p[c].charAt(0) != '~') {
                                    if (clauses.containsKey(p[c]) == true) {
                                        int pos = clauses.get(p[c]) + contClau;
                                        programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + pos + " " + 0 + "\n");
                                        propag.add(clauses.get(p[c]));
                                    }
                                } else {
                                    if (!p[c].isEmpty()) {
                                        String palavra = p[c].substring(1);
                                        if (clauses.containsKey(palavra) == true) {
                                            int pos = clauses.get(palavra) + contClau;
                                            programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + (pos * (-1)) + " " + 0 + "\n");
                                        }
                                    }

                                }

                            }
                        }
                    }
                }

            }
        }

    }

    public void preencherPrePosNaoUsados() {
        for (int it = 1; it <= contClau; it++) {
            int clau = it;

            int numver = 0;
            for (int it2 = 0; it2 < propag.size(); it2++) {
                if (clau == propag.get(it2)) {
                    numver++;

                }

            }
            if (numver == 0) {

                int numver2 = 0;
                for (int c = 0; c < acoes.size(); c++) {
                    int num = clauses.get(acoes.get(c));
                    if (clau == num) {
                        numver2++;

                    }
                }
                if (numver2 == 0) {

                    propagNaoUsado.add(clau);
                }

            }

        }
    }

    public void propagarPrePosNaoUsados() {
        for (int i = 0; i < acoes.size(); i++) {
            if (propagNaoUsado.size() > 0) {
                for (int it = 0; it < propagNaoUsado.size(); it++) {

                    int var = propagNaoUsado.get(it) + contClau;
                    programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + propagNaoUsado.get(it) + " " + (var * (-1)) + " " + 0 + "\n");
                    programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + (propagNaoUsado.get(it) * (-1)) + " " + var + " " + 0 + "\n");
                }
            }
        }
    }

    public void inserirEstadoFinal() {
        for (int i = 0;
                i < estadoFinal.size();
                i++) {
            int num = clauses.get(estadoFinal.get(i)) + contClau;
            programa.add(num + " " + 0 + "\n");
        }
    }

    public void escrever() throws IOException {

        String path = "C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos";
        FileWriter fw = new FileWriter("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\comb.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("p cnf" + " " + contClau + " " + programa.size() + "\n");
        for (int i = 0;i < programa.size();i++) {
            bw.write(programa.get(i) + "");
        }

        bw.close();
        /*FileWriter file;
        try {
            file = new FileWriter("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\comb.txt");
            PrintWriter gravar = new PrintWriter(file);
            String s[] = estadoFinal.get(0).split(";");
            if (nivel > 1) {
                //gravar.write("p cnf " + " " "maior numero + contClau (-1) * nivel" + " " + "programa.size + s.length"  );
            }

        } catch (IOException ex) {
            Logger.getLogger(MundodosBlocos.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
