# 1. Tema 1

[Tema 1.pdf p.1]
Tema 1
8 notes · Exported 2026-10-23

[Tema 1.pdf p.2]
Contents
1. 0 Presentación 3
1. 1 Entornos de un Solo Agente 5
1.2 Espacios de Estados 7
1.3 Búsquedas con árbol 10
1.3 Búsquedas con Árbol (Continuación) 12
1.3.2 Búsquedas con Árbol 15
1.4 Búsquedas con marcha atrás 18
1.5 Heurísticos Bases de Datos de Patrones 19

[Tema 1.pdf p.3]
1. 0 Presentación
2026-09-10
Mandow Lorenzo: Despacho 3.2.38 --- lmandow@uma.es
Introducción:
La IA se divide en:
IA simbólica
Se basa en la Hipótesis del Sistema de Símbolos Físicos (Newell y Simon, 1976)
Hipótesis de la Búsqueda Heurística
Los problemas se resuelven buscando de forma selectiva (heurística) en un espacio de problema
(representación del problema).
Aprendizaje (predicción, inducción)
Resolución de problemas (deducción) ⋆
Un SSF posee los medios necesarios y suficientes para acciones inteligentes generales.
Un SSF posee:
Un conjunto de expresiones simbólicas
Unos procesos que las manipulan
Los seres humanos y los ordenadores pueden usar el SSF

[Tema 1.pdf p.5]
1. 1 Entornos de un Solo Agente
2026-09-10
1. Representación: Espacios de Estados
Representación formal de una parte relevante del mundo.
Un EE se compone de:
Ejemplo: Problema de los Misioneros y los caníbales
Conjunto de estados (E) (Posibles situaciones del mundo)
Conjunto de acciones (cómo pasar de un estado a otro)
Precondición: E -> {true, false}
Efectos: E -> E

[Tema 1.pdf p.7]
1.2 Espacios de Estados
2026-09-15
Búsquedas con Árbol
Un espacio de estados se compone de:
Puzle 8
El puzle 8 está formado por una cuadrícula de con ocho piezas numeradas y un hueco. Un movimiento
consiste en deslizar hacia el hueco una pieza adyacente.
El estado inicial considerado es:
El valor representa el hueco.
Representación de un estado
Cada estado se representa mediante una matriz de enteros de tamaño :
La posición del hueco se denota por , donde es la fila y es la columna. Para el estado inicial:
Amplitud
Dijkstra
A*
Conjunto de Estados (Posibles situaciones)
Conjunto de acciones (Pasar de un estado a otro)
Precondición
Efectos
3 × 3
3 4 2
0 1 5
7 6 8
0
3 × 3
𝐸 =
𝑒0,0 𝑒0,1 𝑒0,2
𝑒1,0 𝑒1,1 𝑒1,2
𝑒2,0 𝑒2,1 𝑒2,2
(𝐹𝑖, 𝐶𝑗) 𝐹𝑖 𝐶𝑗

[Tema 1.pdf p.8]
Acciones
Las acciones consisten en mover el hueco en una de las cuatro direcciones posibles. En cada caso se
intercambia el hueco con la pieza adyacente mediante una operación swap .
| Acción | Precondición | Efecto | Coste |
| Mover el hueco hacia arriba | | | |
| Mover el hueco hacia abajo | | | |
| Mover el hueco hacia la derecha | | | |
| Mover el hueco hacia la izquierda | | | |
Por tanto, una acción solo se puede aplicar si se cumple su precondición. Después de aplicar la acción, el
hueco intercambia su posición con la pieza contigua correspondiente.
Orden del token -> d(t_1, <t_1, t_2, ..., t_k)
d(2, <1,5,2>) = 1 -> Porque si ordeno <1,5,2> me queda <1,2,5> y el número 2 está en la posición 1.
d(5, <1,5,2>) = 2
Rankp me dice el orden de una tupla en una lista ordenada.
Rankp de <2,0,1> es 4 porque: {<0,1,2>, <0,2,1>, <1, 0,2>, <1,2,0>, <2,0,1>, ...} si ordeno las posibles
combinaciones se me queda en la 4 posición.
La fórmula es:
rankp(<2,0,1>) = d(2, <2,0,1>) 2! + rankp(<0,1>) = d(2, <2,0,1>) 2! + d(0, <0,1>) * 1! + 0 = 4
Unrankp([0,1,2], 5) -> NO CAE
𝐸0 =
3 4 2
0 1 5
7 6 8
, (𝐹𝑖, 𝐶𝑗) = (1, 0).
𝐹𝑖 > 0 swap((𝐹𝑖
, 𝐶𝑗),(𝐹𝑖 - 1, 𝐶𝑗)) 1
𝐹𝑖 < 2 swap((𝐹𝑖
, 𝐶𝑗),(𝐹𝑖 + 1, 𝐶𝑗)) 1
𝐶𝑗 < 2 swap((𝐹𝑖
, 𝐶𝑗),(𝐹𝑖
, 𝐶𝑗 + 1)) 1
𝐶𝑗 > 0 swap((𝐹𝑖
, 𝐶𝑗),(𝐹𝑖
, 𝐶𝑗 - 1)) 1
𝑟𝑎𝑛𝑘𝑝(< 𝑡1
,𝑡2
,…,𝑡𝑘) = 𝑑(𝑡1
, < 𝑡1
,𝑡2
,…,𝑡𝑘 >) ∗ (𝑘 - 1)! + 𝑟𝑎𝑛𝑘𝑝(< 𝑡2
,…,𝑡𝑘 >)
𝑟𝑎𝑛𝑘𝑝(< 𝑡1 >) = 0

[Tema 1.pdf p.9]
Un Espacio de Estados define implícitamente un grafo:
Estrategias de búsqueda
Estrategias irrevocables
Estas estrategias exploran un único camino:
Estrategias tentativas
Estas estrategias exploran sistemáticamente el grafo:
Estados == Nodos
Acciones == Arcos
Búsqueda local
Algoritmo genéticos
Con árbol: Guardan todos los caminos interesantes.
Con retroceso: Solo guardan el camino actual.

[Tema 1.pdf p.10]
1.3 Búsquedas con árbol
2026-09-22
Lo que caracteriza a cada método de búsqueda es:
Método de búsqueda de Amplitud:
Propiedades de los algoritmos
Las propiedades de los distintos algoritmos/métodos de búsquedas con árbol son:
Dijkstra
Algoritmo de búsqueda a ciegas.
1. Criterio de selección de ABIERTOS.
2. Qué hacer cuando se encuentran 2 caminos al mismo nodo.
3. Criterio de terminación.
1. Abiertos nuevos -> Al final de la cola
2. Nos quedamos con el antiguo
3. Cuando se genera el estado objetivo / Cuando se selecciona para expansión el estado objetivo.
1. Completo: Si hay solución, la encuentra (en un número finito de pasos).
Ej: Amplitud es completo porque aunque el árbol sea infinito encuentra la solución porque esa solución
tiene que estar a una profundidad finita.
2. Admisible: Si hay solución, encuentra la óptima.
Ej: Amplitud no lo es, en general, porque ni siquiera tiene en cuenta el coste. Si todos los arcos tienen
el mismo coste (el coste es la profundidad), ahí si que encuentra la solución óptima.
3. Consumo de memoria (nº de nodos almacenados en el árbol) y tiempo (nº de iteraciones (selecciones de
abiertos)).
Un árbol infinito con factor de ramificación b (branch) con solución a profundidad d(depth) tiene un
coste de memoria de 𝑂(𝑏
𝑑)
4. Malla cuadrada de 4 vecinos.
1. Selección de ABIERTOS: Menor valor de g(n).
2. 2 caminos al mismo nodo: Conservamos el de menor g(n).
3. Criterio de terminación: Cuando se selecciona el objetivo (con el algoritmo de amplitud también se podía
acabar cuando se seleccionaba para expansión el nodo objetivo, ahora no porque puedes encontrar un

[Tema 1.pdf p.11]
Suposiciones:
Notación:
A*
Algoritmo de búsqueda Heurístico: Conocimiento propio del problema para guiar la respuesta.
atajo).
1. Cada arco del grafo (n, n') tiene un coste c(n,n') >= 0
2. El coste de un camino es la suma de los costes de los arcos.
3. Caminos infinitos tienen infinitos. ∀(𝑛, 𝑛
′)𝑐(𝑛, 𝑛
′) ≥ 𝜀 > 0
g(n) = Coste de camino guardado en el árbol desde S (raíz) hasta n.

[Tema 1.pdf p.12]
1.3 Búsquedas con Árbol (Continuación)
2026-09-25
El algoritmo A* se basa en:
Propiedades de A*
1. Selección de abiertos:
menor 𝑓(𝑛) = 𝑔(𝑛) + ℎ(𝑛)
2. 2 caminos al mismo nodo:
Si el nuevo camino a n' es mejor, redirigir el puntero de n' a ese camino.
Si n' estaba cerrado, lo devolvemos a abierto.
Esto hace que podamos expandir el mismo nodo varias veces en A*.
3. Terminación:
Cuando seleccionamos el objetivo.
𝑓(𝑛) = 𝑔(𝑛) + ℎ(𝑛)

[Tema 1.pdf p.13]
Coste de la solución óptima
Coste óptimo real desde S hasta n. (S es el nodo raíz)
Normalmente
Coste óptimo real desde n al objetivo.
Suposiciones
Propiedad 1
Si es optimista, es admisible.
LEMA 1: termina siempre en grafos finitos.
LEMA 2: Sea el camino solución óptimo: nunca se poda.
LEMA 3: termina siempre incluso en grafos infinitos.
LEMA 4: no puede terminar con una solución subóptima.
Propiedad 2
Si h2 está más informado que h1, entonces si A* con h2 expande n, también lo hará con h1.
Un heurístico es Monótono si cuando hago un movimiento (rellenar)
Comprobar si el heurístico h2 es monótono (distancia euclidea (creo, revisar si es es o es la manhatan)a la
posición final)
𝑔(𝑛) = Coste en el árbol hasta n
ℎ(𝑛) = Estimación del coste n al objetivo
𝑓(𝑛) = Estimación de coste de una solución que pasa por n
𝑐
∗ =
𝑔
∗(𝑛) =
𝑔(𝑛) ≥ 𝑔
∗(𝑛)
ℎ
∗(𝑛) =
𝑓
∗(𝑛) = 𝑔
∗(𝑛) + ℎ
∗(𝑛)
1. ∀𝑛, ℎ(𝑛) ≤ ℎ -> Optimista o admisible ∗(𝑛)
2. Caminos infinitos tienen costes infinitos.
3. El nº de sucesores de un nodo es finito.
ℎ(𝑛) 𝐴∗
𝐴∗
𝑃
∗ = 𝑛0, 𝑛1,…, 𝑛𝑘 𝑃
∗
𝐴∗
𝐴∗

[Tema 1.pdf p.14]
Propiedad 3:
Si h(n) es monótono, cuando A selecciona un nodo para expanción, g(n) = g(n) (Expandirá cada nodo a lo sumo
1 vez). -> Es mejor hacerlo con A que con Dijstra porque A aunque suela ser mejor, si expande los nodos varias
veces puede tardar más.
EJERCICIO
Red de carreteras densa con p nodos por unidad de superficie.
Deseamos encontrar un camino óptimo entre A(0,0) y Z(0, a).
Se utiliza distancia euclídea como heurístico.
La distancia óptima entre A y Z es C*=C.
Calcular la cota superior nº nodos por Dijkstra y por A*

[Tema 1.pdf p.15]
1.3.2 Búsquedas con Árbol
2026-09-28
Propiedades de A*
Propiedad 1
Si es optimista, es admisible.
LEMA 1: termina siempre en grafos finitos.
LEMA 2: Sea el camino solución óptimo: nunca se poda.
LEMA 3: termina siempre incluso en grafos infinitos.
LEMA 4: no puede terminar con una solución subóptima.
Propiedad 2
Si h2 está más informado que h1, entonces si A* con h2 expande n, también lo hará con h1.
ℎ(𝑛) 𝐴∗
𝐴∗
𝑃
∗ = 𝑛0, 𝑛1,…, 𝑛𝑘 𝑃
∗
𝐴∗
𝐴∗

[Tema 1.pdf p.16]
El profe hizo una demostración en la pizarra que concluía en que para unos datos dados, siempre A visita <=
cantidad de nodos que Dijkstra (siendo la elipse roja la zona de búsqueda de A y la zona azul la zona de
búsqueda de Dijkstra).
A* suele ser mejor pero como a veces re-visita un nodo varias veces, en algunos casos puede ser peor.
Un heurístico es monótono, también llamado consistente, si para cada movimiento n → n' se cumple:
donde c(n,n') es el coste del movimiento.
Si cada movimiento cuesta 1:
Equivalentemente:
Es decir, el valor de f no disminuye al avanzar por un camino.
Si h es monótono y A* trabaja sobre un grafo, cuando selecciona un estado para expandir se cumple:
Por tanto, cada estado se expande como máximo una vez y no es necesario reabrirlo posteriormente.
ℎ(𝑛) ≤ 𝑐(𝑛, 𝑛
′
) + ℎ(𝑛
′
)
ℎ(𝑛) ≤ 1 + ℎ(𝑛
′)
𝑓(𝑛) ≤ 𝑓(𝑛
′
)
𝑔(𝑛) = 𝑔
∗
(𝑛)

[Tema 1.pdf p.17]
Distancia Manhattan
Si el movimiento es horizontal o vertical y cuesta 1, la distancia Manhattan es:
Es monótona porque un movimiento cambia la distancia Manhattan como máximo en 1:
Propiedad 3:
Si h(n) es monótono, cuando A selecciona un nodo para expansión, g(n) = g(n) (Expandirá cada nodo a lo sumo
1 vez). -> Es mejor hacerlo con A que con Dijsktra porque A aunque suela ser mejor, si expande los nodos
varias veces puede tardar más.
ℎ𝑀(𝑛) = |𝑥 - 𝑥
∗
| + |𝑦 - 𝑦
∗
|
ℎ𝑀(𝑛) ≤ 1 + ℎ𝑀(𝑛
′
)

[Tema 1.pdf p.18]
1.4 Búsquedas con marcha atrás
2026-09-29
Propiedades de Backtracking:
Completo:
En general no. Sí en grafos acíclicos dirigidos o árboles finitos.
Admisible:
No.
Consumo tiempo / memoria

[Tema 1.pdf p.19]
1.5 Heurísticos Bases de Datos de Patrones
2026-10-08
