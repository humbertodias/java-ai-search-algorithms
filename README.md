# Artificial Intelligence Algorithms with Java

Animation software for algorithms Artificial Intelligence

**Supported Search Algorithms:**

- Breadth-First Search 
- Depth-First Search 
- Simple Hill Climbing 
- Steepest Ascent Hill Climbing 
- Best First Search 
- A* Search

### Prerequires

1. Git 2.6+
2. Maven 3+
3. Java 8+


### How to Play

Clone

```
git clone https://github.com/humbertodias/java-ai-search-algorithms.git
```

Inside the folder

```
cd java-ai-search-algorithms
```

Run

```
mvn compile exec:java -Dexec.mainClass="com.aisearch.AISearch" -Dexec.args="src/main/resources/graph_01.txt"
```


### Output
![Preview](doc/astar.png)


**As an example a table where a graph is defined with three nodes linked shown:**

| Description | Grafo file |
| ------------- | ----------- |
| Total Nodes | 3 |
| Node Name | A |
| PosicionX, PosicionY | 100, 40 |
| Value Node | 10 |
| EsObjetivo select YES or NO {} | NO |
| NumeroSucesores | 2 |
| NombreSucesores, CosteArco | B, 10 |
| NombreSucesores, CosteArco | C, 5 |
| NodeName | B |
| PosicionX, PosicionY | 60, 120 |
| Value Node | 20 |
| EsObjetivo select YES or NO {} | NO |
| NumeroSucesores | 0 |
| NodeName | C |
| PosicionX, PosicionY | 120, 150 |
| Value Node | 15 |
| EsObjetivo select YES or NO {} | SI |
| NumeroSucesores | 0 |


### References

[A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm)