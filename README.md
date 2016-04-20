# Artificial Intelligence Algorithms with Java

Animation software for algorithms Artificial Intelligence

**Supported Search Algorithms:**

- Breadth-First Search 
- Depth-First Search 
- Simple Hill Climbing 
- Steepest Ascent Hill Climbing 
- Best First Search 
- A* Search

## Prerequires

1. Git 2.6+
2. Maven 3+
3. Java 8+


## How to Play

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
mvn compile exec:java \
-Dexec.mainClass="com.aisearch.AISearch" \
-Dexec.args="src/main/resources/graph_02.txt"
```


## Output
![Preview](doc/a-star.gif)


## File format

Example of
[src/main/resources/graph_02.txt](src/main/resources/graph_02.txt)

```
11
A
323,42
5
NO
...
```

| Description | Value |
| ------------- | ----------- |
| Total Nodes | 11 |
| Node Name | A |
| Position X,Position Y | 323,42 |
| Value Node | 5 |
| Is Target Node | NO |
| ... | 


## References

- [Breadth-First Search](https://en.wikipedia.org/wiki/Breadth-first_search)
- [Depth-First Search](https://en.wikipedia.org/wiki/Depth-first_search) 
- [Simple Hill Climbing](https://en.wikipedia.org/wiki/Hill_climbing) 
- [Steepest Ascent Hill Climbing](https://en.wikipedia.org/wiki/Hill_climbing) 
- [Best First Search](https://en.wikipedia.org/wiki/Best-first_search) 
- [A* Search](https://en.wikipedia.org/wiki/A*_search_algorithm)

