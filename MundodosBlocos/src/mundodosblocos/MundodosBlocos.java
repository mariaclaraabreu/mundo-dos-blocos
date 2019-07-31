package mundodosblocos;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * Equipe: Maria Clara, Angelina Sousa e Luciano Quirino
 */
public class MundodosBlocos {

    //throws IOException
    int clausula = 1;
    int numClau;
    int contClau = 0;
    int contLinha = 0;

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

    HashMap<String, Integer> clauses = new HashMap<>();

    public void mapeiaPrePos() {
        
      
        try {
            FileReader arq = new FileReader("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\blo.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            //System.out.println("MAPEANDO AS PRE E POS CONDIÇOES");
            int cont = 1;
            while (!linha.trim().equals("")) {
                String[] b = linha.split(";");
                if (cont == 1) {
                    acoes.add(linha);
                    //System.out.println("ACAO " + linha + "\n");
                    contClau++;
                    cont++;
                } else if (cont == 2 || cont == 3) {
                    if (cont == 3) {
                        posCond.add(linha);
                        //System.out.println("POS  " + linha + "\n");
                        cont = 1;
                    } else {
                        preCond.add(linha);
                        //System.out.println("PRE  " + linha + "\n");
                        cont++;
                    }
                    for (int i = 0; i < b.length; i++) {
                        if (b[i].length() > 1 && b[i].charAt(0) != '~') {
                            if (clauses.containsKey(b[i]) == false) {
                                clauses.put(b[i], clausula);
                                //System.out.println("comando " + b[i] + "     clausula " + clausula);

                                clausula++;
                                contClau++;
                            }
                        }
                    }
                }

                linha = lerArq.readLine();
            }

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

    public void armazenarEstados(String linha, int IouF) {
    
            for (int j = 0; j < 1; j++) {
              
                String[] c = linha.split(";");
           
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

    public void mapeiaAcoes() {
        
        for (int i = 0; i < acoes.size(); i++) {
            String s = acoes.get(i);
            if (clauses.containsKey(s) == false) {
                clauses.put(acoes.get(i), clausula);
             
                clausula++;
            }
        }
    }

    public void preencherUsadoNaoUsado() throws IOException {
        
        try {
            FileReader arq = new FileReader("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\blo.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            int cont = 1;
            while (!linha.trim().equals("")) {
                String[] b;
                b = linha.split(";");
                if1:
                if (b.length == 1) {
                    break if1;
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
                            if (b[i].length() > 1 && b[i].charAt(0) != '~') {
                                if (clauses.containsKey(b[i]) == true) {
                                    naoUsado.add(clauses.get(b[i]));
                                }
                            }
                            
                            
                            for (int j = 0; j < estadoInicial.size(); j++) {
                                if (b[i].equals(estadoInicial.get(j))) {
                                    if (usado.size() == 0) {
                                        usado.add(clauses.get(b[i]));
                                    } else {
                                        int numver = 0;
                                        for (int k = 0; k < usado.size(); k++) {
                                            if (clauses.get(b[i]) == (usado.get(k))) {
                                                numver++;
                                            }
                                        }
                                        if (numver == 0) {
                                            usado.add(clauses.get(b[i]));
                                            //System.out.println(clauses.get(b[i]) + " " + 0 + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                linha = lerArq.readLine();

            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }

    }

    public void inserirEstInicial() {
        for (int i = 0; i < estadoInicial.size(); i++) {
            programa.add(clauses.get(estadoInicial.get(i)) + " " + 0 + "\n");
        }
    }

    public void inserirClauNaoUsadasEstInicial() {
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

    public void inserirAcoesUsadas() {
        //METODO INSERIR AÇOES USADAS
        for (int i = 0; i < acoes.size(); i++) {
            programa.add(clauses.get(acoes.get(i)) + " ");

        }
        programa.add(" " + 0 + "\n");
    }

    public void propagAcoesEmAcoes() {
        // METODO IMPLICAR AÇAO NA OUTRA
        for (int i = 0; i < acoes.size(); i++) {
            for (int j = i + 1; j < acoes.size(); j++) {
                programa.add(((clauses.get(acoes.get(i)) * -1) + " " + (clauses.get(acoes.get(j)) * -1) + " 0 \n"));
            }
        }
    }

    public void acoesPropagPrePos() {
        String p[] = null;
        //int prop [] = null;
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

            //CHAMA METODO PASSANDO O NUMERO DO I PARA PROPAGAR OS NAO USADOS
            MundodosBlocos.this.verificarClauNaoUsadasPelaAcao();
            MundodosBlocos.this.propagarClauNaoUsadasPelaAcao(i);
        }

    }

    public void verificarClauNaoUsadasPelaAcao() {

        //METODO DE VERIFICAR O QUE NAO ESTA SENDO USADO
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

    public void propagarClauNaoUsadasPelaAcao(int i) {

        if (propagNaoUsado.size()
                > 0) {
            for (int it = 0; it < propagNaoUsado.size(); it++) {

                int var = propagNaoUsado.get(it) + contClau;
                programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + propagNaoUsado.get(it) + " " + (var * (-1)) + " " + 0 + "\n");
                programa.add((clauses.get(acoes.get(i)) * (-1)) + " " + (propagNaoUsado.get(it) * (-1)) + " " + var + " " + 0 + "\n");
            }
        }
    }

    public void somaLevel() {
        for (int i = 0; i < estadoFinal.size(); i++) {
            int num =+ clauses.get(estadoFinal.get(i)) + contClau;
            programa.add(num + " " + 0 + "\n");
        }
    }

    public void escrever() throws IOException {

        String path = "C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos";
        FileWriter fw = new FileWriter("C:\\Users\\mclar\\OneDrive\\Documentos\\NetBeansProjects\\MundodosBlocos\\src\\mundodosblocos\\comb.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("p cnf" + " " + contClau + " " + programa.size() + "\n");
        for (int i = 0; i < programa.size(); i++) {

            bw.write(programa.get(i) + "");
        }

        bw.close();
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        //for (int i = 0; i < acoes.size(); i++) {
        //  programa.add((clauses.get(acoes.get(i)) * (-1)) + " ");
        //}//
        //programa.add(" " + 0 + "\n");
        //METODO ACOES IMPLICAM EM PRE
        MundodosBlocos m = new MundodosBlocos();
        m.mapeiaPrePos();
        m.mapeiaAcoes();
        m.preencherUsadoNaoUsado();
        m.inserirEstInicial();
        m.inserirClauNaoUsadasEstInicial();
        m.inserirAcoesUsadas();
        m.propagAcoesEmAcoes();
        m.acoesPropagPrePos();
        m.somaLevel();
        m.escrever();
        
        
        
    }
}
