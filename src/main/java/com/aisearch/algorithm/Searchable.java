package com.aisearch.algorithm;

import java.util.List;

public interface Searchable {

    void reordenar(List<String> vector);

    void inicio();

    void paso0();

    void paso1();

    void paso2();

    void paso3();

    public boolean step();

    public void run();

    public void reset();

    public String getCerrada();

    public String getAbierta();

}